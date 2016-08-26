package com.oncreate.ariadna.Base;

/**
 * Created by azulandres92 on 8/12/16.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.Scopes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oncreate.ariadna.ModelsVO.LoginPost;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.Request.GsonRequest;
import com.oncreate.ariadna.Request.Services;
import com.oncreate.ariadna.SettingsManager;
import com.oncreate.ariadna.UserManager;
import com.oncreate.ariadna.loginLearn.AppFieldNamingPolicy;
import com.oncreate.ariadna.loginLearn.AuthenticationResolver;
import com.oncreate.ariadna.loginLearn.AuthenticationResult;
import com.oncreate.ariadna.loginLearn.BinaryResult;
import com.oncreate.ariadna.loginLearn.Device;
import com.oncreate.ariadna.loginLearn.ParamMap;
import com.oncreate.ariadna.loginLearn.ServiceError;
import com.oncreate.ariadna.loginLearn.ServiceResult;
import com.oncreate.ariadna.loginLearn.UtcDateTypeAdapter;
import com.oncreate.ariadna.loginLearn.XAuth;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ServicesPrincipal {
    // Atributos
    private static ServicesPrincipal singleton;
    public static final String GET_COURSE = "GetCourse";
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private Device device;
    private AuthenticationResolver authenticationResolver;
    private SettingsManager settings;
    private String clientId = "com.sololearn.java", clientSecret = "c936726a2af54a6d9730",sessionId;
    private static Context context;
    private boolean isAuthenticating,isInitialAuthenticationPerformed;
    private ArrayList<Runnable> authCallbacks;
    public static final String AUTHENTICATE_DEVICE = "AuthenticateDevice";
    private static final String SESSION_ID_KEY = "sessionId";
    public static final String LOGIN = "Login";
    private Gson gson;
    private UserManager userManager;

    class C13164 implements Response.Listener<AuthenticationResult> {
        final /* synthetic */ Listener val$listener;

        /* renamed from: com.sololearn.core.web.WebService.4.1 */
        class C13151 implements Listener<AuthenticationResult> {

            /* renamed from: com.sololearn.core.web.WebService.4.1.1 */
            class C13141 implements AuthenticationResolver.Listener {
                final /* synthetic */ AuthenticationResult val$response;

                C13141(AuthenticationResult authenticationResult) {
                    this.val$response = authenticationResult;
                }

                public void onResult(int result) {
                    switch (result) {
                        case 0 /*0*/:
                           // ServicesPrincipal.this.userInterop.setLoggedOut();
                            break;
                        case AppActivity.OFFSET_TOOLBAR /*1*/:
                            break;
                        case AppActivity.OFFSET_TABS /*2*/:
                            ServicesPrincipal.this.authenticate(C13164.this.val$listener);
                            return;
                        default:
                            return;
                    }
                    if (C13164.this.val$listener != null) {
                        C13164.this.val$listener.onResponse(this.val$response);
                    }
                    ServicesPrincipal.this.notifyAuthComplete();
                }
            }

            C13151() {
            }

            public void onResponse(AuthenticationResult response) {
                if (response.isSuccessful()) {
                    Log.i("","loginsehiozassa");
                 //   ServicesPrincipal.this.userInterop.setUser(response.getUser());
                } else if (ServicesPrincipal.this.authenticationResolver != null) {
                    ServicesPrincipal.this.authenticationResolver.resolve(response, new C13141(response));
                    return;
                } else if (response.error.isOperationFault()) {
                   // ServicesPrincipal.this.userInterop.setLoggedOut();
                }
                if (C13164.this.val$listener != null) {
                    C13164.this.val$listener.onResponse(response);
                }
                ServicesPrincipal.this.notifyAuthComplete();
            }
        }

        C13164(Listener listener) {
            this.val$listener = listener;
        }

        public void onResponse(AuthenticationResult response) {
            if (response.isSuccessful() && response.getUser() == null && ServicesPrincipal.this.userManager != null && ServicesPrincipal.this.userManager.isAuthenticated()) {
                String passwordHash = "hashjajajajaj";
                if (passwordHash != null) {
                    ServicesPrincipal.this.requestWithFixup(AuthenticationResult.class, ServicesPrincipal.LOGIN, ParamMap.create().add(Scopes.EMAIL, ServicesPrincipal.this.userManager.getName()).add("password", passwordHash), true, new C13151());
                    return;
                }
              //  ServicesPrincipal.this.userInterop.setLoggedOut();
            }
            if (this.val$listener != null) {
                this.val$listener.onResponse(response);
            }
            ServicesPrincipal.this.notifyAuthComplete();
        }
    }

    /* renamed from: com.sololearn.core.web.WebService.3 */
    class C13133 implements Response.Listener<AuthenticationResult> {
        final /* synthetic */ String val$action;
        final /* synthetic */ Object val$data;
        final /* synthetic */ Response.Listener val$listener;
        final /* synthetic */ Class val$type;

        C13133(Class cls, String str, Object obj, Response.Listener listener) {
            this.val$type = cls;
            this.val$action = str;
            this.val$data = obj;
            this.val$listener = listener;
        }

        public void onResponse(AuthenticationResult response) {
            if (response.isSuccessful() || response.getError().getCode() != 4) {
                if ((response instanceof AuthenticationResult) && response.isSuccessful()) {
                    AuthenticationResult authenticationResult = (AuthenticationResult) response;
                    if (authenticationResult.getSessionId() != null) {
                        ServicesPrincipal.this.sessionId = authenticationResult.getSessionId();
                      //  ServicesPrincipal.this.storageService.setString(ServicesPrincipal.SESSION_ID_KEY, ServicesPrincipal.this.sessionId);
                        Log.d("login","sessionId");
                    }
                    if (authenticationResult.getUser() != null) {
                       // ServicesPrincipal.this.userInterop.setUser(authenticationResult.getUser());

                        Log.d("login","sehizologin");
                    }
                }
                if (this.val$listener != null) {
                    this.val$listener.onResponse(response);
                    return;
                }
                return;
            }
            ServicesPrincipal.this.abandonSession();
            ServicesPrincipal.this.requestWithFixup(this.val$type, this.val$action, this.val$data, true, this.val$listener);
        }
    }


    class C13122 implements Response.Listener<AuthenticationResult> {
        final /* synthetic */ String val$action;
        final /* synthetic */ Object val$data;
        final /* synthetic */ Response.Listener val$listener;
        final /* synthetic */ boolean val$skipAuthCheck;
        final /* synthetic */ Class val$type;

        C13122(Class cls, String str, Object obj, boolean z, Response.Listener listener) {
            this.val$type = cls;
            this.val$action = str;
            this.val$data = obj;
            this.val$skipAuthCheck = z;
            this.val$listener = listener;
        }

        public void onResponse(AuthenticationResult response) {
            ServicesPrincipal.this.requestWithFixup(this.val$type, this.val$action, this.val$data, this.val$skipAuthCheck, this.val$listener);
        }
    }

    public void abandonSession() {
        this.sessionId = null;
        //this.storageService.setString(SESSION_ID_KEY, null);
        this.isInitialAuthenticationPerformed = false;
    }

    private ServicesPrincipal(Context context) {
        ServicesPrincipal.context = context;
        requestQueue = getRequestQueue();
        this.isAuthenticating = false;
        this.isInitialAuthenticationPerformed = false;
        this.authCallbacks = new ArrayList();
        setAuthenticationResolver(new C11143());
        userManager= AriadnaApplication.getInstance().getUserManager();
        this.gson = new GsonBuilder().setFieldNamingStrategy(new AppFieldNamingPolicy()).registerTypeAdapter(Date.class, new UtcDateTypeAdapter()).create();
        this.sessionId = AriadnaApplication.getInstance().getStorage().getString(SESSION_ID_KEY, null);
        this.settings = AriadnaApplication.getInstance().settingsManager;
        this.device = new Device(AriadnaApplication.getInstance());
        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized ServicesPrincipal getInstance(Context context) {
        if (singleton == null) {
            singleton = new ServicesPrincipal(context);
        }
        return singleton;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {

            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(GsonRequest<LoginPost> req) {

        getRequestQueue().add(req);

    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }


    public <T extends ServiceResult> void request(Class<T> type, String action, Object data, Response.Listener<T> listener) {
        if (isNetworkAvailable()) {
            requestWithFixup(type, action, data, false, listener);
            return;
        }
        try {
            ServiceResult response = (ServiceResult) type.newInstance();
            response.setError(ServiceError.NO_CONNECTION);
            if (listener != null) {
                listener.onResponse((T) response);
            }
        } catch (Exception e) {
        }
    }

    private <T extends ServiceResult> void requestWithFixup(Class<T> type, String action, Object data, boolean skipAuthCheck, Response.Listener<T> listener) {
        if (!skipAuthCheck && this.isAuthenticating && !action.equals("")) {
         //   this.authCallbacks.add(new C06891(type, action, data, skipAuthCheck, listener));
        } else if (this.isInitialAuthenticationPerformed) {
            requestWithMaterialization(type, action, data, (Response.Listener<T>) new C13133(type, action, data, listener));
        } else {
            this.isInitialAuthenticationPerformed = true;
            authenticate(new C13122(type, action, data, skipAuthCheck, listener));
        }
    }



    private <T extends ServiceResult> void requestWithMaterialization(Class<T> type, String action, Object data, Response.Listener<T> listener) {
        this.requestQueue.add(new Request(type, Uri.parse("https://api.sololearn.com/"+ action), data, listener));
    }


    private void authenticate(Response.Listener<AuthenticationResult> listener) {

        this.isAuthenticating = true;
        this.device.setLocale(this.settings.getLanguage());
        requestWithFixup(AuthenticationResult.class, AUTHENTICATE_DEVICE, this.device, true, new C13164(listener));
    }


    public boolean isNetworkAvailable() {
        NetworkInfo netInfo = ((ConnectivityManager) AriadnaApplication.getInstance().getSystemService("connectivity")).getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void setAuthenticationResolver(AuthenticationResolver authenticationResolver) {
        this.authenticationResolver = authenticationResolver;
    }

    public class Request<T extends ServiceResult> extends com.android.volley.Request<T> {
        private String authorization;
        private byte[] body;
        private String contentType;
        private Object data;
        private Response.Listener<T> listener;
        private Class<T> type;


        public Request(final Class<T> type, final Uri uri, final Object data, final Response.Listener<T> listener) {
            super(1, uri.toString(), new Response.ErrorListener() {
                Response.Listener vallistener = listener;

               Class valtype = type;

                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                    ServiceResult response = (ServiceResult) this.valtype.newInstance();
                    response.setError(ServiceError.NO_CONNECTION);
                    this.vallistener.onResponse(response);
                } catch (Exception e) {
                }}
            });

            setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            this.type = type;
            this.data = data;
            this.listener = listener;
            if (data instanceof byte[]) {
                this.body = (byte[]) data;
                this.contentType = "application/octet-stream";
            } else {
                this.contentType = "application/json; charset=\"utf-8\"";
                String json = "{}";
                if (data != null) {
                    json = ServicesPrincipal.this.gson.toJson(data);
                }
                try {
                    this.body = json.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                }
            }
            XAuth xAuth = new XAuth(uri, ServicesPrincipal.this.clientId, ServicesPrincipal.this.clientSecret);
            xAuth.getParameters().put("DeviceID", ServicesPrincipal.this.device.getUniqueId());
            if (ServicesPrincipal.this.sessionId != null) {
                xAuth.getParameters().put("SessionID", ServicesPrincipal.this.sessionId);
            }
            xAuth.setBody(this.body);
            this.authorization = xAuth.generateAuthorizationQueryString();
        }

        protected Response<T> parseNetworkResponse(NetworkResponse response) {
            T result = null;
            if (((String) response.headers.get("Content-Type")).contains("application/json")) {
                try {
                    result = (T) ServicesPrincipal.this.gson.fromJson(new String(response.data, "UTF-8"), this.type);
                } catch (UnsupportedEncodingException e) {
                }
            } else if (BinaryResult.class.isAssignableFrom(this.type)) {
                try {
                    result = (T) this.type.newInstance();
                    ((BinaryResult) result).setBody(response.data);
                } catch (Exception e2) {
                }
            }
            if (result != null) {
                return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
            }
            return Response.error(new ParseError());
        }

        protected void deliverResponse(T response) {
            this.listener.onResponse(response);
        }

        public String getBodyContentType() {
            return this.contentType;
        }

        public byte[] getBody() throws AuthFailureError {
            return this.body;
        }

        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap();
            headers.put("Authorization", this.authorization);
            return headers;
        }
    }

    /* renamed from: com.sololearn.app.App.3 */
    class C11143 implements AuthenticationResolver {
        C11143() {
        }

        public void resolve(AuthenticationResult result, AuthenticationResolver.Listener listener) {
            handleAuthenticationError(result, listener);
        }
    }

    private void notifyAuthComplete() {
        ArrayList<Runnable> callbacks = this.authCallbacks;
        this.authCallbacks = new ArrayList();
        this.isAuthenticating = false;
        Iterator it = callbacks.iterator();
        while (it.hasNext()) {
            ((Runnable) it.next()).run();
        }
    }

    private void handleAuthenticationError(AuthenticationResult result, AuthenticationResolver.Listener listener) {
        ServiceError error = result.getError();
        if (error != null) {
            if (error.hasFault(1)) {
                Log.i("","Errorau");
               // MessageDialog.create(getActivity(), (int) R.string.external_logout_title, (int) R.string.external_logout_message, (int) R.string.action_login, (int) R.string.action_cancel, new C11165()).show(getActivity().getSupportFragmentManager());
            }
            if (error.hasFault(2)) {

                Log.i("","Errorau");
//                ActivateAccountDialog activateAccountDialog = new ActivateAccountDialog();
//                activateAccountDialog.setListener(listener);
//                activateAccountDialog.show(getActivity().getSupportFragmentManager());
                return;
            }
        }
        listener.onResult(0);
    }


}

