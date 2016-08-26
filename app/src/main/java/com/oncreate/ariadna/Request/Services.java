package com.oncreate.ariadna.Request;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.oncreate.ariadna.Base.AriadnaApplication;
import com.oncreate.ariadna.Base.ServicesPrincipal;
import com.oncreate.ariadna.Util.ConstantVariables;
import com.oncreate.ariadna.ModelsVO.LoginPost;
import com.oncreate.ariadna.MenuPrincipal;
import com.oncreate.ariadna.loginLearn.AuthenticationResult;
import com.oncreate.ariadna.loginLearn.Device;
import com.oncreate.ariadna.loginLearn.ServiceError;
import com.oncreate.ariadna.loginLearn.ServiceResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by azulandres92 on 2/15/16.
 */
public class Services {

    private Gson gson;

    private static Response.ErrorListener ReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Do whatever you want to do with error.getMessage();
                Log.d(ConstantVariables.TAG, "Error Volley:"+ error.getMessage());

            }
        };
    }


    public static void loginPost(final Activity Acti, String email) {

        JSONObject js = new JSONObject();
        try {
            js.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }





        ServicesPrincipal.getInstance(Acti).addToRequestQueue(
                new GsonRequest<LoginPost>(Request.Method.POST,
                        ConstantVariables.URL_BASE+ConstantVariables.LOGIN,js,
                        LoginPost.class,
                        null,
                        new Response.Listener<LoginPost>(){
                            @Override
                            public void onResponse(LoginPost response) {
                                Log.d(ConstantVariables.TAG, "Respuesta en JSON: " + response);
                                Intent intent = new Intent(Acti, MenuPrincipal.class);

                                Acti.startActivity(intent);

                        }},
                        ReqErrorListener())


        );

    }


    public Gson getGson() {
        return this.gson;
    }


    private void unidadesGet(final Activity Acti){

        ServicesPrincipal.getInstance(Acti).addToRequestQueue(
                new GsonRequest<LoginPost>(Request.Method.POST,
                        ConstantVariables.URL_BASE+ConstantVariables.UNIDADES,"",
                        LoginPost.class,
                        null,
                        new Response.Listener<LoginPost>(){
                            @Override
                            public void onResponse(LoginPost response) {
                                Log.d(ConstantVariables.TAG, "Respuesta en JSON: " + response);

                               // items = JsonParser.parseJson(response);

                               // notifyDataSetChanged();

                            }},
                        ReqErrorListener())
        );
    }
}
