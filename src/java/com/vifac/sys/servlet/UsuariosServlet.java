/*
 * UsuariosServlet para gestionar las operaciones relacionadas con los usuarios:
 * agregar, editar, eliminar y listar usuarios o buscar.
 
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */

package com.vifac.sys.servlet;

import com.google.gson.Gson;
import com.vifac.sys.dao.UsuarioDAO;
import com.vifac.sys.modelo.RespuestaJson;
import com.vifac.sys.modelo.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UsuariosServlet")
public class UsuariosServlet extends HttpServlet {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");
        if (accion == null || "listar".equals(accion)) {
            // Acción por defecto: listar todos los usuarios
            try {
                List<Usuario> usuarios = usuarioDAO.listarUsuarios();
                String json = gson.toJson(usuarios);
                response.getWriter().write(json);
            } catch (Exception e) {
                // Maneja cualquier excepción para enviar una respuesta de error válida
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                RespuestaJson respuesta = new RespuestaJson("error", "Error interno del servidor: " + e.getMessage());
                response.getWriter().write(gson.toJson(respuesta));
            }
        } else if ("buscar".equals(accion)) {
            String busqueda = request.getParameter("busqueda");
            try {
                List<Usuario> usuarios = usuarioDAO.buscarUsuarios(busqueda);
                String json = gson.toJson(usuarios);
                response.getWriter().write(json);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                RespuestaJson respuesta = new RespuestaJson("error", "Error en la búsqueda: " + e.getMessage());
                response.getWriter().write(gson.toJson(respuesta));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        RespuestaJson respuesta = new RespuestaJson("error", "Acción no válida.");

        if (accion != null) {
            switch (accion) {
                case "agregar":
                    Usuario nuevoUsuario = new Usuario();
                    nuevoUsuario.setNombre(request.getParameter("nombre"));
                    nuevoUsuario.setDocumento(request.getParameter("documento"));
                    nuevoUsuario.setTelefono(request.getParameter("telefono"));
                    nuevoUsuario.setEmail(request.getParameter("email"));
                    nuevoUsuario.setNombreUsuario(request.getParameter("nombreUsuario"));
                    nuevoUsuario.setContrasena(request.getParameter("contrasena"));
                    nuevoUsuario.setCargo(request.getParameter("cargo"));
                    try {
                        nuevoUsuario.setIdRol(Integer.parseInt(request.getParameter("idRol")));
                    } catch (NumberFormatException e) {
                        nuevoUsuario.setIdRol(0);
                    }
                    nuevoUsuario.setEstado(request.getParameter("estado"));
                    nuevoUsuario.setIntentosFallidos(0);

                    usuarioDAO.agregarUsuario(nuevoUsuario);
                    respuesta = new RespuestaJson("success", "Usuario agregado con éxito.");
                    break;

                case "editar":
                    Usuario usuarioEditado = new Usuario();
                    try {
                        usuarioEditado.setIdUsuario(Integer.parseInt(request.getParameter("usuarioId")));
                    } catch (NumberFormatException e) {
                        usuarioEditado.setIdUsuario(0);
                    }
                    usuarioEditado.setNombre(request.getParameter("nombre"));
                    usuarioEditado.setDocumento(request.getParameter("documento"));
                    usuarioEditado.setTelefono(request.getParameter("telefono"));
                    usuarioEditado.setEmail(request.getParameter("email"));
                    usuarioEditado.setNombreUsuario(request.getParameter("nombreUsuario"));
                    usuarioEditado.setCargo(request.getParameter("cargo"));
                    try {
                        usuarioEditado.setIdRol(Integer.parseInt(request.getParameter("idRol")));
                    } catch (NumberFormatException e) {
                        usuarioEditado.setIdRol(0);
                    }
                    usuarioEditado.setEstado(request.getParameter("estado"));
                    try {
                        usuarioEditado.setIntentosFallidos(Integer.parseInt(request.getParameter("intentosFallidos")));
                    } catch (NumberFormatException e) {
                        usuarioEditado.setIntentosFallidos(0);
                    }
                    usuarioDAO.actualizarUsuario(usuarioEditado);
                    respuesta = new RespuestaJson("success", "Usuario actualizado con éxito.");
                    break;

                case "eliminar":
                    try {
                        int idUsuarioEliminar = Integer.parseInt(request.getParameter("id"));
                        usuarioDAO.eliminarUsuario(idUsuarioEliminar);
                        respuesta = new RespuestaJson("success", "Usuario eliminado con éxito.");
                    } catch (NumberFormatException e) {
                        respuesta = new RespuestaJson("error", "ID de usuario no válido.");
                    }
                    break;
                 
                 // No se requiere un `default` que redirija, simplemente enviamos una respuesta JSON
            }
        }
         
        response.getWriter().write(gson.toJson(respuesta));
    }
}
