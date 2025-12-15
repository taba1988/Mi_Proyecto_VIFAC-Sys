// La clase establece conexión con MySQL adecuadamente.
// Asegúrar de que la contraseña sea correcta
// y que el conector MySQL esté incluido en el classpath del proyecto (mysql-connector-java).

package com.vifac.sys.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Datos de configuración para la conexión a la base de datos
    private static final String URL_BD = "jdbc:mysql://localhost:3306/vifac_sys_bd?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO_BD = "root";
    private static final String CONTRASENA_BD = "Sagitario1988#";

    // Carga del driver JDBC requerido para la conexión
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
        }
    }

    // Método principal para establecer conexión con la base de datos
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL_BD, USUARIO_BD, CONTRASENA_BD);
    }

    // Método alterno para obtener la conexión utilizando el método principal
    public static Connection getConexion() {
        try {
            return obtenerConexion();
        } catch (SQLException e) {
            return null;
        }
    }

    // Método adicional de conexión; retorna null si ocurre algún error
    public static Connection getConnection() {
        try {
            return obtenerConexion();
        } catch (SQLException e) {
            return null;
        }
    }
}
