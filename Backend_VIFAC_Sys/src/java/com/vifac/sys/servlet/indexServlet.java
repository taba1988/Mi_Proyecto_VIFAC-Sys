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

import com.vifac.sys.dao.ImagenDestacadaDAO;
import com.vifac.sys.modelo.ImagenDestacada;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/indexServlet")
public class indexServlet extends HttpServlet {

    private final ImagenDestacadaDAO imagenDAO = new ImagenDestacadaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("idUsuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Cargar imágenes destacadas
        List<ImagenDestacada> lista = imagenDAO.listarTodas();
        request.setAttribute("imagenesDestacadas", lista);

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
