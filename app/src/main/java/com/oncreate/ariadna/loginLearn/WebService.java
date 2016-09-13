package com.oncreate.ariadna.loginLearn;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

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
import com.oncreate.ariadna.Base.AppFragment;
import com.oncreate.ariadna.Base.AriadnaApplication;
import com.oncreate.ariadna.Dialog.MessageDialog;
import com.oncreate.ariadna.ModelsVO.LoginPost;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.SettingsManager;
import com.oncreate.ariadna.UI.Fragments.LoginFragment;
import com.oncreate.ariadna.UI.HomeActivity;
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
    ;
    public static final String GET_CONTEST = "Challenge/GetContest";
    public static final String GET_CONTEST_FEED = "Challenge/GetContestFeed";
    public static final String GET_COURSE = "Unidades";
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
    private static final String SESSION_ID_KEY = "sessionId";
    public static final String SET_DEVICE_PUSH_ID = "RegisterPushNotificationID";
    private ArrayList<Runnable> authCallbacks;
    private Context context;
    private Gson gson;
    private RequestQueue requestQueue;
    private String sessionAuthorization;
    private SettingsManager settings;
    private StorageService storageService;
    private UserManager.Interop userInterop;
    private UserManager userManager;


    //escucha errores y respuesta de los servicios
    class ListServicesResultado implements Response.Listener<ServiceResult> {
        final String action;
        final Object datos;
        final Listener listener;
        final Class tipo;

        ListServicesResultado(Class cls, String str, Object obj, Listener listener) {
            this.tipo = cls;
            this.action = str;
            this.datos = obj;
            this.listener = listener;
        }

        public void onResponse(ServiceResult response) {
            if (response.isSuccessful()) {

                if ((response instanceof LoginPost) && response.isSuccessful()) {
                    LoginPost authenticationResult = (LoginPost) response;
                    if (authenticationResult.getAuthorization() != null) {
                        WebService.this.sessionAuthorization = authenticationResult.getAuthorization();
                        WebService.this.storageService.setString(WebService.SESSION_ID_KEY, WebService.this.sessionAuthorization);
                    }
                    if (authenticationResult.getItems() != null) {
                        WebService.this.userInterop.setUser(authenticationResult.getItems());
                    }
                }
            } else {

                ServiceError error = response.getError();

                if (error.hasFault(ServiceError.ERROR_NOT_AUTH)) {
                    //  MessageDialog.create(LoginFragment.this.getContext(), R.string.login_error_popup_title, R.string.error_email_invalid, R.string.action_ok).show(LoginFragment.this.getChildFragmentManager());

                    WebService.this.userInterop.setLoggedOut();
                    WebService.this.abandonSession();
                    AriadnaApplication.getInstance().getActivity().navigate(LoginFragment.createBackStackAware());
                    return;
                }

            }

            if (this.listener != null) {
                this.listener.onResponse(response);
                return;
            }
            return;
//            WebService.this.userInterop.setLoggedOut();
//            WebService.this.abandonSession();
            //   WebService.this.requestWithFixup(this.tipo, this.action, this.datos, this.listener);
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
            sessionAuthorization = storageService.getString(SESSION_ID_KEY, null);
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
            this.authorization = "Bearer " + sessionAuthorization;

            Log.i("", authorization);
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
        this.authCallbacks = new ArrayList();
        this.context = context;
        this.settings = settings;
        this.storageService = storageService;
        this.requestQueue = Volley.newRequestQueue(context);
        this.gson = new GsonBuilder().setFieldNamingStrategy(new AppFieldNamingPolicy()).registerTypeAdapter(Date.class, new UtcDateTypeAdapter()).create();

    }

    public void setUserManager(UserManager userManager, UserManager.Interop userInterop) {
        this.userManager = userManager;
        this.userInterop = userInterop;
    }


    public <T extends ServiceResult> void requestinternet(Class<T> type, Listener<T> listener) {
        if (isNetworkAvailable()) {
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

    //metodo para hacer los request al servidor
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

        requestWithMaterialization(type, action, data, (Listener<T>) new ListServicesResultado(type, action, data, listener));
    }


    private <T extends ServiceResult> void requestWithMaterialization(Class<T> type, String action, Object data, Listener<T> listener) {
        this.requestQueue.add(new Request(type, Uri.parse(HOST + VERSION_API + action), data, listener));
    }


    public boolean isNetworkAvailable() {
        NetworkInfo netInfo = ((ConnectivityManager) this.context.getSystemService("connectivity")).getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void abandonSession() {
        this.sessionAuthorization = null;
        this.storageService.setString(SESSION_ID_KEY, null);
    }

    public Gson getGson() {
        return this.gson;
    }

}
