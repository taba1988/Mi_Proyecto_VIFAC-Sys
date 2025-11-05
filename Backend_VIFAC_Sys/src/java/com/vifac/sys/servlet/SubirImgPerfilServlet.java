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
import com.vifac.sys.modelo.Usuario;
import java.io.File;
import java.io.IOException;
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Ruta fija donde se guardan las fotos
        String uploadPath = "D:\\Mi_Proyecto_VIFAC-SysGIT\\Backend_VIFAC_Sys\\web\\uploads\\perfiles";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        try {
            // Recibir el archivo enviado desde el formulario
            Part filePart = request.getPart("fotoPerfil"); // <<=== debe coincidir con el fetch JS

            if (filePart != null && filePart.getSize() > 0) {
                // Generar nombre único para evitar conflictos
                String nombreArchivo = System.currentTimeMillis() + "_" +
                        new File(filePart.getSubmittedFileName()).getName().trim().replace(" ", "_");

                // Guardar físicamente el archivo
                String filePath = uploadPath + File.separator + nombreArchivo;
                filePart.write(filePath);

                // Obtener ID del usuario
                String idParam = request.getParameter("idUsuario");
                if (idParam == null || idParam.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro idUsuario.");
                    return;
                }

                int idUsuario = Integer.parseInt(idParam);

                // Actualizar solo el nombre de archivo en la BD
                Usuario usuario = usuarioDAO.obtenerUsuarioPorId(idUsuario);
                if (usuario != null) {
                    usuario.setFotoPerfil(nombreArchivo);
                    usuarioDAO.actualizarUsuario(usuario);
                }

                response.setContentType("application/json");
                response.getWriter().write("{\"exito\": true}");
            } else {
                response.setContentType("application/json");
                response.getWriter().write("{\"exito\": false, \"mensaje\": \"No se seleccionó ninguna imagen.\"}");
            }

        } catch (IOException | NumberFormatException | ServletException e) {
            response.setContentType("application/json");
            response.getWriter().write("{\"exito\": false, \"mensaje\": \"Error: " + e.getMessage() + "\"}");
        }
    }
}
