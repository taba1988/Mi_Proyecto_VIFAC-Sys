/*
 * Clase modelo DetalleVenta que representa la tabla 'detalleventa'
 * en la base de datos.
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 22/09/2025
 */

package com.vifac.sys.modelo;

public class DetalleVenta {

    private int idDetalleVenta;
    private int idVenta;
    private int idProducto;
    private int cantidad;
    private double precio_unitario;
    private double impuesto_porcentaje;
    private double descuento_porcentaje;
    private double descuento;
    private double total_con_descuento;

    public DetalleVenta() {
    }

    public DetalleVenta(int idDetalleVenta, int idVenta, int idProducto, int cantidad,
        double precio_unitario, double impuesto_porcentaje, double descuento_porcentaje,
        double descuento, double total_con_descuento) {
        this.idDetalleVenta = idDetalleVenta;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.impuesto_porcentaje = impuesto_porcentaje;
        this.descuento_porcentaje = descuento_porcentaje;
        this.descuento = descuento;
        this.total_con_descuento = total_con_descuento;
    }

    // Getters y Setters
    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(int idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public double getImpuesto_porcentaje() {
        return impuesto_porcentaje;
    }

    public void setImpuesto_porcentaje(double impuesto_porcentaje) {
        this.impuesto_porcentaje = impuesto_porcentaje;
    }

    public double getDescuento_porcentaje() {
        return descuento_porcentaje;
    }

    public void setDescuento_porcentaje(double descuento_porcentaje) {
        this.descuento_porcentaje = descuento_porcentaje;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getTotal_con_descuento() {
        return total_con_descuento;
    }

    public void setTotal_con_descuento(double total_con_descuento) {
        this.total_con_descuento = total_con_descuento;
    }
}
