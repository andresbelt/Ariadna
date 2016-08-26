package com.oncreate.ariadna.loginLearn;

import com.google.gson.FieldNamingStrategy;
import java.lang.reflect.Field;

public class AppFieldNamingPolicy implements FieldNamingStrategy {
    public String translateName(Field f) {
        String name = f.getName();
        if (name.endsWith("Id")) {
            return name.substring(0, name.length() - 1) + "D";
        }
        return name;
    }
}
