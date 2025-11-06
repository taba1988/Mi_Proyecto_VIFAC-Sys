/*
 * CambiarContrasenaServlet.java
 * Propósito: Cambiar la contraseña del usuario logeado validando la contraseña actual.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 23/10/2025
 */

package com.vifac.sys.servlet;

import com.google.gson.Gson;
import com.vifac.sys.dao.UsuarioDAO;
import com.vifac.sys.modelo.RespuestaJson;
import com.vifac.sys.util.PasswordUtil;
import com.vifac.sys.modelo.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/CambiarContrasenaServlet")
public class CambiarContrasenaServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        RespuestaJson respuesta;

        // Obtener el usuario logeado de la sesión
        Usuario usuario = session != null ? (Usuario) session.getAttribute("usuarioLogeado") : null;

        if (usuario == null) {
            respuesta = new RespuestaJson("error", "Debe iniciar sesión para cambiar la contraseña.");
            response.getWriter().write(gson.toJson(respuesta));
            return;
        }

        String contrasenaActual = request.getParameter("inputOldPassword");
        String nuevaContrasena = request.getParameter("nuevaContrasena");
        String confirmarContrasena = request.getParameter("confirmarContrasena");

        // Validar campos vacíos
        if (contrasenaActual == null || contrasenaActual.isEmpty() ||
            nuevaContrasena == null || nuevaContrasena.isEmpty() ||
            confirmarContrasena == null || confirmarContrasena.isEmpty()) {
            respuesta = new RespuestaJson("error", "Todos los campos son obligatorios.");
            response.getWriter().write(gson.toJson(respuesta));
            return;
        }

        // Validar que la contraseña actual sea correcta
        boolean esValida = usuarioDAO.validarContrasena(usuario.getIdUsuario(), contrasenaActual);
        if (!esValida) {
            respuesta = new RespuestaJson("error", "La contraseña actual es incorrecta.");
            response.getWriter().write(gson.toJson(respuesta));
            return;
        }

        // Validar que las nuevas contraseñas coincidan
        if (!nuevaContrasena.equals(confirmarContrasena)) {
            respuesta = new RespuestaJson("error", "Las nuevas contraseñas no coinciden.");
            response.getWriter().write(gson.toJson(respuesta));
            return;
        }

        // Validar seguridad
        if (!PasswordUtil.esSegura(nuevaContrasena)) {
            respuesta = new RespuestaJson("error", "La contraseña no cumple los requisitos de seguridad.");
            response.getWriter().write(gson.toJson(respuesta));
            return;
        }

        // Actualizar contraseña
        boolean actualizado = usuarioDAO.actualizarContrasena(usuario.getIdUsuario(), nuevaContrasena);
        
        if (actualizado) {
             request.setAttribute("mensaje", "Contraseña actualizada correctamente.");
         } else {
             request.setAttribute("mensaje", "Ocurrió un error al actualizar la contraseña.");
         }

         //          Redirigir al JSP que mostrará el modal con el mensaje
         request.getRequestDispatcher("CambiarContrasena.jsp").forward(request, response);
    }  
        
}
