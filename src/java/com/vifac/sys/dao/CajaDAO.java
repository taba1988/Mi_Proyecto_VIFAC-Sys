/*
 * Este DAO (Objeto de Acceso a Datos) se encarga de la comunicación
 * directa con la base de datos para la gestión de caja.
 * Su función principal es validar la información registrada.
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 6/09/2024
 */
package com.vifac.sys.dao;

import com.vifac.sys.modelo.Caja;
import com.vifac.sys.util.ConexionBD;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CajaDAO extends ConexionBD {

    private static final Logger LOGGER = Logger.getLogger(CajaDAO.class.getName());

    // Abrir caja
    public boolean abrirCaja(Caja c) {
        String sql = "INSERT INTO caja (fecha_apertura, monto_inicial, idUsuario, observaciones) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(c.getFechaApertura()));
            stmt.setDouble(2, c.getMontoInicial());
            stmt.setInt(3, c.getIdUsuario());
            stmt.setString(4, c.getObservaciones());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al abrir caja", e);
        }
        return false;
    }

    // Cerrar caja
    public boolean cerrarCaja(Caja c) {
        String sql = "UPDATE caja SET monto_final = ?, fecha_cierre = ?, observaciones = ? WHERE idCaja = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setDouble(1, c.getMontoFinal());
            stmt.setTimestamp(2, Timestamp.valueOf(c.getFechaCierre()));
            stmt.setString(3, c.getObservaciones());
            stmt.setInt(4, c.getIdCaja());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al cerrar caja", e);
        }
        return false;
    }

    // Verifica si un usuario ya tiene una caja abierta
    public boolean hayCajaAbierta(int idUsuario) {
        String sql = "SELECT COUNT(*) FROM caja WHERE idUsuario = ? AND fecha_cierre IS NULL";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; 
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al verificar caja abierta usuario: " + idUsuario, e);
        }
        return false; 
    }

    // Obtiene la caja actualmente abierta para un usuario específico
    
public Caja obtenerCajaActivaPorUsuario(int idUsuario) {
    String sql = "SELECT * FROM caja WHERE idUsuario = ? AND (fecha_cierre IS NULL OR fecha_cierre = '0000-00-00 00:00:00') ORDER BY fecha_apertura DESC LIMIT 1";
    try (Connection conn = ConexionBD.obtenerConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idUsuario);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapCaja(rs);
            }
        }
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error al obtener caja activa usuario: " + idUsuario, e);
    }
    return null;
}

    // Listar todas las cajas
    public List<Caja> listarCajas() {
        List<Caja> lista = new ArrayList<>();
        String sql = "SELECT * FROM caja ORDER BY fecha_apertura DESC";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapCaja(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al listar cajas", e);
        }
        return lista;
    }

    // Mapear ResultSet a objeto Caja
    private Caja mapCaja(ResultSet rs) throws SQLException {
        Caja c = new Caja();
        c.setIdCaja(rs.getInt("idCaja"));
        Timestamp apertura = rs.getTimestamp("fecha_apertura");
        c.setFechaApertura(apertura != null ? apertura.toLocalDateTime() : null);
        Timestamp cierre = rs.getTimestamp("fecha_cierre");
        c.setFechaCierre(cierre != null ? cierre.toLocalDateTime() : null);
        c.setMontoInicial(rs.getDouble("monto_inicial"));
        Object montoFinal = rs.getObject("monto_final");
        c.setMontoFinal(montoFinal != null ? rs.getDouble("monto_final") : null);
        c.setObservaciones(rs.getString("observaciones"));
        c.setIdUsuario(rs.getInt("idUsuario"));
        return c;
    }
}