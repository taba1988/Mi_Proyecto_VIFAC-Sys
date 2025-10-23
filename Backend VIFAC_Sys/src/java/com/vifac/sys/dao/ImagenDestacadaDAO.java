/*
 * Clase DAO Propósito: Permite al administrador subir imágenes destacadas 
 * para el carrusel y actualizar individualmente sin duplicar registros.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 18/10/2025
 */

package com.vifac.sys.dao;

import com.vifac.sys.modelo.ImagenDestacada;
import com.vifac.sys.util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object para gestionar operaciones de ImagenDestacada en la base de datos.
 */
public class ImagenDestacadaDAO extends ConexionBD {

    private static final Logger LOGGER = Logger.getLogger(ImagenDestacadaDAO.class.getName());

    public boolean actualizarImagen(String nombreFijo, String nombreArchivo) {
        String sql = "UPDATE imagen_destacada SET nombre_archivo = ?, fecha_subida = NOW() WHERE nombre_fijo = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, nombreArchivo);
            stmt.setString(2, nombreFijo);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar imagen " + nombreFijo, e);
            return false;
        }
    }

    public boolean actualizarImagen(ImagenDestacada img) {
        return actualizarImagen(img.getNombreFijo(), img.getNombreArchivo());
    }

    public List<ImagenDestacada> obtenerTodas() {
        List<ImagenDestacada> lista = new ArrayList<>();
        String sql = "SELECT id, nombre_fijo, nombre_archivo, fecha_subida FROM imagen_destacada ORDER BY id ASC";

        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ImagenDestacada img = new ImagenDestacada();
                img.setId(rs.getInt("id"));
                img.setNombreFijo(rs.getString("nombre_fijo"));
                img.setNombreArchivo(rs.getString("nombre_archivo"));
                img.setFechaSubida(rs.getTimestamp("fecha_subida"));
                lista.add(img);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener imágenes destacadas", e);
        }
        return lista;
    }

    public boolean guardarImagen(ImagenDestacada img) {
        String sql = "INSERT INTO imagen_destacada (nombre_fijo, nombre_archivo, fecha_subida) VALUES (?, ?, NOW())";
        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, img.getNombreFijo());
            stmt.setString(2, img.getNombreArchivo());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar nueva imagen " + img.getNombreFijo(), e);
            return false;
        }
    }

    public boolean eliminarImagen(int id) {
        String sql = "DELETE FROM imagen_destacada WHERE id = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar imagen con id " + id, e);
            return false;
        }
    }

    public List<ImagenDestacada> listarTodas() {
        return obtenerTodas();
    }

    public ImagenDestacada obtenerPorNombreFijo(String nombreFijo) {
        String sql = "SELECT id, nombre_fijo, nombre_archivo, fecha_subida FROM imagen_destacada WHERE nombre_fijo = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, nombreFijo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ImagenDestacada img = new ImagenDestacada();
                    img.setId(rs.getInt("id"));
                    img.setNombreFijo(rs.getString("nombre_fijo"));
                    img.setNombreArchivo(rs.getString("nombre_archivo"));
                    img.setFechaSubida(rs.getTimestamp("fecha_subida"));
                    return img;
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener imagen por nombre fijo: " + nombreFijo, e);
        }
        return null;
    }
}
