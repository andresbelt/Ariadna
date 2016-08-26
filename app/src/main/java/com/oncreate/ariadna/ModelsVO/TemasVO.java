package com.oncreate.ariadna.ModelsVO;

/**
 * Created by azulandres92 on 2/15/16.
 */
public class TemasVO {

    public TemasVO(String unidad_id, String titulo, String cantidad,String expires_at){
        this.unidad_id = titulo;
        this.titulo = expires_at;
        this.cantidad = cantidad;
        this.expires_at = expires_at;

    }



    private String unidad_id;

    public String getUnidad_id() {
        return unidad_id;
    }

    public void setUnidad_id(String unidad_id) {
        this.unidad_id = unidad_id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }

    private String titulo;
    private String cantidad;
    private String expires_at;

}
