/*
 * PerfilServlet para mostrar y actualizar los datos del usuario logueado en perfil.jsp
 * Fecha: 26/10/2025
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Nota: Incluye soporte para actualización de foto de perfil.
 */

package com.vifac.sys.servlet;

import com.google.gson.Gson;
import com.vifac.sys.dao.UsuarioDAO;
import com.vifac.sys.modelo.RespuestaJson;
import com.vifac.sys.modelo.Usuario;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/PerfilServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 5 * 1024 * 1024,
    maxRequestSize = 10 * 1024 * 1024
)
public class PerfilServlet extends HttpServlet {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogeado");

        if (usuario == null) {
            Enumeration<String> nombres = session.getAttributeNames();
            while (nombres.hasMoreElements()) System.out.println(" - " + nombres.nextElement());

            String idStr = request.getParameter("idUsuario");
            if (idStr == null && request.getCookies() != null) {
                for (Cookie c : request.getCookies()) {
                    if ("idUsuario".equals(c.getName())) { idStr = c.getValue(); break; }
                }
            }

            if (idStr != null) {
                try {
                    int idUsuario = Integer.parseInt(idStr);
                    usuario = usuarioDAO.obtenerUsuarioPorId(idUsuario);
                    if (usuario != null) session.setAttribute("usuarioLogueado", usuario);
                } catch (NumberFormatException ignored) {}
            }
        }

        if (usuario == null) {
            request.setAttribute("mensaje", "No se encontró información del usuario activo.");
            request.getRequestDispatcher("Perfil.jsp").forward(request, response);
            return;
        }

        usuario = usuarioDAO.obtenerUsuarioPorId(usuario.getIdUsuario());
        String nota = String.format(
            "Rol: %s | Última actualización: %s<br>Inconsistencias con tus datos? Contacta al administrador.",
            usuario.getCargo(),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        );

        usuario.setNotaSistema(nota);
        session.setAttribute("usuarioLogueado", usuario);
        request.setAttribute("usuario", usuario);
        request.getRequestDispatcher("Perfil.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;
        RespuestaJson r;

        if (usuario == null) {
            r = new RespuestaJson("error", "Sesión no iniciada o vencida.");
        } else if ("actualizarPerfil".equals(request.getParameter("accion"))) {
            usuario.setTelefono(request.getParameter("telefono"));
            usuario.setDireccion(request.getParameter("direccion"));

            Part filePart = request.getPart("fotoPerfil");
            if (filePart != null && filePart.getSize() > 0) {
                String nombreArchivo = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName().replace(" ", "_");
                String rutaGuardado = getServletContext().getRealPath("/uploads/fotosPerfil/");
                File dir = new File(rutaGuardado);
                if (!dir.exists()) dir.mkdirs();
                filePart.write(rutaGuardado + File.separator + nombreArchivo);
                usuario.setFotoPerfil("uploads/fotosPerfil/" + nombreArchivo);
            }

            boolean exito = usuarioDAO.actualizarDatosPerfil(usuario);
            if (exito) {
                session.setAttribute("usuarioLogueado", usuario);
                r = new RespuestaJson("success", "Perfil actualizado correctamente.");
            } else {
                r = new RespuestaJson("error", "No se pudo actualizar el perfil.");
            }
        } else {
            r = new RespuestaJson("error", "Acción no reconocida.");
        }

        response.getWriter().write(gson.toJson(r));
    }

    @Override
    public String getServletInfo() {
        return "PerfilServlet activo";
    }
}
