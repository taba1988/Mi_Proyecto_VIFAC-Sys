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
            // 💡 Establecer un atributo de error ANTES de la redirección
            // para que la página de login pueda mostrar un mensaje contextual.
            request.setAttribute("error", "Su sesión ha expirado. Por favor, inicie sesión de nuevo.");
            
            // 🚨 ACCIÓN CLAVE: Redireccionar directamente a login.jsp 🚨
            response.sendRedirect("login.jsp"); 
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
            request.setAttribute("error", "La contraseña actual es incorrecta.");
            request.getRequestDispatcher("CambiarContrasena.jsp").forward(request, response);
            return;
        }

        // Validar que las nuevas contraseñas coincidan
        // if (!nuevaContrasena.equals(confirmarContrasena)) {
        // request.setAttribute("error", "Las nuevas contraseñas no coinciden.");
        // request.getRequestDispatcher("CambiarContrasena.jsp").forward(request, response);
        //nreturn; }

        // Validar seguridad
        if (!PasswordUtil.esSegura(nuevaContrasena)) {
            request.setAttribute("error", "La contraseña no cumple los requisitos de seguridad.");
            request.getRequestDispatcher("CambiarContrasena.jsp").forward(request, response);
            return;
        }

        // Actualizar contraseña
        boolean actualizado = usuarioDAO.actualizarContrasena(usuario.getIdUsuario(), nuevaContrasena);
        
        if (actualizado) {
             request.setAttribute("mensaje", "Contraseña actualizada correctamente.");
         } else {
             request.setAttribute("error", "Ocurrió un error al actualizar la contraseña.");
         }

         //          Redirigir al JSP que mostrará el modal con el mensaje
         request.getRequestDispatcher("CambiarContrasena.jsp").forward(request, response);
    }  
        
}
