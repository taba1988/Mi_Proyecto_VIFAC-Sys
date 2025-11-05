package com.vifac.sys.modelo;

public class RespuestaJson {
    private String status;
    private String message;
    private Integer idVenta;      
    private double valorDescuento; 

    // Constructor vacío
    public RespuestaJson() {
    }

    // Constructor con status y mensaje
    public RespuestaJson(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters y Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public double getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(double valorDescuento) {
        this.valorDescuento = valorDescuento;
    }
}
