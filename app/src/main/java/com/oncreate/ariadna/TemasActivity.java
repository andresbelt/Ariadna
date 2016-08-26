package com.oncreate.ariadna;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.oncreate.ariadna.Adapters.UnidadesAdapter;
import com.oncreate.ariadna.R;

public class TemasActivity extends AppCompatActivity {

    private  GridView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temas);

        listView= (GridView) findViewById(R.id.gridTemas);

        // Crear adaptador y setear
        UnidadesAdapter adapter = new UnidadesAdapter(this,"/temas/1");
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });
    }

}
