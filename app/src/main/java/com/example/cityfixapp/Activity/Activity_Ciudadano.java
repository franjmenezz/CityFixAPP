package com.example.cityfixapp.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityfixapp.Adapter.Ciudadano_Adapter;
import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.R;

import java.util.Arrays;
import java.util.List;

public class Activity_Ciudadano extends AppCompatActivity implements Ciudadano_Adapter.OnItemClickListener {

    private DBConexion dbConexion;
    private int idCiudadano; // ID del ciudadano que inició sesión

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciudadano);

        dbConexion = new DBConexion(this);

        // Obtén el ID del ciudadano desde el intent
        idCiudadano = getIntent().getIntExtra("id_ciudadano", -1);

        // Configurar RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvFunciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lista de menús
        List<String> menuItems = Arrays.asList("Mis Incidencias", "Crear Incidencia");

        // Configurar adaptador
        Ciudadano_Adapter adapter = new Ciudadano_Adapter(menuItems, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        if (position == 0) {
            // Mostrar las incidencias del ciudadano
            Intent intent = new Intent(this, Activity_MisIncidencias.class);
            intent.putExtra("id_ciudadano", idCiudadano);
            startActivity(intent);
        } else if (position == 1) {
            // Redirigir a la actividad para crear una nueva incidencia
            Intent intent = new Intent(this, Activity_CrearIncidencia.class);
            intent.putExtra("id_ciudadano", idCiudadano);
            startActivity(intent);
        }
    }
}