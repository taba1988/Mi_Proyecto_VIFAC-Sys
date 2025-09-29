/*
 * BienvenidaServlet.java
 * Propósito: Controlador que verifica si el usuario ha iniciado sesión correctamente.
 *            Si está autenticado, lo redirige a la vista de bienvenida.
 *            Si no, lo envía al login para proteger el acceso.
 * 
 * Buenas prácticas:
 * - Control de sesión para evitar acceso no autorizado.
 * - Uso de getSession(false) para no crear sesión nueva si no existe.
 */

package com.vifac.sys.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/BienvenidaServlet")
public class BienvenidaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener la sesión sin crear una nueva si no existe
        HttpSession session = request.getSession(false);

        // Verificar si hay sesión activa y un usuario autenticado
        if (session == null || session.getAttribute("idUsuario") == null) {
            // Redirigir al login si no hay sesión o usuario autenticado
            response.sendRedirect("login.jsp");
            return;
        }

        // Reenviar a la página de bienvenida si el usuario está autenticado
        request.getRequestDispatcher("mensajeBienvenida.jsp").forward(request, response);
    }
}
