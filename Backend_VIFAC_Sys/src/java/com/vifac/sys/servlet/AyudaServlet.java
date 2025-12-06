/*
 * AyudaServlet.java
 * Propósito: Gestionar videos de ayuda con su imagen de portada.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 04/12/2025
 */

package com.vifac.sys.servlet;

import com.vifac.sys.dao.AyudaDAO;
import com.vifac.sys.modelo.Ayuda;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/AyudaServlet")
@MultipartConfig
public class AyudaServlet extends HttpServlet {

    private static final String RUTA_IMAGENES = "D:\\Mi_Proyecto_VIFAC-SysGIT\\Backend_VIFAC_Sys\\web\\uploads\\ayuda";
    private final AyudaDAO ayudaDAO = new AyudaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        
        String accion = request.getParameter("accion");
        PrintWriter out = response.getWriter();

        if ("eliminar".equalsIgnoreCase(accion)) {
            String idStr = request.getParameter("id");
            try {
                int id = Integer.parseInt(idStr);
                // Si la eliminación falla por DB (SQLException), la excepción se propagará
                // y deberá ser manejada por la capa de manejo de excepciones del servidor, 
                // a menos que se añada un catch(Exception e) aquí.
                boolean exito = ayudaDAO.eliminar(id);
        
                if (exito) {
                    out.print("{\"status\":\"success\",\"message\":\"Video eliminado correctamente.\"}");
                } else {
                    out.print("{\"status\":\"error\",\"message\":\"No se pudo eliminar el video.\"}");
                }
            } catch (NumberFormatException e) {
                out.print("{\"status\":\"error\",\"message\":\"ID inválido.\"}");
            }
            out.flush();
            return;
        }

        String titulo = request.getParameter("titulo");
        String urlVideo = request.getParameter("url_video");
        Part imagenPart = request.getPart("imagen");

        String nombreImagen = null;

        // Subir imagen si existe
        if (imagenPart != null && imagenPart.getSize() > 0) {
            nombreImagen = System.currentTimeMillis() + "_" + imagenPart.getSubmittedFileName();
            File carpeta = new File(RUTA_IMAGENES);
            if (!carpeta.exists()) carpeta.mkdirs();
            File archivoDestino = new File(carpeta, nombreImagen);
            imagenPart.write(archivoDestino.getAbsolutePath());
        }

        // Crear objeto Ayuda
        Ayuda ayuda = new Ayuda();
        ayuda.setTitulo(titulo);
        ayuda.setUrlVideo(urlVideo);
        ayuda.setUrlImagen(nombreImagen);

        boolean exito = ayudaDAO.insertar(ayuda);

        // ❌ Línea eliminada: PrintWriter out = response.getWriter(); 

        if (exito) {
            out.print("{\"status\":\"success\",\"message\":\"Video de ayuda guardado correctamente.\"}");
        } else {
            out.print("{\"status\":\"error\",\"message\":\"Error al guardar el video de ayuda.\"}");
        }
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        PrintWriter out = response.getWriter();
        try {
            // Listar todos los videos
            out.print("[");
            boolean first = true;
            for (Ayuda ayuda : ayudaDAO.listarTodos()) {
                if (!first) out.print(",");
                out.print("{"
                        + "\"id\":" + ayuda.getId() + ","
                        + "\"titulo\":\"" + ayuda.getTitulo() + "\","
                        + "\"url_video\":\"" + ayuda.getUrlVideo() + "\","
                        + "\"url_imagen\":\"" + ayuda.getUrlImagen() + "\""
                        + "}");
                first = false;
            }
            out.print("]");
        } catch (Exception e) {
            out.print("{\"status\":\"error\",\"message\":\"Error al listar videos de ayuda.\"}");
        }
        out.flush();
    }
}