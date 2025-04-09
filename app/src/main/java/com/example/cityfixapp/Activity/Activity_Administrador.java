package com.example.cityfixapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cityfixapp.Adapter.Administrador_Adapter;
import com.example.cityfixapp.R;

public class Activity_Administrador extends AppCompatActivity implements Administrador_Adapter.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

    }

    private void toggleVisibility(LinearLayout layout) {
        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void mostrarIncidencias() {

    }

    @Override
    public void nuevoAdministrador() {

    }

    @Override
    public void modificarAdministrador() {

    }

    @Override
    public void mostrarAdministradores() {

    }

    @Override
    public void nuevoTecnico() {

    }

    @Override
    public void modificarTecnico() {

    }

    @Override
    public void mostrarTecnicos() {

    }

    @Override
    public void modificarCiudadano() {

    }

    @Override
    public void mostrarCiudadanos() {

    }
}