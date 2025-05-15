package com.example.cityfixapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityfixapp.Adapter.Ciudadano_Adapter;

import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Activity_Ciudadano extends AppCompatActivity {

    private RecyclerView rvMisIncidencias;
    private Ciudadano_Adapter adapter;
    private DBConexion dbConexion;
    private int idCiudadano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciudadano);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            v.setPadding(0, insets.getSystemWindowInsetTop(), 0, 0);
            return insets;
        });

        EditText etBuscar = findViewById(R.id.etBuscarIncidencia);
        RecyclerView rvMisIncidencias = findViewById(R.id.rvMisIncidencias);
        rvMisIncidencias.setLayoutManager(new LinearLayoutManager(this));

        dbConexion = new DBConexion(this);
        idCiudadano = getIntent().getIntExtra("id_ciudadano", -1);


        List<String> listaIncidencias = cargarIncidencias(idCiudadano);
        adapter = new Ciudadano_Adapter(listaIncidencias);
        rvMisIncidencias.setAdapter(adapter);


        // Filtro en tiempo real
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                adapter.filtrar(s.toString());
            }
        });

        Button btnNuevaIncidencia = findViewById(R.id.btnNuevaIncidencia);
        btnNuevaIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Ciudadano.this, Activity_CrearIncidencia.class);
                intent.putExtra("id_ciudadano", idCiudadano); // Pasamos el ID
                startActivity(intent);
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
            Intent intent = new Intent(this, Configuracion.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_user) {
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
                .setTitle("Cerrando Sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Activity_Ciudadano.this, Login.class);
                        startActivity(intent);
                        finish(); // Finaliza la actividad principal para que no se pueda volver atrás
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private List<String> cargarIncidencias(int idCiudadano) {
        List<String> lista = new ArrayList<>();
        SQLiteDatabase db = dbConexion.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT titulo, estado FROM incidencias WHERE id_ciudadano = ?", new String[]{String.valueOf(idCiudadano)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String titulo = cursor.getString(0);
                String estado = cursor.getString(1);
                lista.add(titulo + " (" + estado + ")");
            }
            cursor.close();
        }

        return lista;
    }


}