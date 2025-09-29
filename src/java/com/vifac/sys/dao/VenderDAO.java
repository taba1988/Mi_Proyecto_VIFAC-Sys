/*
 * Este DAO (Objeto de Acceso a Datos) se encarga de la comunicación
 * directa con la base de datos para la gestión de ventas.
 * Su función principal es validar y gestionar la información registrada.
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 06/09/2024
 */

package com.vifac.sys.dao;

import com.vifac.sys.modelo.Vender;
import com.vifac.sys.modelo.DetalleVenta;
import com.vifac.sys.util.ConexionBD;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VenderDAO {

    private static final Logger LOGGER = Logger.getLogger(VenderDAO.class.getName());

    // Insertar nueva venta
    public boolean insertarVenta(Vender venta) {
        String sql = "INSERT INTO venta (nro_documento_factura, fecha_emision, fecha_validacion, fecha_vencimiento, qr_code_url, total_venta, idUsuario, idCliente, idEmisor, metodoPago, subtotalVenta, totalVenta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, venta.getNro_documento_factura());
            stmt.setDate(2, Date.valueOf(venta.getFecha_emision()));
            stmt.setDate(3, venta.getFecha_validacion() != null ? Date.valueOf(venta.getFecha_validacion()) : null);
            stmt.setDate(4, venta.getFecha_vencimiento() != null ? Date.valueOf(venta.getFecha_vencimiento()) : null);
            stmt.setString(5, venta.getQr_code_url());
            stmt.setFloat(6, (float) venta.getTotal_venta());
            if (venta.getIdUsuario() != null) stmt.setInt(7, venta.getIdUsuario()); else stmt.setNull(7, Types.INTEGER);
            if (venta.getIdCliente() != null) stmt.setInt(8, venta.getIdCliente()); else stmt.setNull(8, Types.INTEGER);
            if (venta.getIdEmisor() != null) stmt.setInt(9, venta.getIdEmisor()); else stmt.setNull(9, Types.INTEGER);
            stmt.setString(10, venta.getMetodoPago());
            stmt.setDouble(11, venta.getSubtotalVenta());
            stmt.setDouble(12, venta.getTotalVenta());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    venta.setIdVenta(rs.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar venta: ", e);
            return false;
        }
    }

    // Insertar venta y devolver ID generado
    public int agregarYDevolverId(Vender venta) {
        String sql = "INSERT INTO venta (nro_documento_factura, fecha_emision, fecha_validacion, fecha_vencimiento, qr_code_url, total_venta, idUsuario, idCliente, idEmisor, metodoPago, subtotalVenta, totalVenta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, venta.getNro_documento_factura());
            stmt.setDate(2, Date.valueOf(venta.getFecha_emision()));
            stmt.setDate(3, venta.getFecha_validacion() != null ? Date.valueOf(venta.getFecha_validacion()) : null);
            stmt.setDate(4, venta.getFecha_vencimiento() != null ? Date.valueOf(venta.getFecha_vencimiento()) : null);
            stmt.setString(5, venta.getQr_code_url());
            stmt.setFloat(6, (float) venta.getTotal_venta());
            if (venta.getIdUsuario() != null) stmt.setInt(7, venta.getIdUsuario()); else stmt.setNull(7, Types.INTEGER);
            if (venta.getIdCliente() != null) stmt.setInt(8, venta.getIdCliente()); else stmt.setNull(8, Types.INTEGER);
            if (venta.getIdEmisor() != null) stmt.setInt(9, venta.getIdEmisor()); else stmt.setNull(9, Types.INTEGER);
            stmt.setString(10, venta.getMetodoPago());
            stmt.setDouble(11, venta.getSubtotalVenta());
            stmt.setDouble(12, venta.getTotalVenta());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    venta.setIdVenta(idGenerado);
                    return idGenerado;
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar venta y devolver ID: ", e);
        }

        return -1; // error
    }

    // Obtener venta por ID
    public Vender obtenerVentaPorId(int idVenta) {
        String sql = "SELECT * FROM venta WHERE idVenta = ?";
        Vender venta = null;

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVenta);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                venta = mapearVenta(rs);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener venta: ", e);
        }

        return venta;
    }

    // Listar todas las ventas
    public List<Vender> listarVentas() {
        String sql = "SELECT * FROM venta";
        List<Vender> lista = new ArrayList<>();

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearVenta(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al listar ventas: ", e);
        }

        return lista;
    }

    // Actualizar venta
    public boolean actualizarVenta(Vender venta) {
        String sql = "UPDATE venta SET nro_documento_factura = ?, fecha_emision = ?, fecha_validacion = ?, fecha_vencimiento = ?, qr_code_url = ?, total_venta = ?, idUsuario = ?, idCliente = ?, idEmisor = ?, metodoPago = ?, subtotalVenta = ?, totalVenta = ? WHERE idVenta = ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, venta.getNro_documento_factura());
            stmt.setDate(2, Date.valueOf(venta.getFecha_emision()));
            stmt.setDate(3, venta.getFecha_validacion() != null ? Date.valueOf(venta.getFecha_validacion()) : null);
            stmt.setDate(4, venta.getFecha_vencimiento() != null ? Date.valueOf(venta.getFecha_vencimiento()) : null);
            stmt.setString(5, venta.getQr_code_url());
            stmt.setFloat(6, (float) venta.getTotal_venta());
            if (venta.getIdUsuario() != null) stmt.setInt(7, venta.getIdUsuario()); else stmt.setNull(7, Types.INTEGER);
            if (venta.getIdCliente() != null) stmt.setInt(8, venta.getIdCliente()); else stmt.setNull(8, Types.INTEGER);
            if (venta.getIdEmisor() != null) stmt.setInt(9, venta.getIdEmisor()); else stmt.setNull(9, Types.INTEGER);
            stmt.setString(10, venta.getMetodoPago());
            stmt.setDouble(11, venta.getSubtotalVenta());
            stmt.setDouble(12, venta.getTotalVenta());

            stmt.setInt(13, venta.getIdVenta());

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar venta: ", e);
            return false;
        }
    }

    // Eliminar venta
    public boolean eliminarVenta(int idVenta) {
        String sql = "DELETE FROM venta WHERE idVenta = ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVenta);
            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar venta: ", e);
            return false;
        }
    }

    // Mapeo de ResultSet a objeto Vender
    private Vender mapearVenta(ResultSet rs) throws SQLException {
        Vender venta = new Vender(
            rs.getInt("idVenta"),
            rs.getString("nro_documento_factura"),
            rs.getDate("fecha_emision").toLocalDate(),
            rs.getDate("fecha_validacion") != null ? rs.getDate("fecha_validacion").toLocalDate() : null,
            rs.getDate("fecha_vencimiento") != null ? rs.getDate("fecha_vencimiento").toLocalDate() : null,
            rs.getString("qr_code_url"),
            rs.getFloat("total_venta"),
            rs.getObject("idUsuario") != null ? rs.getInt("idUsuario") : null,
            rs.getObject("idCliente") != null ? rs.getInt("idCliente") : null,
            rs.getObject("idEmisor") != null ? rs.getInt("idEmisor") : null,
            rs.getString("metodoPago"),
            rs.getDouble("subtotalVenta"),
            rs.getDouble("totalVenta"),
            new ArrayList<>()  // detalles inicial vacíos
        );
        return venta;
    }

    // Conectar a la BD
    private Connection conectar() throws SQLException {
        return ConexionBD.obtenerConexion();
    }

    public boolean agregar(Vender venta) {
        return insertarVenta(venta);
    }
}
