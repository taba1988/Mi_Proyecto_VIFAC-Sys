/*
 * Este DAO (Objeto de Acceso a Datos) se encarga de la comunicación
 * directa con la base de datos para la gestión de inventarios.
 * Su función principal es validar la información registrada.
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 6/09/2024
 */

package com.vifac.sys.dao;

import com.vifac.sys.modelo.Inventario;
import com.vifac.sys.util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InventarioDAO extends ConexionBD {

    private static final Logger LOGGER = Logger.getLogger(InventarioDAO.class.getName());

    // Agregar producto al inventario
    public boolean agregarInventario(Inventario i) {
        String sql = "INSERT INTO inventario(sku, nombre, descripcion, precio_compra, precio_venta, stock, unidad_medida, estado, idCategoria) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, i.getSku());
            stmt.setString(2, i.getNombre());

            if (i.getDescripcion() != null && !i.getDescripcion().isEmpty()) {
                stmt.setString(3, i.getDescripcion());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }

           stmt.setDouble(4, i.getPrecio_compra());
           stmt.setDouble(5, i.getPrecio_venta());
           stmt.setInt(6, i.getStock());

            if (i.getUnidad_medida() != null && !i.getUnidad_medida().isEmpty()) {
                stmt.setString(7, i.getUnidad_medida());
            } else {
                stmt.setNull(7, Types.VARCHAR);
            }

            stmt.setString(8, i.getEstado());

            if (i.getIdCategoria() > 0) {
                stmt.setInt(9, i.getIdCategoria());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al agregar producto al inventario", e);
        }
        return false;
    }

    // Listar todos los productos del inventario (con nombre de categoría)
    public List<Inventario> listarInventario() {
        List<Inventario> lista = new ArrayList<>();
        String sql = "SELECT i.*, c.nombre AS categoria " +
                     "FROM inventario i " +
                     "JOIN categoria c ON i.idCategoria = c.idCategoria";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapInventario(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al listar el inventario", e);
        }

        return lista;
    }

    // Buscar productos por criterio (SKU o nombre) con categoría
    public List<Inventario> buscarInventario(String criterio) {
        List<Inventario> lista = new ArrayList<>();
        String sql = "SELECT i.*, c.nombre AS categoria " +
                     "FROM inventario i " +
                     "JOIN categoria c ON i.idCategoria = c.idCategoria " +
                     "WHERE i.sku LIKE ? OR i.nombre LIKE ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            String busqueda = "%" + criterio + "%";
            stmt.setString(1, busqueda);
            stmt.setString(2, busqueda);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapInventario(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar en el inventario", e);
        }

        return lista;
    }

    // Buscar un solo producto por su ID (con categoría)
    public Inventario buscarInventarioPorId(int id) {
        String sql = "SELECT i.*, c.nombre AS categoria " +
                     "FROM inventario i " +
                     "JOIN categoria c ON i.idCategoria = c.idCategoria " +
                     "WHERE i.idProducto = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapInventario(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar producto por ID: " + id, e);
        }
        return null;
    }

    // Actualizar producto del inventario
    public boolean editarInventario(Inventario i) {
        String sql = "UPDATE inventario SET sku=?, nombre=?, descripcion=?, precio_compra=?, precio_venta=?, stock=?, unidad_medida=?, estado=?, idCategoria=? WHERE idProducto=?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, i.getSku());
            stmt.setString(2, i.getNombre());

            if (i.getDescripcion() != null && !i.getDescripcion().isEmpty()) {
                stmt.setString(3, i.getDescripcion());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }

            stmt.setDouble(4, i.getPrecio_compra());
            stmt.setDouble(5, i.getPrecio_venta());
            stmt.setInt(6, i.getStock());

            if (i.getUnidad_medida() != null && !i.getUnidad_medida().isEmpty()) {
                stmt.setString(7, i.getUnidad_medida());
            } else {
                stmt.setNull(7, Types.VARCHAR);
            }

            stmt.setString(8, i.getEstado());

            if (i.getIdCategoria() > 0) {
                stmt.setInt(9, i.getIdCategoria());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }

            stmt.setInt(10, i.getIdProducto());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar producto con ID: " + i.getIdProducto(), e);
        }

        return false;
    }

    // Eliminar producto del inventario
    public String eliminarInventario(int idProducto) {
    String sql = "DELETE FROM inventario WHERE idProducto=?";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement stmt = conexion.prepareStatement(sql)) {

        stmt.setInt(1, idProducto);
        if (stmt.executeUpdate() > 0) {
            return "success";
        }
        return "No se pudo eliminar el producto.";
    } catch (SQLIntegrityConstraintViolationException e) {
        LOGGER.log(Level.WARNING, "No se puede eliminar producto con ID: " + idProducto + " porque tiene relaciones.", e);
        return "No se puede eliminar el producto porque está relacionado con otras tablas (por ejemplo, ventas o facturas).";
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error al eliminar producto con ID: " + idProducto, e);
        return "Error en la base de datos al intentar eliminar el producto.";
    }
}

    // Método auxiliar para mapear ResultSet a objeto Inventario
    
    private Inventario mapInventario(ResultSet rs) throws SQLException {
    Inventario i = new Inventario();
    i.setIdProducto(rs.getInt("idProducto"));
    i.setSku(rs.getString("sku"));
    i.setNombre(rs.getString("nombre"));
    i.setDescripcion(rs.getString("descripcion"));
    i.setPrecio_compra(rs.getDouble("precio_compra"));
    i.setPrecio_venta(rs.getDouble("precio_venta"));
    i.setStock(rs.getInt("stock"));
    i.setUnidad_medida(rs.getString("unidad_medida"));
    i.setEstado(rs.getString("estado"));
    i.setIdCategoria(rs.getInt("idCategoria"));
    i.setCategoria(rs.getString("categoria"));
    return i;
}
    // Valida si el stock disponible para un producto es suficiente
public boolean validarStock(int idProducto, int cantidadSolicitada) {
    String sql = "SELECT stock FROM inventario WHERE idProducto = ?";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement stmt = conexion.prepareStatement(sql)) {

        stmt.setInt(1, idProducto);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int stockDisponible = rs.getInt("stock");
                return stockDisponible >= cantidadSolicitada;
            }
        }
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error al validar stock del producto ID: " + idProducto, e);
    }

    return false; // Si ocurre un error o no se encuentra el producto
}

// Actualiza el stock de un producto sumando o restando la cantidad indicada
public boolean actualizarStock(int idProducto, int cantidad) {
    String sql = "UPDATE inventario SET stock = stock + ? WHERE idProducto = ?";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement stmt = conexion.prepareStatement(sql)) {

        stmt.setInt(1, cantidad); // puede ser positivo o negativo
        stmt.setInt(2, idProducto);

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error al actualizar el stock del producto ID: " + idProducto, e);
    }

    return false;
}

}
