package com.example.chatms.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceManager {

    private final Map<String, JdbcTemplate> jdbcTemplates = new HashMap<>();
    private final Map<String, DataSource> dataSources = new HashMap<>();

    private final MongoTemplate mongoTemplateMonopatines;
    private final MongoTemplate mongoTemplateViajes;

    public DataSourceManager(
            @Qualifier("paradasJdbcTemplate") JdbcTemplate paradasJdbcTemplate,
            @Qualifier("usuariosJdbcTemplate") JdbcTemplate usuariosJdbcTemplate,
            @Qualifier("viajesJdbcTemplate") JdbcTemplate viajesJdbcTemplate,
            @Qualifier("facturasJdbcTemplate") JdbcTemplate facturasJdbcTemplate,
            @Qualifier("tarifasJdbcTemplate") JdbcTemplate tarifasJdbcTemplate,
            @Qualifier("paradasDataSource") DataSource paradasDataSource,
            @Qualifier("usuariosDataSource") DataSource usuariosDataSource,
            @Qualifier("viajesDataSource") DataSource viajesDataSource,
            @Qualifier("facturasDataSource") DataSource facturasDataSource,
            @Qualifier("tarifasDataSource") DataSource tarifasDataSource,
            @Qualifier("mongoTemplateMonopatines") MongoTemplate mongoTemplateMonopatines,
            @Qualifier("mongoTemplateViajes") MongoTemplate mongoTemplateViajes
    ) {

        // Registrar JDBC Templates
        jdbcTemplates.put("paradas", paradasJdbcTemplate);
        jdbcTemplates.put("usuarios", usuariosJdbcTemplate);
        jdbcTemplates.put("viajes", viajesJdbcTemplate);
        jdbcTemplates.put("facturas", facturasJdbcTemplate);
        jdbcTemplates.put("tarifas", tarifasJdbcTemplate);

        // Registrar DataSources
        dataSources.put("paradas", paradasDataSource);
        dataSources.put("usuarios", usuariosDataSource);
        dataSources.put("viajes", viajesDataSource);
        dataSources.put("facturas", facturasDataSource);
        dataSources.put("tarifas", tarifasDataSource);

        // Registrar MongoTemplates
        this.mongoTemplateMonopatines = mongoTemplateMonopatines;
        this.mongoTemplateViajes = mongoTemplateViajes;
    }

    public JdbcTemplate getJdbcTemplate(String database) {
        JdbcTemplate template = jdbcTemplates.get(database.toLowerCase());
        if (template == null) {
            throw new IllegalArgumentException("Base SQL no configurada: " + database);
        }
        return template;
    }

    public DataSource getDataSource(String database) {
        DataSource ds = dataSources.get(database.toLowerCase());
        if (ds == null) {
            throw new IllegalArgumentException("Base SQL no configurada: " + database);
        }
        return ds;
    }

    public MongoTemplate getMongoTemplate(String database) {
        if (database.equalsIgnoreCase("monopatines")) {
            return mongoTemplateMonopatines;
        }
        if (database.equalsIgnoreCase("viajes")) {
            return mongoTemplateViajes;
        }
        throw new IllegalArgumentException("Base Mongo no configurada: " + database);
    }

    public boolean isMongoDB(String db) {
        return db.equalsIgnoreCase("monopatines") ||
               db.equalsIgnoreCase("viajes");
    }

    public String detectDatabase(String sql) {
        String lower = sql.toLowerCase();

        if (lower.contains("monopatin")) return "monopatines";
        if (lower.contains("viaje")) return "viajes";
        if (lower.contains("usuario") || lower.contains("cuenta")) return "usuarios";
        if (lower.contains("factura")) return "facturas";
        if (lower.contains("tarifa")) return "tarifas";
        if (lower.contains("parada")) return "paradas";

        return "paradas";
    }

    public String[] getAvailableDatabases() {
        String[] sqlBases = jdbcTemplates.keySet().toArray(new String[0]);
        String[] all = new String[sqlBases.length + 2];

        System.arraycopy(sqlBases, 0, all, 0, sqlBases.length);
        all[sqlBases.length] = "monopatines";
        all[sqlBases.length + 1] = "viajes";

        return all;
    }
}
