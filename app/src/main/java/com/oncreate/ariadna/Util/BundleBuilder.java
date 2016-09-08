package com.oncreate.ariadna.Util;

import android.os.Bundle;

public class BundleBuilder {
    private final Bundle bundle;

    public BundleBuilder() {
        this(new Bundle());
    }

    public BundleBuilder(Bundle existingBundle) {
        this.bundle = new Bundle(existingBundle);
    }

    public BundleBuilder putBoolean(String key, boolean value) {
        this.bundle.putBoolean(key, value);
        return this;
    }

    public BundleBuilder putByte(String key, byte value) {
        this.bundle.putByte(key, value);
        return this;
    }

    public BundleBuilder putChar(String key, char value) {
        this.bundle.putChar(key, value);
        return this;
    }

    public BundleBuilder putShort(String key, short value) {
        this.bundle.putShort(key, value);
        return this;
    }

    public BundleBuilder putInt(String key, int value) {
        this.bundle.putInt(key, value);
        return this;
    }

    public BundleBuilder putLong(String key, long value) {
        this.bundle.putLong(key, value);
        return this;
    }

    public BundleBuilder putFloat(String key, float value) {
        this.bundle.putFloat(key, value);
        return this;
    }

    public BundleBuilder putDouble(String key, double value) {
        this.bundle.putDouble(key, value);
        return this;
    }

    public BundleBuilder putString(String key, String value) {
        this.bundle.putString(key, value);
        return this;
    }

    public BundleBuilder putAll(Bundle bundle) {
        this.bundle.putAll(bundle);
        return this;
    }

    public Bundle toBundle() {
        return this.bundle;
    }
}
