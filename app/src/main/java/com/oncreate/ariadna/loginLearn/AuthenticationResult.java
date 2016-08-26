package com.oncreate.ariadna.loginLearn;


import com.oncreate.ariadna.loginLearn.ServiceResult;
import com.oncreate.ariadna.loginLearn.User;

public class AuthenticationResult extends ServiceResult {
    private int deviceId;
    private String sessionId;
    private String status;
    private User user;

    public int getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
