package com.example.chatms.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.example.chatms.client.GroqClient;
import com.example.chatms.config.DataSourceManager;
import com.example.chatms.dto.RespuestaApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
// [MOD] → para permitir DML dentro de una transacción
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 👉 Servicio que:
 * - Construye el prompt con el esquema SQL
 * - Llama a Groq para generar SQL
 * - Detecta que db usar (SQL o MongoDB)
 * - Valida y extrae una ÚNICA sentencia SQL (SELECT/INSERT/UPDATE/DELETE)
 * - Ejecuta de forma segura (bloquea DDL peligrosos)
 */
@Service
public class IaService {

    @Autowired
    private DataSourceManager dataSourceManager;

    @Autowired
    private GroqClient groqChatClient;

    private final String CONTEXTO_SQL;

    private static final Logger log = LoggerFactory.getLogger(IaService.class);

    // ========================================================================
    // [MOD - NUEVO] Reglas de extracción/seguridad para la sentencia SQL
    // ------------------------------------------------------------------------
    // Aceptamos EXACTAMENTE una sentencia que empiece por
    // SELECT|INSERT|UPDATE|DELETE
    // y que termine en ';'. El DOTALL permite capturar saltos de línea.
    private static final Pattern SQL_ALLOWED = Pattern.compile("(?is)\\b(SELECT|INSERT|UPDATE|DELETE)\\b[\\s\\S]*?;");

    // Bloqueamos DDL u otras operaciones peligrosas por si el modelo "derrapa".
    private static final Pattern SQL_FORBIDDEN = Pattern.compile("(?i)\\b(DROP|TRUNCATE|ALTER|CREATE|GRANT|REVOKE)\\b");
    // =========================================================================

    public IaService() {
        this.CONTEXTO_SQL = cargarEsquemaSQL("dump.sql");
    }

    private String cargarEsquemaSQL(String nombreArchivo) {
        try (InputStream inputStream = new ClassPathResource(nombreArchivo).getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al leer el archivo SQL desde resources: " + e.getMessage(), e);
        }
    }

    /**
     * Genera el prompt, obtiene la SQL de Groq, la valida y ejecuta.
     */
    // ========================================================================
    // [MOD - NUEVO] Agregamos @Transactional para soportar INSERT/UPDATE/DELETE
    // ========================================================================
    @Transactional
    public ResponseEntity<?> procesarPrompt(String promptUsuario) {
        try {
            String promptFinal = """
                    Este es el esquema de mi base de datos MySQL:
                    %s

                    Basándote exclusivamente en este esquema, devolveme ÚNICAMENTE una sentencia SQL
                    MySQL completa y VÁLIDA (sin texto adicional, sin markdown, sin comentarios) que
                    termine con punto y coma. La sentencia puede ser SELECT/INSERT/UPDATE/DELETE.

                    IMPORTANTE: Si la consulta es sobre "monopatines" o "viajes" usa db.monopatines.find() o db.viajes.find() respectivamente en formato MongoDB.
                    Para otras tablas usá SQL estándar.
                    %s
                    """
                    .formatted(CONTEXTO_SQL, promptUsuario);

            log.info("==== PROMPT ENVIADO A LA IA ====\n{}", promptFinal);

            String respuestaIa = groqChatClient.preguntar(promptFinal);
            log.info("==== RESPUESTA IA ====\n{}", respuestaIa);

            String database = dataSourceManager.detectDatabase(respuestaIa);
            log.info("==== BASE DE DATOS DETECTADA: {} ====", database);

            if (dataSourceManager.isMongoDB(database)) {
                return ejecutarMongoQuery(promptUsuario);
            }

            String sql = extraerConsultaSQL(respuestaIa);
            if (sql == null || sql.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new RespuestaApi<>(false,
                                "No se encontró una sentencia SQL válida en la respuesta de la IA.", null));
            }

            log.info("==== SQL EXTRAÍDA ====\n{}", sql);

            // Para JDBC/JPA normalmente NO va el ';' final
            String sqlToExecute = sql.endsWith(";") ? sql.substring(0, sql.length() - 1) : sql;

            try {
                Object data;
                if (sql.trim().regionMatches(true, 0, "SELECT", 0, 6)) {
                    // Ejecutar SELECT
                    data = ejecutarSelect(database, sqlToExecute);
                    return ResponseEntity.ok(new RespuestaApi<>(true,
                            "Consulta SELECT ejecutada con éxito en BD: " + database, data));
                } else {
                    // Ejecutar DML (INSERT/UPDATE/DELETE)
                    int rows = ejecutarDML(database, sqlToExecute);
                    data = Map.of("rowsAffected", rows);
                    return ResponseEntity.ok(new RespuestaApi<>(true,
                            "Sentencia DML ejecutada con éxito en BD: " + database, data));
                }
            } catch (Exception e) {
                log.warn("Error al ejecutar SQL {} : {}", database, e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new RespuestaApi<>(false, "Error al ejecutar la sentencia: " + e.getMessage(), null));
            }

        } catch (Exception e) {
            log.error("Fallo al procesar prompt", e);
            return new ResponseEntity<>(
                    new RespuestaApi<>(false, "Error al procesar el prompt: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<?> ejecutarMongoQuery(String promptUsuario) {
        try {
            log.info("==== EJECUTANDO CONSULTA MONGODB ====");

            String texto = promptUsuario == null ? "" : promptUsuario.toLowerCase();

            String coleccion;
            if (texto.contains("viaje") || texto.contains("viajes")) {
                coleccion = "viajes";
            } else if (texto.contains("monopatin") || texto.contains("monopatines") || texto.contains("monopatín")) {
                coleccion = "monopatines";
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new RespuestaApi<>(false,
                                "Consulta MongoDB no especificada: debe mencionar 'monopatin' o 'viaje' en el prompt.",
                                null));
            }

            List<Map> resultados = dataSourceManager.getMongoTemplate(coleccion)
                    .findAll(Map.class, coleccion);

            log.info("==== {} ENCONTRADOS: {} ====", coleccion, resultados.size());

            return ResponseEntity.ok(new RespuestaApi<>(true,
                    "Consulta ejecutada con éxito en BD: " + coleccion + " (MongoDB)", resultados));
        } catch (Exception e) {
            log.error("Error al ejecutar consulta MongoDB: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RespuestaApi<>(false,
                            "Error al ejecutar la consulta en MongoDB: " + e.getMessage(), null));
        }
    }

    
    private List<Map<String, Object>> ejecutarSelect(String database, String sql) {
        JdbcTemplate jdbcTemplate = dataSourceManager.getJdbcTemplate(database);

        List<Map<String, Object>> resultados = new ArrayList<>();

        jdbcTemplate.query(sql, (ResultSet rs) -> {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = rs.getObject(i);
                row.put(columnName, value);
            }
            resultados.add(row);
        });

        return resultados;
    }

    @Transactional
    private int ejecutarDML(String database, String sql) {
        JdbcTemplate jdbcTemplate = dataSourceManager.getJdbcTemplate(database);
        return jdbcTemplate.update(sql);
    }

    // ========================================================================
    // [MOD - REEMPLAZO] Método de extracción robusto y documentado
    // - Acepta SOLO una sentencia que empiece con SELECT/INSERT/UPDATE/DELETE
    // - Exige punto y coma final
    // - Recorta todo lo que venga después del primer ';'
    // - Bloquea DDL peligrosos (DROP/TRUNCATE/ALTER/CREATE/GRANT/REVOKE)
    // ========================================================================
    private String extraerConsultaSQL(String respuesta) {
        if (respuesta == null)
            return null;

        Matcher m = SQL_ALLOWED.matcher(respuesta);
        if (!m.find())
            return null;

        String sql = m.group().trim();

        // Asegurar UNA sola sentencia (hasta el primer ';')
        int first = sql.indexOf(';');
        if (first > -1) {
            sql = sql.substring(0, first + 1);
        }

        // Bloquear DDL
        if (SQL_FORBIDDEN.matcher(sql).find()) {
            log.warn("Sentencia bloqueada por contener DDL prohibido: {}", sql);
            return null;
        }

        return sql;
    }

    // =======================
    // [MOD - HISTÓRICO]
    // Antes estaba este extraerConsultaSQL que solo acepta consultas SELECT:
    //
    // private String extraerConsultaSQL(String respuesta) {
    // Pattern pattern = Pattern.compile("(?i)(SELECT\\s+.*?;)", Pattern.DOTALL);
    // Matcher matcher = pattern.matcher(respuesta);
    // if (matcher.find()) {
    // return matcher.group(1).trim();
    // }
    // return null;
    // }
    //
    // Lo reemplazamos por la versión superior que permite DML y agrega
    // salvaguardas.
    // =======================
}
