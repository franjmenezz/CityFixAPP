package com.example.cityfixapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityfixapp.Adapter.Administrador_Adapter;
import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.Modelo.Incidencia;
import com.example.cityfixapp.R;

import java.util.List;

public class Activity_AdministrarIncidencias extends AppCompatActivity {

    private DBConexion dbConexion;
    private RecyclerView recyclerView;
    private Administrador_Adapter adapter;
    private List<Incidencia> listaIncidencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_incidencias);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            v.setPadding(0, insets.getSystemWindowInsetTop(), 0, 0);
            return insets;
        });

        dbConexion = new DBConexion(this);
        recyclerView = findViewById(R.id.recyclerViewIncidencias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        EditText etBuscar = findViewById(R.id.etBuscarAdmin);

        listaIncidencias = dbConexion.obtenerTodasLasIncidencias();
        adapter = new Administrador_Adapter(this, listaIncidencias);
        recyclerView.setAdapter(adapter);

        // Declarar las opciones del Spinner
        String[] opciones = {"Más recientes", "Nombre A-Z", "Nombre Z-A"};

        Spinner spinnerFiltros = findViewById(R.id.spinnerFiltros);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opciones);
        spinnerFiltros.setAdapter(adapterSpinner);

        spinnerFiltros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        adapter.ordenarPorFecha(); // Más recientes
                        break;
                    case 1:
                        adapter.ordenarPorNombreAZ(); // A-Z
                        break;
                    case 2:
                        adapter.ordenarPorNombreZA(); // Z-A
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });



        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                adapter.filtrar(s.toString());
            }
        });
    }
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
        } else if (item.getItemId() == R.id.action_logout) {
            showLogoutConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrando Sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    startActivity(new Intent(Activity_AdministrarIncidencias.this, Login.class));
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
