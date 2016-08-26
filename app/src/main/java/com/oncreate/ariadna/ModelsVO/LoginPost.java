package com.oncreate.ariadna.ModelsVO;

import java.util.List;

public class LoginPost {
    // Encapsulamiento de Posts
    private List<UserVO> Respuesta;
    private String Authorization;


    public LoginPost(List<UserVO> Respuesta, String Authorization ) {
        this.Respuesta = Respuesta;
        this.Authorization=Authorization;
    }

    public void setItems(List<UserVO> Respuesta) {
        this.Respuesta = Respuesta;
    }

    public List<UserVO> getItems() {
        return Respuesta;
    }

    public String getAuthorization() {
        return Authorization;
    }

    public void setAuthorization(String authorization) {
        Authorization = authorization;
    }
}
