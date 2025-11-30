/*
 * Este DAO se encarga de la comunicación directa con la base de datos
 * para la gestión de transacciones (método pago, recibido, cambio, etc.)
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 27/11/2025
 */

package com.vifac.sys.dao;

import com.vifac.sys.modelo.Transaccion;
import com.vifac.sys.util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransaccionDAO extends ConexionBD {

    private static final Logger LOGGER = Logger.getLogger(TransaccionDAO.class.getName());

    /* ============================
       /* CRUD BASADO EN LA TABLA: transaccion
       idTransaccion
       monto
       recibido
       cambio
       fecha
       descripcion
       idTipo
       idUsuario
       nroFactura
       metodoPago
    ============================ */

    // Agregar transacción
    public boolean agregarTransaccion(Transaccion t) {
        String sql = "INSERT INTO transaccion(monto, recibido, cambio, fecha, descripcion, idUsuario, nro_documento_factura, metodoPago) " +
             "VALUES (?, ?, ?, NOW(), ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
            PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setDouble(1, t.getMonto());
            stmt.setDouble(2, t.getRecibido());
            stmt.setDouble(3, t.getCambio());
            stmt.setString(4, t.getDescripcion());
            stmt.setInt(5, t.getIdUsuario());
            stmt.setString(6, t.getNroDocumentoFactura());
            stmt.setString(7, t.getMetodoPago());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al agregar transacción", e);
        }
        return false;
    }

    // Listar transacciones
    public List<Transaccion> listarTransacciones() {
        List<Transaccion> lista = new ArrayList<>();
        String sql = "SELECT * FROM transaccion";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Transaccion t = mapTransaccion(rs);
                lista.add(t);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al listar transacciones", e);
        }

        return lista;
    }

    // Buscar por número de factura
        public Transaccion buscarPorFactura(String nroDocumentoFactura) {
            String sql = "SELECT * FROM transaccion WHERE nro_documento_factura = ?";
            try (Connection conexion = ConexionBD.obtenerConexion();
                 PreparedStatement stmt = conexion.prepareStatement(sql)) {

                stmt.setString(1, nroDocumentoFactura);
                ResultSet rs = stmt.executeQuery();
        
                if (rs.next()) {
                    return mapTransaccion(rs);
                }

            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al buscar transacción por factura: " + nroDocumentoFactura, e);
            }
            return null;
        }

    // Mapear ResultSet → Transaccion
    private Transaccion mapTransaccion(ResultSet rs) throws SQLException {
        Transaccion t = new Transaccion();
        t.setIdTransaccion(rs.getInt("idTransaccion"));
        t.setMonto(rs.getDouble("monto"));
        t.setRecibido(rs.getDouble("recibido"));
        t.setCambio(rs.getDouble("cambio"));
        t.setFecha(rs.getString("fecha"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setIdUsuario(rs.getInt("idUsuario"));
        t.setNroDocumentoFactura(rs.getString("nro_documento_factura"));
        t.setMetodoPago(rs.getString("metodoPago"));

        return t;
    }

    // Buscar por ID
    public Transaccion buscar(Integer idTransaccion) {
        String sql = "SELECT * FROM transaccion WHERE idTransaccion = ?";

        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, idTransaccion);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapTransaccion(rs);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar transacción con ID: " + idTransaccion, e);
        }

        return null;
    }
}
