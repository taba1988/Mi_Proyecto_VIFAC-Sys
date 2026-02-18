/**
 * Clase encargada de gestionar la conexión a la base de datos MySQL.
 * Implementa una lógica de detección de entorno: funciona en Local (XAMPP) 
 * y en la Nube (Render/Clever Cloud) automáticamente.
 * * Autor: ORLANDUVALIE TABARES GUTIERREZ
 */

package com.vifac.sys.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionBD {

    private static final Logger LOGGER = Logger.getLogger(ConexionBD.class.getName());

    /**
     * Helper para obtener variables de entorno. 
     * Si la variable no existe o está vacía, usa el valor por defecto (Local).
     */
    private static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
    }

    // --- CONFIGURACIÓN DE CONEXIÓN ---
    // En Render, estas variables se sacan del panel "Environment".
    // En Local, se usan los valores de XAMPP.
    
    private static final String URL_BD = getEnv("DB_URL", 
            "jdbc:mysql://localhost:3306/vifac_sys_bd?useSSL=false&serverTimezone=UTC");

    private static final String USUARIO_BD = getEnv("DB_USER", "root");

    private static final String CONTRASENA_BD = getEnv("DB_PASS", "Sagitario1988#");

    // Bloque estático para cargar el driver una sola vez
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "ERROR: No se encontró el Driver de MySQL (mysql-connector-j).", e);
        }
    }

    /**
     * Método principal para obtener la conexión.
     * @return Connection objeto de conexión activo.
     * @throws SQLException si las credenciales en Render o Local están mal.
     */
    public static Connection obtenerConexion() throws SQLException {
        try {
            // Log para verificar en Render qué URL se está intentando usar (sin mostrar password)
            if (System.getenv("DB_URL") != null) {
                LOGGER.log(Level.INFO, "Conectando a base de datos remota: {0}", URL_BD);
            }
            return DriverManager.getConnection(URL_BD, USUARIO_BD, CONTRASENA_BD);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "FALLO DE CONEXIÓN: Verifica las variables DB_URL, DB_USER y DB_PASS en Render.", e);
            throw e;
        }
    }

    /**
     * Métodos de compatibilidad para el resto del proyecto.
     * @return 
     */
    public static Connection getConexion() {
        try {
            return obtenerConexion();
        } catch (SQLException e) {
            return null;
        }
    }

    public static Connection getConnection() {
        try {
            return obtenerConexion();
        } catch (SQLException e) {
            return null;
        }
    }
}