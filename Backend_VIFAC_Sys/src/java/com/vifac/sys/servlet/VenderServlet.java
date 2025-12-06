
/*
 * VenderServlet para gestionar operaciones de ventas y caja.
 * Incluye:
 * - Apertura y cierre de caja
 * - Registro de ventas
 * - Listado de ventas y clientes/inventario
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */

package com.vifac.sys.servlet;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.vifac.sys.dao.*;
import com.vifac.sys.dao.TransaccionDAO;
import com.vifac.sys.modelo.*;
import com.vifac.sys.modelo.Transaccion;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "VenderServlet", urlPatterns = {"/VenderServlet"})
public class VenderServlet extends HttpServlet {

    //private final VenderDAO venderDAO = new VenderDAO();
    private final CajaDAO cajaDAO = new CajaDAO();
    private final InventarioDAO inventarioDAO = new InventarioDAO();
    private final ClientesDAO clientesDAO = new ClientesDAO();
    private final DetalleVentaDAO detalleVentaDAO = new DetalleVentaDAO();
    private final VentaDAO ventaDAO = new VentaDAO();
    private final TransaccionDAO transaccionDAO = new TransaccionDAO();
    private final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, 
            (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                new JsonPrimitive(src.toString()))
        .create();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if (accion == null) accion = "listar";

        switch (accion) {
            case "listar":
                List<Clientes> clientes = clientesDAO.listarClientes();
                req.setAttribute("clientes", clientes);
                List<Inventario> inventario = inventarioDAO.listarInventario();
                req.setAttribute("inventario", inventario);
                req.getRequestDispatcher("Vender.jsp").forward(req, resp);
                break;

            case "listarInventarioJson":
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(gson.toJson(inventarioDAO.listarInventario()));
                break;

            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida: " + accion);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        RespuestaJson r = new RespuestaJson();

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
            String accion = datos.has("accion") && !datos.get("accion").isJsonNull() ? datos.get("accion").getAsString() : null;
            System.out.println("JSON recibido: " + jsonBody);
            System.out.println("Accion: " + accion);

            if (accion == null) {
                r.setStatus("false");
                r.setMessage("Parámetro 'accion' no proporcionado");
                resp.getWriter().write(gson.toJson(r));
                return;
            }

            switch (accion) {

                case "abrirCaja":
                    manejarAbrirCaja(req, resp, datos, r);
                    break;

                case "obtenerCajaActiva":
                    manejarObtenerCajaActiva(req, resp, r);
                    break;

                case "cerrarCaja":
                    manejarCerrarCaja(req, resp, datos, r);
                    break;

                case "vender":
                    manejarVenta(req, resp, datos, r);
                    break;

                default:
                    r.setStatus("false");
                    r.setMessage("Acción '" + accion + "' no válida.");
                    resp.getWriter().write(gson.toJson(r));
                    break;
            }

        } catch (NumberFormatException nfe) {
            r.setStatus("false");
            r.setMessage("Error de formato numérico: " + nfe.getMessage());
            resp.getWriter().write(gson.toJson(r));

        } catch (JsonIOException | JsonSyntaxException | IOException e) {
            r.setStatus("error");
            r.setMessage("Error en el servidor: " + e.getMessage());
            resp.getWriter().write(gson.toJson(r));
        }
    }

    private void manejarAbrirCaja(HttpServletRequest req, HttpServletResponse resp, JsonObject datos, RespuestaJson r) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            r.setStatus("error");
            r.setMessage("Sesión inválida. Por favor inicia sesión.");
            resp.getWriter().write(gson.toJson(r));
            return;
        }
        int idUsuario = (Integer) session.getAttribute("idUsuario");
        if (cajaDAO.hayCajaAbierta(idUsuario)) {
            r.setStatus("error");
            r.setMessage("Ya existe una caja abierta para este usuario.");
            resp.getWriter().write(gson.toJson(r));
            return;
        }
        
       // 🟩 Nuevo: recibir número de caja física
        int numeroCaja = datos.has("numero_caja") && !datos.get("numero_caja").isJsonNull()
                ? datos.get("numero_caja").getAsInt()
                : 0;
    
        if (numeroCaja <= 0) {
            r.setStatus("error");
            r.setMessage("Debe seleccionar un número de caja válido.");
            resp.getWriter().write(gson.toJson(r));
            return;
        }
    
        double montoInicial = datos.get("montoInicial").getAsDouble();
        
        Caja nuevaCaja = new Caja();
        nuevaCaja.setNumeroCaja(numeroCaja);
        nuevaCaja.setFechaApertura(LocalDateTime.now().minusHours(5));
        nuevaCaja.setMontoInicial(montoInicial);
        nuevaCaja.setIdUsuario(idUsuario);
        nuevaCaja.setObservaciones("Apertura de caja exitosa");
        
        boolean abierta = cajaDAO.abrirCaja(nuevaCaja);
        r.setStatus(abierta ? "ok" : "error");
        r.setMessage(abierta ? "Caja abierta exitosamente" : "Error al abrir la caja");
        resp.getWriter().write(gson.toJson(r));
    }

    private void manejarObtenerCajaActiva(HttpServletRequest req, HttpServletResponse resp, RespuestaJson r) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            r.setStatus("error");
            r.setMessage("Sesión inválida. Por favor inicia sesión.");
            resp.getWriter().write(gson.toJson(r));
            return;
        }
        int idUsuario = (Integer) session.getAttribute("idUsuario");
        Caja cajaActiva = cajaDAO.obtenerCajaActivaPorUsuario(idUsuario);
        if (cajaActiva != null) resp.getWriter().write(gson.toJson(cajaActiva));
        else {
            r.setStatus("error");
            r.setMessage("No se encontró ninguna caja activa para este usuario.");
            resp.getWriter().write(gson.toJson(r));
        }
    }

    private void manejarCerrarCaja(HttpServletRequest req, HttpServletResponse resp, JsonObject datos, RespuestaJson r) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            r.setStatus("error");
            r.setMessage("Sesión inválida para cerrar la caja. Por favor inicia sesión.");
            resp.getWriter().write(gson.toJson(r));
            return;
        }
        int idUsuario = (Integer) session.getAttribute("idUsuario");
        Caja cajaActiva = cajaDAO.obtenerCajaActivaPorUsuario(idUsuario);
        if (cajaActiva == null) {
            r.setStatus("error");
            r.setMessage("No se encontró ninguna caja activa para este usuario.");
            resp.getWriter().write(gson.toJson(r));
            return;
        }
        double montoFinal = datos.get("montoFinal").getAsDouble();
        String observaciones = datos.has("observaciones") && !datos.get("observaciones").isJsonNull() ? datos.get("observaciones").getAsString() : "";
        cajaActiva.setMontoFinal(montoFinal);
        cajaActiva.setObservaciones(observaciones);
        cajaActiva.setFechaCierre(LocalDateTime.now().minusHours(5));
        boolean cerrada = cajaDAO.cerrarCaja(cajaActiva);
        r.setStatus(cerrada ? "ok" : "error");
        r.setMessage(cerrada ? "Caja cerrada exitosamente" : "Error al cerrar la caja");
        resp.getWriter().write(gson.toJson(r));
    }

    private void manejarVenta(HttpServletRequest req, HttpServletResponse resp, JsonObject datos, RespuestaJson r) throws IOException {
    try {
        if (!datos.has("idCliente") || datos.get("idCliente").isJsonNull()) {
            r.setStatus("false");
            r.setMessage("Falta el ID del cliente");
            resp.getWriter().write(gson.toJson(r));
            return;
        }

        int clienteId = datos.get("idCliente").getAsInt();
        String metodoPago = datos.has("metodoPago") ? datos.get("metodoPago").getAsString() : "Efectivo";

        // Todos los valores numéricos en double
        double subtotalVenta = datos.has("subtotal_venta") ? datos.get("subtotal_venta").getAsDouble() : 0.0;
        double totalVenta = datos.has("total_venta") ? datos.get("total_venta").getAsDouble() : 0.0;
        double valorDescuento = datos.has("descuento_venta") ? datos.get("descuento_venta").getAsDouble() : subtotalVenta - totalVenta;

        if (!datos.has("productosData") || datos.get("productosData").isJsonNull()) {
            r.setStatus("false");
            r.setMessage("Productos no proporcionados");
            resp.getWriter().write(gson.toJson(r));
            return;
        }

        JsonArray productosArray = datos.get("productosData").getAsJsonArray();
        if (productosArray.size() == 0) {
            r.setStatus("false");
            r.setMessage("Productos no proporcionados");
            resp.getWriter().write(gson.toJson(r));
            return;
        }

        List<DetalleVenta> detalles = new ArrayList<>();
        for (JsonElement elem : productosArray) {
            JsonObject obj = elem.getAsJsonObject();
            DetalleVenta dv = new DetalleVenta();
            dv.setIdProducto(obj.get("idProducto").getAsInt());
            dv.setCantidad(obj.get("cantidad").getAsInt());
            dv.setPrecio_unitario(obj.get("precio_unitario").getAsDouble());
            dv.setDescuento(obj.has("descuento") ? obj.get("descuento").getAsDouble() : 0.0);
            dv.setDescuento_porcentaje(obj.has("descuento_porcentaje") ? obj.get("descuento_porcentaje").getAsDouble() : 0.0);
            dv.setTotal_con_descuento(obj.has("total_con_descuento") ? obj.get("total_con_descuento").getAsDouble() : dv.getPrecio_unitario() * dv.getCantidad() - dv.getDescuento());
            dv.setImpuesto_porcentaje(obj.has("impuesto_porcentaje") ? obj.get("impuesto_porcentaje").getAsDouble() : 0.0);
            detalles.add(dv);
        }

        // Validación de stock
        for (DetalleVenta d : detalles) {
            if (!inventarioDAO.validarStock(d.getIdProducto(), (int)d.getCantidad())) {
                r.setStatus("false");
                r.setMessage("Stock insuficiente para el producto con ID: " + d.getIdProducto());
                resp.getWriter().write(gson.toJson(r));
                return;
            }
        }

        // Crear objeto venta con valores numéricos correctos
        Venta ventaForm = new Venta();
        ventaForm.setIdCliente(clienteId);
        //ventaForm.setMetodoPago(metodoPago);//
        ventaForm.setSubtotalVenta(subtotalVenta);
        ventaForm.setTotalVenta(totalVenta);
        ventaForm.setDescuentoVenta(valorDescuento);

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            r.setStatus("error");
            r.setMessage("Sesión inválida. Por favor inicia sesión.");
            resp.getWriter().write(gson.toJson(r));
            return;
        }
        ventaForm.setIdUsuario((Integer) session.getAttribute("idUsuario"));
        ventaForm.setDetalles(detalles);
        ventaForm.setFechaEmision(LocalDateTime.now());
        ventaForm.setNroDocumentoFactura(ventaDAO.generarNroFactura());
        ventaForm.setFechaValidacion(LocalDateTime.now());
        ventaForm.setFechaVencimiento(LocalDateTime.now().plusDays(30));
        ventaForm.setIdEmisor(1);

        Caja cajaActiva = cajaDAO.obtenerCajaActivaPorUsuario((Integer) session.getAttribute("idUsuario"));
        if (cajaActiva != null) {
            ventaForm.setIdCaja(cajaActiva.getIdCaja());
        }

        int idVentaGenerada = ventaDAO.registrarVenta(ventaForm);
        
        // Determina si es POS o Electrónica según el checkbox
        boolean esPOS = "on".equals(req.getParameter("toggleTicket"));
        String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
        String urlFactura = esPOS
            ? baseUrl + "/FacturaPOSServlet?idVenta=" + idVentaGenerada
            : baseUrl + "/FacturaElectronicaServlet?idVenta=" + idVentaGenerada;

        ventaForm.setQrCodeUrl(urlFactura);
        ventaDAO.actualizarQrCode(idVentaGenerada, urlFactura);

        double recibido = datos.has("efectivoRecibido") && !datos.get("efectivoRecibido").isJsonNull()
        ? datos.get("efectivoRecibido").getAsDouble()
        : 0.0;

        double cambio = recibido - totalVenta;
        int idUsuarioDeSesion = (Integer) session.getAttribute("idUsuario");
        
        Transaccion transaccion = new Transaccion();
        transaccion.setIdUsuario(idUsuarioDeSesion);
        transaccion.setNroDocumentoFactura(ventaForm.getNroDocumentoFactura());
        transaccion.setMonto(totalVenta);
        transaccion.setRecibido(recibido);
        transaccion.setCambio(cambio);
        transaccion.setMetodoPago(metodoPago);
        transaccion.setDescripcion("Pago de la venta #" + ventaForm.getNroDocumentoFactura());
        // Asignar referencia según método de pago
        if (metodoPago.equalsIgnoreCase("Efectivo")) {
            transaccion.setReferencia("N/A");
        } else {
            String referencia = "VTA-" + ventaForm.getNroDocumentoFactura() + "-" + System.currentTimeMillis();
            transaccion.setReferencia(referencia);
        }
        
        // prints para debug
        System.out.println("---- TRANSACCIÓN ----");
        System.out.println("Factura: " + transaccion.getNroDocumentoFactura());
        System.out.println("Monto: " + transaccion.getMonto());
        System.out.println("Recibido: " + transaccion.getRecibido());
        System.out.println("Cambio: " + transaccion.getCambio());
        System.out.println("Método de pago: " + transaccion.getMetodoPago());
        System.out.println("Referencia generada: " + transaccion.getReferencia());
        System.out.println("--------------------");

        transaccionDAO.agregarTransaccion(transaccion);

        req.setAttribute("recibido", recibido);
        req.setAttribute("cambio", cambio);
        req.setAttribute("venta", ventaForm);

        boolean exitoDetalles = true;
        for (DetalleVenta d : detalles) {
            d.setIdVenta(idVentaGenerada);
            if (!detalleVentaDAO.agregar(d)) {
                exitoDetalles = false;
                break;
            }
        }

        if (exitoDetalles) {
            r.setStatus("true");
            r.setMessage("Venta registrada correctamente.");
            r.setIdVenta(idVentaGenerada);
            r.setValorDescuento(valorDescuento);
            r.setRecibido(recibido); 
            r.setCambio(cambio); 
        } else {
            r.setStatus("false");
            r.setMessage("Error al registrar los detalles de la venta.");
        }
        resp.getWriter().write(gson.toJson(r));

    } catch (IOException e) {
        r.setStatus("error");
        r.setMessage("Error inesperado en el servidor durante la venta");
        resp.getWriter().write(gson.toJson(r));
    }
  }
}