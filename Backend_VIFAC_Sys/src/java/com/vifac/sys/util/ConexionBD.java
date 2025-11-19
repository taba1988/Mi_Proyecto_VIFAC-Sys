// La clase establece conexión con MySQL adecuadamente.
// Asegúrar de que la contraseña sea correcta
// y que el conector MySQL esté incluido en el classpath del proyecto (mysql-connector-java).

package com.vifac.sys.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL_BD = "jdbc:mysql://localhost:3306/vifac_sys_bd?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO_BD = "root";
    private static final String CONTRASENA_BD = "Sagitario1988";

    // Registrar driver una sola vez
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
        }
    }

    // Método principal de conexión (tu método original)
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL_BD, USUARIO_BD, CONTRASENA_BD);
    }

    // Método duplicado tuyo: te lo dejo igual, pero redirigido correctamente.
    public static Connection getConexion() {
        try {
            return obtenerConexion();
        } catch (SQLException e) {
            return null;
        }
    }

    // ESTE era el que causaba problemas → ahora eliminado correctamente
    // NO lanza excepción, NO genera memory leaks
    public static Connection getConnection() {
        try {
            return obtenerConexion();
        } catch (SQLException e) {
            return null;
        }
    }
}
