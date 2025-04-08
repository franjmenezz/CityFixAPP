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

    // Método para mostrar el mensaje al detectar un administrador
    private void mostrarDialogoAdministrador() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Acceso de Administrador");
        builder.setMessage("Se ha detectado un usuario administrador. Introduzca el PIN de acceso:");

        // Campo de texto para el PIN
        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        builder.setView(input);

        // Botón de aceptar
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pinIngresado = input.getText().toString();
                if (validarPin(pinIngresado)) {
                    Toast.makeText(getApplicationContext(), "Acceso concedido", Toast.LENGTH_SHORT).show();
                    // Continuar con el flujo de administrador
                } else {
                    Toast.makeText(getApplicationContext(), "PIN incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Botón de cancelar
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

        if (dbConexion.verificarCredenciales("administrador", "admin_usuario", "admin_password", username, password)) {
            return "admin";
        } else if (dbConexion.verificarCredenciales("ciudadano", "usuario", "password", username, password)) {
            return "ciudadano";
        } else {
            return "invalid";
        }
    }
}