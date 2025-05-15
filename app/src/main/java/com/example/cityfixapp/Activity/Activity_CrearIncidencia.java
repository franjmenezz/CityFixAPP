package com.example.cityfixapp.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.R;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Activity_CrearIncidencia extends AppCompatActivity {

    private EditText etTitulo, etDescripcion, etUbicacion;
    private Button btnGuardarIncidencia, btnSeleccionarUbicacion, btnSeleccionarFoto;
    private ImageView ivFotoSeleccionada;
    private Uri fotoUri; // para almacenar temporalmente la URI de la imagen
    private int idCiudadano;
    private DBConexion dbConexion;

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
        etUbicacion = findViewById(R.id.etUbicacion);
        btnGuardarIncidencia = findViewById(R.id.btnGuardarIncidencia);
        btnSeleccionarUbicacion = findViewById(R.id.btnSeleccionarUbicacion);
        btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);
        ivFotoSeleccionada = findViewById(R.id.ivFoto);

        // Guardar incidencia
        btnGuardarIncidencia.setOnClickListener(v -> guardarIncidencia());

        btnSeleccionarUbicacion.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_SeleccionarUbicacion.class);
            startActivityForResult(intent, 1001);
        });


    }

    private void guardarIncidencia() {
        String titulo = etTitulo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String ubicacion = etUbicacion.getText().toString().trim();
        String estado = "Pendiente";
        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        if (titulo.isEmpty() || descripcion.isEmpty() || ubicacion.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues valores = new ContentValues();
        valores.put("titulo", titulo);
        valores.put("descripcion", descripcion);
        valores.put("ubicacion", ubicacion);
        valores.put("estado", estado);
        valores.put("fecha_hora", fechaHora);
        valores.put("id_ciudadano", idCiudadano);

        // Guardar foto si hay
        if (fotoUri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(fotoUri);
                byte[] fotoBytes = new byte[inputStream.available()];
                inputStream.read(fotoBytes);
                valores.put("foto", fotoBytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long resultado = dbConexion.getWritableDatabase().insert("incidencias", null, valores);
        if (resultado != -1) {
            Toast.makeText(this, "Incidencia creada correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar la incidencia", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            double lat = data.getDoubleExtra("lat", 0);
            double lng = data.getDoubleExtra("lng", 0);
            String ubicacion = "Lat: " + lat + ", Lng: " + lng;
            etUbicacion.setText(ubicacion);
        }
    }


}