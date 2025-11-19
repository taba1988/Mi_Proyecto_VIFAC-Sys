/*
 * RespuestaJsonCliente
 * Clase para manejar las respuestas JSON al agregar, editar o eliminar clientes.
 * Contiene el estado de la operación (success/error) y un mensaje descriptivo.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 15/11/2025
 */
package com.vifac.sys.modelo;

public class RespuestaJsonCliente {
    private String status;
    private String message;

    public RespuestaJsonCliente() {
    }

    public RespuestaJsonCliente(String status, String message) {
        this.status = status;
        this.message = message;
    }

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
}
