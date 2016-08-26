package com.oncreate.ariadna;

import android.content.Context;
import android.content.SharedPreferences;

import com.oncreate.ariadna.Util.StorageService;

import java.util.Arrays;
import java.util.Locale;

public class SettingsManager {
    private static final String APPLICATION_LANGUAGE = "ApplicationLanguage";
    public static final int CODE_EDITOR_DEFAULT = 0;
    public static final int CODE_EDITOR_FULL_WEB = 2;
    public static final int CODE_EDITOR_THEME_DARK = 2;
    private static final String CODE_EDITOR_THEME_KEY = "CodeEditorTheme";
    public static final int CODE_EDITOR_THEME_LIGHT = 1;
    public static final int CODE_EDITOR_WEB = 1;
    public static boolean FLING_TO_SCROLL = false;
    private static final String FORCE_SPLASH_LOGIN = "ForceSplashLogin";
    private static final String LOCATION_KEY = "LOCATION_IS_ON";
    private static final String PLAYGROUND_EDITOR_TEXT_SCALE = "PlayGroundEditorTextScale";
    private static final String PUSH_KEY = "PUSH_IS_ON";
    public static boolean SHOW_LINE_NUMBERS = false;
    private static final String SKIP_LOGIN_PREFERRED = "skip_login_preferred";
    private static final String SOUND_KEY = "SOUND_IS_ON";
    public static boolean WORDWRAP;
    private int codeEditorMode;
    private int codeEditorTheme;
    private Context context;
    private String deviceDefaultLanguage;
    private boolean isLocalizationEnabled;
    private boolean isLocationEnabled;
    private boolean isLocationSet;
    private boolean isPushEnabled;
    private boolean isSoundEnabled;
    private boolean shouldResetCourse;
    private StorageService storageService;
    private String[] supportedLanguageList;

    public SettingsManager(Context context, StorageService storageService, boolean isLocalizationEnabled) {
        boolean z = false;
        this.storageService = storageService;
        this.context = context;
        this.isLocalizationEnabled = isLocalizationEnabled;
        if (isLocalizationEnabled) {
            this.supportedLanguageList = context.getResources().getStringArray(R.array.supported_languages);
            try {
                this.deviceDefaultLanguage = Locale.getDefault().getLanguage().substring(CODE_EDITOR_DEFAULT, CODE_EDITOR_THEME_DARK).toLowerCase();
            } catch (Exception e) {
                this.deviceDefaultLanguage = "en";
            }
            if (storageService.getString(APPLICATION_LANGUAGE, null) == null) {
                String selectedLanguage;
                if (Arrays.asList(this.supportedLanguageList).contains(this.deviceDefaultLanguage)) {
                    selectedLanguage = this.deviceDefaultLanguage;
                } else {
                    selectedLanguage = this.supportedLanguageList[CODE_EDITOR_DEFAULT];
                }
                storageService.setString(APPLICATION_LANGUAGE, selectedLanguage);
                if (!selectedLanguage.equals("en")) {
                    z = true;
                }
                this.shouldResetCourse = z;
            }
        } else {
            this.deviceDefaultLanguage = "en";
        }
        SharedPreferences pref = storageService.getPreferences();
        this.isLocationSet = pref.contains(LOCATION_KEY);
        this.isLocationEnabled = pref.getBoolean(LOCATION_KEY, true);
        this.isSoundEnabled = pref.getBoolean(SOUND_KEY, true);
        this.isPushEnabled = pref.getBoolean(PUSH_KEY, true);
        this.codeEditorTheme = pref.getInt(CODE_EDITOR_THEME_KEY, CODE_EDITOR_THEME_DARK);
        this.codeEditorMode = context.getResources().getInteger(R.integer.code_editor_mode);
    }

    public boolean isLocationEnabled() {
        return this.isLocationEnabled;
    }

    public void setIsLocationEnabled(boolean isLocationEnabled) {
        this.isLocationEnabled = isLocationEnabled;
        this.isLocationSet = true;
        this.storageService.setBoolean(LOCATION_KEY, isLocationEnabled);
    }

    public boolean isLocationSet() {
        return this.isLocationSet;
    }

    public boolean isPushEnabled() {
        return this.isPushEnabled;
    }

    public void setIsPushEnabled(boolean isPushEnabled) {
        this.isPushEnabled = isPushEnabled;
        this.storageService.setBoolean(PUSH_KEY, isPushEnabled);
    }

    public boolean isSoundEnabled() {
        return this.isSoundEnabled;
    }

    public void setIsSoundEnabled(boolean isSoundEnabled) {
        this.isSoundEnabled = isSoundEnabled;
        this.storageService.setBoolean(SOUND_KEY, isSoundEnabled);
    }

    public String getLanguage() {
        if (this.isLocalizationEnabled) {
            return this.storageService.getString(APPLICATION_LANGUAGE, this.deviceDefaultLanguage);
        }
        return "en";
    }

    public void setLanguage(String language) {
        this.storageService.setString(APPLICATION_LANGUAGE, language);
    }

    public void setForceSplashLogin(boolean forceSplashLogin) {
        this.storageService.setBoolean(FORCE_SPLASH_LOGIN, forceSplashLogin);
    }

    public boolean forceSplashLogin() {
        return this.storageService.getBoolean(FORCE_SPLASH_LOGIN, false);
    }

    public boolean isLoginSkipPreferred() {
        return this.storageService.getBoolean(SKIP_LOGIN_PREFERRED, false);
    }

    public void setLoginSkipPreferred(boolean skip) {
        this.storageService.setBoolean(SKIP_LOGIN_PREFERRED, skip);
    }

    static {
        SHOW_LINE_NUMBERS = true;
        WORDWRAP = false;
        FLING_TO_SCROLL = true;
    }

    public float getPlaygroundEditTextSizePercent() {
        return this.storageService.getFloat(PLAYGROUND_EDITOR_TEXT_SCALE, -1.0f);
    }

    public void setPlaygroundEditorTextSizePercent(float size) {
        this.storageService.setFloat(PLAYGROUND_EDITOR_TEXT_SCALE, size);
    }

    public boolean shouldResetCourse() {
        return this.shouldResetCourse;
    }

    public int getCodeEditorMode() {
        return this.codeEditorMode;
    }

    public int getCodeEditorMode(String language) {
        if (language == null) {
            return this.codeEditorMode;
        }
        int i = -1;
        switch (language.hashCode()) {
            case 110968:
                if (language.equals("php")) {
                    i = CODE_EDITOR_WEB;
                    break;
                }
                break;
        }
        switch (i) {
            case CODE_EDITOR_DEFAULT /*0*/:
                return CODE_EDITOR_THEME_DARK;
            case CODE_EDITOR_WEB /*1*/:
                return CODE_EDITOR_WEB;
            default:
                return this.codeEditorMode;
        }
    }

    public int getCodeEditorTheme() {
        return this.codeEditorTheme;
    }

    public void setCodeEditorTheme(int theme) {
        this.codeEditorTheme = theme;
        this.storageService.setInt(CODE_EDITOR_THEME_KEY, theme);
    }
}
