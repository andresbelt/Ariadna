package com.oncreate.ariadna.ModelsVO;

/**
 * Created by azulandres92 on 2/15/16.
 */
public class UnidadesVO {

    public UnidadesVO(String titulo, String expires_at, String image){
        this.titulo = titulo;
        this.expires_at = expires_at;
        this.image = image;


    }

    private String titulo;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGrupo_id() {
        return grupo_id;
    }

    public void setGrupo_id(String grupo_id) {
        this.grupo_id = grupo_id;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String grupo_id;
    private String expires_at;
    private String image;

}
