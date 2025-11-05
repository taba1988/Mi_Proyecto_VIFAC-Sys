/*
 * RestablecerContrasenaServlet.java
 * Propósito: Recibe token + nueva contraseña, valida y actualiza la contraseña del usuario en la base de datos.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 23/10/2025
 */

package com.vifac.sys.servlet;

import com.google.gson.Gson;
import com.vifac.sys.dao.UsuarioDAO;
import com.vifac.sys.modelo.RespuestaJson;
import com.vifac.sys.util.PasswordUtil; // Clase utilitaria para hashear la contraseña
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RestablecerContrasenaServlet")
public class RestablecerContrasenaServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String token = request.getParameter("token");
        String nuevaContrasena = request.getParameter("nuevaContrasena");
        String confirmarContrasena = request.getParameter("confirmarContrasena");

        RespuestaJson respuesta;

        // Validar token
        if (token == null || token.isEmpty()) {
            respuesta = new RespuestaJson("error", "Token inválido.");
            response.getWriter().write(gson.toJson(respuesta));
            return;
        }

        // Validar contraseñas
        if (nuevaContrasena == null || confirmarContrasena == null || !nuevaContrasena.equals(confirmarContrasena)) {
            respuesta = new RespuestaJson("error", "Las contraseñas no coinciden.");
            response.getWriter().write(gson.toJson(respuesta));
            return;
        }

        // Validar seguridad
        if (!PasswordUtil.esSegura(nuevaContrasena)) {
            respuesta = new RespuestaJson("error", "La contraseña no cumple los requisitos de seguridad.");
            response.getWriter().write(gson.toJson(respuesta));
            return;
        }

        // Actualizar contraseña en la base de datos
        boolean actualizado = usuarioDAO.actualizarContrasenaPorToken(token, nuevaContrasena);

        if (actualizado) {
            respuesta = new RespuestaJson("success", "Contraseña recuperada correctamente.");
        } else {
            respuesta = new RespuestaJson("error", "Token inválido o expirado.");
        }

        response.getWriter().write(gson.toJson(respuesta));
    }
}
