package com.example.cityfixapp.Activity;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityfixapp.Adapter.MisIncidencias_Adapter;
import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.R;

import java.util.ArrayList;
import java.util.List;

public class Activity_MisIncidencias extends AppCompatActivity {

    private RecyclerView rvMisIncidencias;
    private MisIncidencias_Adapter adapter;
    private DBConexion dbConexion;
    private int idCiudadano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_incidencias);

        dbConexion = new DBConexion(this);

        // Obtén el ID del ciudadano desde el intent
        idCiudadano = getIntent().getIntExtra("id_ciudadano", -1);

        // Configurar el RecyclerView
        rvMisIncidencias = findViewById(R.id.rvMisIncidencias);
        rvMisIncidencias.setLayoutManager(new LinearLayoutManager(this));

        // Cargar las incidencias del ciudadano

    }


}