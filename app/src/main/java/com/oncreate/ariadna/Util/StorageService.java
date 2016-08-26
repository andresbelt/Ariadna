package com.oncreate.ariadna.Util;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class StorageService {
    private Context context;
    private final String key;

    public StorageService(Context context) {
        this.context = context;
        this.key = context.getPackageName();
    }

    public String getPath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return this.context.getFilesDir() + path;
    }

    public FileOutputStream openFileOutput(String path) {
        try {
            int indexOfFile = path.lastIndexOf("/") + 1;
            File file = null;
            if (indexOfFile > 1) {
                File rootDir = new File(this.context.getFilesDir(), path.substring(0, indexOfFile));
                rootDir.mkdirs();
                path = path.substring(indexOfFile);
                file = new File(rootDir, path);
            } else if (indexOfFile == 1) {
                path = path.substring(1);
            }
            if (file == null) {
                file = new File(this.context.getFilesDir(), path);
            }
            return new FileOutputStream(file);
        } catch (IOException e) {
            return null;
        }
    }

    public FileInputStream openFileInput(String path) {
        try {
            return new FileInputStream(new File(this.context.getFilesDir(), path));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public boolean closeStream(Closeable stream) {
        try {
            stream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean writeText(String path, String text) {
        FileOutputStream stream = openFileOutput(path);
        if (stream != null) {
            boolean z;
            try {
                stream.write(text.getBytes("UTF-8"));
                z = true;
                return z;
            } catch (IOException e) {

            } finally {
                closeStream(stream);
            }
        }
        return false;
    }

    public String readText(String path) {
        FileInputStream stream = openFileInput(path);
        if (stream != null) {
            String str;
            try {
                byte[] bytes = new byte[stream.available()];
                stream.read(bytes, 0, bytes.length);
                str = new String(bytes, "UTF-8");
                return str;
            } catch (IOException e) {
                str = e.getMessage();
            } finally {
                closeStream(stream);
            }
        }
        return null;
    }

    public int getInt(String key, int fallback) {
        return getPreferences().getInt(key, fallback);
    }

    public float getFloat(String key, float fallback) {
        return getPreferences().getFloat(key, fallback);
    }

    public boolean getBoolean(String key, boolean fallback) {
        return getPreferences().getBoolean(key, fallback);
    }

    public String getString(String key, String fallback) {
        return getPreferences().getString(key, fallback);
    }

    public void setInt(String key, int value) {
        getPreferences().edit().putInt(key, value).apply();
    }

    public void setFloat(String key, float value) {
        getPreferences().edit().putFloat(key, value).apply();
    }

    public void setBoolean(String key, boolean value) {
        getPreferences().edit().putBoolean(key, value).apply();
    }

    public void setString(String key, String value) {
        getPreferences().edit().putString(key, value).apply();
    }

    public SharedPreferences getPreferences() {
        return this.context.getSharedPreferences(this.key, 0);
    }

    public void deleteFile(String path) {
        this.context.deleteFile(path);
    }
}
