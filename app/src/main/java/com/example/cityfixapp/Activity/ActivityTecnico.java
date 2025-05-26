package com.example.cityfixapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityfixapp.Adapter.TecnicoIncidenciasAdapter;
import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.Modelo.Incidencia;
import com.example.cityfixapp.R;

import java.util.List;

public class ActivityTecnico extends AppCompatActivity {

    private DBConexion dbConexion;
    private TecnicoIncidenciasAdapter adapter;
    private RecyclerView recyclerView;
    private int idTecnico;  // Este id se debe pasar desde login o contexto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnico);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            v.setPadding(0, insets.getSystemWindowInsetTop(), 0, 0);
            return insets;
        });

        recyclerView = findViewById(R.id.rvMisIncidencias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbConexion = new DBConexion(this);

        // Obtener idTecnico de extras
        idTecnico = getIntent().getIntExtra("id_tecnico", -1);
        Log.d("ActivityTecnico", "ID Técnico recibido: " + idTecnico);

        cargarIncidenciasAsignadas();
    }

    // Menú superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, Configuracion.class));
            return true;
        }
        if (item.getItemId() == R.id.action_logout) {
            showLogoutConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrando sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    startActivity(new Intent(ActivityTecnico.this, Login.class));
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void cargarIncidenciasAsignadas() {
        List<Incidencia> listaIncidencias = dbConexion.obtenerIncidenciasPorTecnico(idTecnico);
        Log.d("ActivityTecnico", "Incidencias encontradas: " + listaIncidencias.size());
        adapter = new TecnicoIncidenciasAdapter(this, listaIncidencias);
        recyclerView.setAdapter(adapter);
    }
}
