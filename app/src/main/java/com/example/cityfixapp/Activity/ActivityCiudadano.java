package com.example.cityfixapp.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityfixapp.Adapter.CiudadanoAdapter;
import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.Modelo.Incidencia;
import com.example.cityfixapp.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityCiudadano extends AppCompatActivity {

    private RecyclerView rvMisIncidencias;
    private CiudadanoAdapter adapter;
    private DBConexion dbConexion;
    private int idCiudadano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciudadano);

        // Configurar la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            v.setPadding(0, insets.getSystemWindowInsetTop(), 0, 0);
            return insets;
        });

        // Inicializar componentes de la interfaz
        EditText etBuscar = findViewById(R.id.etBuscarIncidencia);
        rvMisIncidencias = findViewById(R.id.rvMisIncidencias);
        rvMisIncidencias.setLayoutManager(new LinearLayoutManager(this));

        dbConexion = new DBConexion(this);
        idCiudadano = getIntent().getIntExtra("id_ciudadano", -1);

        refrescarRecyclerView();

        // Configurar el adaptador

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                adapter.filtrar(s.toString());
            }
        });

        // Configurar el botón para crear una nueva incidencia
        Button btnNuevaIncidencia = findViewById(R.id.btnNuevaIncidencia);
        btnNuevaIncidencia.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityCiudadano.this, ActivityCrearIncidencia.class);
            intent.putExtra("id_ciudadano", idCiudadano);
            startActivity(intent);
        });
    }

    // Método para refrescar el RecyclerView al volver a la actividad
    @Override
    protected void onResume() {
        super.onResume();
        refrescarRecyclerView();
    }

    // Método para refrescar el RecyclerView con las incidencias del ciudadano

    private void refrescarRecyclerView() {
        List<Incidencia> listaActualizada = cargarIncidencias(idCiudadano);
        adapter = new CiudadanoAdapter(this, listaActualizada);
        rvMisIncidencias.setAdapter(adapter);
        rvMisIncidencias.scrollToPosition(0);
    }

    // Método para cargar las incidencias del ciudadano desde la base de datos


    private List<Incidencia> cargarIncidencias(int idCiudadano) {
        List<Incidencia> lista = new ArrayList<>();
        SQLiteDatabase db = dbConexion.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT _id, titulo, descripcion, ubicacion, estado, fecha_hora FROM incidencias WHERE id_ciudadano = ? ORDER BY _id DESC",
                new String[]{String.valueOf(idCiudadano)}
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String titulo = cursor.getString(1);
            String descripcion = cursor.getString(2);
            String ubicacion = cursor.getString(3);
            String estado = cursor.getString(4);
            String fechaHora = cursor.getString(5);

            lista.add(new Incidencia(id, titulo, descripcion, ubicacion, estado, fechaHora, null));
        }

        cursor.close();
        return lista;
    }


    // Método para crear el menú de opciones y manejar las acciones del menú

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    // Método para manejar la selección de opciones del menú
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

    // Método para mostrar un diálogo de confirmación al cerrar sesión

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrando Sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    startActivity(new Intent(ActivityCiudadano.this, Login.class));
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
