package com.oncreate.ariadna.loginLearn;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;

import java.util.Locale;

public class Device {
    private String appVersion;
    private String firmware;
    private String locale;
    private String model;
    private String uniqueId;

    public Device(Context context) {
        this.uniqueId = Secure.getString(context.getContentResolver(), "android_id");
        this.uniqueId += "--" + Build.SERIAL;
        this.uniqueId += "--" + StringUtils.onlyLettersAndNumbers(XAuth.hashPassword((Build.MANUFACTURER + " " + Build.MODEL).toLowerCase(Locale.ROOT))).toLowerCase(Locale.ROOT);
        String manufacturer = Build.MANUFACTURER;
        String modelName = Build.MODEL;
        if (modelName.startsWith(manufacturer)) {
            this.model = StringUtils.capitalize(modelName);
        } else {
            this.model = StringUtils.capitalize(manufacturer) + " " + modelName;
        }
        this.firmware = VERSION.RELEASE;
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pInfo != null) {
            this.appVersion = pInfo.versionName;
        }
    }

    public String getUniqueId() {
        return this.uniqueId;
    }

    public String getModel() {
        return this.model;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public String getPlatform() {
        return "Android";
    }

    public String getFirmware() {
        return this.firmware;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
