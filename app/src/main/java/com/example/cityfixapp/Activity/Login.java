package com.example.cityfixapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cityfixapp.R;

public class Login extends AppCompatActivity {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etUsername = findViewById(R.id.Email);
        EditText etPassword = findViewById(R.id.Contraseña);
        Button btnLogin = findViewById(R.id.BTAceptarlogin);
        Button btnRegister = findViewById(R.id.BTregistrar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = etUsername.getText().toString();
                String inputPassword = etPassword.getText().toString();

                if (USERNAME.equals(inputUsername) && PASSWORD.equals(inputPassword)) {
                    Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Main.class);
                    startActivity(intent);
                    finish(); // Finaliza la actividad de inicio de sesión para que no se pueda volver atrás
                } else {
                    Toast.makeText(Login.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registro.class);
                startActivity(intent);
            }
        });
    }
}