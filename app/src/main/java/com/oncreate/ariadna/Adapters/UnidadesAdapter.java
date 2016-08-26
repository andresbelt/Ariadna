package com.oncreate.ariadna.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.ModelsVO.UnidadesVO;


import java.util.List;

/**
 * Created by azulandres92 on 2/15/16.
 */
public class UnidadesAdapter extends ArrayAdapter{

    private RequestQueue requestQueue;
    private JsonObjectRequest jsArrayRequest;
    private List<UnidadesVO> items;

    public UnidadesAdapter(Context context, String complement) {
        super(context, 0);

    }



    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Referencia del view_shortcut procesado
        View listItemView;

        //Comprobando si el View no existe
        listItemView = null == convertView ? layoutInflater.inflate(
                R.layout.item_unidad,
                parent,
                false) : convertView;


        // Obtener el item actual
        UnidadesVO item = items.get(position);

        // Obtener Views
        TextView textoTitulo = (TextView) listItemView.
                findViewById(R.id.module_name);
        final ImageView imagenPost = (ImageView) listItemView.
                findViewById(R.id.module_icon);

        // Actualizar los Views
        textoTitulo.setText(item.getTitulo());

        // Petición para obtener la imagen
//        ImageRequest request = new ImageRequest(
//                URL_BASE + item.getImagen(),
//                new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap bitmap) {
//                        imagenPost.setImageBitmap(bitmap);
//                    }
//                }, 0, 0, null,null,
//                new Response.ErrorListener() {
//                    public void onErrorResponse(VolleyError error) {
//                        imagenPost.setImageResource(R.drawable.error);
//                        Log.d(TAG, "Error en respuesta Bitmap: "+ error.getMessage());
//                    }
//                });

        // Añadir petición a la cola
      //  requestQueue.add(request);


        return listItemView;
    }





}
