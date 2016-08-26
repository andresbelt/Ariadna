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
    public static final int FAULT_EXISTING_EMAIL = 16;
    public static final int FAULT_INCORRECT_EMAIL = 4;
    public static final int FAULT_INCORRECT_NAME = 8;
    public static final int FAULT_INCORRECT_PASSWORD = 32;
    public static final int FAULT_INSUFFICIENT_PERMISSIONS = 256;
    public static final int FAULT_NONE = 0;
    public static final int FAULT_NOT_ACTIVATED = 2;
    public static final int FAULT_SOCIAL_CONFLICT = 128;
    public static final int FAULT_WRONG_CREDENTIALS = 1;
    public static final ServiceError NO_CONNECTION;
    private int code;
    private Object data;
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

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isOperationFault() {
        return this.code == ERROR_OPERATION_FAULT;
    }

    public boolean hasFault(int fault) {
        if (this.faultMask == -1 && isOperationFault() && (this.data instanceof Number)) {
            this.faultMask = ((Number) this.data).intValue();
        }
        return this.faultMask != -1 && (this.faultMask & fault) == fault;
    }

    public int getFault() {
        if (!isOperationFault()) {
            return FAULT_NONE;
        }
        if (this.faultMask == -1 && (this.data instanceof Number)) {
            this.faultMask = ((Number) this.data).intValue();
        }
        return this.faultMask;
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
