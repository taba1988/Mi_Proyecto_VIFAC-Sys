/*
 * NotificacionServlet para gestionar notificaciones del sistema.
 * Incluye:
 * - Listado de notificaciones por usuario
 * - Marcar notificación como leída
 * - Registro de nuevas notificaciones
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 */

package com.vifac.sys.servlet;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.vifac.sys.dao.NotificacionDAO;
import com.vifac.sys.modelo.Notificacion;
import com.vifac.sys.modelo.RespuestaJsonNotificacion;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "NotificacionServlet", urlPatterns = {"/NotificacionServlet"})
public class NotificacionServlet extends HttpServlet {

    private final NotificacionDAO notificacionDAO = new NotificacionDAO();

    private final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class,
            (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                new JsonPrimitive(src.toString()))
        .create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sesión inválida");
            return;
        }

        int idUsuario = (Integer) session.getAttribute("idUsuario");
        List<Notificacion> lista = notificacionDAO.listarPorUsuario(idUsuario);
        resp.getWriter().write(gson.toJson(lista));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        RespuestaJsonNotificacion r = new RespuestaJsonNotificacion();

        try {
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) sb.append(line);
            String jsonBody = sb.toString();
            if (jsonBody == null || jsonBody.trim().isEmpty()) jsonBody = "{}";

            JsonReader jsonReader = new JsonReader(new StringReader(jsonBody));
            jsonReader.setLenient(true);
            JsonElement elemento = gson.fromJson(jsonReader, JsonElement.class);

            if (!elemento.isJsonObject()) {
                r.setStatus("false");
                r.setMessage("Se esperaba un objeto JSON");
                resp.getWriter().write(gson.toJson(r));
                return;
            }

            JsonObject datos = elemento.getAsJsonObject();
            String accion = datos.has("accion") && !datos.get("accion").isJsonNull()
                    ? datos.get("accion").getAsString()
                    : null;

            if (accion == null) {
                r.setStatus("false");
                r.setMessage("Parámetro 'accion' no proporcionado");
                resp.getWriter().write(gson.toJson(r));
                return;
            }

            switch (accion) {

                case "crear":
                    manejarCrearNotificacion(req, resp, datos, r);
                    break;

                case "marcarLeida":
                    manejarMarcarLeida(resp, datos, r);
                    break;

                default:
                    r.setStatus("false");
                    r.setMessage("Acción no válida: " + accion);
                    resp.getWriter().write(gson.toJson(r));
                    break;
            }

        } catch (JsonIOException | JsonSyntaxException | IOException e) {
            r.setStatus("error");
            r.setMessage("Error en el servidor: " + e.getMessage());
            resp.getWriter().write(gson.toJson(r));
        }
    }

    // ==========================
    // MÉTODOS PRIVADOS
    // ==========================

    private void manejarCrearNotificacion(HttpServletRequest req, HttpServletResponse resp,
        JsonObject datos, RespuestaJsonNotificacion r) throws IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            r.setStatus("error");
            r.setMessage("Sesión inválida");
            resp.getWriter().write(gson.toJson(r));
            return;
        }

        int idUsuario = (Integer) session.getAttribute("idUsuario");

        if (!datos.has("mensaje") || datos.get("mensaje").isJsonNull()) {
            r.setStatus("false");
            r.setMessage("El mensaje es obligatorio");
            resp.getWriter().write(gson.toJson(r));
            return;
        }

        String mensaje = datos.get("mensaje").getAsString();
        String tipo = datos.has("tipo") ? datos.get("tipo").getAsString() : "INFO";
        Integer referenciaId = datos.has("referencia_id") && !datos.get("referencia_id").isJsonNull()
                ? datos.get("referencia_id").getAsInt()
                : null;

        Notificacion n = new Notificacion();
        n.setIdUsuario(idUsuario);
        n.setMensaje(mensaje);
        n.setTipo(tipo);
        n.setReferenciaId(referenciaId);
        n.setLeido(false);
        n.setFechaCreacion(new java.sql.Timestamp(System.currentTimeMillis()));


        boolean creada = notificacionDAO.registrar(n);

        r.setStatus(creada ? "ok" : "error");
        r.setMessage(creada ? "Notificación creada" : "Error al crear la notificación");
        resp.getWriter().write(gson.toJson(r));
    }

    private void manejarMarcarLeida(HttpServletResponse resp, JsonObject datos,
            RespuestaJsonNotificacion r) throws IOException {

        if (!datos.has("idNotificacion") || datos.get("idNotificacion").isJsonNull()) {
            r.setStatus("false");
            r.setMessage("ID de notificación requerido");
            resp.getWriter().write(gson.toJson(r));
            return;
        }

        int idNotificacion = datos.get("idNotificacion").getAsInt();
        boolean actualizada = notificacionDAO.marcarLeida(idNotificacion);

        r.setStatus(actualizada ? "ok" : "error");
        r.setMessage(actualizada ? "Notificación marcada como leída" : "Error al actualizar");
        resp.getWriter().write(gson.toJson(r));
    }
}
