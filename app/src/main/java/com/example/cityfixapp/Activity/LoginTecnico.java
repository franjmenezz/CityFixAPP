package com.example.cityfixapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.R;

public class LoginTecnico extends AppCompatActivity {

    private EditText etUsuario, etPassword;
    private Button btnLogin;
    private DBConexion dbConexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_tecnico);

        // Inicializar componentes de la interfaz

        etUsuario = findViewById(R.id.Usuario);
        etPassword = findViewById(R.id.Contraseña);
        btnLogin = findViewById(R.id.BTAceptarlogin);

        dbConexion = new DBConexion(this);


        // Configurar el botón de inicio de sesión
        btnLogin.setOnClickListener(v -> {
            String usuario = etUsuario.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validar que los campos no estén vacíosÇ


            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginTecnico.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar las credenciales del técnico

            boolean valido = dbConexion.verificarCredenciales("tecnicos", "usuario", "password", usuario, password);
            if (valido) {

                // Obtén el id del técnico con el usuario dado
                int idTecnico = dbConexion.obtenerIdTecnicoPorNombre(usuario);

                Toast.makeText(LoginTecnico.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                // Abre la actividad principal para el técnico, pasando el id
                Intent intent = new Intent(LoginTecnico.this, ActivityTecnico.class);
                intent.putExtra("id_tecnico", idTecnico);
                startActivity(intent);
                finish();
            } else {// Si las credenciales son incorrectas
                Toast.makeText(LoginTecnico.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
