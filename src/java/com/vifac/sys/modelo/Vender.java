/*
 * Clase modelo Vender que representa la estructura de la tabla 'Vender' en la base de datos.
 * Esta clase contiene todos los atributos que reflejan las columnas de la tabla,
 * y se utiliza para mapear los datos desde la base de datos hacia objetos Java y viceversa.
 * 
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */

package com.vifac.sys.modelo;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Vender {
    
    private int idVenta;
    
    @SerializedName("nro_documento_factura")
    private String nro_documento_factura;
    
    private LocalDate fecha_emision;
    private LocalDate fecha_validacion;
    private LocalDate fecha_vencimiento;
    private String qr_code_url;
    private double total_venta;
    private Integer idUsuario;
    private Integer idCliente;
    private Integer idEmisor;
    private List<DetalleVenta> detalles = new ArrayList<>();

    // 🔥 Nuevos campos para compatibilidad con el JSON del frontend
    private String metodoPago;
    private double subtotalVenta;
    private double totalVenta;

    public Vender() {}

    // Constructor completo con todos los campos
    public Vender(int idVenta, String nro_documento_factura, LocalDate fecha_emision, 
                  LocalDate fecha_validacion, LocalDate fecha_vencimiento, String qr_code_url, 
                  float total_venta, Integer idUsuario, Integer idCliente, Integer idEmisor,
                  String metodoPago, double subtotalVenta, double totalVenta, List<DetalleVenta> detalles) {
        this.idVenta = idVenta;
        this.nro_documento_factura = nro_documento_factura;
        this.fecha_emision = fecha_emision;
        this.fecha_validacion = fecha_validacion;
        this.fecha_vencimiento = fecha_vencimiento;
        this.qr_code_url = qr_code_url;
        this.total_venta = total_venta;
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
        this.idEmisor = idEmisor;
        this.metodoPago = metodoPago;
        this.subtotalVenta = subtotalVenta;
        this.totalVenta = totalVenta;
        this.detalles = detalles;
    }
    
    // -------- Getters y Setters --------

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getNro_documento_factura() {
        return nro_documento_factura;
    }

    public void setNro_documento_factura(String nro_documento_factura) {
        this.nro_documento_factura = nro_documento_factura;
    }

    public LocalDate getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(LocalDate fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public LocalDate getFecha_validacion() {
        return fecha_validacion;
    }

    public void setFecha_validacion(LocalDate fecha_validacion) {
        this.fecha_validacion = fecha_validacion;
    }

    public LocalDate getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(LocalDate fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public String getQr_code_url() {
        return qr_code_url;
    }

    public void setQr_code_url(String qr_code_url) {
        this.qr_code_url = qr_code_url;
    }

    public double getTotal_venta() {
        return total_venta;
    }

    public void setTotal_venta(float total_venta) {
        this.total_venta = total_venta;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdEmisor() {
        return idEmisor;
    }

    public void setIdEmisor(Integer idEmisor) {
        this.idEmisor = idEmisor;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getSubtotalVenta() {
        return subtotalVenta;
    }

    public void setSubtotalVenta(double subtotalVenta) {
        this.subtotalVenta = subtotalVenta;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }
}
