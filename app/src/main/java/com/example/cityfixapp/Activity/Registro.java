package com.example.cityfixapp.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.R;
import com.example.cityfixapp.DB.DBEncriptacion;

public class Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        // Inicializar componentes de la interfaz
        EditText etDNI = findViewById(R.id.DNI);
        EditText etNombre = findViewById(R.id.Nombre);
        EditText etUsuario = findViewById(R.id.Usuario);
        EditText etEmail = findViewById(R.id.Email);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etTelefono = findViewById(R.id.Telefono);
        Button btnRegistrar = findViewById(R.id.BTAceptarRegistro);
        Button btnCancelar = findViewById(R.id.BTCancelarRegistro);

        // Configurar el botón de registro

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni = etDNI.getText().toString().trim();
                String nombre = etNombre.getText().toString().trim();
                String usuario = etUsuario.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String telefono = etTelefono.getText().toString().trim();

                // Validar que los campos no estén vacíos

                if (dni.isEmpty() || nombre.isEmpty() || email.isEmpty() || password.isEmpty() || telefono.isEmpty()) {
                    Toast.makeText(Registro.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    registrarCiudadano(dni, nombre, usuario, email, password, telefono);
                }
            }
        });

        // Configurar el botón de cancelar registro

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registro.this, Login.class);
                startActivity(intent);
                finish(); // Finaliza la actividad de registro para que no se pueda volver atrás
            }
        });
    }

    // Método para registrar un ciudadano en la base de datos
    private void registrarCiudadano(String dni, String nombre, String usuario, String email, String contraseña, String telefono) {
        DBConexion dbConexion = new DBConexion(this);
        SQLiteDatabase db = dbConexion.getWritableDatabase();

        // Verificar si el usuario ya existe

        try {
            ContentValues valores = new ContentValues();
            valores.put("dni", DBEncriptacion.encrypt(dni));
            valores.put("nombre", nombre);// No se encripta
            valores.put("usuario", usuario);
            valores.put("email", DBEncriptacion.encrypt(email));
            valores.put("password", DBEncriptacion.encrypt(contraseña));
            valores.put("telefono", DBEncriptacion.encrypt(telefono));

            // Insertar el nuevo ciudadano en la base de datos

            long resultado = db.insert("ciudadano", null, valores);

            // Verificar si la inserción fue exitosa
            if (resultado != -1) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Registro.this, Login.class);
                startActivity(intent);
                finish();
            } else { // Si la inserción falla
                Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally { // Cerrar la base de datos para liberar recursos
            db.close();
        }
    }
}