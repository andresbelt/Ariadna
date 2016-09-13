package com.oncreate.ariadna.loginLearn;

public class ServiceError {
    public static final int ERROR_NOT_AUTH = 1;
    public static final int ERROR_EMAIL_NOT_FOUND = 2;
    public static final int ERROR_EMAIL_WRONG = 3;
    public static final int ERROR_ARGUMENT_MISSING = 6;
    public static final int ERROR_ENDPOINT_NOT_FOUND = 7;
    public static final int ERROR_OPERATION_FAULT = 5;

    public static final int ERROR_UNKNOWN = 0;
    public static final int FAULT_DEVICE_NOT_FOUND = 64;
    public static final int FAULT_NONE = 0;
    public static final int FAULT_NOT_ACTIVATED = 404;
    public static final int FAULT_IP = 44;
    public static final ServiceError NO_CONNECTION;
    private int code;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }



    public boolean hasFault(int fault) {

        return this.code == fault;
    }


    static {
        NO_CONNECTION = new ServiceError(FAULT_NONE, "NoConnection");
    }

    public ServiceError() {
        this.code = FAULT_NONE;
    }

    public ServiceError(int code, String name) {
        this.code = FAULT_NONE;
        this.code = code;
        this.status = name;
    }
}
