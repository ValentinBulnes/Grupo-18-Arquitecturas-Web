-- Elimina la tabla si existe para evitar errores al crearla de nuevo
DROP TABLE IF EXISTS factura;

-- Tabla de facturación general
CREATE TABLE factura (
    id BIGINT NOT NULL AUTO_INCREMENT,      -- Identificador único de la factura
    fecha_emision DATE NOT NULL,            -- Fecha en que se emitió la factura
    monto_total DOUBLE NOT NULL,            -- Monto total calculado del viaje
    descripcion VARCHAR(255),               -- Texto opcional con notas adicionales
    viaje_id VARCHAR(255),                  -- ID del viaje en MongoDB (por eso es String)
    cuenta_id BIGINT NOT NULL,              -- Relación con la cuenta del usuario (MySQL)
    usuario_id BIGINT NOT NULL,             -- Relación con el usuario (MySQL)
    PRIMARY KEY (id)
);


-- Tabla de paradas estándar
DROP TABLE IF EXISTS parada;

CREATE TABLE parada (
    id BIGINT NOT NULL AUTO_INCREMENT,      -- Identificador único
    nombre VARCHAR(255),                    -- Nombre de la parada
    direccion VARCHAR(255),                 -- Dirección física
    latitud DOUBLE,                         -- Ubicación geográfica
    longitud DOUBLE,                        -- Ubicación geográfica
    PRIMARY KEY (id)
);


-- Tabla con las tarifas vigentes del sistema
DROP TABLE IF EXISTS tarifa;

CREATE TABLE tarifa (
    id BIGINT NOT NULL AUTO_INCREMENT,      -- ID de la tarifa
    precio_por_km DOUBLE NOT NULL,          -- Costo por kilómetro
    precio_por_minuto DOUBLE NOT NULL,      -- Costo por minuto
    tarifa_extra_por_pausa DOUBLE NOT NULL, -- Costo extra por pausa
    fecha_inicio_vigencia DATE,             -- Fecha desde que aplica esta tarifa
    PRIMARY KEY (id)
);


-- Tabla principal de usuarios
DROP TABLE IF EXISTS usuario;

CREATE TABLE usuario (
    id BIGINT NOT NULL AUTO_INCREMENT,      -- ID único
    nombre VARCHAR(255),                    -- Nombre del usuario
    apellido VARCHAR(255),                  -- Apellido
    email VARCHAR(255),                     -- Correo
    telefono VARCHAR(255),                  -- Teléfono
    latitud DOUBLE,                         -- Ubicación actual
    longitud DOUBLE,                        -- Ubicación actual
    PRIMARY KEY (id)
);


-- Tabla de cuentas monetarias
DROP TABLE IF EXISTS cuenta;

CREATE TABLE cuenta (
    numero_identificatorio BIGINT NOT NULL AUTO_INCREMENT, -- ID único de cuenta
    tipo VARCHAR(255),                     -- Tipo (ej: prepaga, crédito, débito)
    saldo DOUBLE,                          -- Saldo disponible
    mercado_pago_id VARCHAR(255),          -- ID de MP vinculado
    fecha_alta DATE,                       -- Fecha de creación
    activa BOOLEAN DEFAULT TRUE,           -- Estado de la cuenta
    PRIMARY KEY (numero_identificatorio)
);


-- Tabla puente para relación muchos-a-muchos entre cuentas y usuarios
DROP TABLE IF EXISTS cuenta_usuario;

CREATE TABLE cuenta_usuario (
    cuenta_id BIGINT NOT NULL,             -- FK a cuenta
    usuario_id BIGINT NOT NULL,            -- FK a usuario
    PRIMARY KEY (cuenta_id, usuario_id),

    -- Relaciones con eliminación en cascada
    CONSTRAINT fk_cuenta_usuario_cuenta
        FOREIGN KEY (cuenta_id) REFERENCES cuenta(numero_identificatorio)
        ON DELETE CASCADE,

    CONSTRAINT fk_cuenta_usuario_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE
);


db_monopatines: schema
    + collections
        monopatines: collection
            + fields
                _id: String                   
                _class: String                
                estado: String                 
                kmTotales: Double              
                tiempoUsoTotal: Int64          
                paradaActualID: Int64          
                fechaAlta: Date                
                ultimoMantenimiento: Date      
                requiereMantenimiento: Boolean 
                latitud: Double                
                longitud: Double               
                kilometrajeMaximo: Double      


db_viajes: schema
    + collections
        viajes: collection
            + fields
                _id: ObjectId / String         
                _class: String                 
                usuarioId: Int64               
                monopatinId: String            
                paradaOrigenId: Int64          
                paradaDestinoId: Int64         
                fechaInicio: Date              
                fechaFin: Date                 
                kilometrosRecorridos: Double   
                pausaInicio: Date              
                totalSegundosPausa: Int64      
