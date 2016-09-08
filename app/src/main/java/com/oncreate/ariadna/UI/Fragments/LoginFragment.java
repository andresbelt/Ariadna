package com.oncreate.ariadna.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.oncreate.ariadna.Base.AppFragment;
import com.oncreate.ariadna.Base.AriadnaApplication;
import com.oncreate.ariadna.Dialog.MessageDialog;
import com.oncreate.ariadna.ModelsVO.LoginPost;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.Util.InputValidator;
import com.oncreate.ariadna.loginLearn.ServiceError;

public class LoginFragment extends AppFragment implements View.OnClickListener, View.OnKeyListener{
    protected TextInputLayout emailLayout;
    protected InputValidator validator;
    private Button btning;
    private EditText email;
    private boolean isMenuEnabled;
    private View rootView;
    private View loginRoot;
    private boolean isAppInitialized;


    class Initialized implements AriadnaApplication.InitializationListener {


        class C12061 implements MessageDialog.Listener {
            C12061() {
            }

            public void onResult(int result) {
                LoginFragment.this.initialize();
            }
        }

        Initialized() {
        }

        public void onSuccess() {
            LoginFragment.this.isAppInitialized = true;
            LoginFragment.this.completeInitialization();
        }

        public void onError() {

            Log.i("","error inicializando");
            MessageDialog.build(LoginFragment.this.getContext()).setTitle((int) R.string.no_internet_connection_title).setMessage((int) R.string.no_internet_connection_message).setPositiveButton((int) R.string.action_retry).setListener(new C12061()).show(LoginFragment.this.getChildFragmentManager());
        }
    }


    class servicesLoginListener implements Response.Listener<LoginPost> {
        final String val$email;


        servicesLoginListener(String str) {
            this.val$email = str;
        }

        public void onResponse(LoginPost response) {

            if (response.isSuccessful()) {
               LoginFragment.this.returnFromLogin();
                return;
            }
            ServiceError error = response.getError();
            if (error.isOperationFault()) {
                if (error.hasFault(1)) {
                   // MessageDialog.create(LoginFragment.this.getContext(), (int) C0471R.string.login_error_popup_title, (int) C0471R.string.error_wrong_credentials, (int) C0471R.string.action_ok).show(LoginFragment.this.getChildFragmentManager());
                    return;
            }
            if (error == ServiceError.NO_CONNECTION) {
              //  MessageDialog.showNoConnectionDialog(LoginFragment.this.getContext(), LoginFragment.this.getChildFragmentManager());
            } else {
          //      MessageDialog.showUnknownErrorDialog(LoginFragment.this.getContext(), LoginFragment.this.getChildFragmentManager());
            }
        }}}



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    public LoginFragment() {
        isMenuEnabled = true;
    }

    public boolean isMenuEnabled() {
        return isMenuEnabled;
    }


    private void initialize() {
        getApp().initialize(new Initialized());
        this.validator = new InputValidator(getContext());
    }


    private void completeInitialization() {
        if (!this.isAppInitialized) {
            return;
        }
        if ((!getApp().getUserManager().isAuthenticated()) || !isAlive()) {
            return;
        }
        if (getApp().getUserManager().isAuthenticated()) {
            returnFromLogin();
        } else if (getApp().isStartupLoginEnabled() && getApp().getSettings().isLoginSkipPreferred()) {
            returnFromLogin();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        loginRoot = rootView.findViewById(R.id.login_layout);

        email = (EditText) rootView.findViewById(R.id.input_email);
        btning = (Button) rootView.findViewById(R.id.btn_ingre);
        btning.setOnClickListener(this);

        if (email != null) {
            emailLayout = (TextInputLayout) rootView.findViewById(R.id.input_layout_email);
            email.setOnKeyListener(this);
        }
        return rootView;
    }



    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public boolean isEntryPoint() {
        return true;
    }

    public boolean isToolbarEnabled() {
        return false;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_ingre:
                if(validateAll())
                if(!this.email.getText().toString().isEmpty()) {
                    //String passwordHash = XAuth.hashPassword("2728596");
                    getApp().getUserManager().login(email.getText().toString(), new servicesLoginListener(email.getText().toString()));
//                    String passwordHash = XAuth.hashPassword("2728596");
//                    ServicesPrincipal.getInstance(getActivity()).request(AuthenticationResult.class, ServicesPrincipal.LOGIN, ParamMap.create().add(Scopes.EMAIL, email).add("password",passwordHash ).add("isExplicit", Boolean.valueOf(true)), new C13031(passwordHash, new servicesLoginListener(email.getText().toString(),passwordHash)));


                 //   Services.loginPost(getActivity(),email.getText().toString());
//                    JSONObject js = new JSONObject();
//                    try {
//                        js.put("email", email.getText());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    jsArrayRequest = new JsonObjectRequest(Request.Method.POST, ConstantVariables.URL_BASE + "/login", js, new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//
//                            Log.d(ConstantVariables.TAG, "Respuesta en JSON: " + response);
//                            Intent intent = new Intent(LoginActivity.this, MenuPrincipal.class);
//
//                            startActivity(intent);
//
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                            Log.d(ConstantVariables.TAG, "Error Respuesta en JSON: " + error.getMessage());
//
//                        }
//
//
//                    }) {
//
//
//                        @Override
//                        protected VolleyError parseNetworkError(VolleyError volleyError) {
//                            if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
//                                VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
//                                volleyError = error;
//                            }
//
//                            return volleyError;
//                        }
//
//
//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            HashMap<String, String> headers = new HashMap<String, String>();
//                            headers.put("Authorization", "Bearer " + "000");
//
//                            return headers;
//                        }
//
//                    };
//
//
//                    requestQueue.add(jsArrayRequest);
                }else{

                    Log.i("e","nohaytexto");
                }

                break;
        }

    }


    protected String validate(EditText input, boolean required) {
        TextInputLayout layout = null;
        String text = input.getText().toString();
        String message = null;
        if (input == email) {
            layout = emailLayout;
            message = validator.validateEmail(text, required);
        }
        if (layout != null) {
            layout.setError(message);
        }
        return message;
    }

    protected boolean validateAll() {
        boolean isValid = false;
        if (this.email != null) {
            isValid = validate(this.email, true) == null;
        }
        return isValid;
    }


    public static LoginFragment createBackStackAware() {
        LoginFragment fragment = new LoginFragment();
        fragment.isMenuEnabled = false;
        return fragment;
    }



    protected void returnFromLogin() {
        navigateHome();
    }



    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }
}
