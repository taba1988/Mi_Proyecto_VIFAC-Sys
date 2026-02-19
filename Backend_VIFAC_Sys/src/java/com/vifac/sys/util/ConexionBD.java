/**
 * Clase encargada de gestionar la conexión a la base de datos MySQL.
 * 
 * ✔ Compatible con entorno LOCAL (XAMPP)
 * ✔ Compatible con entorno NUBE (Render + Clever Cloud)
 * ✔ Maneja reconexión automática cuando MySQL está en estado "sleep"
 * ✔ Implementa reintentos para evitar fallos cuando el servidor despierta
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
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
     * Método auxiliar para obtener variables de entorno.
     * Si no existen (ejecución local), usa el valor por defecto.
     */
    private static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
    }

    // ================================
    // CONFIGURACIÓN DE CONEXIÓN
    // ================================

    /*
     * En Render:
     *   DB_URL
     *   DB_USER
     *   DB_PASS
     *
     * En Local (XAMPP):
     *   Se usan los valores por defecto.
     */

    private static final String URL_BD = getEnv(
            "DB_URL",
            "jdbc:mysql://localhost:3306/vifac_sys_bd?useSSL=false&serverTimezone=UTC"
    );

    private static final String USUARIO_BD = getEnv("DB_USER", "root");

    private static final String CONTRASENA_BD = getEnv("DB_PASS", "");

    // ================================
    // CARGA DEL DRIVER (solo una vez)
    // ================================

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.info("Driver MySQL cargado correctamente.");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "ERROR: No se encontró el Driver de MySQL (mysql-connector-j).", e);
        }
    }

    // ================================
    // MÉTODO PRINCIPAL DE CONEXIÓN
    // ================================

    /**
     * Obtiene una conexión activa a la base de datos.
     *
     * Implementa lógica de reintento para cuando:
     * - Clever Cloud tiene MySQL en estado sleep.
     * - La primera conexión falla mientras el servidor despierta.
     *
     * @return Connection activa
     * @throws SQLException si no logra conectarse después de varios intentos
     */
    public static Connection obtenerConexion() throws SQLException {

        int intentos = 0;
        int maxIntentos = 3;
        int tiempoEspera = 5000; // 5 segundos

        while (intentos < maxIntentos) {
            try {

                if (System.getenv("DB_URL") != null) {
                    LOGGER.log(Level.INFO, "Intentando conexión a BD remota: {0}", URL_BD);
                } else {
                    LOGGER.log(Level.INFO, "Intentando conexión a BD local.");
                }

                Connection conexion = DriverManager.getConnection(URL_BD, USUARIO_BD, CONTRASENA_BD);

                LOGGER.info("Conexión a base de datos exitosa.");
                return conexion;

            } catch (SQLException e) {

                intentos++;
                LOGGER.log(Level.WARNING,
                        "Intento de conexión fallido (" + intentos + "/" + maxIntentos + "). Reintentando...",
                        e);

                if (intentos >= maxIntentos) {
                    LOGGER.log(Level.SEVERE,
                            "No se pudo establecer conexión con la base de datos después de varios intentos.");
                    throw e;
                }

                try {
                    Thread.sleep(tiempoEspera);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        throw new SQLException("Error inesperado al intentar conectar a la base de datos.");
    }

    // ================================
    // MÉTODOS DE COMPATIBILIDAD
    // ================================

    /**
     * Método alternativo compatible con código antiguo.
     * @return 
     */
    public static Connection getConexion() {
        try {
            return obtenerConexion();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error en getConexion()", e);
            return null;
        }
    }

    /**
     * Método alternativo compatible con código antiguo.
     * @return 
     */
    public static Connection getConnection() {
        try {
            return obtenerConexion();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error en getConnection()", e);
            return null;
        }
    }
}
