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

        EditText etEmail = findViewById(R.id.Usuario);
        EditText etPassword = findViewById(R.id.Contraseña);
        Button BTConfirmarLogin = findViewById(R.id.BTAceptarlogin);
        Button BTRegistrar = findViewById(R.id.BTRegistrar);

        BTConfirmarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = etEmail.getText().toString();
                String inputPassword = etPassword.getText().toString();

                String userType = verifyUser(inputUsername, inputPassword);

                if ("admin".equals(userType)) {
                    Toast.makeText(Login.this, "Inicio de sesión como administrador exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Main.class);
                    intent.putExtra("userType", "admin");
                    startActivity(intent);
                    finish(); // Finaliza la actividad de inicio de sesión para que no se pueda volver atrás
                } else if ("ciudadano".equals(userType)) {
                    Toast.makeText(Login.this, "Inicio de sesión como ciudadano exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Main.class);
                    intent.putExtra("userType", "ciudadano");
                    startActivity(intent);
                    finish(); // Finaliza la actividad de inicio de sesión para que no se pueda volver atrás
                } else {
                    Toast.makeText(Login.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });


        BTRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registro.class);
                startActivity(intent);
            }
        });
    }
    private String verifyUser(String username, String password) {
        // Implementa la lógica de verificación de la contraseña aquí
        // Por ejemplo, puedes comparar con una contraseña almacenada
        if ("admin".equals(username) && "1234".equals(password)) {
            return "admin";
        } else if ("ciudadano".equals(username) && "4321".equals(password)) {
            return "ciudadano";
        } else {
            return "invalid";
        }
    }
}