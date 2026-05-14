-- =============================================
-- WEARBY — Script de creación de base de datos
-- Versión: 1.0 | Mayo 2026
-- =============================================

CREATE DATABASE IF NOT EXISTS wearby
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE wearby;

-- =============================================
-- TABLAS DE CARACTERÍSTICAS
-- =============================================

CREATE TABLE IF NOT EXISTS categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS color (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS estilo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS formalidad (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS temporada (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

-- =============================================
-- TABLAS PRINCIPALES
-- =============================================

CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol ENUM('usuario', 'admin') NOT NULL DEFAULT 'usuario',
    activo TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS prenda (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    imagen_url VARCHAR(500),
    favorito TINYINT(1) NOT NULL DEFAULT 0,
    notas TEXT,
    id_categoria INT,
    id_color INT,
    id_estilo INT,
    id_formalidad INT,
    id_temporada INT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id),
    FOREIGN KEY (id_color) REFERENCES color(id),
    FOREIGN KEY (id_estilo) REFERENCES estilo(id),
    FOREIGN KEY (id_formalidad) REFERENCES formalidad(id),
    FOREIGN KEY (id_temporada) REFERENCES temporada(id)
);

CREATE TABLE IF NOT EXISTS outfit (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    fecha_generacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS outfit_prenda (
    id_outfit INT NOT NULL,
    id_prenda INT NOT NULL,
    PRIMARY KEY (id_outfit, id_prenda),
    FOREIGN KEY (id_outfit) REFERENCES outfit(id) ON DELETE CASCADE,
    FOREIGN KEY (id_prenda) REFERENCES prenda(id) ON DELETE CASCADE
);

-- =============================================
-- DATOS INICIALES — CARACTERÍSTICAS
-- =============================================

INSERT IGNORE INTO categoria (nombre) VALUES
    ('Camiseta'),
    ('Pantalón'),
    ('Vestido'),
    ('Abrigo'),
    ('Zapatos'),
    ('Accesorios'),
    ('Falda'),
    ('Sudadera'),
    ('Chaqueta'),
    ('Shorts');

INSERT IGNORE INTO color (nombre) VALUES
    ('Blanco'),
    ('Negro'),
    ('Azul'),
    ('Rojo'),
    ('Verde'),
    ('Amarillo'),
    ('Gris'),
    ('Beige'),
    ('Marrón'),
    ('Rosa'),
    ('Naranja'),
    ('Morado');

INSERT IGNORE INTO estilo (nombre) VALUES
    ('Casual'),
    ('Formal'),
    ('Deportivo'),
    ('Elegante');

INSERT IGNORE INTO formalidad (nombre) VALUES
    ('Informal'),
    ('Semi-formal'),
    ('Formal');

INSERT IGNORE INTO temporada (nombre) VALUES
    ('Verano'),
    ('Invierno'),
    ('Primavera'),
    ('Otoño'),
    ('Todo el año');

-- =============================================
-- PROCEDIMIENTO ALMACENADO
-- =============================================

DROP PROCEDURE IF EXISTS generarOutfit;

DELIMITER $$

CREATE PROCEDURE generarOutfit(
    IN p_usuarioId INT,
    IN p_categoriaId INT,
    IN p_estiloId INT,
    IN p_temporadaId INT,
    IN p_formalidadId INT
)
BEGIN
    SELECT
        p.id,
        p.nombre,
        p.imagen_url AS imagenUrl,
        p.favorito,
        p.notas,
        c.id AS categoria_id,
        c.nombre AS categoria_nombre,
        co.id AS color_id,
        co.nombre AS color_nombre,
        e.id AS estilo_id,
        e.nombre AS estilo_nombre,
        f.id AS formalidad_id,
        f.nombre AS formalidad_nombre,
        t.id AS temporada_id,
        t.nombre AS temporada_nombre
    FROM prenda p
    LEFT JOIN categoria c ON p.id_categoria = c.id
    LEFT JOIN color co ON p.id_color = co.id
    LEFT JOIN estilo e ON p.id_estilo = e.id
    LEFT JOIN formalidad f ON p.id_formalidad = f.id
    LEFT JOIN temporada t ON p.id_temporada = t.id
    WHERE p.id_usuario = p_usuarioId
      AND (p_categoriaId IS NULL OR p.id_categoria = p_categoriaId)
      AND (p_estiloId IS NULL OR p.id_estilo = p_estiloId)
      AND (p_temporadaId IS NULL OR p.id_temporada = p_temporadaId)
      AND (p_formalidadId IS NULL OR p.id_formalidad = p_formalidadId)
    ORDER BY p.favorito DESC;
END$$

DELIMITER ;

-- =============================================
-- FIN DEL SCRIPT
-- =============================================