package com.vifac.sys.modelo;

import java.sql.Timestamp;

/**
 * Clase que representa una imagen destacada del sistema.
 */
public class ImagenDestacada {

    private int id;                 // ID de la imagen
    private String nombreFijo;      // nombre fijo (banner1, banner1.1, etc.)
    private String nombreArchivo;   // nombre del archivo subido
    private Timestamp fechaSubida;  // fecha de la última subida

    public ImagenDestacada() {}

    public ImagenDestacada(int id, String nombreFijo, String nombreArchivo, Timestamp fechaSubida) {
        this.id = id;
        this.nombreFijo = nombreFijo;
        this.nombreArchivo = nombreArchivo;
        this.fechaSubida = fechaSubida;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreFijo() { return nombreFijo; }
    public void setNombreFijo(String nombreFijo) { this.nombreFijo = nombreFijo; }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    public Timestamp getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(Timestamp fechaSubida) { this.fechaSubida = fechaSubida; }

    @Override
    public String toString() {
        return "ImagenDestacada{" +
                "id=" + id +
                ", nombreFijo='" + nombreFijo + '\'' +
                ", nombreArchivo='" + nombreArchivo + '\'' +
                ", fechaSubida=" + fechaSubida +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImagenDestacada)) return false;
        ImagenDestacada that = (ImagenDestacada) o;
        return id == that.id &&
               nombreFijo.equals(that.nombreFijo) &&
               nombreArchivo.equals(that.nombreArchivo) &&
               fechaSubida.equals(that.fechaSubida);
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(id);
        result = 31 * result + nombreFijo.hashCode();
        result = 31 * result + nombreArchivo.hashCode();
        result = 31 * result + fechaSubida.hashCode();
        return result;
    }
}