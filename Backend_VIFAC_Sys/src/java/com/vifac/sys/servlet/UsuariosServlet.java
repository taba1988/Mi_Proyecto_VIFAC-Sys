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
import com.vifac.sys.dao.NotificacionDAO;
import com.vifac.sys.modelo.Notificacion;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

@WebServlet("/UsuariosServlet")
public class UsuariosServlet extends HttpServlet {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final NotificacionDAO notificacionDAO = new NotificacionDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

         String accion = request.getParameter("accion");
         HttpSession session = request.getSession(false);
         int idUsuario = 0;
         if (session != null && session.getAttribute("idUsuario") != null) {
             idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString());
         }
        Usuario u = usuarioDAO.obtenerUsuarioPorId(idUsuario);
        String nombreUsuario = u.getNombre();
        String usuarioTxt = "El Usuario #" + idUsuario + " - " + nombreUsuario + " ";
        if (accion == null || "listar".equals(accion)) {
            // Acción por defecto: listar todos los usuarios
            try {
                List<Usuario> usuarios = usuarioDAO.listarUsuarios();
                String json = gson.toJson(usuarios);
                response.getWriter().write(json);
            } catch (IOException e) {
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
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                RespuestaJson respuesta = new RespuestaJson("error", "Error en la búsqueda: " + e.getMessage());
                response.getWriter().write(gson.toJson(respuesta));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        HttpSession session = request.getSession(false);
        int idUsuario = 0;
        if (session != null && session.getAttribute("idUsuario") != null) {
            idUsuario = Integer.parseInt(session.getAttribute("idUsuario").toString());
        }
        Usuario u = usuarioDAO.obtenerUsuarioPorId(idUsuario);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        RespuestaJson respuesta = new RespuestaJson("error", "Acción no válida.");

        if (accion != null) {
            switch (accion) {
              
                // --- Acción agregar usuario --- //
       case "agregar":
           Usuario nuevoUsuario = new Usuario();
           nuevoUsuario.setNombre(request.getParameter("nombre"));
           nuevoUsuario.setDocumento(request.getParameter("documento"));
           nuevoUsuario.setDireccion(request.getParameter("direccion"));
           nuevoUsuario.setTelefono(request.getParameter("telefono"));
           nuevoUsuario.setEmail(request.getParameter("email"));
           nuevoUsuario.setNombreUsuario(request.getParameter("nombreUsuario"));
           nuevoUsuario.setContrasena(request.getParameter("contrasena"));
           nuevoUsuario.setCargo(request.getParameter("cargo"));
           try {
               nuevoUsuario.setIdRol(Integer.parseInt(request.getParameter("idRol")));
           } 
           catch (NumberFormatException e) {
                  nuevoUsuario.setIdRol(0);
           }
           try {
               nuevoUsuario.setIdEmpresa(Integer.parseInt(request.getParameter("idEmpresa")));
           }            
           catch (NumberFormatException e) {
                  nuevoUsuario.setIdEmpresa(1);
           }

           nuevoUsuario.setEstado(request.getParameter("estado"));
           nuevoUsuario.setIntentosFallidos(0);
       
           // Validar si el documento ya existe
           if (usuarioDAO.existeDocumento(nuevoUsuario.getDocumento())) {
               respuesta = new RespuestaJson("error", "Documento o cédula ya existe.");
           } else 
               
           if (usuarioDAO.existeUsuarioPorEmail(nuevoUsuario.getEmail())) {
               respuesta = new RespuestaJson("error", "Correo electrónico ya existe.");
           } else {
               // Si todo bien, agrega el usuario
               usuarioDAO.agregarUsuario(nuevoUsuario);
               
               int idUsuarioGenerado = usuarioDAO.listarUsuarios()
                   .stream()
                   .filter(x -> x.getDocumento().equals(nuevoUsuario.getDocumento()))
                   .findFirst()
                   .map(x -> x.getIdUsuario())
                   .orElse(0);
               respuesta = new RespuestaJson("success", "Usuario agregado con éxito.");                             
               Notificacion n = new Notificacion();
               n.setIdUsuario(idUsuario);
               n.setTipo("USUARIOS");
               n.setMensaje(
                   "El Usuario #" + idUsuario + " - " + u.getNombre() + 
                   " realizó el registro del usuario ID #" + idUsuarioGenerado +
                   " (" + nuevoUsuario.getNombre() + ")"
               );
               n.setLeido(false);
               n.setFechaCreacion(new Timestamp(System.currentTimeMillis() - (5 * 3600 * 1000)));
               notificacionDAO.registrar(n);
               }
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
                    usuarioEditado.setDireccion(request.getParameter("direccion"));
                    usuarioEditado.setEmail(request.getParameter("email"));
                    usuarioEditado.setNombreUsuario(request.getParameter("nombreUsuario"));
                    usuarioEditado.setCargo(request.getParameter("cargo"));
                    try {
                        usuarioEditado.setIdRol(Integer.parseInt(request.getParameter("idRol")));
                    } catch (NumberFormatException e) {
                        usuarioEditado.setIdRol(0);
                    }
                    try {
                       usuarioEditado.setIdEmpresa(Integer.parseInt(request.getParameter("idEmpresa")));
                    } catch (NumberFormatException e) {
                       usuarioEditado.setIdEmpresa(1);
                    }
                    usuarioEditado.setEstado(request.getParameter("estado"));
                    try {
                        usuarioEditado.setIntentosFallidos(Integer.parseInt(request.getParameter("intentosFallidos")));
                    } catch (NumberFormatException e) {
                        usuarioEditado.setIntentosFallidos(0);
                    }

                    boolean exito = usuarioDAO.actualizarUsuario(usuarioEditado);
                    if (exito) {
                        respuesta = new RespuestaJson(
                        "success",
                        "Los datos del usuario se han guardado correctamente."
                    );
                        
                        Notificacion n = new Notificacion();
                        n.setIdUsuario(idUsuario);
                        n.setTipo("USUARIOS");
                        n.setMensaje(
                            "El Usuario #" + idUsuario + " - " + u.getNombre() +
                            " actualizó los datos del usuario ID #" + usuarioEditado.getIdUsuario() +
                            " (" + usuarioEditado.getNombre() + ")"
                        );
                        n.setLeido(false);
                        n.setFechaCreacion(new Timestamp(System.currentTimeMillis() - (5 * 3600 * 1000)));
                        notificacionDAO.registrar(n);
                    } else {
                        respuesta = new RespuestaJson("error", "No se pudo actualizar el usuario. Verifica el ID.");
                    }
                    break;

            case "eliminar":
                try {
                    int idUsuarioEliminar = Integer.parseInt(request.getParameter("id"));
            
                    // --- Obtener usuario antes de eliminar ---
                    Usuario usuarioAEliminar = usuarioDAO.obtenerUsuarioPorId(idUsuarioEliminar);
            
                    // --- Eliminar usuario ---
                    boolean eliminado = usuarioDAO.eliminarUsuario(idUsuarioEliminar);
                    if (eliminado) {
                        respuesta = new RespuestaJson("success", "Usuario eliminado con éxito.");
            
                        // --- Registrar notificación ---
                        if (usuarioAEliminar != null) {
                            Notificacion n = new Notificacion();
                            n.setIdUsuario(idUsuario);
                            n.setTipo("USUARIOS");
                            n.setMensaje(
                                "El Usuario #" + idUsuario + " - " + u.getNombre() + 
                                " eliminó el usuario ID #" + idUsuarioEliminar +
                                " (" + usuarioAEliminar.getNombre() + ")"
                            );
                            n.setLeido(false);
                            n.setFechaCreacion(new Timestamp(System.currentTimeMillis() - (5 * 3600 * 1000)));
                            notificacionDAO.registrar(n);
                        }
                    } else {
                        respuesta = new RespuestaJson("error", "No se pudo eliminar el usuario.");
                    }
            
                } catch (NumberFormatException e) {
                    respuesta = new RespuestaJson("error", "ID de usuario no válido.");
                }
                break;           
            }
        }
         
      response.getWriter().write(gson.toJson(respuesta));
    }
}