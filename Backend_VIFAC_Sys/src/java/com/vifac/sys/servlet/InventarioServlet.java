/*
 * InventarioServlet para gestionar las operaciones relacionadas con el inventario salidad y entradas de producto:
 * agregar, editar, eliminar, listar y buscar.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */
package com.vifac.sys.servlet;

import com.google.gson.Gson;
import com.vifac.sys.dao.InventarioDAO;
import com.vifac.sys.modelo.Inventario;
import com.vifac.sys.dao.NotificacionDAO;
import com.vifac.sys.modelo.Notificacion;
import com.vifac.sys.dao.UsuarioDAO;
import com.vifac.sys.modelo.Usuario;
import com.vifac.sys.modelo.RespuestaJson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.sql.Timestamp;
import javax.servlet.http.HttpSession;

@WebServlet("/InventarioServlet")
public class InventarioServlet extends HttpServlet {

    private final InventarioDAO inventarioDAO = new InventarioDAO();
    private final NotificacionDAO notificacionDAO = new NotificacionDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final Gson gson = new Gson();

    private int parseIntSafe(String s) {
        try {
            return (s == null || s.trim().isEmpty()) ? 0 : Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private float parseFloatSafe(String s) {
        try {
            return (s == null || s.trim().isEmpty()) ? 0f : Float.parseFloat(s.trim());
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");

        String accion = request.getParameter("accion");
        try {
            if (accion == null || accion.equals("listar")) {
                List<Inventario> inventario = inventarioDAO.listarInventario();
                response.getWriter().write(gson.toJson(inventario));
                return;
            }

            if ("buscar".equals(accion)) {
                String busqueda = request.getParameter("busqueda");
                List<Inventario> inventario = inventarioDAO.buscarInventario(busqueda);
                response.getWriter().write(gson.toJson(inventario));
                return;
            }

            if ("buscarId".equals(accion)) {
                int id = parseIntSafe(request.getParameter("idProducto"));
                Inventario producto = inventarioDAO.buscarInventarioPorId(id);
                if (producto != null) {
                    response.getWriter().write(gson.toJson(producto));
                } else {
                    response.getWriter().write(gson.toJson(new RespuestaJson("error", "Producto no encontrado")));
                }
                return;
            }

            // acción no reconocida
            response.getWriter().write(gson.toJson(new RespuestaJson("error", "Acción GET no válida")));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new RespuestaJson("error", "Error interno: " + e.getMessage())));
        }
    }

// método doPost completo
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json; charset=UTF-8");
    RespuestaJson respuesta;

    String accion = request.getParameter("accion");
    HttpSession session = request.getSession(false);
    int idUsuarioSesion = 0;
    Usuario usuarioSesion = null;
    if (session != null && session.getAttribute("idUsuario") != null) {
    idUsuarioSesion = (Integer) session.getAttribute("idUsuario");
    usuarioSesion = usuarioDAO.obtenerUsuarioPorId(idUsuarioSesion);
}

    try {
        if (accion == null || accion.trim().isEmpty()) {
            respuesta = new RespuestaJson("error", "Acción POST requerida.");
        } else {
            Inventario producto = obtenerDatosInventario(request);
            if (producto.getIdCategoria() < 0) {
                respuesta = new RespuestaJson("error", "ID de categoría no válido.");
            } else {
switch (accion) {
    case "agregar":
        
        // Validar si el SKU ya existe
        if (inventarioDAO.buscarInventario(producto.getSku()) != null &&
            !inventarioDAO.buscarInventario(producto.getSku()).isEmpty()) {
            respuesta = new RespuestaJson("error", "El SKU ya existe, no se puede agregar el producto.");
            break;
        }
        boolean ok = inventarioDAO.agregarInventario(producto);
        respuesta = ok ? new RespuestaJson("success", "Producto agregado con éxito.")
                       : new RespuestaJson("error", "No se pudo agregar el producto.");

        if (ok) {
            Notificacion n = new Notificacion();
            n.setIdUsuario(idUsuarioSesion);
            n.setTipo("INVENTARIO");
            n.setMensaje(
                "El Usuario #" + idUsuarioSesion + " - " + usuarioSesion.getNombre() +
                " agregó el producto ID #" + inventarioDAO.buscarInventario(producto.getSku())
                .get(inventarioDAO.buscarInventario(producto.getSku()).size() - 1)
                .getIdProducto() +
                " (" + producto.getNombre() + ")"
            );
            n.setLeido(false);
            n.setFechaCreacion(new Timestamp(System.currentTimeMillis() - (5 * 3600 * 1000)));
            notificacionDAO.registrar(n);
        }
        break;

    case "editar":
        boolean ok2 = inventarioDAO.editarInventario(producto);
        respuesta = ok2 ? new RespuestaJson("success", "Producto actualizado con éxito.")
                        : new RespuestaJson("error", "No se pudo actualizar el producto.");

        if (ok2) {
            Notificacion n = new Notificacion();
            n.setIdUsuario(idUsuarioSesion);
            n.setTipo("INVENTARIO");
            n.setMensaje(
                "El Usuario #" + idUsuarioSesion + " - " + usuarioSesion.getNombre() +
                " actualizó el producto ID #" + producto.getIdProducto() +
                " (" + producto.getNombre() + ")"
            );
            n.setLeido(false);
            n.setFechaCreacion(new Timestamp(System.currentTimeMillis() - (5 * 3600 * 1000)));
            notificacionDAO.registrar(n);
        }
        break;

    case "eliminar":
        int idProductoEliminar = parseIntSafe(request.getParameter("idProducto"));
        Inventario productoEliminado = inventarioDAO.buscarInventarioPorId(idProductoEliminar);
        String resultadoEliminacion = inventarioDAO.eliminarInventario(idProductoEliminar);
        respuesta = "success".equals(resultadoEliminacion) ? new RespuestaJson("success", "Producto eliminado con éxito.")
                                                           : new RespuestaJson("error", resultadoEliminacion);

        if ("success".equals(resultadoEliminacion) && productoEliminado != null) {
            Notificacion n = new Notificacion();
            n.setIdUsuario(idUsuarioSesion);
            n.setTipo("INVENTARIO");
            n.setMensaje(
                "El Usuario #" + idUsuarioSesion + " - " + usuarioSesion.getNombre() +
                " eliminó el producto ID #" + idProductoEliminar +
                " (" + productoEliminado.getNombre() + ")"
            );
            n.setLeido(false);
            n.setFechaCreacion(new Timestamp(System.currentTimeMillis() - (5 * 3600 * 1000)));
            notificacionDAO.registrar(n);
        }
        break;

    default:
        respuesta = new RespuestaJson("error", "Acción POST no válida.");
}

            }
        }
    } catch (Exception e) {
        respuesta = new RespuestaJson("error", "Error en el procesamiento: " + e.getMessage());
    }

    response.getWriter().write(gson.toJson(respuesta));
}
    private Inventario obtenerDatosInventario(HttpServletRequest request) {
    Inventario inventario = new Inventario();

    String idProductoStr = request.getParameter("idProducto");
    if (idProductoStr != null && !idProductoStr.trim().isEmpty()) {
        inventario.setIdProducto(parseIntSafe(idProductoStr));
    }

    inventario.setSku(request.getParameter("sku"));
    inventario.setNombre(request.getParameter("nombre"));
    inventario.setDescripcion(request.getParameter("descripcion"));
    inventario.setPrecio_compra(parseFloatSafe(request.getParameter("precio_compra")));
    inventario.setPrecio_venta(parseFloatSafe(request.getParameter("precio_venta")));
    inventario.setStock(parseIntSafe(request.getParameter("stock")));
    inventario.setUnidad_medida(request.getParameter("unidad_medida"));

    String estado = request.getParameter("estado");
    if (estado != null && (estado.equalsIgnoreCase("Activo") || estado.equalsIgnoreCase("Inactivo"))) {
        inventario.setEstado(estado);
    } else {
        inventario.setEstado("Inactivo");
    }

    inventario.setIdCategoria(parseIntSafe(request.getParameter("idCategoria")));

    return inventario;
  }
}
