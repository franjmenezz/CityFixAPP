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

        EditText etDNI = findViewById(R.id.DNI);
        EditText etNombre = findViewById(R.id.Nombre);
        EditText etUsuario = findViewById(R.id.Usuario);
        EditText etEmail = findViewById(R.id.Email);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etTelefono = findViewById(R.id.Telefono);
        Button btnRegistrar = findViewById(R.id.BTAceptarRegistro);
        Button btnCancelar = findViewById(R.id.BTCancelarRegistro);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni = etDNI.getText().toString().trim();
                String nombre = etNombre.getText().toString().trim();
                String usuario = etUsuario.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String telefono = etTelefono.getText().toString().trim();

                if (dni.isEmpty() || nombre.isEmpty() || email.isEmpty() || password.isEmpty() || telefono.isEmpty()) {
                    Toast.makeText(Registro.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    registrarCiudadano(dni, nombre, usuario, email, password, telefono);
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registro.this, Login.class);
                startActivity(intent);
                finish(); // Finaliza la actividad de registro para que no se pueda volver atrás
            }
        });
    }

    private void registrarCiudadano(String dni, String nombre, String usuario, String email, String contraseña, String telefono) {
        DBConexion dbConexion = new DBConexion(this);
        SQLiteDatabase db = dbConexion.getWritableDatabase();

        try {
            ContentValues valores = new ContentValues();
            valores.put("dni", DBEncriptacion.encrypt(dni));
            valores.put("nombre", nombre);// No se encripta
            valores.put("usuario", usuario);
            valores.put("email", DBEncriptacion.encrypt(email));
            valores.put("password", DBEncriptacion.encrypt(contraseña));
            valores.put("telefono", DBEncriptacion.encrypt(telefono));

            long resultado = db.insert("ciudadano", null, valores);

            if (resultado != -1) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Registro.this, Login.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }
}