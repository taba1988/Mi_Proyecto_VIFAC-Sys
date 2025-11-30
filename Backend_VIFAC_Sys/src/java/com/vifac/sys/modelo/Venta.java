/*
 * Clase modelo Venta que representa la estructura de la tabla 'Venta' en la base de datos.
 * Esta clase contiene todos los atributos que reflejan las columnas de la tabla,
 * y se utiliza para mapear los datos desde la base de datos hacia objetos Java y viceversa.
 * 
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */

package com.vifac.sys.modelo;

import java.time.LocalDateTime;
import java.util.List;

public class Venta {

    private int idVenta;
    private String nroDocumentoFactura;
    private LocalDateTime fechaEmision;
    private LocalDateTime fechaValidacion;
    private LocalDateTime fechaVencimiento;
    private String qrCodeUrl;
    private double subtotalVenta;
    private double descuentoVenta;
    private double totalVenta;
    private int idUsuario;
    private int idCliente;
    private int idEmisor;
    private int idCaja;
    private String metodoPago;
    private List<DetalleVenta> detalles;
    private int numeroCaja;

    public Venta() {}

    public Venta(int idVenta, String nroDocumentoFactura, LocalDateTime fechaEmision, 
                 LocalDateTime fechaValidacion, LocalDateTime fechaVencimiento, 
                 String qrCodeUrl, double subtotalVenta, double descuentoVenta, 
                 double totalVenta, int idUsuario, int idCliente, int idEmisor, 
                 String metodoPago, List<DetalleVenta> detalles) {

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

    public int getIdVenta() {
        return idVenta;
    }
    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getNroDocumentoFactura() {
        return nroDocumentoFactura;
    }
    public void setNroDocumentoFactura(String nroDocumentoFactura) {
        this.nroDocumentoFactura = nroDocumentoFactura;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }
    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDateTime getFechaValidacion() {
        return fechaValidacion;
    }
    public void setFechaValidacion(LocalDateTime fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public LocalDateTime getFechaVencimiento() {
        return fechaVencimiento;
    }
    public void setFechaVencimiento(LocalDateTime fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }
    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public double getSubtotalVenta() {
        return subtotalVenta;
    }
    public void setSubtotalVenta(double subtotalVenta) {
        this.subtotalVenta = subtotalVenta;
    }

    public double getDescuentoVenta() {
        return descuentoVenta;
    }
    public void setDescuentoVenta(double descuentoVenta) {
        this.descuentoVenta = descuentoVenta;
    }

    public double getTotalVenta() {
        return totalVenta;
    }
    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEmisor() {
        return idEmisor;
    }
    public void setIdEmisor(int idEmisor) {
        this.idEmisor = idEmisor;
    }
    
    public int getIdCaja() {
    return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }
    
    public int getNumeroCaja() {
        return numeroCaja;
    }

    public void setNumeroCaja(int numeroCaja) {
        this.numeroCaja = numeroCaja;
    }

    public String getMetodoPago() {
        return metodoPago;
    }
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
}
