/*
 * Clase modelo Transaccion que representa la estructura de la tabla 'Transaccion' en la base de datos.
 * Esta clase contiene todos los atributos que reflejan las columnas de la tabla, 
 * Se utiliza para mapear los datos desde la base de datos hacia objetos Java y viceversa.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */

package com.vifac.sys.modelo;

public class Transaccion {
    private int idTransaccion;
    private double monto;
    private double recibido;
    private double cambio;
    private String fecha;
    private String descripcion;
    private int idUsuario;
    private String nro_documento_factura;
    private String metodoPago;
    private String referencia;

    // getters y setters

    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getRecibido() {
        return recibido;
    }

    public void setRecibido(double recibido) {
        this.recibido = recibido;
    }

    public double getCambio() {
        return cambio;
    }

    public void setCambio(double cambio) {
        this.cambio = cambio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNroDocumentoFactura() {
        return nro_documento_factura;
    }

    public void setNroDocumentoFactura(String nro_documento_factura) {
        this.nro_documento_factura = nro_documento_factura;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    
    public String getReferencia() {
    return referencia;
    }

    public void setReferencia(String referencia) {
    this.referencia = referencia;
    }  
}
