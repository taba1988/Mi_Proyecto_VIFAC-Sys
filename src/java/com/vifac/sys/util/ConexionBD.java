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

    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL_BD, USUARIO_BD, CONTRASENA_BD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Error: No se encontró el driver de MySQL.", e);
        }
    }

public static Connection getConexion() {
    try {
        return obtenerConexion();
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}
}