/*
 * ImagenServlet.java
 * Propósito: Servir imágenes para el carrusel
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 19/10/2025
 */

package com.vifac.sys.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ImagenServlet")
public class ImagenServlet extends HttpServlet {

    private static final String RUTA_IMAGENES = "/usr/local/tomcat/uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
        String idStr = request.getParameter("id");
        com.vifac.sys.dao.ImagenDestacadaDAO dao = new com.vifac.sys.dao.ImagenDestacadaDAO();
    
        try {
            int id = Integer.parseInt(idStr);
            com.vifac.sys.modelo.ImagenDestacada img = dao.obtenerPorId(id);

            if (img != null && img.getArchivoBinario() != null) {
                String nombre = img.getNombreArchivo().toLowerCase();
    
                if (nombre.endsWith(".svg")) {
                   response.setContentType("image/svg+xml");
                } else if (nombre.endsWith(".png")) {
                    response.setContentType("image/png");
                } else if (nombre.endsWith(".gif")) {
                    response.setContentType("image/gif");
                } else {
                    response.setContentType("image/jpeg");
                }
                    response.setContentLength(img.getArchivoBinario().length);
                    response.getOutputStream().write(img.getArchivoBinario());
                    response.getOutputStream().flush(); 
                } else {
                    response.sendError(404, "Imagen no encontrada en BD");
                }
            } catch (IOException | NumberFormatException e) {
                   response.sendError(400, "ID inválido");
        }
    }
}
