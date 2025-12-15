/*
 * Clase modelo Notificacion utilizada para el manejo de notificaciones del sistema.
 * Permite representar y transportar la información relacionada con los eventos generados
 * por las acciones del usuario (ventas, creación de registros, cambios de estado, etc.).
 * Se emplea como entidad de intercambio entre la base de datos y la lógica del sistema.
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 06/12/2025
 */

package com.vifac.sys.modelo;

import java.sql.Timestamp;

public class Notificacion {

    private int idNotificacion;
    private int idUsuario;
    private String mensaje;
    private String tipo;
    private Integer referenciaId; // puede ser null
    private boolean leido;
    private Timestamp fechaCreacion;

    // Constructor vacío
    public Notificacion() {}

    // Constructor completo
    public Notificacion(int idNotificacion, int idUsuario, String mensaje, String tipo,
                         Integer referenciaId, boolean leido, Timestamp fechaCreacion) {
        this.idNotificacion = idNotificacion;
        this.idUsuario = idUsuario;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.referenciaId = referenciaId;
        this.leido = leido;
        this.fechaCreacion = fechaCreacion;
    }

    // Constructor para insertar
    public Notificacion(int idUsuario, String mensaje, String tipo,
                         Integer referenciaId, boolean leido, Timestamp fechaCreacion) {
        this.idUsuario = idUsuario;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.referenciaId = referenciaId;
        this.leido = leido;
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Integer referenciaId) {
        this.referenciaId = referenciaId;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
