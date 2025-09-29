/*
 * indexServlet.java
 * Propósito: Sirve como un controlador de seguridad para la página de inicio (dashboard).
 *
 * Este servlet valida si el usuario está autenticado verificando la sesión.
 * Si no hay una sesión activa, redirige a la página de inicio de sesión para
 * proteger el contenido. Si el usuario está autenticado, simplemente reenvía
 * la solicitud a la vista (index.jsp).

   Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */

package com.vifac.sys.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/indexServlet")
public class indexServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Recupera la sesión actual sin crear una nueva.
        HttpSession session = request.getSession(false);
        
        // Si la sesión es nula o el ID de usuario no existe en ella, redirige al login.
        if (session == null || session.getAttribute("idUsuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // El usuario está autenticado. Reenvía la solicitud a la página principal.
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}