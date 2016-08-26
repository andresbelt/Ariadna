package com.oncreate.ariadna.loginLearn;

import java.util.HashMap;

public class ParamMap extends HashMap<String, Object> {
    public static ParamMap create() {
        return new ParamMap();
    }

    private ParamMap() {
    }

    public ParamMap add(String key, Object value) {
        put(key, value);
        return this;
    }
}
