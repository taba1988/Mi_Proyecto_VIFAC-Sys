
/*
 * logoutServlet.java
 * Propósito: Gestiona el proceso de cierre de sesión del usuario.
 * Invalida la sesión actual y elimina cualquier cookie persistente,
 * como la de "Recordarme", para garantizar una salida segura.
   Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */

package com.vifac.sys.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/logoutServlet")
public class logoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtiene la sesión actual sin crear una nueva.
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Invalida la sesión, eliminando todos los atributos guardados.
            session.invalidate();
        }

        // Obtiene todas las cookies para buscar la de "Recordarme".
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Si la cookie es la de "Recordarme", la elimina.
                if ("rememberMe".equals(cookie.getName())) {
                    cookie.setMaxAge(0); // Establece la edad en 0 para borrarla.
                    response.addCookie(cookie); // Agrega la cookie modificada a la respuesta.
                    break; // Salir del bucle una vez que se encuentra y elimina.
                }
            }
        }
        
        // Redirige al usuario a la página de inicio de sesión.
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirige las solicitudes POST a doGet() para manejar el cierre de sesión de forma consistente.
        doGet(request, response);
    }
}