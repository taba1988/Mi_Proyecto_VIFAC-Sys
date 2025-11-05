/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

