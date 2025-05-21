package com.example.cityfixapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityfixapp.Adapter.MenuAdministradorAdapter;
import com.example.cityfixapp.R;

public class Activity_Administrador extends AppCompatActivity implements MenuAdministradorAdapter.OnMenuClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            v.setPadding(0, insets.getSystemWindowInsetTop(), 0, 0);
            return insets;
        });

        // RecyclerView con adaptador de menú
        RecyclerView recyclerView = findViewById(R.id.rvFunciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MenuAdministradorAdapter adapter = new MenuAdministradorAdapter(this);
        recyclerView.setAdapter(adapter);
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
                    startActivity(new Intent(Activity_Administrador.this, Login.class));
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    // Implementación del listener del menú
    @Override public void mostrarIncidencias() {
        startActivity(new Intent(this, Activity_AdministrarIncidencias.class));
    }

    @Override public void nuevoAdministrador() {
        // Por implementar
    }

    @Override public void modificarAdministrador() {
        // Por implementar
    }

    @Override public void mostrarAdministradores() {
        // Por implementar
    }

    @Override public void nuevoTecnico() {
        // Por implementar
    }

    @Override public void modificarTecnico() {
        // Por implementar
    }

    @Override public void mostrarTecnicos() {
        // Por implementar
    }

    @Override public void modificarCiudadano() {
        // Por implementar
    }

    @Override public void mostrarCiudadanos() {
        // Por implementar
    }
}
