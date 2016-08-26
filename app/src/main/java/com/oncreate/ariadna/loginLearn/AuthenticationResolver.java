package com.oncreate.ariadna.loginLearn;

public interface AuthenticationResolver {
    public static final int FAILED = 0;
    public static final int RESOLVED = 1;
    public static final int RETRY = 2;

    public interface Listener {
        void onResult(int i);
    }

    void resolve(AuthenticationResult authenticationResult, Listener listener);
}
