/*
 * Clase modelo Clientes que representa la estructura de la tabla 'clientes' en la base de datos.
 * Esta clase contiene todos los atributos que reflejan las columnas de la tabla, 
 * Se utiliza para mapear los datos desde la base de datos hacia objetos Java y viceversa.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */

package com.vifac.sys.modelo;

import com.google.gson.annotations.SerializedName;

public class Clientes {
    
    private int idClientes;
    private String razon_social;
    
    @SerializedName("documento_NIT")
    private String documento_NIT;
    
    private String telefono;
    private String direccion;
    private String email;
    private String actividad_economica;
    private String responsabilidad_iva;
    private String estado;

    public Clientes() {}

    public Clientes(int idClientes, String razon_social, String documento_NIT, String telefono, String direccion, String email, String actividad_economica, String responsabilidad_iva, String estado) {
        this.idClientes = idClientes;
        this.razon_social = razon_social;
        this.documento_NIT = documento_NIT;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
        this.actividad_economica = actividad_economica;
        this.responsabilidad_iva = responsabilidad_iva;
        this.estado = estado;
    }

    /*getter and setter*/
    
    public int getIdClientes() {
        return idClientes;
    }

    public void setIdClientes(int idClientes) {
        this.idClientes = idClientes;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getDocumento_NIT() {
        return documento_NIT;
    }

    public void setDocumento_NIT(String documento_NIT) {
        this.documento_NIT = documento_NIT;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
