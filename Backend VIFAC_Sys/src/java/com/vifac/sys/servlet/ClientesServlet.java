/*
 * ClientesServlet para gestionar las operaciones relacionadas con los clientes:
 * agregar, editar, eliminar, listar y buscar.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */

package com.vifac.sys.servlet;

import com.google.gson.Gson;
import com.vifac.sys.dao.ClientesDAO;
import com.vifac.sys.modelo.Clientes;
import com.vifac.sys.modelo.RespuestaJson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/ClientesServlet")
public class ClientesServlet extends HttpServlet {

    private final ClientesDAO clienteDAO = new ClientesDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ Cabeceras CORS para permitir conexión desde React
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");
        try {
            if (accion == null || accion.equals("listar")) {
                List<Clientes> clientes = clienteDAO.listarClientes();
                response.getWriter().write(gson.toJson(clientes));
            } else if (accion.equals("buscar")) {
                String busqueda = request.getParameter("busqueda");
                List<Clientes> clientes = clienteDAO.buscarClientes(busqueda);
                response.getWriter().write(gson.toJson(clientes));
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            RespuestaJson error = new RespuestaJson("error", "Error interno del servidor: " + e.getMessage());
            response.getWriter().write(gson.toJson(error));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ Cabeceras CORS también aquí
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String accion = request.getParameter("accion");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        RespuestaJson respuesta = new RespuestaJson("error", "Acción no válida.");

        try {
            if (accion != null) {
                switch (accion) {
                    case "agregar":
                        Clientes nuevoCliente = new Clientes();
                        nuevoCliente.setRazon_social(request.getParameter("razon_social"));
                        nuevoCliente.setDocumento_NIT(request.getParameter("documento_NIT"));
                        nuevoCliente.setTelefono(request.getParameter("telefono"));
                        nuevoCliente.setDireccion(request.getParameter("direccion"));
                        nuevoCliente.setEmail(request.getParameter("email"));
                        nuevoCliente.setActividad_economica(request.getParameter("actividad_economica"));
                        nuevoCliente.setResponsabilidad_iva(request.getParameter("responsabilidad_iva"));
                        nuevoCliente.setEstado(request.getParameter("estado"));

                        Clientes existente = clienteDAO.buscarPorDocumento(nuevoCliente.getDocumento_NIT());
                        if (existente != null) {
                            respuesta = new RespuestaJson("error", "Documento o cédula ya existe.");
                        } else {
                            boolean agregado = clienteDAO.agregarCliente(nuevoCliente);
                            if (agregado) {
                                respuesta = new RespuestaJson("success", "Cliente agregado con éxito.");
                            } else {
                                respuesta = new RespuestaJson("error", "No se pudo agregar el cliente. Verifique la información.");
                            }
                        }
                        break;

                    case "editar":
                        Clientes clienteEditado = new Clientes();
                        clienteEditado.setIdClientes(Integer.parseInt(request.getParameter("idCliente")));
                        clienteEditado.setRazon_social(request.getParameter("razon_social"));
                        clienteEditado.setDocumento_NIT(request.getParameter("documento_NIT"));
                        clienteEditado.setTelefono(request.getParameter("telefono"));
                        clienteEditado.setDireccion(request.getParameter("direccion"));
                        clienteEditado.setEmail(request.getParameter("email"));
                        clienteEditado.setActividad_economica(request.getParameter("actividad_economica"));
                        clienteEditado.setResponsabilidad_iva(request.getParameter("responsabilidad_iva"));
                        clienteEditado.setEstado(request.getParameter("estado"));

                        clienteDAO.actualizarCliente(clienteEditado);
                        respuesta = new RespuestaJson("success", "Cliente actualizado con éxito.");
                        break;

                    case "eliminar":
                        int idClienteEliminar = Integer.parseInt(request.getParameter("id"));
                        clienteDAO.eliminarCliente(idClienteEliminar);
                        respuesta = new RespuestaJson("success", "Cliente eliminado con éxito.");
                        break;
                }
            }
        } catch (NumberFormatException e) {
            respuesta = new RespuestaJson("error", "Error en el procesamiento: " + e.getMessage());
        }

        response.getWriter().write(gson.toJson(respuesta));
    }
}
