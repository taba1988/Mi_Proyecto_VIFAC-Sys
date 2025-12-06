/**
 * Servlet que maneja la visualización y actualización del perfil del usuario.
 * Solo permite actualizar teléfono y dirección.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 26/10/2025
 */
package com.vifac.sys.servlet;

import com.google.gson.Gson;
import com.vifac.sys.dao.UsuarioDAO;
import com.vifac.sys.modelo.RespuestaJsonPerfil;
import com.vifac.sys.modelo.Usuario;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/PerfilServlet")
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
                    if (usuario != null) session.setAttribute("usuarioLogeado", usuario);
                } catch (NumberFormatException ignored) {}
            }
        }

        if (usuario == null) {
            request.setAttribute("mensaje", "No se encontró información del usuario activo.");
            request.getRequestDispatcher("Perfil.jsp").forward(request, response);
            return;
        }

        Usuario actualizado = usuarioDAO.obtenerUsuarioPorId(usuario.getIdUsuario());

        String nota = String.format(
            "Rol: %s | Última actualización: %s<br>Inconsistencias con tus datos? Contacta al administrador.",
            actualizado.getCargo(),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        );
        actualizado.setNotaSistema(nota);

        session.setAttribute("usuarioLogeado", actualizado);
        request.setAttribute("usuario", actualizado);
        request.getRequestDispatcher("Perfil.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession(false);
        Usuario usuario = null;
        if (session != null) {
            Usuario uSession = (Usuario) session.getAttribute("usuarioLogeado");
            if (uSession != null) {
                usuario = usuarioDAO.obtenerUsuarioPorId(uSession.getIdUsuario());
            }
        }
        RespuestaJsonPerfil r;

        if (usuario == null) {
            r = new RespuestaJsonPerfil("error", "Sesión no iniciada o vencida.");
        } else if ("actualizarPerfil".equals(request.getParameter("accion"))) {
        Usuario usuarioLogeado = (Usuario) session.getAttribute("usuarioLogeado");
        usuario.setIdUsuario(usuarioLogeado.getIdUsuario());
        usuario.setTelefono(request.getParameter("telefono"));
        usuario.setDireccion(request.getParameter("direccion"));

            boolean exito = usuarioDAO.actualizarDatosPerfil(usuario);
            if (exito) {
                session.setAttribute("usuarioLogeado", usuario);
                r = new RespuestaJsonPerfil("success", "Perfil actualizado correctamente.");
            } else {
                r = new RespuestaJsonPerfil("error", "No se pudo actualizar el perfil.");
            }
        } else {
            r = new RespuestaJsonPerfil("error", "Acción no reconocida.");
        }

        response.getWriter().write(gson.toJson(r));
    }

    @Override
    public String getServletInfo() {
        return "PerfilServlet activo - permite editar teléfono y dirección";
    }
}
