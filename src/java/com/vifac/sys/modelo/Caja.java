/*
 * Clase modelo caja que representa la estructura de la tabla 'caja' en la base de datos.
 * Esta clase contiene todos los atributos que reflejan las columnas de la tabla, 
 * Se utiliza para mapear los datos desde la base de datos hacia objetos Java y viceversa.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */package com.vifac.sys.modelo;
 
 

import java.time.LocalDateTime;

public class Caja {
    private int idCaja;
    private LocalDateTime fechaApertura;
    private double montoInicial;
    private LocalDateTime fechaCierre; // Puede ser null si la caja está abierta
    private Double montoFinal;          // Puede ser null si la caja está abierta
    private String observaciones;
    private int idUsuario;

    public Caja() {
    }

    public Caja(int idCaja, LocalDateTime fechaApertura, double montoInicial, LocalDateTime fechaCierre,
                Double montoFinal, String observaciones, int idUsuario) {
        this.idCaja = idCaja;
        this.fechaApertura = fechaApertura;
        this.montoInicial = montoInicial;
        this.fechaCierre = fechaCierre;
        this.montoFinal = montoFinal;
        this.observaciones = observaciones;
        this.idUsuario = idUsuario;
    }

    // Getters y setters
    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDateTime fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public double getMontoInicial() {
        return montoInicial;
    }

    public void setMontoInicial(double montoInicial) {
        this.montoInicial = montoInicial;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Double getMontoFinal() {
        return montoFinal;
    }

    public void setMontoFinal(Double montoFinal) {
        this.montoFinal = montoFinal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
