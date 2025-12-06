/*
 * Este DAO se encarga de la comunicación directa con la base de datos
 * para la gestión de videos de ayuda.
 * Permite insertar, actualizar, consultar y listar videos de ayuda.
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 04/12/2025
 */
package com.vifac.sys.dao;

import com.vifac.sys.modelo.Ayuda;
import com.vifac.sys.util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AyudaDAO extends ConexionBD {

    private static final Logger LOGGER = Logger.getLogger(AyudaDAO.class.getName());

    // Insertar nuevo video de ayuda
    public boolean insertar(Ayuda a) {
        String sql = "INSERT INTO ayuda (titulo, url_video, url_imagen) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getTitulo());
            ps.setString(2, a.getUrlVideo());
            ps.setString(3, a.getUrlImagen());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar video de ayuda", e);
        }
        return false;
    }

    // Actualizar video existente por id
    public boolean actualizar(Ayuda a) {
        String sql = "UPDATE ayuda SET titulo = ?, url_video = ?, url_imagen = ? WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getTitulo());
            ps.setString(2, a.getUrlVideo());
            ps.setString(3, a.getUrlImagen());
            ps.setInt(4, a.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar video de ayuda", e);
        }
        return false;
    }
    
    // Eliminar video de ayuda por id
public boolean eliminar(int id) {
    String sql = "DELETE FROM ayuda WHERE id = ?";
    try (Connection conn = ConexionBD.obtenerConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, id);
        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error al eliminar video de ayuda", e);
    }
    return false;
}

    // Obtener video por id
    public Ayuda obtenerPorId(int id) {
        String sql = "SELECT * FROM ayuda WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapAyuda(rs);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener video de ayuda por id: " + id, e);
        }
        return null;
    }

    // Listar todos los videos
    public List<Ayuda> listarTodos() {
        List<Ayuda> lista = new ArrayList<>();
        String sql = "SELECT * FROM ayuda ORDER BY id ASC";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapAyuda(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al listar videos de ayuda", e);
        }
        return lista;
    }

    // Mapear ResultSet a objeto Ayuda
    private Ayuda mapAyuda(ResultSet rs) throws SQLException {
        Ayuda a = new Ayuda();
        a.setId(rs.getInt("id"));
        a.setTitulo(rs.getString("titulo"));
        a.setUrlVideo(rs.getString("url_video"));
        a.setUrlImagen(rs.getString("url_imagen"));
        return a;
    }
}
