/*
 * Clase modelo Usuario que representa la estructura de la tabla 'usuario' en la base de datos.
 * Incluye campos extendidos de otras tablas (Empresa, Dependencia) para el perfil de usuario.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 10/09/2025
 */

package com.vifac.sys.modelo;

import java.sql.Timestamp;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String documento;
    private String telefono;
    private String email;
    private String direccion; 
    private String nombreUsuario;
    private String contrasena;
    
    // Campo ORIGINAL de la BD. Lo mantenemos para inserción/actualización.
    private String cargo; 
    private int idRol;
    
    // Campo NUEVO para la relación con la tabla 'empresa'
    private int idEmpresa; 
    
    // Campo ORIGINAL mapeado a 'Situación Laboral'
    private String estado; 
    
    private int intentosFallidos;
    private String tokenRecuperacion;
    private Timestamp tokenExpira;
    
    // Campos EXTRA que vienen de la consulta JOIN y se usan para el perfil
    private String empresa;        
    private String dependencia;    
    private String situacionLaboral; 
    private String notaSistema;    
    
    // NUEVO campo para la foto de perfil
    private byte[] fotoPerfil;

    public Usuario() {}

    public Usuario(int idUsuario, String nombre, String documento, String telefono, String email, String direccion, String nombreUsuario,
                   String contrasena, String cargo, int idRol, int idEmpresa, String estado, int intentosFallidos,
                   String tokenRecuperacion, Timestamp tokenExpira, String empresa, String dependencia, 
                   String situacionLaboral, String notaSistema, byte[] fotoPerfil) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.documento = documento;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.cargo = cargo;
        this.idRol = idRol;
        this.idEmpresa = idEmpresa;
        this.estado = estado;
        this.intentosFallidos = intentosFallidos;
        this.tokenRecuperacion = tokenRecuperacion;
        this.tokenExpira = tokenExpira;
        
        this.empresa = empresa;
        this.dependencia = dependencia;
        this.situacionLaboral = situacionLaboral;
        this.notaSistema = notaSistema;
        this.fotoPerfil = fotoPerfil;
    }

    // Getters y Setters existentes
    public int getIdEmpresa() {
        return idEmpresa;
    }
    
    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
    
    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDependencia() {
        return dependencia;
    }

    public void setDependencia(String dependencia) {
        this.dependencia = dependencia;
    }

    public String getSituacionLaboral() {
        return situacionLaboral;
    }

    public void setSituacionLaboral(String situacionLaboral) {
        this.situacionLaboral = situacionLaboral;
    }
    
    public String getNotaSistema() {
        return notaSistema;
    }

    public void setNotaSistema(String notaSistema) {
        this.notaSistema = notaSistema;
    }

    // NUEVO getter y setter para fotoPerfil
    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    // Getters y setters antiguos (nombre, documento, etc.) siguen intactos
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
    public String getDireccion() {
        return direccion;
    }   
    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
    public String getTokenRecuperacion() {
        return tokenRecuperacion;
    }
    public void setTokenRecuperacion(String tokenRecuperacion) {
        this.tokenRecuperacion = tokenRecuperacion;
    }
    public Timestamp getTokenExpira() {
        return tokenExpira;
    } 
    public void setTokenExpira(Timestamp tokenExpira) {
        this.tokenExpira = tokenExpira;
    }
}