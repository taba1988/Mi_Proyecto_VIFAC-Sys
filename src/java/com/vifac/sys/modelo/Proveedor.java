/*
 * Clase modelo Proveedor que representa la estructura de la tabla 'Proveedor' en la base de datos.
 * Esta clase contiene todos los atributos que reflejan las columnas de la tabla, incluyendo:
 * idProveedor, nombreEmpresa, documento, asesor, telefono, email, diaVisita, estado.
 * Se utiliza para mapear los datos desde la base de datos hacia objetos Java y viceversa.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */

package com.vifac.sys.modelo;

import com.google.gson.annotations.SerializedName;

public class Proveedor {
    private int idProveedor;
    private String nombreEmpresa;

    @SerializedName("documento_NIT")
    private String documento_NIT;

    private String asesor;
    private String telefono;
    private String email;
    private String diaVisita;
    private String estado;

    public Proveedor() {}

    public Proveedor(int idProveedor, String nombreEmpresa, String documento_NIT, String asesor,
                     String telefono, String email, String diaVisita, String estado) {
        this.idProveedor = idProveedor;
        this.nombreEmpresa = nombreEmpresa;
        this.documento_NIT = documento_NIT;
        this.asesor = asesor;
        this.telefono = telefono;
        this.email = email;
        this.diaVisita = diaVisita;
        this.estado = estado;
    }

    
    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getDocumentoNIT() {
        return documento_NIT;
    }

    public void setDocumentoNIT(String documento_NIT) {
        this.documento_NIT = documento_NIT;
    }

    public String getAsesor() {
        return asesor;
    }

    public void setAsesor(String asesor) {
        this.asesor = asesor;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaVisita() {
        return diaVisita;
    }

    public void setDiaVisita(String diaVisita) {
        this.diaVisita = diaVisita;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setDocumento_NIT(String documento_NIT) {
    this.documento_NIT = documento_NIT;
}

public String getDocumento_NIT() {
    return this.documento_NIT;
}
}
