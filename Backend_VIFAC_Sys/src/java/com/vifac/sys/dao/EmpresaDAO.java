/*
 * Este DAO (Objeto de Acceso a Datos) se encarga de la comunicación
 * directa con la base de datos para la gestión de la tabla 'empresa'.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 06/11/2025
 */

package com.vifac.sys.dao;

import com.vifac.sys.modelo.Empresa;
import com.vifac.sys.util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmpresaDAO extends ConexionBD {

    private static final Logger LOGGER = Logger.getLogger(EmpresaDAO.class.getName());

    /**
     * Agrega una nueva empresa a la base de datos.
     * @param e
     * @return 
     */
    public boolean agregarEmpresa(Empresa e) {
        String sql = "INSERT INTO empresa(idEmpresa, razon_social, cc_nit, actividad_economica, responsabilidad_iva, direccion, ciudad, telefono, email, resolucion_mercantil, fecha_registro_res, fecha_vencimiento_res) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, e.getIdEmpresa());
            stmt.setString(2, e.getRazon_social());
            stmt.setString(3, e.getCc_nit());
            stmt.setString(4, e.getActividad_economica());
            stmt.setString(5, e.getResponsabilidad_iva());
            stmt.setString(6, e.getDireccion());
            stmt.setString(7, e.getCiudad());
            stmt.setString(8, e.getTelefono());
            stmt.setString(9, e.getEmail());
            stmt.setString(10, e.getResolucion_mercantil());
            stmt.setString(11, e.getFecha_registro_res());
            stmt.setString(12, e.getFecha_vencimiento_res());

            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al agregar empresa: " + e.getRazon_social(), ex);
            return false;
        }
    }

    /**
     * Actualiza la información de una empresa existente.
     * @param e
     * @return 
     */
    public boolean actualizarEmpresa(Empresa e) {
        String sql = "UPDATE empresa SET razon_social=?, cc_nit=?, actividad_economica=?, responsabilidad_iva=?, direccion=?, ciudad=?, telefono=?, email=?, resolucion_mercantil=?, fecha_registro_res=?, fecha_vencimiento_res=? "
                   + "WHERE idEmpresa=?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, e.getRazon_social());
            stmt.setString(2, e.getCc_nit());
            stmt.setString(3, e.getActividad_economica());
            stmt.setString(4, e.getResponsabilidad_iva());
            stmt.setString(5, e.getDireccion());
            stmt.setString(6, e.getCiudad());
            stmt.setString(7, e.getTelefono());
            stmt.setString(8, e.getEmail());
            stmt.setString(9, e.getResolucion_mercantil());
            stmt.setString(10, e.getFecha_registro_res());
            stmt.setString(11, e.getFecha_vencimiento_res());
            stmt.setInt(12, e.getIdEmpresa());

            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar empresa ID: " + e.getIdEmpresa(), ex);
            return false;
        }
    }

    /**
     * Elimina una empresa por su ID.
     * @param idEmpresa
     * @return 
     */
    public boolean eliminarEmpresa(int idEmpresa) {
        String sql = "DELETE FROM empresa WHERE idEmpresa=?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, idEmpresa);
            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al eliminar empresa ID: " + idEmpresa, ex);
            return false;
        }
    }

    /**
     * Obtiene una empresa por su ID.
     * @param idEmpresa
     * @return 
     */
    public Empresa obtenerEmpresaPorId(int idEmpresa) {
        Empresa e = null;
        String sql = "SELECT * FROM empresa WHERE idEmpresa=?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, idEmpresa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    e = new Empresa(
                        rs.getInt("idEmpresa"),
                        rs.getString("razon_social"),
                        rs.getString("cc_nit"),
                        rs.getString("actividad_economica"),
                        rs.getString("responsabilidad_iva"),
                        rs.getString("direccion"),
                        rs.getString("ciudad"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("resolucion_mercantil"),
                        rs.getString("fecha_registro_res"),
                        rs.getString("fecha_vencimiento_res")
                    );
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener empresa ID: " + idEmpresa, ex);
        }
        return e;
    }

    /**
     * Lista todas las empresas.
     * @return 
     */
    public List<Empresa> listarEmpresas() {
        List<Empresa> lista = new ArrayList<>();
        String sql = "SELECT * FROM empresa";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Empresa e = new Empresa(
                    rs.getInt("idEmpresa"),
                    rs.getString("razon_social"),
                    rs.getString("cc_nit"),
                    rs.getString("actividad_economica"),
                    rs.getString("responsabilidad_iva"),
                    rs.getString("direccion"),
                    rs.getString("ciudad"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("resolucion_mercantil"),
                    rs.getString("fecha_registro_res"),
                    rs.getString("fecha_vencimiento_res")
                );
                lista.add(e);
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar empresas.", ex);
        }
        return lista;
    }

    /**
     * Busca empresas por razón social o ciudad.
     * @param criterio
     * @return 
     */
    public List<Empresa> buscarEmpresas(String criterio) {
        List<Empresa> lista = new ArrayList<>();
        String sql = "SELECT * FROM empresa WHERE razon_social LIKE ? OR ciudad LIKE ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            String busq = "%" + criterio + "%";
            stmt.setString(1, busq);
            stmt.setString(2, busq);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Empresa e = new Empresa(
                        rs.getInt("idEmpresa"),
                        rs.getString("razon_social"),
                        rs.getString("cc_nit"),
                        rs.getString("actividad_economica"),
                        rs.getString("responsabilidad_iva"),
                        rs.getString("direccion"),
                        rs.getString("ciudad"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("resolucion_mercantil"),
                        rs.getString("fecha_registro_res"),
                        rs.getString("fecha_vencimiento_res")
                    );
                    lista.add(e);
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar empresas con criterio: " + criterio, ex);
        }
        return lista;
    }
    
    public Empresa obtenerEmpresa() {
    List<Empresa> lista = listarEmpresas();
    return lista.isEmpty() ? null : lista.get(0);
  }
}
