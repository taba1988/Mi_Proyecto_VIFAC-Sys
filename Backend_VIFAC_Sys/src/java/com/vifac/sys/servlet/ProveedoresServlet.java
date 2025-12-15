/*
 * ProveedoresServlet para gestionar las operaciones relacionadas con los proveedores:
 * agregar, editar, eliminar, listar y buscar.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */

package com.vifac.sys.servlet;

import com.google.gson.Gson;
import com.vifac.sys.dao.ProveedorDAO;
import com.vifac.sys.modelo.Proveedor;
import com.vifac.sys.dao.UsuarioDAO;
import com.vifac.sys.modelo.Usuario;
import com.vifac.sys.dao.NotificacionDAO;
import com.vifac.sys.modelo.Notificacion;
import com.vifac.sys.modelo.RespuestaJson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.sql.Timestamp;

@WebServlet("/ProveedoresServlet")
public class ProveedoresServlet extends HttpServlet {

    private final ProveedorDAO proveedorDAO = new ProveedorDAO();
    private final NotificacionDAO notificacionDAO = new NotificacionDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");
        try {
            if (accion == null || accion.equals("listar")) {
                List<Proveedor> proveedores = proveedorDAO.listarProveedores();
                response.getWriter().write(gson.toJson(proveedores));
            } else if (accion.equals("buscar")) {
                String busqueda = request.getParameter("busqueda");
                List<Proveedor> proveedores = proveedorDAO.buscarProveedores(busqueda);
                response.getWriter().write(gson.toJson(proveedores));
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            RespuestaJson error = new RespuestaJson("error", "Error interno del servidor: " + e.getMessage());
            response.getWriter().write(gson.toJson(error));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        int idUsuario = (Integer) request.getSession().getAttribute("idUsuario");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario u = usuarioDAO.obtenerUsuarioPorId(idUsuario);
        String usuarioTxt = "El Usuario #" + idUsuario + " - " + u.getNombre() + " ";
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        RespuestaJson respuesta = new RespuestaJson("error", "Acción no válida.");

        try {
            if (accion != null) {
                switch (accion) {
                    case "agregar":
                       Proveedor nuevoProveedor = new Proveedor();
                       nuevoProveedor.setNombreEmpresa(request.getParameter("nombreEmpresa"));
                       nuevoProveedor.setDocumentoNIT(request.getParameter("documento_NIT"));
                       nuevoProveedor.setAsesor(request.getParameter("asesor"));
                       nuevoProveedor.setTelefono(request.getParameter("telefono"));
                       nuevoProveedor.setEmail(request.getParameter("email"));
                       nuevoProveedor.setDiaVisita(request.getParameter("diaVisita"));
                       nuevoProveedor.setEstado(request.getParameter("estado"));
                                            
                       // Validar si el documento/NIT o el email ya existen
                       Proveedor existeDocumento = proveedorDAO.buscarPorDocumento(nuevoProveedor.getDocumentoNIT());
                       Proveedor existeEmail = proveedorDAO.buscarPorEmail(nuevoProveedor.getEmail());
                       
                       if (proveedorDAO.buscarPorDocumento(nuevoProveedor.getDocumentoNIT()) != null) {
                           respuesta = new RespuestaJson("error", "Documento/NIT ya existe.");
                           break;
                       } else if (proveedorDAO.buscarPorEmail(nuevoProveedor.getEmail()) != null) {
                           respuesta = new RespuestaJson("error", "Correo electrónico ya existe.");
                           break;
                       }
                  
                       // La fecha de registro se generará automáticamente en el DAO.
                       proveedorDAO.agregarProveedor(nuevoProveedor);
                       int idProveedorGenerado = proveedorDAO.listarProveedores()
                       .stream()
                       .filter(x -> x.getDocumento_NIT().equals(nuevoProveedor.getDocumento_NIT()))
                       .findFirst()
                       .map(x -> x.getIdProveedor())
                       .orElse(0);
                       respuesta = new RespuestaJson("success", "Proveedor agregado con éxito.");
                       Notificacion n = new Notificacion();
                       n.setIdUsuario(idUsuario);
                       n.setTipo("PROVEEDORES");
                       n.setMensaje(
                           usuarioTxt +
                           "agrego el proveedor ID #" +
                           idProveedorGenerado +
                           " (" + nuevoProveedor.getNombreEmpresa() + ")"
                       );
                       n.setLeido(false);
                       n.setFechaCreacion(new Timestamp(System.currentTimeMillis() - (5 * 3600 * 1000)));
                       notificacionDAO.registrar(n);
                       break;

                    case "editar":
                        Proveedor proveedorEditado = new Proveedor();
                        proveedorEditado.setIdProveedor(Integer.parseInt(request.getParameter("idProveedor")));
                        proveedorEditado.setNombreEmpresa(request.getParameter("nombreEmpresa"));
                        proveedorEditado.setDocumentoNIT(request.getParameter("documento_NIT"));
                        proveedorEditado.setAsesor(request.getParameter("asesor"));
                        proveedorEditado.setTelefono(request.getParameter("telefono"));
                        proveedorEditado.setEmail(request.getParameter("email"));
                        proveedorEditado.setDiaVisita(request.getParameter("diaVisita"));
                        proveedorEditado.setEstado(request.getParameter("estado"));

                        proveedorDAO.actualizarProveedor(proveedorEditado);
                        respuesta = new RespuestaJson("success", "Proveedor actualizado con éxito.");
                        n = new Notificacion();
                        n.setIdUsuario(idUsuario);
                        n.setTipo("PROVEEDORES");
                        n.setMensaje(usuarioTxt
                                + "actualizó el proveedor ID #"
                                + proveedorEditado.getIdProveedor()
                                + " (" + proveedorEditado.getNombreEmpresa() + ")");
                        n.setLeido(false);
                        n.setFechaCreacion(new Timestamp(System.currentTimeMillis() - (5 * 3600 * 1000)));
                        notificacionDAO.registrar(n);     
                        break;

case "eliminar":
    int idProveedorEliminar = Integer.parseInt(request.getParameter("id"));
    String nombreEmpresa = request.getParameter("nombreEmpresa"); // ← YA EXISTE EN EL FORM

    proveedorDAO.eliminarProveedor(idProveedorEliminar);
    respuesta = new RespuestaJson("success", "Proveedor eliminado con éxito.");

    n = new Notificacion();
    n.setIdUsuario(idUsuario);
    n.setTipo("PROVEEDORES");
    n.setMensaje(
        usuarioTxt +
        "eliminó el proveedor ID #" +
        idProveedorEliminar +
        (nombreEmpresa != null ? " (" + nombreEmpresa + ")" : "")
    );
    n.setLeido(false);
    n.setFechaCreacion(new Timestamp(System.currentTimeMillis() - (5 * 3600 * 1000)));
    notificacionDAO.registrar(n);
    break;
                 
                         // ✅ NUEVOS CASOS para marcar estado
                        case "marcarCumplida":
                            int idCumplida = Integer.parseInt(request.getParameter("id"));
                            if (proveedorDAO.marcarCumplida(idCumplida)) {
                                respuesta = new RespuestaJson("success", "Proveedor marcado como cumplida.");
                            } else {
                                respuesta = new RespuestaJson("error", "Error al marcar como cumplida.");
                            }
                            break;

                        case "marcarIncumplida":
                            int idIncumplida = Integer.parseInt(request.getParameter("id"));
                            if (proveedorDAO.marcarIncumplida(idIncumplida)) {
                                respuesta = new RespuestaJson("success", "Proveedor marcado como incumplida.");
                            } else {
                                respuesta = new RespuestaJson("error", "Error al marcar como incumplida.");
                            }
                            break;                                             
                        }
                    }
                } 
            catch (NumberFormatException e) {
          respuesta = new RespuestaJson("error", "Error en el procesamiento: " + e.getMessage());
        }

      response.getWriter().write(gson.toJson(respuesta));
    }
}

