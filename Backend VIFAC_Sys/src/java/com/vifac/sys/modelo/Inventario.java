/*
 * Clase modelo Inventario que representa la estructura de la tabla 'inventario' en la base de datos.
 * Esta clase contiene todos los atributos que reflejan las columnas de la tabla.
 * Se utiliza para mapear los datos desde la base de datos hacia objetos Java y viceversa.
 * Autor: ORLANDUVALIE TABARES GUTIERREZ
 * Fecha: 16/09/2025
 */

package com.vifac.sys.modelo;

import com.google.gson.annotations.SerializedName;

public class Inventario {

    private int idProducto;
    private String sku;
    private String nombre;
    private String descripcion;

    @SerializedName("precio_compra")
    private double precio_compra;

    @SerializedName("precio_venta")
    private double precio_venta;

    private int stock;

    @SerializedName("unidad_medida")
    private String unidad_medida;

    private String estado;

    @SerializedName("idCategoria")
    private int idCategoria;

    private String categoria;

    public Inventario() {}

    public Inventario(int idProducto, String sku, String nombre, String descripcion, double precio_compra, double precio_venta, int stock, String unidad_medida, String estado, int idCategoria) {
        this.idProducto = idProducto;
        this.sku = sku;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio_compra = precio_compra;
        this.precio_venta = precio_venta;
        this.stock = stock;
        this.unidad_medida = unidad_medida;
        this.estado = estado;
        this.idCategoria = idCategoria;
    }

    /* Getters y Setters */

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio_compra() {
        return precio_compra;
    }

    public void setPrecio_compra(double precio_compra) {
        this.precio_compra = precio_compra;
    }

    public double getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(double precio_venta) {
        this.precio_venta = precio_venta;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}