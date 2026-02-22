/*
 * MovimientosServlet para gestionar las operaciones relacionadas con los movimientos contables.
 * Actualmente el módulo se encuentra en desarrollo.
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */

package com.vifac.sys.servlet;

import com.google.gson.Gson;
import com.vifac.sys.modelo.RespuestaJsonContable;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/MovimientosServlet")
public class MovimientosServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        RespuestaJsonContable respuesta = new RespuestaJsonContable(
            "info",
            "Este módulo se encuentra en desarrollo. En el momento que esté disponible se notificará para su uso."
        );

        response.getWriter().write(gson.toJson(respuesta));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        RespuestaJsonContable respuesta = new RespuestaJsonContable(
            "info",
            "Este módulo se encuentra en desarrollo. En el momento que esté disponible se notificará para su uso."
        );

        response.getWriter().write(gson.toJson(respuesta));
    }
}