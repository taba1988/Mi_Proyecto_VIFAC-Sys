/*
 * Clase modelo Empresa que representa la estructura de la tabla 'empresa' en la base de datos.
 * Esta clase contiene todos los atributos que reflejan las columnas de la tabla:
 * idEmpresa, razon_social, cc_nit, actividad_economica, responsabilidad_iva, direccion,
 * ciudad, telefono, email, resolucion_mercantil, fecha_registro_res, fecha_vencimiento_res.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 06/11/2025
 */

package com.vifac.sys.modelo;

import com.google.gson.annotations.SerializedName;

public class Empresa {
    private int idEmpresa;

    @SerializedName("razon_social")
    private String razon_social;

    @SerializedName("cc_nit")
    private String cc_nit;

    @SerializedName("actividad_economica")
    private String actividad_economica;

    @SerializedName("responsabilidad_iva")
    private String responsabilidad_iva;

    private String direccion;
    private String ciudad;
    private String telefono;
    private String email;

    @SerializedName("resolucion_mercantil")
    private String resolucion_mercantil;

    @SerializedName("fecha_registro_res")
    private String fecha_registro_res;

    @SerializedName("fecha_vencimiento_res")
    private String fecha_vencimiento_res;

    public Empresa() {}

    public Empresa(int idEmpresa, String razon_social, String cc_nit, String actividad_economica,
                   String responsabilidad_iva, String direccion, String ciudad, String telefono,
                   String email, String resolucion_mercantil, String fecha_registro_res,
                   String fecha_vencimiento_res) {
        this.idEmpresa = idEmpresa;
        this.razon_social = razon_social;
        this.cc_nit = cc_nit;
        this.actividad_economica = actividad_economica;
        this.responsabilidad_iva = responsabilidad_iva;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.telefono = telefono;
        this.email = email;
        this.resolucion_mercantil = resolucion_mercantil;
        this.fecha_registro_res = fecha_registro_res;
        this.fecha_vencimiento_res = fecha_vencimiento_res;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getCc_nit() {
        return cc_nit;
    }

    public void setCc_nit(String cc_nit) {
        this.cc_nit = cc_nit;
    }

    public String getActividad_economica() {
        return actividad_economica;
    }

    public void setActividad_economica(String actividad_economica) {
        this.actividad_economica = actividad_economica;
    }

    public String getResponsabilidad_iva() {
        return responsabilidad_iva;
    }

    public void setResponsabilidad_iva(String responsabilidad_iva) {
        this.responsabilidad_iva = responsabilidad_iva;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
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

    public String getResolucion_mercantil() {
        return resolucion_mercantil;
    }

    public void setResolucion_mercantil(String resolucion_mercantil) {
        this.resolucion_mercantil = resolucion_mercantil;
    }

    public String getFecha_registro_res() {
        return fecha_registro_res;
    }

    public void setFecha_registro_res(String fecha_registro_res) {
        this.fecha_registro_res = fecha_registro_res;
    }

    public String getFecha_vencimiento_res() {
        return fecha_vencimiento_res;
    }

    public void setFecha_vencimiento_res(String fecha_vencimiento_res) {
        this.fecha_vencimiento_res = fecha_vencimiento_res;
    }
}
