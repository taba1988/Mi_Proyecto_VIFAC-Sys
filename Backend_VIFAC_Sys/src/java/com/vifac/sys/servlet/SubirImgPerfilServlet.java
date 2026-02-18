/*
 * SubirImgPerfilServlet.java
 * Propósito: Permite subir la imagen de perfil de un usuario
 * y actualizarla tanto en la base de datos como en la carpeta física.
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 30/10/2025
 */

package com.vifac.sys.servlet;

import com.vifac.sys.dao.UsuarioDAO;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/SubirImgPerfilServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,   // 1 MB
    maxFileSize = 5 * 1024 * 1024,     // 5 MB
    maxRequestSize = 10 * 1024 * 1024  // 10 MB
)
public class SubirImgPerfilServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final String RUTA_PERFILES = "/usr/local/tomcat/uploads/perfiles";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        File uploadDir = new File(RUTA_PERFILES);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            Part filePart = request.getPart("fotoPerfil"); // debe coincidir con el input del modal
            String idParam = request.getParameter("idUsuario");

            if (filePart == null || filePart.getSize() == 0 || idParam == null || idParam.isEmpty()) {
                out.write("{\"status\":\"error\",\"message\":\"Archivo o parámetro idUsuario faltante.\"}");
                return;
            }

            int idUsuario = Integer.parseInt(idParam);

            // Generar nombre fijo para evitar conflictos
            String nombreArchivo = "perfil_" + idUsuario + filePart.getSubmittedFileName()
               .substring(filePart.getSubmittedFileName().lastIndexOf('.'));


            // Guardar archivo físicamente
            File archivoDestino = new File(RUTA_PERFILES, nombreArchivo);
               filePart.write(archivoDestino.getAbsolutePath());

            // Actualizar solo el nombre de archivo en la base de datos
            boolean actualizado = usuarioDAO.actualizarFotoPerfil(idUsuario, nombreArchivo);

               if (actualizado) {
                   out.write("{\"status\":\"success\",\"message\":\"Foto de perfil actualizada correctamente.\"}");
               } else {
                   out.write("{\"status\":\"error\",\"message\":\"Error al actualizar la foto en la base de datos.\"}");
               }

        } catch (IOException | NumberFormatException | ServletException e) {
            out.write("{\"status\":\"error\",\"message\":\"Error: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}
