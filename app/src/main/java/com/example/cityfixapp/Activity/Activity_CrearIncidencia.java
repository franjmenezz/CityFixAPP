package com.example.cityfixapp.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Activity_CrearIncidencia extends AppCompatActivity {

    private static final int REQUEST_UBICACION = 1001;
    private static final int REQUEST_IMAGE_PICK = 2001;

    private EditText etTitulo, etDescripcion, etUbicacion;
    private Button btnGuardarIncidencia, btnSeleccionarUbicacion, btnSeleccionarFoto;
    private ImageView ivFotoSeleccionada;
    private Uri fotoUri;
    private int idCiudadano;
    private DBConexion dbConexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_incidencia);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            v.setPadding(0, insets.getInsets(WindowInsetsCompat.Type.systemBars()).top, 0, 0);
            return insets;
        });

        dbConexion = new DBConexion(this);
        idCiudadano = getIntent().getIntExtra("id_ciudadano", -1);

        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etUbicacion = findViewById(R.id.etUbicacion);
        btnGuardarIncidencia = findViewById(R.id.btnGuardarIncidencia);
        btnSeleccionarUbicacion = findViewById(R.id.btnSeleccionarUbicacion);
        btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);
        ivFotoSeleccionada = findViewById(R.id.ivFoto);

        btnGuardarIncidencia.setOnClickListener(v -> guardarIncidencia());

        btnSeleccionarUbicacion.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_SeleccionarUbicacion.class);
            startActivityForResult(intent, REQUEST_UBICACION);
        });

        btnSeleccionarFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
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

        // Comprimir y guardar foto
        if (fotoUri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(fotoUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Redimensionar y comprimir
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 800, 800, true);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                scaled.compress(Bitmap.CompressFormat.JPEG, 60, stream); // 60% calidad
                byte[] fotoBytes = stream.toByteArray();

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

        if (requestCode == REQUEST_UBICACION && resultCode == RESULT_OK && data != null) {
            double lat = data.getDoubleExtra("lat", 0);
            double lng = data.getDoubleExtra("lng", 0);
            etUbicacion.setText("Lat: " + lat + ", Lng: " + lng);
        }

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            fotoUri = data.getData();
            if (fotoUri != null) {
                ivFotoSeleccionada.setImageURI(fotoUri);
            }
        }
    }
}
