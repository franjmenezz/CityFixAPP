package com.example.cityfixapp.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityCrearIncidencia extends AppCompatActivity {

    // Constantes para los requests
    private static final int REQUEST_UBICACION = 1001;
    private static final int REQUEST_IMAGE_PICK = 2001;

    private MapView mapView;
    private double latitudSeleccionada = 0;
    private double longitudSeleccionada = 0;


    // Componentes de la interfaz
    private EditText etTitulo, etDescripcion;
    private Button btnGuardarIncidencia, btnSeleccionarUbicacion, btnSeleccionarFoto;
    private ImageView ivFotoSeleccionada;
    private Uri fotoUri;
    private int idCiudadano;
    private DBConexion dbConexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_incidencia);

        // Configurar la Toolbar

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            v.setPadding(0, insets.getInsets(WindowInsetsCompat.Type.systemBars()).top, 0, 0);
            return insets;
        });

        // Inicializar componentes de la interfaz

        dbConexion = new DBConexion(this);
        idCiudadano = getIntent().getIntExtra("id_ciudadano", -1);

        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);

        btnGuardarIncidencia = findViewById(R.id.btnGuardarIncidencia);
        btnSeleccionarUbicacion = findViewById(R.id.btnSeleccionarUbicacion);
        btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);
        ivFotoSeleccionada = findViewById(R.id.ivFoto);

        btnGuardarIncidencia.setOnClickListener(v -> guardarIncidencia());

        // Configurar los botones para seleccionar ubicación y foto

        btnSeleccionarUbicacion.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivitySeleccionarUbicacion.class);
            startActivityForResult(intent, REQUEST_UBICACION);
        });

        btnSeleccionarFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });

        // Inicializar el MapView
        mapView = findViewById(R.id.mapViewUbicacion);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void guardarIncidencia() { // Método para guardar la incidencia
        String titulo = etTitulo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String ubicacion = "Lat: " + latitudSeleccionada + ", Lng: " + longitudSeleccionada;
        String estado = "Pendiente";
        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        if (titulo.isEmpty() || descripcion.isEmpty() || ubicacion.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear ContentValues para insertar en la base de datos
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

        // Insertar en la base de datos

        long resultado = dbConexion.getWritableDatabase().insert("incidencias", null, valores);
        if (resultado != -1) {
            Toast.makeText(this, "Incidencia creada correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar la incidencia", Toast.LENGTH_SHORT).show();
        }
    }

    // Manejar el resultado de las actividades iniciadas
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_UBICACION && resultCode == RESULT_OK && data != null) {
            latitudSeleccionada = data.getDoubleExtra("lat", 0);
            longitudSeleccionada = data.getDoubleExtra("lng", 0);

            // Actualizar el MapView con la nueva ubicación

            mapView.getMapAsync(googleMap -> {
                googleMap.clear(); // Limpia anteriores
                com.google.android.gms.maps.model.LatLng coord = new com.google.android.gms.maps.model.LatLng(latitudSeleccionada, longitudSeleccionada);
                googleMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions().position(coord).title("Ubicación seleccionada"));
                googleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(coord, 15));
            });
        }

        // Manejar la selección de foto

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            fotoUri = data.getData();
            if (fotoUri != null) {
                ivFotoSeleccionada.setImageURI(fotoUri);
            }
        }
    }
}
