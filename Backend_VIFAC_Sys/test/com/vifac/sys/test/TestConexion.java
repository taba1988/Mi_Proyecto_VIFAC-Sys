/*
 * Prueba unitaria para validar la correcta conexión a la base de datos
 * mediante la clase ConexionBD.
 */
package com.vifac.sys.test;

import com.vifac.sys.util.ConexionBD;
import java.sql.Connection;
import java.sql.SQLException;

public class TestConexion {

    public static void main(String[] args) {
        try (Connection conn = ConexionBD.obtenerConexion()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Conexión exitosa a la base de datos.");
            } else {
                System.out.println("⚠️ La conexión está cerrada o es nula.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar a la base de datos:");
            e.printStackTrace();
        }
    }
}

