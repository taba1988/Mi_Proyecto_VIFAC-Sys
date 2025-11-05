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
import com.vifac.sys.modelo.RespuestaJson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/ProveedoresServlet")
public class ProveedoresServlet extends HttpServlet {

    private final ProveedorDAO proveedorDAO = new ProveedorDAO();
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

                       // La fecha de registro se generará automáticamente en el DAO.
                       proveedorDAO.agregarProveedor(nuevoProveedor);
                       respuesta = new RespuestaJson("success", "Proveedor agregado con éxito.");
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
                        break;

                    case "eliminar":
                        int idProveedorEliminar = Integer.parseInt(request.getParameter("id"));
                        proveedorDAO.eliminarProveedor(idProveedorEliminar);
                        respuesta = new RespuestaJson("success", "Proveedor eliminado con éxito.");
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
        } catch (NumberFormatException e) {
            respuesta = new RespuestaJson("error", "Error en el procesamiento: " + e.getMessage());
        }

        response.getWriter().write(gson.toJson(respuesta));
    }
}

