CREATE DATABASE IF NOT EXISTS `defaultdb`;
USE `defaultdb`;

CREATE TABLE IF NOT EXISTS roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;
CREATE TABLE IF NOT EXISTS documentos_legales (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tipo ENUM('terminos_condiciones', 'politica_privacidad', 'politica_cancelacion', 'otro') NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    contenido TEXT NOT NULL,
    version VARCHAR(20) NOT NULL,
    fecha_vigencia DATE NOT NULL,
    esta_activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;
CREATE TABLE IF NOT EXISTS tipos_deporte (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    icono VARCHAR(500),
    esta_activo BOOLEAN DEFAULT TRUE
) ENGINE = InnoDB;
CREATE TABLE IF NOT EXISTS configuracion_sistema (
    id INT PRIMARY KEY AUTO_INCREMENT,
    clave VARCHAR(255) NOT NULL UNIQUE,
    valor TEXT NOT NULL,
    tipo VARCHAR(100),
    descripcion TEXT,
    es_publica BOOLEAN DEFAULT FALSE,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;
CREATE TABLE IF NOT EXISTS membresias (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    descripcion TEXT,
    precio_mensual DECIMAL(10, 2) NOT NULL,
    beneficios JSON,
    max_reservas_mes INT,
    descuento_porcentaje DECIMAL(5, 2),
    esta_activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;
CREATE TABLE IF NOT EXISTS usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol_id INT NOT NULL,
    esta_activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (rol_id) REFERENCES roles(id)
) ENGINE = InnoDB;
CREATE TABLE IF NOT EXISTS perfiles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL UNIQUE,
    nombre_completo VARCHAR(255) NULL,
    telefono VARCHAR(20),
    documento_identidad VARCHAR(50),
    fecha_nacimiento DATE,
    genero ENUM('masculino', 'femenino', 'otro', 'prefiero_no_decir'),
    foto_perfil VARCHAR(500),
    biografia TEXT,
    ciudad VARCHAR(100),
    pais VARCHAR(100),
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE = InnoDB;
CREATE TABLE IF NOT EXISTS usuarios_aceptacion_terminos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    documento_legal_id INT NOT NULL,
    ip_address VARCHAR(45),
    fecha_aceptacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (documento_legal_id) REFERENCES documentos_legales(id)
) ENGINE = InnoDB;
CREATE TABLE IF NOT EXISTS preferencias_usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL UNIQUE,
    tipo_deporte_id INT,
    nivel_juego VARCHAR(100),
    posicion_preferida VARCHAR(100),
    horario_preferido_inicio TIME,
    horario_preferido_fin TIME,
    dias_preferidos JSON,
    ciudad_preferida VARCHAR(255),
    radio_distancia_km INT,
    notificaciones_email BOOLEAN DEFAULT TRUE,
    notificaciones_push BOOLEAN DEFAULT TRUE,
    notificaciones_partidos BOOLEAN DEFAULT TRUE,
    notificaciones_torneos BOOLEAN DEFAULT TRUE,
    notificaciones_invitaciones BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (tipo_deporte_id) REFERENCES tipos_deporte(id)
) ENGINE = InnoDB;
CREATE TABLE IF NOT EXISTS usuario_membresias (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    membresia_id INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NULL,
    esta_activa BOOLEAN DEFAULT TRUE,
    renovacion_automatica BOOLEAN DEFAULT FALSE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (membresia_id) REFERENCES membresias(id)
) ENGINE = InnoDB;