/*
 * Este DAO (Objeto de Acceso a Datos) se encarga de la comunicación
 * directa con la base de datos para la gestión de clientes.
 * Su función principal es validar la información registrada.
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 6/09/2024
 */

package com.vifac.sys.dao;

import com.vifac.sys.modelo.Clientes;
import com.vifac.sys.util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientesDAO extends ConexionBD {

    private static final Logger LOGGER = Logger.getLogger(ClientesDAO.class.getName());

    // Agregar cliente
    public boolean agregarCliente(Clientes c) {
        String sql = "INSERT INTO cliente(razon_social, documento_NIT, telefono, direccion, email, actividad_economica, responsabilidad_iva, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, c.getRazon_social());
            stmt.setString(2, c.getDocumento_NIT());
            stmt.setString(3, c.getTelefono());
            stmt.setString(4, c.getDireccion());
            stmt.setString(5, c.getEmail());
            stmt.setString(6, c.getActividad_economica());
            stmt.setString(7, c.getResponsabilidad_iva());
            stmt.setString(8, c.getEstado());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al agregar cliente", e);
        }
        return false;
    }

    // Listar clientes
    public List<Clientes> listarClientes() {
        List<Clientes> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Clientes c = mapCliente(rs);
                lista.add(c);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al listar clientes", e);
        }

        return lista;
    }

    // Buscar cliente
    public List<Clientes> buscarClientes(String criterio) {
        List<Clientes> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE razon_social LIKE ? OR documento_NIT LIKE ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            String busqueda = "%" + criterio + "%";
            stmt.setString(1, busqueda);
            stmt.setString(2, busqueda);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Clientes c = mapCliente(rs);
                lista.add(c);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar clientes", e);
        }

        return lista;
    }

    // Actualizar cliente
    public boolean actualizarCliente(Clientes c) {
        String sql = "UPDATE cliente SET razon_social=?, documento_NIT=?, telefono=?, direccion=?, email=?, actividad_economica=?, responsabilidad_iva=?, estado=? WHERE idCliente=?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, c.getRazon_social());
            stmt.setString(2, c.getDocumento_NIT());
            stmt.setString(3, c.getTelefono());
            stmt.setString(4, c.getDireccion());
            stmt.setString(5, c.getEmail());
            stmt.setString(6, c.getActividad_economica());
            stmt.setString(7, c.getResponsabilidad_iva());
            stmt.setString(8, c.getEstado());
            stmt.setInt(9, c.getIdClientes());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar cliente con ID: " + c.getIdClientes(), e);
        }

        return false;
    }

    // Eliminar cliente
    public boolean eliminarCliente(int idCliente) {
        String sql = "DELETE FROM cliente WHERE idCliente=?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar cliente con ID: " + idCliente, e);
        }

        return false;
    }

    // Cambiar estado
    public boolean marcarEstado(int idCliente, String nuevoEstado) {
        String sql = "UPDATE cliente SET estado = ? WHERE idCliente = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idCliente);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al marcar estado del cliente con ID: " + idCliente, e);
        }

        return false;
    }

    // Método auxiliar para mapear ResultSet a objeto Clientes
    private Clientes mapCliente(ResultSet rs) throws SQLException {
        Clientes c = new Clientes();
        c.setIdClientes(rs.getInt("idCliente"));
        c.setRazon_social(rs.getString("razon_social"));
        c.setDocumento_NIT(rs.getString("documento_NIT"));
        c.setTelefono(rs.getString("telefono"));
        c.setDireccion(rs.getString("direccion"));
        c.setEmail(rs.getString("email"));
        c.setActividad_economica(rs.getString("actividad_economica"));
        c.setResponsabilidad_iva(rs.getString("responsabilidad_iva"));
        c.setEstado(rs.getString("estado"));
        return c;
    }

    public Clientes buscar(Integer idCliente) {
    String sql = "SELECT * FROM cliente WHERE idCliente = ?";
    try (Connection conexion = obtenerConexion();
         PreparedStatement stmt = conexion.prepareStatement(sql)) {
        stmt.setInt(1, idCliente);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return mapCliente(rs);
            }
        }
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error al buscar cliente con ID: " + idCliente, e);
    }
    return null;
}

    // Buscar cliente por documento_NIT
    public Clientes buscarPorDocumento(String documento) {
        String sql = "SELECT * FROM cliente WHERE documento_NIT = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, documento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapCliente(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar cliente por documento: " + documento, e);
        }
        return null;
    }    
}