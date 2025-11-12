/*
 * Clase DAO para la gestión de los detalles de venta en la base de datos.
 * Su función es permitir la creación, actualización, eliminación y búsqueda de detalles de venta.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 22/09/2025
 */

package com.vifac.sys.dao;

import com.vifac.sys.modelo.DetalleVenta;
import com.vifac.sys.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DetalleVentaDAO extends ConexionBD {

    private static final Logger LOGGER = Logger.getLogger(DetalleVentaDAO.class.getName());

    /**
     * Agregar un nuevo detalle de venta
     * @param detalle Objeto DetalleVenta con los datos a insertar
     * @return true si se agregó correctamente, false si hubo error
     */
    public boolean agregar(DetalleVenta detalle) {
        String sql = "INSERT INTO detalleventa " +
                     "(idVenta, idProducto, cantidad, precio_unitario, impuesto_porcentaje, descuento_porcentaje, descuento, total_con_descuento) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, detalle.getIdVenta());
            stmt.setInt(2, detalle.getIdProducto());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getPrecio_unitario());
            stmt.setDouble(5, detalle.getImpuesto_porcentaje());
            stmt.setDouble(6, detalle.getDescuento_porcentaje());
            stmt.setDouble(7, detalle.getDescuento());
            stmt.setDouble(8, detalle.getTotal_con_descuento());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al agregar detalle de venta", e);
            return false;
        }
    }

    /**
     * Actualizar un detalle de venta existente
     * @param detalle Objeto DetalleVenta con los datos actualizados
     * @return true si se actualizó correctamente, false si hubo error
     */
    public boolean actualizar(DetalleVenta detalle) {
        String sql = "UPDATE detalleventa SET " +
                     "idVenta = ?, idProducto = ?, cantidad = ?, precio_unitario = ?, " +
                     "impuesto_porcentaje = ?, descuento_porcentaje = ?, descuento = ?, total_con_descuento = ? " +
                     "WHERE idDetalleVenta = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, detalle.getIdVenta());
            stmt.setInt(2, detalle.getIdProducto());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getPrecio_unitario());
            stmt.setDouble(5, detalle.getImpuesto_porcentaje());
            stmt.setDouble(6, detalle.getDescuento_porcentaje());
            stmt.setDouble(7, detalle.getDescuento());
            stmt.setDouble(8, detalle.getTotal_con_descuento());
            stmt.setInt(9, detalle.getIdDetalleVenta());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar detalle de venta", e);
            return false;
        }
    }

    /**
     * Eliminar un detalle de venta por su ID
     * @param idDetalleVenta ID del detalle a eliminar
     * @return true si se eliminó correctamente, false si hubo error
     */
    public boolean eliminar(int idDetalleVenta) {
        String sql = "DELETE FROM detalleventa WHERE idDetalleVenta = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, idDetalleVenta);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar detalle de venta", e);
            return false;
        }
    }

    /**
     * Listar los detalles de una venta específica
     * @param idVenta ID de la venta
     * @return Lista de detalles de esa venta
     */
    public List<DetalleVenta> listarDetallesPorVenta(int idVenta) {
    List<DetalleVenta> lista = new ArrayList<>();
    String sql = "SELECT dv.*, i.nombre AS nombreProducto, i.sku AS sku "
               + "FROM detalleventa dv "
               + "INNER JOIN inventario i ON dv.idProducto = i.idProducto "
               + "WHERE dv.idVenta = ?";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement stmt = conexion.prepareStatement(sql)) {

        stmt.setInt(1, idVenta);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setIdDetalleVenta(rs.getInt("idDetalleVenta"));
            detalle.setIdVenta(rs.getInt("idVenta"));
            detalle.setIdProducto(rs.getInt("idProducto"));
            detalle.setCantidad(rs.getInt("cantidad"));
            detalle.setPrecio_unitario(rs.getDouble("precio_unitario"));
            detalle.setImpuesto_porcentaje(rs.getDouble("impuesto_porcentaje"));
            detalle.setDescuento_porcentaje(rs.getDouble("descuento_porcentaje"));
            detalle.setDescuento(rs.getDouble("descuento"));
            detalle.setTotal_con_descuento(rs.getDouble("total_con_descuento"));

            // --- Nuevos campos desde la tabla inventario ---
            detalle.setNombreProducto(rs.getString("nombreProducto"));
            detalle.setSku(rs.getString("sku"));

            lista.add(detalle);
        }

    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error al listar detalles por venta", e);
    }

    return lista;
  }
}
