package com.wearby.wearby_api.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión directa a la base de datos mediante JDBC.
 * Se usa en la capa DAO para operaciones que requieren control
 * manual sobre las consultas y transacciones.
 */
@Component
public class ConexionDB {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String usuario;

    @Value("${spring.datasource.password}")
    private String contrasena;

    public Connection getConexion() throws SQLException {
        return DriverManager.getConnection(url, usuario, contrasena);
    }
}