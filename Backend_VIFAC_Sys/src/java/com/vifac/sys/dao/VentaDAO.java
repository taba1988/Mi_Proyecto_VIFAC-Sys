/*
 * Este DAO (Objeto de Acceso a Datos) se encarga de la comunicación
 * directa con la base de datos para el registro final de las ventas realizadas.
 * Su función principal es validar y gestionar la información registrada.
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 06/09/2024
 */

package com.vifac.sys.dao;

import com.vifac.sys.modelo.Venta;
import com.vifac.sys.util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VentaDAO extends ConexionBD {

    private static final Logger LOGGER = Logger.getLogger(VentaDAO.class.getName());

    // Registrar venta y devolver ID generado
    public int registrarVenta(Venta venta) {
        String sql = "INSERT INTO venta (nro_documento_factura, fecha_emision, fecha_validacion, fecha_vencimiento, qr_code_url, subtotal_venta, descuento_venta, total_venta, idUsuario, idCliente, idEmisor) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, venta.getNroDocumentoFactura());
            ps.setDate(2, venta.getFechaEmision() != null ? new java.sql.Date(venta.getFechaEmision().getTime()) : null);
            if (venta.getFechaValidacion() != null) ps.setDate(3, new java.sql.Date(venta.getFechaValidacion().getTime()));
            else ps.setNull(3, Types.DATE);
            if (venta.getFechaVencimiento() != null) ps.setDate(4, new java.sql.Date(venta.getFechaVencimiento().getTime()));
            else ps.setNull(4, Types.DATE);

            ps.setString(5, venta.getQrCodeUrl());
            ps.setDouble(6, venta.getSubtotalVenta());
            ps.setDouble(7, venta.getDescuentoVenta());
            ps.setDouble(8, venta.getTotalVenta());

            if (venta.getIdUsuario() != 0) ps.setInt(9, venta.getIdUsuario()); else ps.setNull(9, Types.INTEGER);
            if (venta.getIdCliente() != 0) ps.setInt(10, venta.getIdCliente()); else ps.setNull(10, Types.INTEGER);
            if (venta.getIdEmisor() != 0) ps.setInt(11, venta.getIdEmisor()); else ps.setNull(11, Types.INTEGER);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error registrando venta", e);
        }
        return -1;
    }

    // Listar todas las ventas
    public List<Venta> listarVentas() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM venta";

        try (Connection con = conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Venta v = new Venta();
                v.setIdVenta(rs.getInt("idVenta"));
                v.setNroDocumentoFactura(rs.getString("nro_documento_factura"));
                v.setFechaEmision(rs.getDate("fecha_emision"));
                v.setFechaValidacion(rs.getDate("fecha_validacion"));
                v.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
                v.setQrCodeUrl(rs.getString("qr_code_url"));
                v.setSubtotalVenta(rs.getDouble("subtotal_venta"));
                v.setDescuentoVenta(rs.getDouble("descuento_venta"));
                v.setTotalVenta(rs.getDouble("total_venta"));
                v.setIdUsuario(rs.getInt("idUsuario"));
                v.setIdCliente(rs.getInt("idCliente"));
                v.setIdEmisor(rs.getInt("idEmisor"));
                lista.add(v);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error listando ventas", e);
        }

        return lista;
    }

    // Buscar venta por ID
    public Venta obtenerVentaPorId(int idVenta) {
        String sql = "SELECT * FROM venta WHERE idVenta=?";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVenta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Venta v = new Venta();
                    v.setIdVenta(rs.getInt("idVenta"));
                    v.setNroDocumentoFactura(rs.getString("nro_documento_factura"));
                    v.setFechaEmision(rs.getDate("fecha_emision"));
                    v.setFechaValidacion(rs.getDate("fecha_validacion"));
                    v.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
                    v.setQrCodeUrl(rs.getString("qr_code_url"));
                    v.setSubtotalVenta(rs.getDouble("subtotal_venta"));
                    v.setDescuentoVenta(rs.getDouble("descuento_venta"));
                    v.setTotalVenta(rs.getDouble("total_venta"));
                    v.setIdUsuario(rs.getInt("idUsuario"));
                    v.setIdCliente(rs.getInt("idCliente"));
                    v.setIdEmisor(rs.getInt("idEmisor"));
                    return v;
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error obteniendo venta por ID", e);
        }
        return null;
    }

    // Conexión a la BD
    private Connection conectar() {
        return ConexionBD.getConexion();
    }

    // Genera un número de factura secuencial basado en el último ID de venta
    public String generarNroFactura() {
        int ultimoId = 0;
        String sql = "SELECT MAX(idVenta) AS maxId FROM venta";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                ultimoId = rs.getInt("maxId");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error obteniendo último ID de venta", e);
        }

        return String.format("%04d", ultimoId + 1);
    }
}
