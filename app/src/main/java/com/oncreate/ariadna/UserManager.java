package com.oncreate.ariadna;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.android.volley.Response;
import com.google.android.gms.common.Scopes;
import com.oncreate.ariadna.ModelsVO.LoginPost;
import com.oncreate.ariadna.ModelsVO.UserVO;
import com.oncreate.ariadna.Util.StorageService;
import com.oncreate.ariadna.loginLearn.ParamMap;
import com.oncreate.ariadna.loginLearn.WebService;

import java.util.ArrayList;
import java.util.Iterator;

public class UserManager {
    public static final int LOGGED_IN = 1;
    public static final int LOGGED_OUT = 0;
    private static final String PREF_USER_ID = "user_id";
    private static final String PREF_USER_LOGGED_OUT = "user_logged_out";
    private static final String PREF_USER_NAME = "user_name";
    private static final String PREF_USER_PASSWORD_HASH = "user_password_hash";
    public static final int RESET = 3;
    public static final int UPDATED = 2;
    private int id;
    private ArrayList<Listener> listeners;
    private String name;
    private StorageService storageService;
    private WebService webService;

    public class Interop {
        private Interop() {
        }

        public void setUser(UserVO user) {
            boolean isLoggedIn = UserManager.this.isAuthenticated();
            UserManager.this.setUser(user);
            UserManager.this.notifyStateChanged(isLoggedIn ? UserManager.UPDATED : UserManager.LOGGED_IN);
        }

        public void setLoggedOut() {
            UserManager.this.resetUser();
            UserManager.this.clearCachedUser(false);
            UserManager.this.notifyStateChanged(UserManager.LOGGED_OUT);
        }
        public String getPasswordHash() {
            if (UserManager.this.isAuthenticated()) {
                return UserManager.this.storageService.getString(UserManager.PREF_USER_PASSWORD_HASH, null);
            }
            return null;
        }
    }

    public interface Listener {
        void onStateChanged(UserManager userManager, int i);
    }



//    class LogoutListener implements com.android.volley.Response.Listener<ServiceResult> {
//        LogoutListener() {
//        }
//
//        public void onResponse(ServiceResult response) {
//            if (!response.isSuccessful()) {
//                UserManager.this.webService.abandonSession();
//            }
//        }
//    }

    class LoginListener implements Response.Listener<LoginPost> {
        final /* synthetic */ Response.Listener val$listener;

        LoginListener(Response.Listener listener) {
            this.val$listener = listener;
        }

        public void onResponse(LoginPost response) {
            if (response.isSuccessful()) {
                UserManager.this.setUser(response.getItems());
                UserManager.this.notifyStateChanged(UserManager.LOGGED_IN);
            }
            if (this.val$listener != null) {
                this.val$listener.onResponse(response);
            }
        }}


    public UserManager(WebService webService, StorageService storageService) {
        this.listeners = new ArrayList();
        this.webService = webService;
        this.storageService = storageService;
        loadCachedUser();
        webService.setUserManager(this, new Interop());
    }

    public boolean isAuthenticated() {
        return this.id > 0;
    }

    public boolean isLoggedOut() {
        return this.storageService.getBoolean(PREF_USER_LOGGED_OUT, false);
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }


    public void login(String email, Response.Listener<LoginPost> listener) {

        webService.request(LoginPost.class, WebService.LOGIN, ParamMap.create().add(Scopes.EMAIL, email), new LoginListener(listener));
    }


    public void logout() {
//        if (isAuthenticated()) {
//            this.webService.request(ServiceResult.class, WebService.LOGOUT, null, new C12945());
//            resetUser();
//            notifyStateChanged(LOGGED_OUT);
//        }
//        clearCachedUser(true);
//        LoginManager.getInstance().logOut();
    }


    private void setUser(UserVO user) {
        this.id = user.getId();
        this.name = user.getNombre();
        this.storageService.getPreferences().edit().putInt(PREF_USER_ID, this.id).putString(PREF_USER_NAME, this.name).remove(PREF_USER_LOGGED_OUT).apply();
    }

    private void updateUser(String name) {
        this.name = name;
        Editor editor = this.storageService.getPreferences().edit().putString(PREF_USER_NAME, name);
        editor.apply();
        notifyStateChanged(UPDATED);
    }

    private void loadCachedUser() {
        SharedPreferences preferences = this.storageService.getPreferences();
        this.id = preferences.getInt(PREF_USER_ID, LOGGED_OUT);
        if (this.id > 0) {
            this.name = preferences.getString(PREF_USER_NAME, BuildConfig.VERSION_NAME);
        } else if (!migrateUser()) {
            resetUser();
        }
    }

    private void resetUser() {
        this.id = LOGGED_OUT;
        this.name = null;
    }

    private void clearCachedUser(boolean logout) {
        Editor editor = this.storageService.getPreferences().edit();
        editor.remove(PREF_USER_ID).remove(PREF_USER_NAME);
        if (logout) {
            editor.putBoolean(PREF_USER_LOGGED_OUT, true);
        }
        editor.apply();
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public void notifyUpdate() {
        notifyStateChanged(UPDATED);
    }

    public void notifyReset() {
        notifyStateChanged(RESET);
    }

    private void notifyStateChanged(int state) {
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            ((Listener) i$.next()).onStateChanged(this, state);
        }
    }

    private boolean migrateUser() {
        SharedPreferences preferences = this.storageService.getPreferences();
        String userJson = preferences.getString("user", null);
        if (userJson != null) {
            try {
//              //  UserVO user = (UserVO) webService.getGson().fromJson(userJson, UserVO.class);
//                if (!(user == null || user.getId() <= 0 )) {
//                    setUser(user);
//                }
            } catch (Exception e) {
            }
        }
        preferences.edit().remove("user").apply();
        return this.id > 0;
    }

}
