
/*
 * Servlet para gestionar las notificaciones del sistema.
 * Permite listar, crear, marcar como leídas, eliminar y restaurar notificaciones.
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
import java.sql.Timestamp;
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
    String bandeja = req.getParameter("bandeja");

    List<Notificacion> lista;

    if (bandeja != null && !bandeja.trim().isEmpty()) {
        lista = notificacionDAO.listarPorBandeja(idUsuario, bandeja);
    } else {
        lista = notificacionDAO.listarPorUsuario(idUsuario);
    }

    resp.getWriter().write(gson.toJson(lista));
}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        RespuestaJsonNotificacion r = new RespuestaJsonNotificacion();

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("idUsuario") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            r.setStatus("error");
            r.setMessage("Sesión inválida");
            resp.getWriter().write(gson.toJson(r));
            return;
        }

        int idUsuario = (Integer) session.getAttribute("idUsuario");

        try {
            StringBuilder sb = new StringBuilder();
            String line;

            try (BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            String jsonBody = sb.toString();

            if (jsonBody.trim().isEmpty()) {
                jsonBody = "{}";
            }

            JsonReader jsonReader =
                    new JsonReader(new StringReader(jsonBody));
            jsonReader.setLenient(true);

            JsonElement elemento =
                    gson.fromJson(jsonReader, JsonElement.class);

            if (elemento == null || !elemento.isJsonObject()) {
                responder(resp, r, "error", "Se esperaba un objeto JSON");
                return;
            }

            JsonObject datos = elemento.getAsJsonObject();

            String accion = datos.has("accion")
                    && !datos.get("accion").isJsonNull()
                    ? datos.get("accion").getAsString()
                    : null;

            if (accion == null || accion.trim().isEmpty()) {
                responder(resp, r, "error",
                        "Parámetro 'accion' no proporcionado");
                return;
            }

            switch (accion) {

                case "crear":
                    manejarCrearNotificacion(datos, idUsuario, resp, r);
                    break;

                case "marcarLeida":
                    manejarMarcarLeida(datos, idUsuario, resp, r);
                    break;

                case "eliminar":
                    manejarEliminar(datos, idUsuario, resp, r);
                    break;

                case "restaurar":
                    manejarRestaurar(datos, idUsuario, resp, r);
                    break;

                case "eliminarDefinitivamente":
                    manejarEliminarDefinitivamente(datos, idUsuario, resp, r);
                    break;

                default:
                    responder(resp, r, "error",
                            "Acción no válida: " + accion);
                    break;
            }

        } catch (JsonParseException | IllegalStateException e) {
            responder(resp, r, "error", "JSON inválido");
        }
    }

    // Crear notificación para el usuario autenticado
    private void manejarCrearNotificacion(JsonObject datos, int idUsuario,
            HttpServletResponse resp, RespuestaJsonNotificacion r)
            throws IOException {

        if (!datos.has("mensaje") || datos.get("mensaje").isJsonNull()) {
            responder(resp, r, "error", "El mensaje es obligatorio");
            return;
        }

        String mensaje = datos.get("mensaje").getAsString();

        String tipo = datos.has("tipo") && !datos.get("tipo").isJsonNull()
                ? datos.get("tipo").getAsString()
                : "INFO";

        Integer referenciaId =
                datos.has("referencia_id")
                && !datos.get("referencia_id").isJsonNull()
                ? datos.get("referencia_id").getAsInt()
                : null;

        Notificacion n = new Notificacion();
        n.setIdUsuario(idUsuario);
        n.setMensaje(mensaje);
        n.setTipo(tipo);
        n.setReferenciaId(referenciaId);
        n.setLeido(false);
        n.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
        n.setEliminado(false);
        n.setFechaEliminacion(null);

        boolean creada = notificacionDAO.registrar(n);

        responder(resp, r,
                creada ? "ok" : "error",
                creada ? "Notificación creada"
                       : "Error al crear la notificación");
    }

    // Marcar notificación como leída
    private void manejarMarcarLeida(JsonObject datos, int idUsuario,
            HttpServletResponse resp, RespuestaJsonNotificacion r)
            throws IOException {

        Integer id = obtenerId(datos, resp, r);

        if (id == null) {
            return;
        }

        boolean actualizada =
                notificacionDAO.marcarLeida(id, idUsuario);

        responder(resp, r,
                actualizada ? "ok" : "error",
                actualizada ? "Notificación marcada como leída"
                            : "No se pudo actualizar la notificación");
    }

    // Mover notificación a Eliminados
    private void manejarEliminar(JsonObject datos, int idUsuario,
            HttpServletResponse resp, RespuestaJsonNotificacion r)
            throws IOException {

        Integer id = obtenerId(datos, resp, r);

        if (id == null) {
            return;
        }

        boolean eliminada = notificacionDAO.eliminar(id, idUsuario);

        responder(resp, r,
                eliminada ? "ok" : "error",
                eliminada ? "Notificación movida a Eliminados"
                          : "No se pudo eliminar la notificación");
    }

    // Restaurar notificación
    private void manejarRestaurar(JsonObject datos, int idUsuario,
            HttpServletResponse resp, RespuestaJsonNotificacion r)
            throws IOException {

        Integer id = obtenerId(datos, resp, r);

        if (id == null) {
            return;
        }

        boolean restaurada = notificacionDAO.restaurar(id, idUsuario);

        responder(resp, r,
                restaurada ? "ok" : "error",
                restaurada ? "Notificación restaurada"
                           : "No se pudo restaurar la notificación");
    }

    // Eliminar definitivamente una notificación
    private void manejarEliminarDefinitivamente(JsonObject datos, int idUsuario,
            HttpServletResponse resp, RespuestaJsonNotificacion r)
            throws IOException {

        Integer id = obtenerId(datos, resp, r);

        if (id == null) {
            return;
        }

        boolean eliminada =
                notificacionDAO.eliminarDefinitivamente(id, idUsuario);

        responder(resp, r,
                eliminada ? "ok" : "error",
                eliminada ? "Notificación eliminada definitivamente"
                          : "No se pudo eliminar definitivamente");
    }

    // Validar y obtener el ID recibido
    private Integer obtenerId(JsonObject datos,
            HttpServletResponse resp, RespuestaJsonNotificacion r)
            throws IOException {

        if (!datos.has("idNotificacion")
                || datos.get("idNotificacion").isJsonNull()) {
            responder(resp, r, "error",
                    "ID de notificación requerido");
            return null;
        }

        try {
            int id = datos.get("idNotificacion").getAsInt();

            if (id <= 0) {
                responder(resp, r, "error",
                        "ID de notificación inválido");
                return null;
            }

            return id;

        } catch (NumberFormatException | IllegalStateException e) {
            responder(resp, r, "error",
                    "ID de notificación inválido");
            return null;
        }
    }

    // Respuesta JSON uniforme
    private void responder(HttpServletResponse resp,
            RespuestaJsonNotificacion r, String estado, String mensaje)
            throws IOException {

        r.setStatus(estado);
        r.setMessage(mensaje);
        resp.getWriter().write(gson.toJson(r));
    }
}
