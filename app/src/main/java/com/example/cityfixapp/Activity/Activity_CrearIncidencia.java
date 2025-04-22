package com.example.cityfixapp.Activity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.R;

public class Activity_CrearIncidencia extends AppCompatActivity {

    private EditText etTitulo, etDescripcion, etEstado;
    private Button btnGuardarIncidencia;
    private DBConexion dbConexion;
    private int idCiudadano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_incidencia);

        dbConexion = new DBConexion(this);

        // Obtén el ID del ciudadano desde el intent
        idCiudadano = getIntent().getIntExtra("id_ciudadano", -1);

        // Inicializar vistas
        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etEstado = findViewById(R.id.etEstado);
        btnGuardarIncidencia = findViewById(R.id.btnGuardarIncidencia);

        // Configurar el botón para guardar la incidencia
        btnGuardarIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarIncidencia();
            }
        });
    }

    private void guardarIncidencia() {
        String titulo = etTitulo.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        String estado = etEstado.getText().toString();

        if (titulo.isEmpty() || descripcion.isEmpty() || estado.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues valores = new ContentValues();
        valores.put("titulo", titulo);
        valores.put("descripcion", descripcion);
        valores.put("estado", estado);
        valores.put("id_ciudadano", idCiudadano);

        long resultado = dbConexion.getWritableDatabase().insert("incidencias", null, valores);

        if (resultado != -1) {
            Toast.makeText(this, "Incidencia creada con éxito", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al crear la incidencia", Toast.LENGTH_SHORT).show();
        }
    }
}