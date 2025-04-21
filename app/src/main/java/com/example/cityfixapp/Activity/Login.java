package com.example.cityfixapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

        BTConfirmarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = etEmail.getText().toString().trim();
                String inputPassword = etPassword.getText().toString().trim();

                if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(Login.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userType = VerificarUsuario(inputUsername, inputPassword);

                if ("admin".equals(userType)) {
                    mostrarDialogoAdministrador(inputUsername, inputPassword);
                } else if ("tecnico".equals(userType)) {
                    Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Activity_Tecnico.class);
                    intent.putExtra("userType", userType);
                    startActivity(intent);
                    finish();
                } else if ("ciudadano".equals(userType)) {
                    Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Activity_Ciudadano.class);
                    intent.putExtra("userType", userType);
                    startActivity(intent);
                    finish();
                }else {
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


    // Método para mostrar el mensaje al detectar un administrador
    private void mostrarDialogoAdministrador(String username, String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Acceso de Administrador");
        builder.setMessage("Se ha detectado un usuario administrador. Introduzca el PIN de acceso:");

        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pinIngresado = input.getText().toString();
                if (validarPin(pinIngresado)) {
                    Toast.makeText(getApplicationContext(), "Acceso concedido", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Activity_Administrador.class);
                    startActivity(intent);
                    finish(); // Finaliza la actividad de inicio de sesión para que no se pueda volver atrás
                } else {
                    Toast.makeText(getApplicationContext(), "PIN incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Método para validar el PIN
    private boolean validarPin(String pin) {
        final String PIN_CORRECTO = "1234"; // Cambia esto por el PIN real
        return PIN_CORRECTO.equals(pin);
    }

    private String VerificarUsuario(String username, String password) {
        DBConexion dbConexion = new DBConexion(this);

        // Verificar si es administrador
        if (dbConexion.verificarCredenciales("administrador", "admin_usuario", "admin_password", username, password)) {
            return "admin";
        }

        // Verificar si es técnico
        if (dbConexion.verificarCredenciales("tecnicos", "usuario", "password", username, password)) {
            return "tecnico";
        }

        // Verificar si es ciudadano
        if (dbConexion.verificarCredenciales("ciudadano", "usuario", "password", username, password)) {
            return "ciudadano";
        }

        return "invalid";
    }
}