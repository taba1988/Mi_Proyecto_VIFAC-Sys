/*
 * SubirImagenServlet.java
 * Propósito: Permite al administrador subir imágenes destacadas 
 * para el carrusel y actualizar individualmente sin duplicar registros.
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 19/10/2025
 */

package com.vifac.sys.servlet;

import com.vifac.sys.dao.ImagenDestacadaDAO;
import com.vifac.sys.modelo.ImagenDestacada;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.InputStream;

@WebServlet("/SubirImagenServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,    // 1MB
    maxFileSize = 1024 * 1024 * 5,      // 5MB
    maxRequestSize = 1024 * 1024 * 10   // 10MB
)
public class SubirImagenServlet extends HttpServlet {

    private final ImagenDestacadaDAO imagenDAO = new ImagenDestacadaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Inputs para los banners (hasta 6)
        String[] nombresFijos = {"banner1", "banner2", "banner3", "banner4", "banner5", "banner6"};

        try {
            for (String nombreFijo : nombresFijos) {
                Part filePart = request.getPart(nombreFijo);

                if (filePart != null && filePart.getSize() > 0) {

                    // Nombre real del archivo
                    String nombreReal = filePart.getSubmittedFileName().replace(" ", "_");

                    // Capturar los bytes directamente del input
                    byte[] bytesImagen;
                    try (InputStream is = filePart.getInputStream()) {
                        bytesImagen = is.readAllBytes(); 
                    }

                    // Revisar si ya existe una imagen con este nombre fijo
                    ImagenDestacada existente = imagenDAO.obtenerPorNombreFijo(nombreFijo);

                   if (existente != null) {
                        // Actualizar imagen existente
                       existente.setNombreArchivo(nombreReal);
                       existente.setArchivoBinario(bytesImagen);
                       imagenDAO.actualizarImagen(existente);
                   } else {
                        
                    // Crear nueva imagen usando directamente el nombre fijo
                       ImagenDestacada img = new ImagenDestacada();
                       img.setNombreFijo(nombreFijo);
                       img.setNombreArchivo(nombreReal);
                       img.setArchivoBinario(bytesImagen);
                       imagenDAO.guardarImagen(img);

                   }
                }
            }

            // Redirigir al indexServlet para mantener carrusel funcional
            response.sendRedirect("indexServlet");

        } catch (IOException | ServletException e) {
             e.printStackTrace();
            response.getWriter().println("Error al subir las imágenes: " + e.getMessage());
        }
    }
}
