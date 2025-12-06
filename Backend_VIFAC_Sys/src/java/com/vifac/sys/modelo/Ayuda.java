/*
 * Clase modelo Ayuda que representa la estructura de la tabla 'ayuda' en la base de datos.
 * Esta clase contiene todos los atributos que reflejan las columnas de la tabla.
 * Se utiliza para mapear los datos desde la base de datos hacia objetos Java y viceversa.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 04/12/2025
 */
package com.vifac.sys.modelo;

public class Ayuda {
    private int id;
    private String titulo;
    private String urlVideo;
    private String urlImagen;

    // Constructor vacío
    public Ayuda() {}

    // Constructor con todos los campos
    public Ayuda(int id, String titulo, String urlVideo, String urlImagen) {
        this.id = id;
        this.titulo = titulo;
        this.urlVideo = urlVideo;
        this.urlImagen = urlImagen;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    @Override
    public String toString() {
        return "Ayuda{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", urlVideo='" + urlVideo + '\'' +
                ", urlImagen='" + urlImagen + '\'' +
                '}';
    }
}
