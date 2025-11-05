package com.vifac.sys.modelo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Venta {

    private int idVenta;
    private String nroDocumentoFactura;
    private Date fechaEmision;
    private Date fechaValidacion;
    private Date fechaVencimiento;
    private String qrCodeUrl;
    private double subtotalVenta;
    private double descuentoVenta;
    private double totalVenta;
    private int idUsuario;
    private int idCliente;
    private int idEmisor;
    private String metodoPago;
    private List<DetalleVenta> detalles;

    // Constructor con todos los atributos
    public Venta(int idVenta, String nroDocumentoFactura, Date fechaEmision, Date fechaValidacion,
                 Date fechaVencimiento, String qrCodeUrl, double subtotalVenta, double descuentoVenta,
                 double totalVenta, int idUsuario, int idCliente, int idEmisor, String metodoPago,
                 List<DetalleVenta> detalles) {
        this.idVenta = idVenta;
        this.nroDocumentoFactura = nroDocumentoFactura;
        this.fechaEmision = fechaEmision;
        this.fechaValidacion = fechaValidacion;
        this.fechaVencimiento = fechaVencimiento;
        this.qrCodeUrl = qrCodeUrl;
        this.subtotalVenta = subtotalVenta;
        this.descuentoVenta = descuentoVenta;
        this.totalVenta = totalVenta;
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
        this.idEmisor = idEmisor;
        this.metodoPago = metodoPago;
        this.detalles = detalles;
    }

    // Constructor vacío
    public Venta() {
    }

    // Getters y Setters
    public int getIdVenta() {
        return this.idVenta;
    }
    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getNroDocumentoFactura() {
        return this.nroDocumentoFactura;
    }
    public void setNroDocumentoFactura(String nroDocumentoFactura) {
        this.nroDocumentoFactura = nroDocumentoFactura;
    }

    public Date getFechaEmision() {
        return this.fechaEmision;
    }
    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaValidacion() {
        return this.fechaValidacion;
    }
    public void setFechaValidacion(Date fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public Date getFechaVencimiento() {
        return this.fechaVencimiento;
    }
    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getQrCodeUrl() {
        return this.qrCodeUrl;
    }
    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public double getSubtotalVenta() {
        return this.subtotalVenta;
    }
    public void setSubtotalVenta(double subtotalVenta) {
        this.subtotalVenta = subtotalVenta;
    }

    public double getDescuentoVenta() {
        return this.descuentoVenta;
    }
    public void setDescuentoVenta(double descuentoVenta) {
        this.descuentoVenta = descuentoVenta;
    }

    public double getTotalVenta() {
        return this.totalVenta;
    }
    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }

    public int getIdUsuario() {
        return this.idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdCliente() {
        return this.idCliente;
    }
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEmisor() {
        return this.idEmisor;
    }
    public void setIdEmisor(int idEmisor) {
        this.idEmisor = idEmisor;
    }

    public String getMetodoPago() {
        return this.metodoPago;
    }
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<DetalleVenta> getDetalles() {
        return this.detalles;
    }
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    public Timestamp getFecha() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public double getTotal() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
