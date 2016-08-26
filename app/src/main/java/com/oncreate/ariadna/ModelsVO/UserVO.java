package com.oncreate.ariadna.ModelsVO;

/**
 * Created by azulandres92 on 2/15/16.
 */
public class UserVO {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String nombre;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(int id,String nombre) {
        this.id = id;
    this.nombre = nombre;
    }


    public UserVO(String nombre) {
        this.nombre = nombre;

    }

}
