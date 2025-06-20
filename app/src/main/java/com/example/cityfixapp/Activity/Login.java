package com.example.cityfixapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etEmail = findViewById(R.id.Usuario);
        EditText etPassword = findViewById(R.id.Contraseña);
        Button BTConfirmarLogin = findViewById(R.id.BTAceptarlogin);
        Button BTRegistrar = findViewById(R.id.BTRegistrar);
        Button BTLoginAdmin = findViewById(R.id.BTLoginAdmin);
        Button BTLoginTecnico = findViewById(R.id.BTLoginTecnico);

        // Botón para iniciar sesión como ciudadano
        BTConfirmarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = etEmail.getText().toString().trim();
                String inputPassword = etPassword.getText().toString().trim();

                // Validar que los campos no estén vacíos

                if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(Login.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar las credenciales del usuario ciudadano
                if (VerificarUsuarioCiudadano(inputUsername, inputPassword)) {
                    Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                    // Obtener el ID del ciudadano y pasar a la siguiente actividad

                    DBConexion dbConexion = new DBConexion(Login.this);
                    int idCiudadano = dbConexion.obtenerIdCiudadano(inputUsername);

                    // Verificar si se obtuvo un ID válido

                    if (idCiudadano != -1) {
                        Intent intent = new Intent(Login.this, ActivityCiudadano.class);
                        intent.putExtra("id_ciudadano", idCiudadano);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Error al obtener el ID del usuario", Toast.LENGTH_SHORT).show();
                    }
                }
                // Si las credenciales son incorrectas, mostrar un mensaje de error

                else {
                    Toast.makeText(Login.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Botón para redirigir al login de administrador
        BTLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, LoginAdministrador.class);
                startActivity(intent);
            }
        });

        // Botón para redirigir al login de técnico
        BTLoginTecnico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, LoginTecnico.class);
                startActivity(intent);
            }
        });

        // Botón para registrar un nuevo usuario
        BTRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registro.class);
                startActivity(intent);
            }
        });
    }

    // Método para verificar credenciales de ciudadano
    private boolean VerificarUsuarioCiudadano(String username, String password) {
        DBConexion dbConexion = new DBConexion(this);
        return dbConexion.verificarCredenciales("ciudadano", "usuario", "password", username, password);
    }

}