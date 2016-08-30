package com.oncreate.ariadna;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.support.annotation.WorkerThread;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView.ScaleType;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.oncreate.ariadna.Util.StorageService;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ImageManager {
    private String achievementPath;
    private String achievementUrl;
    private String avatarPath;
    private String avatarUrl;
    private BitmapCache bitmapCache;
    private Context context;
    private String courseIconUrl;
    private String courseWhiteIconUrl;
    private String host;
    private String imagePath;
    private String imageUrl;
    private ImageLoader loader;
    private String modulePath;
    private String moduleUrl;
    private RequestQueue requestQueue;
    private StorageService storage;

    public interface Listener {
        void onResult(Bitmap bitmap);
    }

    class C12962 implements Listener {
        final /* synthetic */ Listener val$listener;
        final /* synthetic */ String val$path;

        C12962(String str, Listener listener) {
            this.val$path = str;
            this.val$listener = listener;
        }

        public void onResult(Bitmap result) {
            if (result != null) {
                ImageManager.this.cache(this.val$path, result, CompressFormat.PNG);
            }
            this.val$listener.onResult(result);
        }
    }

    public static class BitmapCache extends LruCache<String, Bitmap> implements ImageCache {
        private String ignoredKey;

        public BitmapCache(int maxSize) {
            super(maxSize);
        }

        public BitmapCache(Context context) {
            this(getCacheSize(context));
        }

        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }

        public Bitmap getBitmap(String url) {
            if (url.equals(this.ignoredKey)) {
                return null;
            }
            return (Bitmap) get(url);
        }

        public Bitmap getBitmapNoIgnore(String url) {
            return (Bitmap) get(url);
        }

        public void putBitmap(String url, Bitmap bitmap) {
            Log.i("BITMAP_CACHE", "adding bitmap to cache: " + sizeOf(url, bitmap));
            if (url.equals(this.ignoredKey)) {
                this.ignoredKey = null;
            }
            put(url, bitmap);
            logCacheSize();
        }

        protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
            if (!(oldValue == null || oldValue.isRecycled())) {
                oldValue.recycle();
                System.gc();
            }
            Log.i("BITMAP_CACHE", "bitmap removed cache: " + (oldValue != null ? sizeOf(key, oldValue) : -1));
            logCacheSize();
        }

        private void logCacheSize() {
            int cacheSize = 0;
            for (Bitmap item : snapshot().values()) {
                cacheSize += sizeOf(null, item);
            }
            Log.i("BITMAP_CACHE", "Cache size: " + ((((float) cacheSize) / 1024.0f) / 1024.0f) + "MB");
        }

        private static int getCacheSize(Context ctx) {
            DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
            return ((displayMetrics.widthPixels * displayMetrics.heightPixels) * 4) * 2;
        }

        public void removeBitmap(String url) {
            for (String key : snapshot().keySet()) {
                if (key.contains(url)) {
                    remove(key);
                }
            }
        }
    }

    class C14321 implements ImageListener {
        final /* synthetic */ Listener val$listener;

        C14321(Listener listener) {
            this.val$listener = listener;
        }

        public void onResponse(ImageContainer response, boolean isImmediate) {
            Bitmap bitmap = response.getBitmap();
            if (bitmap != null) {
                this.val$listener.onResult(bitmap);
            }
        }

        public void onErrorResponse(VolleyError error) {
            this.val$listener.onResult(null);
        }
    }

    public ImageManager(Context context, StorageService storage) {
        this.context = context;
        this.storage = storage;
        this.bitmapCache = new BitmapCache(context);
        this.host = context.getString(R.string.service_host);
        if (this.host.endsWith("/")) {
            this.host = this.host.substring(0, this.host.length() - 1);
        }
        this.imageUrl = this.host + "/DownloadFile?id=%d";
        this.avatarUrl = this.host + "/uploads/avatars/%d.jpg";
        this.moduleUrl = this.host + "/uploads/modules/%1$d/%2$s.png";
        this.achievementUrl = this.host + "/uploads/achievements/%d.png";
        this.courseIconUrl = this.host + "/uploads/courses/%d.png";
        this.courseWhiteIconUrl = this.host + "/uploads/courses/%d_web.png";
        this.imagePath = "images/%d.jpg";
        this.avatarPath = "avatars/%d.jpg";
        this.modulePath = "modules/module_%s.png";
        this.achievementPath = "achievements/%d.png";
    }

    public void getImage(int fileId, Listener listener) {
        getImage(String.format(this.imagePath, new Object[]{Integer.valueOf(fileId)}), String.format(this.imageUrl, new Object[]{Integer.valueOf(fileId)}), listener);
    }

    private String fixUrl(String url) {
        if (url.startsWith("/")) {
            return this.host + url;
        }
        return url;
    }

    public void getImage(String url, Listener listener) {
        getImage(url, 0, 0, listener);
    }

    public void getImage(String url, int maxWidth, int maxHeight, Listener listener) {
        getLoader().get(fixUrl(url), new C14321(listener), maxWidth, maxHeight);
    }

    public void getImage(String path, String url, Listener listener) {
        getImage(path, url, false, listener);
    }

    public void getImage(String path, String url, boolean forceUpdate, Listener listener) {
        getImage(path, url, 0, 0, forceUpdate, listener);
    }

    public void getImage(String path, String url, int width, int height, boolean forceUpdate, Listener listener) {
        if (forceUpdate || !handleWithCachedImage(path, url, width, height, listener)) {
            getImageAndCache(path, url, width, height, listener);
        }
    }

    private boolean handleWithCachedImage(String path, String url, Listener listener) {
        return handleWithCachedImage(path, url, 0, 0, listener);
    }

    private boolean handleWithCachedImage(String path, String url, int width, int height, Listener listener) {
        String cacheKey = getImageLoaderCacheKey(url, width, height);
        Bitmap bitmap = this.bitmapCache.getBitmapNoIgnore(cacheKey);
        if (bitmap != null) {
            listener.onResult(bitmap);
            return true;
        }
        Options options = new Options();
        options.inPreferredConfig = Config.RGB_565;
        bitmap = BitmapFactory.decodeFile(this.storage.getPath(path), options);
        if (bitmap == null) {
            return false;
        }
        this.bitmapCache.putBitmap(cacheKey, bitmap);
        listener.onResult(bitmap);
        return true;
    }

    private void getImageAndCache(String path, String url, Listener listener) {
        getImageAndCache(path, url, 0, 0, listener);
    }

    private void getImageAndCache(String path, String url, int maxWidth, int maxHeight, Listener listener) {
        getImage(url, maxWidth, maxHeight, new C12962(path, listener));
    }

    private void cache(String path, Bitmap bitmap, CompressFormat format) {
        if (!bitmap.isRecycled()) {
            FileOutputStream stream = this.storage.openFileOutput(path);
            bitmap.compress(format, 100, stream);
            this.storage.closeStream(stream);
        }
    }

    private ImageLoader getLoader() {
        if (this.loader == null) {
            this.loader = new ImageLoader(getRequestQueue(), this.bitmapCache);
        }
        return this.loader;
    }

    private RequestQueue getRequestQueue() {
        if (this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(this.context);
        }
        return this.requestQueue;
    }

    public Bitmap decodeImage(byte[] data, int maxWidth, int maxHeight, boolean cache) {
        String cacheKey = "b_" + maxWidth + "_" + maxHeight + "_" + Arrays.hashCode(data);
        Bitmap bitmap = this.bitmapCache.getBitmap(cacheKey);
        if (bitmap != null) {
            return bitmap;
        }
        Options decodeOptions = new Options();
        if (maxWidth == 0 && maxHeight == 0) {
            decodeOptions.inPreferredConfig = Config.RGB_565;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
        } else {
            decodeOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
            int actualWidth = decodeOptions.outWidth;
            int actualHeight = decodeOptions.outHeight;
            decodeOptions.inJustDecodeBounds = false;
            decodeOptions.inSampleSize = findBestSampleSize(actualWidth, actualHeight, maxWidth, maxHeight);
            Bitmap tempBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
            if (tempBitmap == null || (tempBitmap.getWidth() <= maxWidth && tempBitmap.getHeight() <= maxHeight)) {
                bitmap = tempBitmap;
            } else {
                bitmap = Bitmap.createScaledBitmap(tempBitmap, maxWidth, maxHeight, true);
                tempBitmap.recycle();
            }
        }
        if (cache) {
            this.bitmapCache.putBitmap(cacheKey, bitmap);
        }
        return bitmap;
    }

    static int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double ratio = Math.min(((double) actualWidth) / ((double) desiredWidth), ((double) actualHeight) / ((double) desiredHeight));
        float n = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        while (((double) (2.0f * n)) <= ratio) {
            n *= 2.0f;
        }
        return (int) n;
    }

    private static String getImageLoaderCacheKey(String url) {
        return getImageLoaderCacheKey(url, 0, 0);
    }

    private static String getImageLoaderCacheKey(String url, int width, int height) {
        return "#W" + width + "#H" + height + "#S7" + url;
    }

    @WorkerThread
    public void cacheFile(int fileId) throws InterruptedException, ExecutionException, TimeoutException {
        RequestFuture<Bitmap> future = RequestFuture.newFuture();
        String url = String.format(this.imageUrl, new Object[]{Integer.valueOf(fileId)});
        String path = String.format(this.imagePath, new Object[]{Integer.valueOf(fileId)});
        getRequestQueue().add(new ImageRequest(url, future, 0, 0, ScaleType.CENTER_INSIDE, Config.RGB_565, null));
        Bitmap bitmap = (Bitmap) future.get(30, TimeUnit.SECONDS);
        cache(path, bitmap, CompressFormat.JPEG);
        bitmap.recycle();
    }

    public void getAvatar(int userId, Listener listener) {
        getImage(String.format(this.avatarUrl, new Object[]{Integer.valueOf(userId)}), listener);
    }

    public void getAvatarFromCacheAndUpdate(int userId, Listener listener) {
        String path = String.format(this.avatarPath, new Object[]{Integer.valueOf(userId)});
        String url = String.format(this.avatarUrl, new Object[]{Integer.valueOf(userId)});
        handleWithCachedImage(path, url, listener);
        this.bitmapCache.ignoredKey = getImageLoaderCacheKey(url);
        getImageAndCache(path, url, listener);
    }

    public void forcefeedAvatar(int userId, Bitmap bitmap) {
        cache(String.format(this.avatarPath, new Object[]{Integer.valueOf(userId)}), bitmap, CompressFormat.JPEG);
        this.bitmapCache.removeBitmap(fixUrl(String.format(this.avatarUrl, new Object[]{Integer.valueOf(userId)})));
    }

    public void getAvatarFromCache(int userId, Listener listener) {
        String path = String.format(this.avatarPath, new Object[]{Integer.valueOf(userId)});
        String url = String.format(this.avatarUrl, new Object[]{Integer.valueOf(userId)});
        if (!handleWithCachedImage(path, url, listener)) {
            getImageAndCache(path, url, listener);
        }
    }

    public void getModule(int courseId, int moduleId, boolean disabled, Listener listener) {
        int size = this.context.getResources().getDimensionPixelSize(R.dimen.module_icon_size);
        String iconName = moduleId + (disabled ? "_disabled" : BuildConfig.VERSION_NAME);
        getImage(String.format(this.modulePath, new Object[]{iconName}), String.format(this.moduleUrl, new Object[]{Integer.valueOf(courseId), iconName}), size, size, false, listener);
    }

    public void getAchievement(int achievementId, Listener listener) {
        String path = String.format(this.achievementPath, new Object[]{Integer.valueOf(achievementId)});
        String url = String.format(this.achievementUrl, new Object[]{Integer.valueOf(achievementId)});
        if (!handleWithCachedImage(path, url, listener)) {
            int size = this.context.getResources().getDisplayMetrics().widthPixels / 5;
            getImageAndCache(path, url, size, size, listener);
        }
    }

    public Bitmap getAchievementFromCache(int achievementId) {
        String path = String.format(this.achievementPath, new Object[]{Integer.valueOf(achievementId)});
        String cacheKey = getImageLoaderCacheKey(String.format(this.achievementUrl, new Object[]{Integer.valueOf(achievementId)}), 0, 0);
        Bitmap bitmap = this.bitmapCache.getBitmapNoIgnore(cacheKey);
        if (bitmap == null) {
            Options options = new Options();
            options.inPreferredConfig = Config.RGB_565;
            bitmap = BitmapFactory.decodeFile(this.storage.getPath(path), options);
            if (bitmap != null) {
                this.bitmapCache.putBitmap(cacheKey, bitmap);
            }
        }
        return bitmap;
    }

    public String getAchievementUrl(int achievementId) {
        return String.format(this.achievementUrl, new Object[]{Integer.valueOf(achievementId)});
    }

    public void getCourseIcon(int courseId, Listener listener) {
        getImage(String.format(this.courseIconUrl, new Object[]{Integer.valueOf(courseId)}), listener);
    }

    public void getCourseWhiteIcon(int courseId, Listener listener) {
        getImage(String.format(this.courseWhiteIconUrl, new Object[]{Integer.valueOf(courseId)}), listener);
    }
}
