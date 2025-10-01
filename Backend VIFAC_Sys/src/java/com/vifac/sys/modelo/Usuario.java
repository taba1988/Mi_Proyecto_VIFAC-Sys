/*
 * Clase modelo Usuario que representa la estructura de la tabla 'usuario' en la base de datos.
 * Esta clase contiene todos los atributos que reflejan las columnas de la tabla, incluyendo:
 * idUsuario, nombre, documento, telefono, email, nombreUsuario, cargo, idRol, contrasena, estado, intentosFallidos.
 * Se utiliza para mapear los datos desde la base de datos hacia objetos Java y viceversa.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */

package com.vifac.sys.modelo;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String documento;
    private String telefono;
    private String email;
    private String nombreUsuario;
    private String contrasena;
    private String cargo;
    private int idRol;
    private String estado;
    private int intentosFallidos;

    public Usuario() {}

    public Usuario(int idUsuario, String nombre, String documento, String telefono, String email, String nombreUsuario,
                   String contrasena, String cargo, int idRol, String estado, int intentosFallidos) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.documento = documento;
        this.telefono = telefono;
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.cargo = cargo;
        this.idRol = idRol;
        this.estado = estado;
        this.intentosFallidos = intentosFallidos;
    }

    // Getters y setters
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDocumento() {
        return documento;
    }
    public void setDocumento(String documento) {
        this.documento = documento;
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
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public String getCargo() {
        return cargo;
    }
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    public int getIdRol() {
        return idRol;
    }
    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public int getIntentosFallidos() {
        return intentosFallidos;
    }
    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }
}
