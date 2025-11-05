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
}

