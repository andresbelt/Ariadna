package com.oncreate.ariadna.loginLearn;

public class ServiceResult {
    public ServiceError error;

    public ServiceError getError() {
        return this.error;
    }

    public void setError(ServiceError error) {
        this.error = error;
    }

    public boolean isSuccessful() {
        return this.error == null;
    }
}
