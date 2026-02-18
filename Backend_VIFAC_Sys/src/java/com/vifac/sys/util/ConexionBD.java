
/**
 * Clase encargada de gestionar la conexión a la base de datos MySQL.
 * Implementa una lógica de detección de entorno: funciona en Local (XAMPP) 
 * y en la Nube (Render) sin necesidad de modificar el código.
 */

package com.vifac.sys.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    /* * CONFIGURACIÓN DINÁMICA:
     * El método System.getenv() busca variables en el servidor (Render).
     * Si no las encuentra (como en tu PC), usa los valores por defecto tras el operador ":".
     */

    // URL de conexión: Prioriza la de Render/Clever Cloud; si es nula, usa localhost.
    private static final String URL_BD = System.getenv("DB_URL") != null 
            ? System.getenv("DB_URL") 
            : "jdbc:mysql://localhost:3306/vifac_sys_bd?useSSL=false&serverTimezone=UTC";

    // Usuario: Usa el de la nube o 'root' por defecto para XAMPP.
    private static final String USUARIO_BD = System.getenv("DB_USER") != null 
            ? System.getenv("DB_USER") 
            : "root";

    // Contraseña: Usa la de la nube o tu clave local de XAMPP.
    private static final String CONTRASENA_BD = System.getenv("DB_PASS") != null 
            ? System.getenv("DB_PASS") 
            : "Sagitario1988#";

    // Bloque estático para registrar el Driver de MySQL una sola vez al cargar la clase.
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: No se encontró el Driver de MySQL (mysql-connector-java).");
        }
    }

    /**
     * Establece y retorna la conexión activa con la base de datos.
     * @return Connection objeto de conexión.
     * @throws SQLException si las credenciales o la URL son incorrectas.
     */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL_BD, USUARIO_BD, CONTRASENA_BD);
    }

    /**
     * Intenta obtener una conexión; si falla, captura la excepción y retorna null.Útil para evitar que la aplicación se detenga por completo ante un error de red.
     * @return
     */
    public static Connection getConexion() {
        try {
            return obtenerConexion();
        } catch (SQLException e) {
            System.err.println("Error en getConexion(): " + e.getMessage());
            return null;
        }
    }

    /**
     * Método adicional de compatibilidad para asegurar la conectividad 
     * desde cualquier parte del proyecto.
     * @return 
     */
    public static Connection getConnection() {
        try {
            return obtenerConexion();
        } catch (SQLException e) {
            System.err.println("Error en getConnection(): " + e.getMessage());
            return null;
        }
    }
}