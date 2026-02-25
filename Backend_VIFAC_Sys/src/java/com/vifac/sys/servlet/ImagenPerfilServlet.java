/*
 * ImagenPerfilServlet.java
 * Propósito: Servir imágenes directamente desde la base de datos (bytes)
 * y permitir la subida de nuevas fotos sin usar carpetas físicas.
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 19/10/2025
 */

package com.vifac.sys.servlet;

import com.vifac.sys.dao.UsuarioDAO;
import com.vifac.sys.modelo.Usuario;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/ImagenPerfilServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,   // 1 MB
    maxFileSize = 5 * 1024 * 1024,      // 5 MB
    maxRequestSize = 10 * 1024 * 1024  // 10 MB
)
public class ImagenPerfilServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("idUsuario");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de usuario no especificado.");
            return;
        }

        try {
            int idUsuario = Integer.parseInt(idParam);
            Usuario u = usuarioDAO.obtenerUsuarioPorId(idUsuario);
            byte[] imagenBytes = (u != null) ? u.getFotoPerfil() : null;

            if (imagenBytes != null && imagenBytes.length > 0) {
                // Servir los bytes desde la base de datos
                response.setContentType("image/jpeg"); 
                response.setContentLength(imagenBytes.length);
                try (ServletOutputStream out = response.getOutputStream()) {
                    out.write(imagenBytes);
                    out.flush();
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "El usuario no tiene foto de perfil.");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de usuario inválido.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            String idParam = request.getParameter("idUsuario");
            Part filePart = request.getPart("fotoPerfil");

            if (idParam == null || filePart == null || filePart.getSize() == 0) {
                out.print("{\"status\":\"error\",\"message\":\"Faltan parámetros o archivo.\"}");
                return;
            }

            int idUsuario = Integer.parseInt(idParam);
            byte[] fotoBytes;

            // Convertir la subida directamente a bytes
            try (InputStream is = filePart.getInputStream()) {
                fotoBytes = is.readAllBytes();
            }

            // Actualizar en la BD con el método binario del DAO
            boolean actualizado = usuarioDAO.actualizarFotoPerfil(idUsuario, fotoBytes);

            if (actualizado) {
                out.print("{\"status\":\"success\",\"message\":\"Foto subida correctamente a la BD.\"}");
            } else {
                out.print("{\"status\":\"error\",\"message\":\"Error al actualizar en la base de datos.\"}");
            }
        } catch (IOException | NumberFormatException | ServletException e) {
            out.print("{\"status\":\"error\",\"message\":\"Error: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}