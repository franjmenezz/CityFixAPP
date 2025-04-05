package com.example.cityfixapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cityfixapp.R;

public class Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        EditText etDNI = findViewById(R.id.DNI);
        EditText etNombre = findViewById(R.id.Nombre);
        EditText etEmail = findViewById(R.id.Email);
        EditText etContraseña = findViewById(R.id.Contraseña);
        EditText etTelefono = findViewById(R.id.Telefono);
        Button btnRegistrar = findViewById(R.id.BTAceptarRegistro);
        Button btnCancelar = findViewById(R.id.BTCancelarRegistro);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni = etDNI.getText().toString();
                String nombre = etNombre.getText().toString();
                String email = etEmail.getText().toString();
                String contraseña = etContraseña.getText().toString();
                String telefono = etTelefono.getText().toString();

                if (dni.isEmpty() || nombre.isEmpty() || email.isEmpty() || contraseña.isEmpty() || telefono.isEmpty()) {
                    Toast.makeText(Registro.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Aquí puedes agregar la lógica para registrar al usuario
                    Toast.makeText(Registro.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Registro.this, Login.class);
                    startActivity(intent);
                    finish(); // Finaliza la actividad de registro para que no se pueda volver atrás
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
}