package com.oncreate.ariadna.loginLearn;

public class ServiceError {
    public static final int ERROR_ARGUMENT_MISSING = 6;
    public static final int ERROR_AUTHENTICATION_FAILED = 1;
    public static final int ERROR_DEVICE_REQUIRED = 2;
    public static final int ERROR_ENDPOINT_NOT_FOUND = 7;
    public static final int ERROR_OPERATION_FAULT = 5;
    public static final int ERROR_SESSION_EXPIRED = 4;
    public static final int ERROR_UNKNOWN = 0;
    public static final int ERROR_USER_REQUIRED = 3;
    public static final int FAULT_DEVICE_NOT_FOUND = 64;
    public static final int FAULT_INSUFFICIENT_PERMISSIONS = 256;
    public static final int FAULT_NONE = 0;
    public static final int FAULT_NOT_ACTIVATED = 2;
    public static final int FAULT_IP = 44;
    public static final ServiceError NO_CONNECTION;
    private int code;
    private int faultMask;
    private String name;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isOperationFault() {
        return this.code == ERROR_OPERATION_FAULT;
    }

    public boolean hasFault(int fault) {

        return this.faultMask != -1 && (this.faultMask & fault) == fault;
    }


    static {
        NO_CONNECTION = new ServiceError(FAULT_NONE, "NoConnection");
    }

    public ServiceError() {
        this.code = FAULT_NONE;
        this.faultMask = -1;
    }

    public ServiceError(int code, String name) {
        this.code = FAULT_NONE;
        this.faultMask = -1;
        this.code = code;
        this.name = name;
    }
}
