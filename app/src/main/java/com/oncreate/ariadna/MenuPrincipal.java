package com.oncreate.ariadna;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.oncreate.ariadna.Adapters.UnidadesAdapter;

public class MenuPrincipal extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        GridView listView= (GridView) findViewById(R.id.gridUnidades);

        // Crear adaptador y setear
        UnidadesAdapter  adapter = new UnidadesAdapter(this,"/unidades");
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MenuPrincipal.this, TemasActivity.class);

                startActivity(intent);
            }
        });
    }


}
