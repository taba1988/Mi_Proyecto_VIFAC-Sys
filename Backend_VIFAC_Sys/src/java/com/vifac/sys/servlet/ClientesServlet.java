/*
 * ClientesServlet para gestionar las operaciones relacionadas con los clientes:
 * agregar, editar, eliminar, listar y buscar.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 *
 * Este servlet funciona como API REST para el módulo de clientes.
 * Responde solicitudes GET, POST, PUT y DELETE desde Postman o frontend.
 * Devuelve datos en formato JSON utilizando Gson.
 */

package com.vifac.sys.servlet;

import com.google.gson.Gson;
import com.vifac.sys.dao.ClientesDAO;
import com.vifac.sys.modelo.Clientes;
import com.vifac.sys.modelo.RespuestaJsonCliente;
import com.vifac.sys.dao.UsuarioDAO;
import com.vifac.sys.modelo.Usuario;
import com.vifac.sys.dao.NotificacionDAO;
import com.vifac.sys.modelo.Notificacion;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.sql.Timestamp;

@WebServlet("/ClientesServlet")
public class ClientesServlet extends HttpServlet {

    // --- Acceso al DAO y herramienta JSON --- //
    private final ClientesDAO clienteDAO = new ClientesDAO();
    private final NotificacionDAO notificacionDAO = new NotificacionDAO();
    private final Gson gson = new Gson();

    // --- Método GET: listar y buscar clientes --- //
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- Configuración CORS (permite pruebas desde Postman o frontend) --- //
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");

        try {
            // --- Acción listar clientes --- //
            if (accion == null || accion.equals("listar")) {
                List<Clientes> clientes = clienteDAO.listarClientes();
                response.getWriter().write(gson.toJson(clientes));

            // --- Acción buscar clientes por texto --- //
            } else if (accion.equals("buscar")) {
                String busqueda = request.getParameter("busqueda");
                List<Clientes> clientes = clienteDAO.buscarClientes(busqueda);
                response.getWriter().write(gson.toJson(clientes));
            }

        } catch (IOException e) {

            // --- Respuesta JSON de error (control de fallos en la API) --- //
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            RespuestaJsonCliente error = new RespuestaJsonCliente(
                    "error",
                    "Error interno del servidor: " + e.getMessage()
            );
            response.getWriter().write(gson.toJson(error));
        }
    }

    // --- Método POST --- //
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- Configuración CORS --- //
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String accion = request.getParameter("accion");
        int idUsuario = (Integer) request.getSession().getAttribute("idUsuario");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario u = usuarioDAO.obtenerUsuarioPorId(idUsuario);
        String nombreUsuario = u.getNombre();
        String usuarioTxt = "El Usuario #" + idUsuario + " - " + nombreUsuario + " ";
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // --- Respuesta por defecto cuando no hay acción válida --- //
        RespuestaJsonCliente respuesta = new RespuestaJsonCliente("error", "Acción no válida.");

        try {
            if (accion != null) {
                switch (accion) {

                    // --- Acción agregar cliente --- //
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

                        Clientes existeDocumento = clienteDAO.buscarPorDocumento(nuevoCliente.getDocumento_NIT());
                        Clientes existeEmail = clienteDAO.buscarPorEmail(nuevoCliente.getEmail());

                        if (existeDocumento != null) {
                               respuesta = new RespuestaJsonCliente("error", "Documento o cédula ya existe.");
                        } else 
                        
                            if (existeEmail != null) {
                               respuesta = new RespuestaJsonCliente("error", "Correo electrónico ya existe.");
                        } else {
                            int idClienteGenerado = clienteDAO.agregarCliente(nuevoCliente);

                            // --- Confirmación de registro --- //
                            if (idClienteGenerado > 0) {
                                respuesta = new RespuestaJsonCliente("success", "Cliente agregado con éxito.");
                                Notificacion n = new Notificacion();
                                n.setIdUsuario((Integer) request.getSession().getAttribute("idUsuario"));
                                n.setTipo("CLIENTES");
                                n.setMensaje(usuarioTxt 
                                    + "realizó el registro del cliente ID #" 
                                    + idClienteGenerado
                                    + " (" + nuevoCliente.getRazon_social() + ")");
                                n.setLeido(false);                               
                                n.setFechaCreacion(new Timestamp(System.currentTimeMillis() - (5 * 3600 * 1000)));
                                notificacionDAO.registrar(n);
                            } else {
                                respuesta = new RespuestaJsonCliente("error",
                                        "No se pudo agregar el cliente. Verifique la información y que esta sea la correcta.");
                            }
                        }
                        break;

                    // --- Acción editar cliente --- //
                    case "editar":
                        try {
                            Clientes clienteEditado = new Clientes();

                            clienteEditado.setIdClientes(Integer.parseInt(request.getParameter("idClientes")));
                            clienteEditado.setRazon_social(request.getParameter("razon_social"));
                            clienteEditado.setDocumento_NIT(request.getParameter("documento_NIT"));
                            clienteEditado.setTelefono(request.getParameter("telefono"));
                            clienteEditado.setDireccion(request.getParameter("direccion"));
                            clienteEditado.setEmail(request.getParameter("email"));
                            clienteEditado.setActividad_economica(request.getParameter("actividad_economica"));
                            clienteEditado.setResponsabilidad_iva(request.getParameter("responsabilidad_iva"));
                            clienteEditado.setEstado(request.getParameter("estado"));

                            clienteDAO.actualizarCliente(clienteEditado);

                            respuesta = new RespuestaJsonCliente("success", "Cliente actualizado con éxito.");
                            Notificacion n = new Notificacion();
                            n.setIdUsuario((Integer) request.getSession().getAttribute("idUsuario"));
                            n.setTipo("CLIENTES");
                            n.setMensaje(usuarioTxt 
                               + "realizó la actualización del cliente ID #" 
                               + clienteEditado.getIdClientes() 
                               + " (" + clienteEditado.getRazon_social() + ")");
                            n.setLeido(false);
                            n.setFechaCreacion(new Timestamp(System.currentTimeMillis() - (5 * 3600 * 1000)));
                            notificacionDAO.registrar(n);

                        } catch (NumberFormatException e) {
                            respuesta = new RespuestaJsonCliente("error", "ID de cliente inválido.");
                        } catch (Exception e) {
                            respuesta = new RespuestaJsonCliente("error", "Ocurrió un error al actualizar el cliente.");
                        }
                        break;

                    // --- Acción eliminar cliente (POST) --- //
                    case "eliminar":
                        int idClienteEliminar = Integer.parseInt(request.getParameter("id"));
                        Clientes clienteEliminado = clienteDAO.obtenerClientePorId(idClienteEliminar);
                        clienteDAO.eliminarCliente(idClienteEliminar);
                        respuesta = new RespuestaJsonCliente("success", "Cliente eliminado con éxito.");
                        Notificacion n = new Notificacion();
                        n.setIdUsuario((Integer) request.getSession().getAttribute("idUsuario"));
                        n.setTipo("CLIENTES");
                        n.setMensaje(usuarioTxt
                          + "eliminó el cliente ID #"
                          + idClienteEliminar
                          + (clienteEliminado != null
                             ? " (" + clienteEliminado.getRazon_social() + ")"
                             : ""));                    
                        n.setLeido(false);
                        n.setFechaCreacion(new Timestamp(System.currentTimeMillis() - (5 * 3600 * 1000)));
                        notificacionDAO.registrar(n);
                        break;
                }
            }
        } catch (NumberFormatException e) {

            // --- Error al convertir campos numéricos --- //
            respuesta = new RespuestaJsonCliente("error", "Error en el procesamiento: " + e.getMessage());
        }

        response.getWriter().write(gson.toJson(respuesta));
    }

    // --- Método PUT: actualizar cliente mediante JSON --- //
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // --- Configuración CORS --- //
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setContentType("application/json;charset=UTF-8");

        // --- Lectura del JSON enviado desde Postman o JS --- //
        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String linea;
        while ((linea = br.readLine()) != null) sb.append(linea);

        // --- Conversión del JSON al modelo Cliente --- //
        Clientes cliente = gson.fromJson(sb.toString(), Clientes.class);

        // --- Ajustes de mapeo para campos que vienen con nombres diferentes --- //
        if (cliente.getDocumento_NIT() == null && request.getParameter("documentoNit") != null) {
            cliente.setDocumento_NIT(request.getParameter("documentoNit"));
        }
        if (cliente.getActividad_economica() == null && request.getParameter("actividadEconomica") != null) {
            cliente.setActividad_economica(request.getParameter("actividadEconomica"));
        }
        if (cliente.getResponsabilidad_iva() == null && request.getParameter("responsableIva") != null) {
            cliente.setResponsabilidad_iva(request.getParameter("responsableIva"));
        }

        boolean ok = clienteDAO.actualizarCliente(cliente);

        // --- Respuesta JSON del PUT --- //
        if (ok) {
            response.getWriter().write("{\"status\":\"success\",\"message\":\"Cliente actualizado (PUT)\"}");
        } else {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"No se pudo actualizar\"}");
        }
    }

    // --- Método DELETE: eliminar cliente desde URL mediante parámetro ID --- //
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // --- Configuración CORS --- //
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setContentType("application/json;charset=UTF-8");

        String id = request.getParameter("id");

        // --- Validación: verificar que el ID sí se recibió --- //
        if (id == null || id.trim().isEmpty()) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"ID no recibido\"}");
            return;
        }

        boolean ok = clienteDAO.eliminarCliente(Integer.parseInt(id));

        // --- Respuesta JSON del DELETE --- //
        if (ok) {
            response.getWriter().write("{\"status\":\"success\",\"message\":\"Cliente eliminado (DELETE)\"}");
        } else {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"No se pudo eliminar\"}");
        }
    }
}