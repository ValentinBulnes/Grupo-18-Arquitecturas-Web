-- Tabla de facturación general
DROP TABLE IF EXISTS factura;
CREATE TABLE factura (
    id BIGINT NOT NULL AUTO_INCREMENT,     
    fecha_emision DATE NOT NULL,           
    monto_total DOUBLE NOT NULL,           
    descripcion VARCHAR(255),              
    viaje_id VARCHAR(255),                 
    cuenta_id BIGINT NOT NULL,             
    usuario_id BIGINT NOT NULL,            
    PRIMARY KEY (id)
);

INSERT INTO factura (fecha_emision, monto_total, descripcion, viaje_id, cuenta_id, usuario_id)
VALUES
('2024-03-01', 1500, 'Viaje urbano', '670abf9123afbc12de447901', 1, 1),
('2024-03-03', 1800, 'Viaje con pausa', '670abfa5123afbc12de447905', 2, 3);

-- Tabla de paradas estándar
DROP TABLE IF EXISTS parada;

CREATE TABLE parada (
    id BIGINT NOT NULL AUTO_INCREMENT,     
    nombre VARCHAR(255),                   
    direccion VARCHAR(255),                
    latitud DOUBLE,                        
    longitud DOUBLE,                       
    PRIMARY KEY (id)
);

INSERT INTO parada (nombre, direccion, latitud, longitud)
VALUES
('Parada Central', 'Av. Siempre Viva 123', -34.603, -58.381),
('Parada Norte',   'Calle 9 de Julio 456', -34.610, -58.400),
('Parada Sur',     'San Martín 789',       -34.620, -58.360);

-- Tabla con las tarifas vigentes del sistema
DROP TABLE IF EXISTS tarifa;

CREATE TABLE tarifa (
    id BIGINT NOT NULL AUTO_INCREMENT,     
    precio_por_km DOUBLE NOT NULL,         
    precio_por_minuto DOUBLE NOT NULL,     
    tarifa_extra_por_pausa DOUBLE NOT NULL,
    fecha_inicio_vigencia DATE,            
    PRIMARY KEY (id)
);

INSERT INTO tarifa (precio_por_km, precio_por_minuto, tarifa_extra_por_pausa, fecha_inicio_vigencia)
VALUES (50, 10, 5, '2024-01-01');

-- Tabla principal de usuarios
DROP TABLE IF EXISTS usuario;

CREATE TABLE usuario (
    id BIGINT NOT NULL AUTO_INCREMENT,    
    nombre VARCHAR(255),                  
    apellido VARCHAR(255),                
    email VARCHAR(255),                   
    telefono VARCHAR(255),                
    latitud DOUBLE,                       
    longitud DOUBLE,                      
    PRIMARY KEY (id)
);

INSERT INTO usuario (nombre, apellido, email, telefono, latitud, longitud)
VALUES 
('Ana', 'Martínez', 'ana@mail.com', '111-222-3333', -34.6037, -58.3816),
('Luis', 'Pérez', 'luis@mail.com', '222-333-4444', -34.6100, -58.3900),
('Carla', 'Gómez', 'carla@mail.com', '333-444-5555', -34.6200, -58.3700);

-- Tabla de cuentas monetarias
DROP TABLE IF EXISTS cuenta;

CREATE TABLE cuenta (
    numero_identificatorio BIGINT NOT NULL AUTO_INCREMENT, 
    tipo VARCHAR(255),                    
    saldo DOUBLE,                         
    mercado_pago_id VARCHAR(255),         
    fecha_alta DATE,                      
    activa BOOLEAN DEFAULT TRUE,          
    PRIMARY KEY (numero_identificatorio)
);

INSERT INTO cuenta (tipo, saldo, mercado_pago_id, fecha_alta, activa)
VALUES
('BASICA', 5000, 'MPA123', '2024-01-15', TRUE),
('PREMIUM', 20000, 'MPA456', '2024-02-10', TRUE);

-- Tabla puente para relación muchos-a-muchos entre cuentas y usuarios
DROP TABLE IF EXISTS cuenta_usuario;

CREATE TABLE cuenta_usuario (
    cuenta_id BIGINT NOT NULL,           
    usuario_id BIGINT NOT NULL,          
    PRIMARY KEY (cuenta_id, usuario_id),

    -- Relaciones con eliminación en cascada
    CONSTRAINT fk_cuenta_usuario_cuenta
        FOREIGN KEY (cuenta_id) REFERENCES cuenta(numero_identificatorio)
        ON DELETE CASCADE,

    CONSTRAINT fk_cuenta_usuario_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE
);

INSERT INTO cuenta_usuario (cuenta_id, usuario_id)
VALUES
(1, 1),
(1, 2),
(2, 3);

-- Dumping db: db_monopatines
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


-- Dumping db: db_viajes
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
