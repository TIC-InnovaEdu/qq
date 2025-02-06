CREATE DATABASE IF NOT EXISTS BD_PROQQ;

-- Usar la base de datos
USE BD_PROQQ;

-- Tabla: usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    clave VARCHAR(255) NOT NULL,
    estado CHAR(1) NOT NULL DEFAULT 'A' COMMENT 'A: Activo, I: Inactivo'
);

-- Tabla: personas
CREATE TABLE IF NOT EXISTS personas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    puntuacion_actual INT DEFAULT 0,
    puntuacion_maxima INT DEFAULT 0,
    usuario_id INT UNIQUE NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla: catalogos
CREATE TABLE IF NOT EXISTS catalogos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_catalogo VARCHAR(100) NOT NULL,
    id_codigo_catalogo VARCHAR(100) NOT NULL UNIQUE, -- Añadido UNIQUE
    descripcion VARCHAR(250) NOT NULL,
    estado CHAR(1) NOT NULL DEFAULT 'A' COMMENT 'A: Activo, I: Inactivo'
);

-- Tabla: preguntas
CREATE TABLE IF NOT EXISTS preguntas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_codigo_catalogo_tema VARCHAR(100) NOT NULL,
    texto_pregunta TEXT NOT NULL,
    estado CHAR(1) NOT NULL DEFAULT 'A' COMMENT 'A: Activa, I: Inactiva',
    FOREIGN KEY (id_codigo_catalogo_tema) REFERENCES catalogos(id_codigo_catalogo) ON DELETE CASCADE
);

-- Tabla: respuestas
CREATE TABLE IF NOT EXISTS respuestas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pregunta INT NOT NULL,
    texto_respuesta TEXT NOT NULL,
    tipo_respuesta CHAR(1) NOT NULL COMMENT 'C: Correcta, I: Incorrecta',
    FOREIGN KEY (id_pregunta) REFERENCES preguntas(id) ON DELETE CASCADE
);

-- Tabla: partidas
CREATE TABLE IF NOT EXISTS partidas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario_anfitrion INT NOT NULL,
    id_usuario_invitado INT NOT NULL,
    fecha_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_fin TIMESTAMP,
    estado CHAR(1) NOT NULL DEFAULT 'A' COMMENT 'A: Activa, I: Inactiva',
    FOREIGN KEY (id_usuario_anfitrion) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (id_usuario_invitado) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabla: historial_partidas
CREATE TABLE IF NOT EXISTS historial_partidas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_partida INT NOT NULL,
    id_pregunta INT NOT NULL,
    id_respuesta_anfitrion INT,
    id_respuesta_invitado INT,
    fecha_respuesta_anfitrion TIMESTAMP,
    fecha_respuesta_invitado TIMESTAMP,
    puntaje_anfitrion INT DEFAULT 0,
    puntaje_invitado INT DEFAULT 0,
    FOREIGN KEY (id_partida) REFERENCES partidas(id) ON DELETE CASCADE,
    FOREIGN KEY (id_pregunta) REFERENCES preguntas(id) ON DELETE CASCADE,
    FOREIGN KEY (id_respuesta_anfitrion) REFERENCES respuestas(id) ON DELETE SET NULL,
    FOREIGN KEY (id_respuesta_invitado) REFERENCES respuestas(id) ON DELETE SET NULL
);
