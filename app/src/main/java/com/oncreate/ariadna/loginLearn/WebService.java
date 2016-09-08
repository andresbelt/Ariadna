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
import com.oncreate.ariadna.ModelsVO.LoginPost;
import com.oncreate.ariadna.ModelsVO.LoginPost;
import com.oncreate.ariadna.SettingsManager;
import com.oncreate.ariadna.UserManager;
import com.oncreate.ariadna.Util.StorageService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WebService {

    public static final String HOST = "http://www.ariadnapp.biz/apiariadna/";
    public static final String VERSION_API = "v1/api/";
    public static final String ADD_DISCUSSION_POST = "AddDiscussionPost";
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
    public static final String LOGIN = "login";
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
    private Context context;
    private Gson gson;
    private boolean isAuthenticating;
    private boolean isInitialAuthenticationPerformed;
    private RequestQueue requestQueue;
    private String sessionId;
    private SettingsManager settings;
    private StorageService storageService;
    private UserManager.Interop userInterop;
    private UserManager userManager;


    class C13122 implements Listener<LoginPost> {
        final /* synthetic */ String val$action;
        final /* synthetic */ Object val$data;
        final /* synthetic */ Listener val$listener;
        final /* synthetic */ Class val$type;

        C13122(Class cls, String str, Object obj, Listener listener) {
            this.val$type = cls;
            this.val$action = str;
            this.val$data = obj;
            this.val$listener = listener;
        }

        public void onResponse(LoginPost response) {
            WebService.this.requestWithFixup(this.val$type, this.val$action, this.val$data, this.val$listener);
        }
    }


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
                if ((response instanceof LoginPost) && response.isSuccessful()) {
                    LoginPost authenticationResult = (LoginPost) response;
                    if (authenticationResult.getAuthorization() != null) {
                        WebService.this.sessionId = authenticationResult.getAuthorization();
                        WebService.this.storageService.setString(WebService.SESSION_ID_KEY, WebService.this.sessionId);
                    }
                    if (authenticationResult.getItems() != null) {
                        WebService.this.userInterop.setUser(authenticationResult.getItems());
                    }
                }
                if (this.val$listener != null) {
                    this.val$listener.onResponse(response);
                    return;
                }
                return;
            }
            WebService.this.abandonSession();
            WebService.this.requestWithFixup(this.val$type, this.val$action, this.val$data, this.val$listener);
        }
    }


    class C13164 implements Listener<LoginPost> {
        final Listener val$listener;

        C13164(Listener listener) {
            this.val$listener = listener;
        }

        class C13151 implements Listener<LoginPost> {

            C13151() {
            }


            public void onResponse(LoginPost response) {
                if (response.isSuccessful()) {
                    WebService.this.userInterop.setUser(response.getItems());
                } else if (response.error.isOperationFault()) {
                    WebService.this.userInterop.setLoggedOut();
                }
                if (C13164.this.val$listener != null) {
                    C13164.this.val$listener.onResponse(response);
                }
            }
        }


        public void onResponse(LoginPost response) {
            if (response.isSuccessful() && response.getItems() == null && WebService.this.userManager != null && WebService.this.userManager.isAuthenticated()) {
                String passwordHash = WebService.this.userInterop.getPasswordHash();
                if (passwordHash != null) {
                    WebService.this.requestWithFixup(LoginPost.class, WebService.LOGIN, ParamMap.create().add(Scopes.EMAIL, WebService.this.userManager.getName()), new C13151());
                    return;
                }
                WebService.this.userInterop.setLoggedOut();
            }
            if (this.val$listener != null) {
                this.val$listener.onResponse(response);
            }
        }
    }

    public class Request<T extends ServiceResult> extends com.android.volley.Request<T> {
        private String authorization;
        private byte[] body;
        private String contentType;
        private Object data;
        private Listener<T> listener;
        private Class<T> type;

        class C13171 implements ErrorListener {
            final Listener val$listener;
            final WebService val$this$0;
            final Class val$type;

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
                this.contentType = "application/json; charset=\"utf-8\"";
                String json = "{}";
                if (data != null) {
                    json = WebService.this.gson.toJson(data);
                }
                try {
                    this.body = json.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                }
            this.authorization = "Bearer 1";


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

    public WebService(Context context, StorageService storageService, SettingsManager settings) {
        this.isInitialAuthenticationPerformed = false;
        this.isAuthenticating = false;
        this.authCallbacks = new ArrayList();
        this.context = context;
        this.settings = settings;
        this.storageService = storageService;
        this.requestQueue = Volley.newRequestQueue(context);
        this.gson = new GsonBuilder().setFieldNamingStrategy(new AppFieldNamingPolicy()).registerTypeAdapter(Date.class, new UtcDateTypeAdapter()).create();
        this.sessionId = storageService.getString(SESSION_ID_KEY, null);
    }

    public void setUserManager(UserManager userManager, UserManager.Interop userInterop) {
        this.userManager = userManager;
        this.userInterop = userInterop;
    }

    public <T extends ServiceResult> void request(Class<T> type, String action, Object data, Listener<T> listener) {
        if (isNetworkAvailable()) {
            requestWithFixup(type, action, data, listener);
            return;
        }

        try {
            ServiceResult response = type.newInstance();
            response.setError(ServiceError.NO_CONNECTION);
            if (listener != null) {
                listener.onResponse((T) response);
            }
        } catch (Exception e) {
        }
    }


    private <T extends ServiceResult> void requestWithFixup(Class<T> type, String action, Object data, Listener<T> listener) {
        if (this.isInitialAuthenticationPerformed) {
            requestWithMaterialization(type, action, data, (Listener<T>) new C13133(type, action, data, listener));
        } else {
            this.isInitialAuthenticationPerformed = true;
            authenticate(data, new C13122(type, action, data, listener));
        }
    }


    private <T extends ServiceResult> void requestWithMaterialization(Class<T> type, String action, Object data, Listener<T> listener) {
        this.requestQueue.add(new Request(type, Uri.parse(HOST + VERSION_API + action), data, listener));
    }

    private void authenticate(Object data, Listener<LoginPost> listener) {
        requestWithFixup(LoginPost.class, LOGIN, data, new C13164(listener));
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

}
