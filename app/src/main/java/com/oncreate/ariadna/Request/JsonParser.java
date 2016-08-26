package com.oncreate.ariadna.Request;

import android.util.Log;

import com.oncreate.ariadna.Util.ConstantVariables;
import com.oncreate.ariadna.ModelsVO.TemasVO;
import com.oncreate.ariadna.ModelsVO.UnidadesVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by azulandres92 on 8/7/16.
 */
public class JsonParser {


    public static List<UnidadesVO> parseJson(JSONObject jsonObject){
        // Variables locales
        List<UnidadesVO> posts = new ArrayList<>();
        JSONArray jsonArray= null;

        try {
            // Obtener el array del objeto
            jsonArray = jsonObject.getJSONArray("Respuesta");

            for(int i=0; i<jsonArray.length(); i++){

                try {
                    JSONObject objeto= jsonArray.getJSONObject(i);

                    UnidadesVO post = new UnidadesVO(
                            objeto.getString("titulo"),
                            objeto.getString("expires_at"),
                            objeto.getString("grupo_id"));


                    posts.add(post);

                } catch (JSONException e) {
                    Log.e(ConstantVariables.TAG, "Error de parsing: "+ e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return posts;
    }

    public static List<TemasVO> parseJsonTemas(JSONObject jsonObject){
        // Variables locales
        List<TemasVO> posts = new ArrayList<>();
        JSONArray jsonArray= null;

        try {
            // Obtener el array del objeto
            jsonArray = jsonObject.getJSONArray("Respuesta");

            for(int i=0; i<jsonArray.length(); i++){

                try {
                    JSONObject objeto= jsonArray.getJSONObject(i);

                    TemasVO post = new TemasVO(
                            objeto.getString("unidad_id"),
                            objeto.getString("titulo"),
                            objeto.getString("cantidad"),
                            objeto.getString("expires_at"));


                    posts.add(post);

                } catch (JSONException e) {
                    Log.e(ConstantVariables.TAG, "Error de parsing: "+ e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return posts;
    }



}
