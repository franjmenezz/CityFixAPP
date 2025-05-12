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
        List<String> listaIncidencias = obtenerIncidenciasDelCiudadano();

        // Configurar el adaptador
        adapter = new MisIncidencias_Adapter(listaIncidencias);
        rvMisIncidencias.setAdapter(adapter);
    }

    private List<String> obtenerIncidenciasDelCiudadano() {
        List<String> incidencias = new ArrayList<>();
        Cursor cursor = dbConexion.seleccionarIncidenciasPorCiudadano(dbConexion.getReadableDatabase(), idCiudadano);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("titulo");
            if (columnIndex != -1) { // Verifica si la columna existe
                do {
                    incidencias.add(cursor.getString(columnIndex));
                } while (cursor.moveToNext());
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return incidencias;
    }
}