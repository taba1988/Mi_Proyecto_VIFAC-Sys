/*
 * Este DAO (Objeto de Acceso a Datos) se encarga de la comunicación
 * directa con la base de datos para la gestión de notificaciones.
 * Permite registrar, consultar y actualizar el estado de las notificaciones
 * generadas por acciones del sistema (ventas, clientes, usuarios, inventario).
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 06/12/2025
 */
package com.vifac.sys.dao;

import com.vifac.sys.modelo.Notificacion;
import com.vifac.sys.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificacionDAO extends ConexionBD {

    private static final Logger LOGGER = Logger.getLogger(NotificacionDAO.class.getName());

    // Registrar notificación
    public boolean registrar(Notificacion n) {
        String sql = "INSERT INTO notificacion (idUsuario, mensaje, tipo, referencia_id, leido, fecha_creacion) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, n.getIdUsuario());
            ps.setString(2, n.getMensaje());
            ps.setString(3, n.getTipo());

            if (n.getReferenciaId() != null) {
                ps.setInt(4, n.getReferenciaId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            ps.setBoolean(5, n.isLeido());
            ps.setTimestamp(6, n.getFechaCreacion());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al registrar notificación", e);
        }
        return false;
    }

    // Listar notificaciones por usuario
    public List<Notificacion> listarPorUsuario(int idUsuario) {
        List<Notificacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM notificacion WHERE idUsuario = ? ORDER BY fecha_creacion DESC";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapNotificacion(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al listar notificaciones usuario: " + idUsuario, e);
        }
        return lista;
    }

    // Marcar notificación como leída
    public boolean marcarLeida(int idNotificacion) {
        String sql = "UPDATE notificacion SET leido = 1 WHERE idNotificacion = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idNotificacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al marcar notificación como leída", e);
        }
        return false;
    }

    // Contar notificaciones no leídas por usuario
    public int contarNoLeidas(int idUsuario) {
        String sql = "SELECT COUNT(*) FROM notificacion WHERE idUsuario = ? AND leido = 0";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al contar notificaciones no leídas", e);
        }
        return 0;
    }

    // Mapear ResultSet a objeto Notificacion
    private Notificacion mapNotificacion(ResultSet rs) throws SQLException {
        Notificacion n = new Notificacion();
        n.setIdNotificacion(rs.getInt("idNotificacion"));
        n.setIdUsuario(rs.getInt("idUsuario"));
        n.setMensaje(rs.getString("mensaje"));
        n.setTipo(rs.getString("tipo"));

        Object ref = rs.getObject("referencia_id");
        n.setReferenciaId(ref != null ? rs.getInt("referencia_id") : null);

        n.setLeido(rs.getBoolean("leido"));
        n.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        return n;
    }
}
