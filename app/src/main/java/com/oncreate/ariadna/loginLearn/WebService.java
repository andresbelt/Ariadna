package com.oncreate.ariadna.loginLearn;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.Scopes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oncreate.ariadna.Base.AppActivity;
import com.oncreate.ariadna.SettingsManager;
import com.oncreate.ariadna.UserManager;
import com.oncreate.ariadna.Util.StorageService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WebService {
    public static final String ADD_DISCUSSION_POST = "AddDiscussionPost";
    public static final String AUTHENTICATE_DEVICE = "AuthenticateDevice";
    public static final String AUTHENTICATE_EXTERNAL = "AuthenticateExternal";
    public static final String CHANGE_EMAIL = "ChangeEmail";
    public static final String CLEAR_CONTEST_result = "Challenge/ClearContestResults";
    public static final String CREATE_CONTEST = "Challenge/CreateContest";
    public static final String DECLINE_CONTEST = "Challenge/DeclineContest";
    public static final String DISCONNECT_SERVICE = "DisconnectService";
    public static final String DISCUSSION_CREATE_LESSON_COMMENT = "Discussion/CreateLessonComment";
    public static final String DISCUSSION_CREATE_POST = "Discussion/CreatePost";
    public static final String DISCUSSION_CREATE_REPLY = "Discussion/CreateReply";
    public static final String DISCUSSION_DELETE_LESSON_COMMENT = "Discussion/DeleteLessonComment";
    public static final String DISCUSSION_DELETE_POST = "Discussion/DeletePost";
    public static final String DISCUSSION_EDIT_LESSON_COMMENT = "Discussion/EditLessonComment";
    public static final String DISCUSSION_EDIT_POST = "Discussion/EditPost";
    public static final String DISCUSSION_FOLLOW_POST = "Discussion/FollowPost";
    public static final String DISCUSSION_GET_LESSON_COMMENTS = "Discussion/GetLessonComments";
    public static final String DISCUSSION_GET_LESSON_COMMENT_COUNT = "Discussion/GetLessonCommentCount";
    public static final String DISCUSSION_GET_POST = "Discussion/GetPost";
    public static final String DISCUSSION_GET_REPLIES = "Discussion/GetReplies";
    public static final String DISCUSSION_GET_TAGGED_COUNT = "Discussion/GetTaggedCount";
    public static final String DISCUSSION_GET_TAGS = "Discussion/GetTags";
    public static final String DISCUSSION_SEARCH = "Discussion/Search";
    public static final String DISCUSSION_UNFOLLOW_POST = "Discussion/UnfollowPost";
    public static final String DISCUSSION_VOTE_LESSON_COMMENT = "Discussion/VoteLessonComment";
    public static final String DISCUSSION_VOTE_POST = "Discussion/VotePost";
    public static final String EDIT_DISCUSSION_POST = "EditDiscussionPost";
    public static final String FIND_PLAYERS = "Challenge/FindPlayers";
    public static final String FORGOT_PASSWORD = "ForgotPassword";
    public static final String GET_CERTIFICATE = "DownloadCertificate";
    public static final String GET_CHALLENGE_FEED = "Challenge/GetChallengeFeed";
    public static final String GET_CONTEST = "Challenge/GetContest";
    public static final String GET_CONTEST_FEED = "Challenge/GetContestFeed";
    public static final String GET_COURSE = "GetCourse";
    public static final String GET_COURSES = "Profile/GetCourses";
    public static final String GET_DISCUSSION = "GetDiscussion";
    public static final String GET_FACEBOOK_FRIENDS = "Challenge/GetFacebookFriends";
    public static final String GET_FEED = "Profile/GetFeed";
    public static final String GET_LAUNCH_ACTIONS = "GetLaunchActions";
    public static final String GET_LEADERBOARD = "GetLeaderboard";
    public static final String GET_PLAY_COURSES = "Profile/GetPlayCourses";
    public static final String GET_PROFILE = "GetProfile";
    public static final String GET_PROGRESS = "GetProgress";
    public static final String GET_SIMILAR_COURSES = "GetSimilarCourses";
    public static final String LOGIN = "Login";
    public static final String LOGIN_WITH_ACCESS_TOKEN = "SocialAuthenticationWithAccessToken";
    public static final String LOGOUT = "Logout";
    public static final String ONE_APP_IS_SUBSCRIBED = "IsOneAppSubscribed";
    public static final String ONE_APP_SUBSCRIBE = "SubscribeOneApp";
    public static final String PLAYGROUND_COMPILE_CODE = "Playground/CompileCode";
    public static final String PLAYGROUND_DELETE_CODE = "Playground/DeleteCode";
    public static final String PLAYGROUND_GET_CODE = "Playground/GetCode";
    public static final String PLAYGROUND_GET_CODES = "Playground/GetCodes";
    public static final String PLAYGROUND_GET_CODE_SAMPLE = "Playground/GetCodeSample";
    public static final String PLAYGROUND_GET_PUBLIC_CODES = "Playground/GetPublicCodes";
    public static final String PLAYGROUND_SAVE_CODE = "Playground/SaveCode";
    public static final String PLAYGROUND_TOGGLE_CODE_PUBLIC = "Playground/ToggleCodePublic";
    public static final String PLAYGROUND_VOTE_CODE = "Playground/VoteCode";
    public static final String PUSH_CONTEST_RESULT = "Challenge/PushContestResult";
    public static final String PUSH_PROGRESS = "PushProgress";
    public static final String REGISTER = "Register";
    public static final String REMOVE_AVATAR = "RemoveAvatar";
    public static final String REMOVE_DISCUSSION_POST = "RemoveDiscussionPost";
    public static final String RESET_PROGRESS = "ResetProgress";
    private static final String SESSION_ID_KEY = "sessionId";
    public static final String SET_DEVICE_LOCATION = "SetDeviceLocation";
    public static final String SET_DEVICE_PUSH_ID = "RegisterPushNotificationID";
    public static final String TOGGLE_LEARN = "Profile/ToggleLearn";
    public static final String TOGGLE_PLAY = "Profile/TogglePlay";
    public static final String UPDATE_AVATAR = "UpdateAvatar";
    public static final String UPDATE_PROFILE = "UpdateProfile";
    public static final String VOTE_DISCUSSION_POST = "VoteDiscussionPost";
    private ArrayList<Runnable> authCallbacks;
    private AuthenticationResolver authenticationResolver;
    private String clientId;
    private String clientSecret;
    private Context context;
    private Device device;
    private Gson gson;
    private String host;
    private boolean isAuthenticating;
    private boolean isInitialAuthenticationPerformed;
    private RequestQueue requestQueue;
    private String sessionId;
    private SettingsManager settings;
    private StorageService storageService;
    private UserManager.Interop userInterop;
    private UserManager userManager;

    /* renamed from: com.sololearn.core.web.WebService.1 */
    class C06891 implements Runnable {
        final /* synthetic */ String val$action;
        final /* synthetic */ Object val$data;
        final /* synthetic */ Listener val$listener;
        final /* synthetic */ boolean val$skipAuthCheck;
        final /* synthetic */ Class val$type;

        C06891(Class cls, String str, Object obj, boolean z, Listener listener) {
            this.val$type = cls;
            this.val$action = str;
            this.val$data = obj;
            this.val$skipAuthCheck = z;
            this.val$listener = listener;
        }

        public void run() {
            WebService.this.requestWithFixup(this.val$type, this.val$action, this.val$data, this.val$skipAuthCheck, this.val$listener);
        }
    }

    /* renamed from: com.sololearn.core.web.WebService.2 */
    class C13122 implements Listener<AuthenticationResult> {
        final /* synthetic */ String val$action;
        final /* synthetic */ Object val$data;
        final /* synthetic */ Listener val$listener;
        final /* synthetic */ boolean val$skipAuthCheck;
        final /* synthetic */ Class val$type;

        C13122(Class cls, String str, Object obj, boolean z, Listener listener) {
            this.val$type = cls;
            this.val$action = str;
            this.val$data = obj;
            this.val$skipAuthCheck = z;
            this.val$listener = listener;
        }

        public void onResponse(AuthenticationResult response) {
            WebService.this.requestWithFixup(this.val$type, this.val$action, this.val$data, this.val$skipAuthCheck, this.val$listener);
        }
    }

    /* renamed from: com.sololearn.core.web.WebService.3 */
    class C13133 implements Response.Listener<ServiceResult> {
        final /* synthetic */ String val$action;
        final /* synthetic */ Object val$data;
        final /* synthetic */ Listener val$listener;
        final /* synthetic */ Class val$type;

        C13133(Class cls, String str, Object obj, Listener listener) {
            this.val$type = cls;
            this.val$action = str;
            this.val$data = obj;
            this.val$listener = listener;
        }

        public void onResponse(ServiceResult response) {
            if (response.isSuccessful() || response.getError().getCode() != 4) {
                if ((response instanceof AuthenticationResult) && response.isSuccessful()) {
                    AuthenticationResult authenticationResult = (AuthenticationResult) response;
                    if (authenticationResult.getSessionId() != null) {
                        WebService.this.sessionId = authenticationResult.getSessionId();
                        WebService.this.storageService.setString(WebService.SESSION_ID_KEY, WebService.this.sessionId);
                    }
                    if (authenticationResult.getUser() != null) {
                        WebService.this.userInterop.setUser(authenticationResult.getUser());
                    }
                }
                if (this.val$listener != null) {
                    this.val$listener.onResponse(response);
                    return;
                }
                return;
            }
            WebService.this.abandonSession();
            WebService.this.requestWithFixup(this.val$type, this.val$action, this.val$data, true, this.val$listener);
        }
    }

    /* renamed from: com.sololearn.core.web.WebService.4 */
    class C13164 implements Listener<AuthenticationResult> {
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
                            WebService.this.userInterop.setLoggedOut();
                            break;
                        case AppActivity.OFFSET_TOOLBAR /*1*/:
                            break;
                        case AppActivity.OFFSET_TABS /*2*/:
                            WebService.this.authenticate(C13164.this.val$listener);
                            return;
                        default:
                            return;
                    }
                    if (C13164.this.val$listener != null) {
                        C13164.this.val$listener.onResponse(this.val$response);
                    }
                    WebService.this.notifyAuthComplete();
                }
            }

            C13151() {
            }

            public void onResponse(AuthenticationResult response) {
                if (response.isSuccessful()) {
                    WebService.this.userInterop.setUser(response.getUser());
                } else if (WebService.this.authenticationResolver != null) {
                    WebService.this.authenticationResolver.resolve(response, new C13141(response));
                    return;
                } else if (response.error.isOperationFault()) {
                    WebService.this.userInterop.setLoggedOut();
                }
                if (C13164.this.val$listener != null) {
                    C13164.this.val$listener.onResponse(response);
                }
                WebService.this.notifyAuthComplete();
            }
        }

        C13164(Listener listener) {
            this.val$listener = listener;
        }

        public void onResponse(AuthenticationResult response) {
            if (response.isSuccessful() && response.getUser() == null && WebService.this.userManager != null && WebService.this.userManager.isAuthenticated()) {
                String passwordHash = WebService.this.userInterop.getPasswordHash();
                if (passwordHash != null) {
                    WebService.this.requestWithFixup(AuthenticationResult.class, WebService.LOGIN, ParamMap.create().add(Scopes.EMAIL, WebService.this.userManager.getName()).add("password", passwordHash), true, new C13151());
                    return;
                }
                WebService.this.userInterop.setLoggedOut();
            }
            if (this.val$listener != null) {
                this.val$listener.onResponse(response);
            }
            WebService.this.notifyAuthComplete();
        }
    }

    public class Request<T extends ServiceResult> extends com.android.volley.Request<T> {
        private String authorization;
        private byte[] body;
        private String contentType;
        private Object data;
        private Listener<T> listener;
        private Class<T> type;

        /* renamed from: com.sololearn.core.web.WebService.Request.1 */
        class C13171 implements ErrorListener {
            final /* synthetic */ Listener val$listener;
            final /* synthetic */ WebService val$this$0;
            final /* synthetic */ Class val$type;

            C13171(WebService webService, Class cls, Listener listener) {
                this.val$this$0 = webService;
                this.val$type = cls;
                this.val$listener = listener;
            }

            public void onErrorResponse(VolleyError error) {
                try {
                    ServiceResult response = (ServiceResult) this.val$type.newInstance();
                    response.setError(ServiceError.NO_CONNECTION);
                    this.val$listener.onResponse(response);
                } catch (Exception e) {
                }
            }
        }

        public Request(final Class<T> type, Uri uri, final Object data, final Response.Listener<T> listener) {
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
                    json = WebService.this.gson.toJson(data);
                }
                try {
                    this.body = json.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                }
            }
            XAuth xAuth = new XAuth(uri, WebService.this.clientId, WebService.this.clientSecret);
            xAuth.getParameters().put("DeviceID", WebService.this.device.getUniqueId());
            if (WebService.this.sessionId != null) {
                xAuth.getParameters().put("SessionID", WebService.this.sessionId);
            }
            xAuth.setBody(this.body);
            this.authorization = xAuth.generateAuthorizationQueryString();
        }

        protected Response<T> parseNetworkResponse(NetworkResponse response) {
            T result = null;
            if (((String) response.headers.get("Content-Type")).contains("application/json")) {
                try {
                    result = (T) WebService.this.gson.fromJson(new String(response.data, "UTF-8"), this.type);
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

    public WebService(Context context, StorageService storageService, SettingsManager settings, String clientId, String clientSecret) {
        this.isInitialAuthenticationPerformed = false;
        this.isAuthenticating = false;
        this.authCallbacks = new ArrayList();
        this.host = "https://api.sololearn.com/";
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.context = context;
        this.settings = settings;
        this.storageService = storageService;
        this.requestQueue = Volley.newRequestQueue(context);
        this.device = new Device(context);
        this.gson = new GsonBuilder().setFieldNamingStrategy(new AppFieldNamingPolicy()).registerTypeAdapter(Date.class, new UtcDateTypeAdapter()).create();
        this.sessionId = storageService.getString(SESSION_ID_KEY, null);
    }

    public void setUserManager(UserManager userManager, UserManager.Interop userInterop) {
        this.userManager = userManager;
        this.userInterop = userInterop;
    }

    public <T extends ServiceResult> void request(Class<T> type, String action, Object data, Listener<T> listener) {
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

    private <T extends ServiceResult> void requestWithFixup(Class<T> type, String action, Object data, boolean skipAuthCheck, Listener<T> listener) {
        if (!skipAuthCheck && this.isAuthenticating && !action.equals(CHANGE_EMAIL)) {
            this.authCallbacks.add(new C06891(type, action, data, skipAuthCheck, listener));
        } else if (this.isInitialAuthenticationPerformed) {
            requestWithMaterialization(type, action, data,  (Response.Listener<T>) new C13133(type, action, data, listener));
        } else {
            this.isInitialAuthenticationPerformed = true;
            authenticate(new C13122(type, action, data, skipAuthCheck, listener));
        }
    }

    private <T extends ServiceResult> void requestWithMaterialization(Class<T> type, String action, Object data, Listener<T> listener) {
        this.requestQueue.add(new Request(type, Uri.parse(this.host + action), data, listener));
    }

    private void authenticate(Listener<AuthenticationResult> listener) {
        this.isAuthenticating = true;
        this.device.setLocale(this.settings.getLanguage());
        requestWithFixup(AuthenticationResult.class, AUTHENTICATE_DEVICE, this.device, true, new C13164(listener));
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

    public boolean isNetworkAvailable() {
        NetworkInfo netInfo = ((ConnectivityManager) this.context.getSystemService("connectivity")).getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void abandonSession() {
        this.sessionId = null;
        this.storageService.setString(SESSION_ID_KEY, null);
        this.isInitialAuthenticationPerformed = false;
    }

    public Gson getGson() {
        return this.gson;
    }

    public void setAuthenticationResolver(AuthenticationResolver authenticationResolver) {
        this.authenticationResolver = authenticationResolver;
    }

    public void forceAuthentication() {
        this.isInitialAuthenticationPerformed = false;
    }

    public void forceAuthentication(String sessionId, Listener<AuthenticationResult> listener) {
        this.sessionId = sessionId;
        this.storageService.setString(SESSION_ID_KEY, sessionId);
        authenticate(listener);
    }
}
