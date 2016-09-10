package com.oncreate.ariadna.ModelsVO;

import com.oncreate.ariadna.loginLearn.ServiceResult;

public class LoginPost extends ServiceResult {
    // Encapsulamiento de Posts
    private UserVO respuesta;
    private String authorization;


    public LoginPost(UserVO Respuesta, String Authorization) {
        this.respuesta = Respuesta;
        this.authorization = Authorization;
    }

    public void setItems(UserVO Respuesta) {
        this.respuesta = Respuesta;
    }

    public UserVO getItems() {
        return respuesta;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
