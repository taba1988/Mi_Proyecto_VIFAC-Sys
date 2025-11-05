/*
 * ImagenPerfilServlet.java
 * Propósito: Servir imágenes de la carpeta uploads\perfiles 
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 19/10/2025
 */

package com.vifac.sys.servlet;

import com.vifac.sys.dao.UsuarioDAO;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;

@WebServlet("/ImagenPerfilServlet")
@MultipartConfig
public class ImagenPerfilServlet extends HttpServlet {

    private static final String RUTA_PERFILES = "D:\\Mi_Proyecto_VIFAC-SysGIT\\Backend_VIFAC_Sys\\web\\uploads\\perfiles";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombreArchivo = request.getParameter("nombreArchivo");
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nombre de archivo no especificado.");
            return;
        }
        
        // Construir ruta completa al archivo
        File archivo = new File(RUTA_PERFILES, nombreArchivo);
        if (!archivo.exists() || archivo.isDirectory()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Archivo no encontrado: " + nombreArchivo);
            return;
        }
        
        // Determinar tipo MIME según el archivo
        String tipoMime = getServletContext().getMimeType(archivo.getName());
        if (tipoMime == null) {
            tipoMime = "application/octet-stream";
        }
        response.setContentType(tipoMime);
        response.setContentLengthLong(archivo.length());

        // Transmitir la imagen al navegador
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(archivo));
             ServletOutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesLeidos;
            while ((bytesLeidos = bis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesLeidos);
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        Part filePart = request.getPart("fotoPerfil");
        String nombreArchivo = filePart.getSubmittedFileName();

        File carpeta = new File(RUTA_PERFILES);
        if (!carpeta.exists()) carpeta.mkdirs();

        File archivoDestino = new File(carpeta, nombreArchivo);
        filePart.write(archivoDestino.getAbsolutePath());

        // Actualizar la ruta en la base de datos
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        boolean actualizado = usuarioDAO.actualizarFotoPerfil(idUsuario, nombreArchivo);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        if(actualizado){
            out.print("{\"status\":\"success\",\"message\":\"Foto subida correctamente.\"}");
        } else {
            out.print("{\"status\":\"error\",\"message\":\"Error al actualizar la foto.\"}");
        }
        out.flush();
    }
}
