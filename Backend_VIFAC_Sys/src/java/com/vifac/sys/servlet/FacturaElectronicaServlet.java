/*
 * FacturaElectronicaServlet para mostrar la factura electrónica de la última venta registrada.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 30/11/2025
 */

package com.vifac.sys.servlet;

import com.vifac.sys.dao.ClientesDAO;
import com.vifac.sys.dao.DetalleVentaDAO;
import com.vifac.sys.dao.EmpresaDAO;
import com.vifac.sys.dao.UsuarioDAO;
import com.vifac.sys.dao.VentaDAO;
import com.vifac.sys.dao.CajaDAO;
import com.vifac.sys.dao.TransaccionDAO;
// import com.vifac.sys.dao.MedioPagoDAO;
import com.vifac.sys.modelo.DetalleVenta;
import com.vifac.sys.modelo.Empresa;
import com.vifac.sys.modelo.Usuario;
import com.vifac.sys.modelo.Clientes;
import com.vifac.sys.modelo.Venta;
import com.vifac.sys.modelo.Transaccion;
//import com.vifac.sys.modelo.MedioPago;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/FacturaElectronicaServlet")
public class FacturaElectronicaServlet extends HttpServlet {

    private final VentaDAO ventaDAO = new VentaDAO();
    private final DetalleVentaDAO detalleDAO = new DetalleVentaDAO();
    private final ClientesDAO clienteDAO = new ClientesDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final EmpresaDAO empresaDAO = new EmpresaDAO();
    private final CajaDAO cajaDAO = new CajaDAO();
    private final TransaccionDAO transaccionDAO = new TransaccionDAO();

    private final DateTimeFormatter fechaFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter horaFormat = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idVentaParam = request.getParameter("idVenta");
        Venta venta;

        if (idVentaParam != null && !idVentaParam.isEmpty()) {
            venta = ventaDAO.obtenerVentaPorId(Integer.parseInt(idVentaParam));
        } else {
            venta = ventaDAO.obtenerUltimaVenta();
        }

        if (venta == null) {
            response.getWriter().write("No hay ventas registradas");
            return;
        }

        int idVenta = venta.getIdVenta();

        // Obtener transacción
        Transaccion transaccion = transaccionDAO.buscarPorFactura(venta.getNroDocumentoFactura());
        if (transaccion != null) {
            request.setAttribute("transaccion", transaccion);
        }

        // Datos relacionados
        Integer numeroCaja = cajaDAO.obtenerNumeroCajaPorId(venta.getIdCaja());
        venta.setNumeroCaja(numeroCaja != null ? numeroCaja : 0);

        List<DetalleVenta> detalle = detalleDAO.listarDetallesPorVenta(idVenta);
        Clientes cliente = clienteDAO.obtenerClientePorId(venta.getIdCliente());
        Usuario vendedor = usuarioDAO.obtenerUsuarioPorId(venta.getIdUsuario());
        Empresa empresa = empresaDAO.obtenerEmpresa();

        int totalUnidades = detalle.stream().mapToInt(DetalleVenta::getCantidad).sum();
        int totalReferencias = detalle.size();

        // Fechas
        String fechaEmision = venta.getFechaEmision() != null ? venta.getFechaEmision().format(fechaFormat) : "";
        String horaEmision  = venta.getFechaEmision() != null ? venta.getFechaEmision().format(horaFormat) : "";
        String fechaVencimiento = venta.getFechaVencimiento() != null ? venta.getFechaVencimiento().format(fechaFormat) : "";
        String horaVencimiento  = venta.getFechaVencimiento() != null ? venta.getFechaVencimiento().format(horaFormat) : "";
        String fechaValidacion  = venta.getFechaValidacion() != null ? venta.getFechaValidacion().format(fechaFormat) : "";

        // QR
        venta.setQrCodeUrl("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + venta.getQrCodeUrl());

        // Pasar datos al JSP
        request.setAttribute("venta", venta);
        request.setAttribute("detalle", detalle);
        request.setAttribute("cliente", cliente);
        request.setAttribute("vendedor", vendedor);
        request.setAttribute("empresa", empresa); 
        request.setAttribute("totalUnidades", totalUnidades);
        request.setAttribute("totalReferencias", totalReferencias);
        request.setAttribute("fechaEmision", fechaEmision);
        request.setAttribute("horaEmision", horaEmision);
        request.setAttribute("fechaVencimiento", fechaVencimiento);
        request.setAttribute("horaVencimiento", horaVencimiento);
        request.setAttribute("fechaValidacion", fechaValidacion);

        request.getRequestDispatcher("FacturaElectronica.jsp").forward(request, response);
    }
}
