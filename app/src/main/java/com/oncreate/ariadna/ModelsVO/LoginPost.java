package com.oncreate.ariadna.ModelsVO;

import com.oncreate.ariadna.loginLearn.ServiceResult;

import java.util.List;

public class LoginPost extends ServiceResult {
    // Encapsulamiento de Posts
    private UserVO Respuesta;
    private String Authorization;


    public LoginPost(UserVO Respuesta, String Authorization) {
        this.Respuesta = Respuesta;
        this.Authorization=Authorization;
    }

    public void setItems(UserVO Respuesta) {
        this.Respuesta = Respuesta;
    }

    public UserVO getItems() {
        return Respuesta;
    }

    public String getAuthorization() {
        return Authorization;
    }

    public void setAuthorization(String authorization) {
        Authorization = authorization;
    }
}
