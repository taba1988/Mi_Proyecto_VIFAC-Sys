/*
 * ImagenServlet.java
 * Propósito: Servir imágenes de la carpeta uploads para el carrusel
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

    private static final String RUTA_IMAGENES = "D:\\Mi_Proyecto_VIFAC-SysGIT\\Backend_VIFAC_Sys\\web\\uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombreArchivo = request.getParameter("nombreArchivo");
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nombre de archivo no especificado.");
            return;
        }

        File archivo = new File(RUTA_IMAGENES, nombreArchivo);
        if (!archivo.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Archivo no encontrado: " + nombreArchivo);
            return;
        }

        // Determinar tipo MIME
        String tipoMime = getServletContext().getMimeType(archivo.getName());
        if (tipoMime == null) {
            tipoMime = "application/octet-stream";
        }
        response.setContentType(tipoMime);

        // Enviar archivo al navegador
        try (FileInputStream fis = new FileInputStream(archivo);
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesLeidos;
            while ((bytesLeidos = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesLeidos);
            }
        }
    }
}
