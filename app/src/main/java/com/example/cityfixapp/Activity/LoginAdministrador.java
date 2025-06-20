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

public class LoginAdministrador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_administrador);

        // Inicializar componentes de la interfaz
        EditText etAdminUsuario = findViewById(R.id.Usuario);
        EditText etAdminPassword = findViewById(R.id.Contraseña);
        Button btnLoginAdmin = findViewById(R.id.BTAceptarlogin);

        // Configurar el botón de inicio de sesión

        btnLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores ingresados por el usuario
                String inputUsername = etAdminUsuario.getText().toString().trim();
                String inputPassword = etAdminPassword.getText().toString().trim();
                // Validar que los campos no estén vacíos

                if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(LoginAdministrador.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar las credenciales del administrador

                DBConexion dbConexion = new DBConexion(LoginAdministrador.this);
                if (dbConexion.verificarCredenciales("administrador", "admin_usuario", "admin_password", inputUsername, inputPassword)) {
                    Toast.makeText(LoginAdministrador.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginAdministrador.this, ActivityAdministrador.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginAdministrador.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}