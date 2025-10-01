/*
 * Este DAO (Objeto de Acceso a Datos) se encarga de la comunicación
 * directa con la base de datos para la gestión de proveedores.
 * Su función principal es validar la información registrada.
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 6/09/2024
 */

package com.vifac.sys.dao;

import com.vifac.sys.modelo.Proveedor;
import com.vifac.sys.util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProveedorDAO extends ConexionBD {

    private static final Logger LOGGER = Logger.getLogger(ProveedorDAO.class.getName());

    // Agregar proveedor
    public boolean agregarProveedor(Proveedor p) {
        String sql = "INSERT INTO proveedor(nombreEmpresa, documento_NIT, asesor, telefono, email, dia_visita, estado, fecha_registro) " + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, p.getNombreEmpresa());
            stmt.setString(2, p.getDocumento_NIT());
            stmt.setString(3, p.getAsesor());
            stmt.setString(4, p.getTelefono());
            stmt.setString(5, p.getEmail());
            stmt.setString(6, p.getDiaVisita());
            stmt.setString(7, p.getEstado());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al agregar proveedor", e);
        }
        return false;
    }

    // Listar proveedores
    public List<Proveedor> listarProveedores() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedor";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Proveedor p = mapProveedor(rs);
                lista.add(p);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al listar proveedores", e);
        }

        return lista;
    }

    // Buscar proveedor
    public List<Proveedor> buscarProveedores(String criterio) {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedor WHERE nombreEmpresa LIKE ? OR `documento_NIT` LIKE ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            String busqueda = "%" + criterio + "%";
            stmt.setString(1, busqueda);
            stmt.setString(2, busqueda);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Proveedor p = mapProveedor(rs);
                lista.add(p);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar proveedores", e);
        }

        return lista;
    }

    // Actualizar proveedor
    public boolean actualizarProveedor(Proveedor p) {
        String sql = "UPDATE proveedor SET nombreEmpresa=?, `documento_NIT`=?, asesor=?, telefono=?, email=?, dia_visita=?, estado=? WHERE idProveedor=?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, p.getNombreEmpresa());
            stmt.setString(2, p.getDocumento_NIT());
            stmt.setString(3, p.getAsesor());
            stmt.setString(4, p.getTelefono());
            stmt.setString(5, p.getEmail());
            stmt.setString(6, p.getDiaVisita());
            stmt.setString(7, p.getEstado());
            stmt.setInt(8, p.getIdProveedor());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar proveedor con ID: " + p.getIdProveedor(), e);
        }

        return false;
    }

    // Eliminar proveedor
    public boolean eliminarProveedor(int idProveedor) {
        String sql = "DELETE FROM proveedor WHERE idProveedor=?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, idProveedor);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar proveedor con ID: " + idProveedor, e);
        }

        return false;
    }
    
    // selecionar estado
    public boolean marcarEstado(int idProveedor, String nuevoEstado) {
        String sql = "UPDATE proveedor SET estado = ? WHERE idProveedor = ?";
    
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
    
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idProveedor);
    
            return stmt.executeUpdate() > 0;
    
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al selecionar estado  del proveedor con ID: " + idProveedor, e);
        }
    
        return false;
    }
    
    // Marcar proveedor como "Cumplida"
public boolean marcarCumplida(int idProveedor) {
    String sql = "UPDATE proveedor SET estado_cumplimiento = 'Cumplida' WHERE idProveedor = ?";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement stmt = conexion.prepareStatement(sql)) {

        stmt.setInt(1, idProveedor);

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error al marcar estado 'Cumplida' del proveedor con ID: " + idProveedor, e);
    }

    return false;
}

// Marcar proveedor como "Incumplida"
public boolean marcarIncumplida(int idProveedor) {
    String sql = "UPDATE proveedor SET estado_cumplimiento = 'Incumplida' WHERE idProveedor = ?";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement stmt = conexion.prepareStatement(sql)) {

        stmt.setInt(1, idProveedor);

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error al marcar estado 'Incumplida' del proveedor con ID: " + idProveedor, e);
    }

    return false;
}
 
    // Método auxiliar para mapear ResultSet a objeto Proveedor
    private Proveedor mapProveedor(ResultSet rs) throws SQLException {
        Proveedor p = new Proveedor();
        p.setIdProveedor(rs.getInt("idProveedor"));
        p.setNombreEmpresa(rs.getString("nombreEmpresa"));
        p.setDocumentoNIT(rs.getString("documento_NIT"));
        p.setAsesor(rs.getString("asesor"));
        p.setTelefono(rs.getString("telefono"));
        p.setEmail(rs.getString("email"));
        p.setDiaVisita(rs.getString("dia_visita"));
        p.setEstado(rs.getString("estado"));
        return p;
    }
}
