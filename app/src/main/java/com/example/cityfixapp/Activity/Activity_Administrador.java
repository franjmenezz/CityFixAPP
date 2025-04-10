package com.example.cityfixapp.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cityfixapp.Adapter.Administrador_Adapter;
import com.example.cityfixapp.R;

import java.util.Arrays;
import java.util.List;

public class Activity_Administrador extends AppCompatActivity implements Administrador_Adapter.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configurar RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvFunciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lista de menús
        List<String> menuItems = Arrays.asList("Incidencias", "Administradores", "Técnicos", "Ciudadanos");

        // Configurar adaptador
        Administrador_Adapter adapter = new Administrador_Adapter(menuItems, this);
        recyclerView.setAdapter(adapter);
    }

    private void toggleVisibility(View layout) {
        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void mostrarIncidencias() {
        // Implementar lógica para mostrar incidencias
    }

    @Override
    public void nuevoAdministrador() {
        // Implementar lógica para nuevo administrador
    }

    @Override
    public void modificarAdministrador() {
        // Implementar lógica para modificar administrador
    }

    @Override
    public void mostrarAdministradores() {
        // Implementar lógica para mostrar administradores
    }

    @Override
    public void nuevoTecnico() {
        // Implementar lógica para nuevo técnico
    }

    @Override
    public void modificarTecnico() {
        // Implementar lógica para modificar técnico
    }

    @Override
    public void mostrarTecnicos() {
        // Implementar lógica para mostrar técnicos
    }

    @Override
    public void modificarCiudadano() {
        // Implementar lógica para modificar ciudadano
    }

    @Override
    public void mostrarCiudadanos() {
        // Implementar lógica para mostrar ciudadanos
    }
}