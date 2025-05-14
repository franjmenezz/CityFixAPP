package com.example.cityfixapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cityfixapp.Adapter.Administrador_Adapter;
import com.example.cityfixapp.R;

import java.util.Arrays;
import java.util.List;

public class Activity_Administrador extends AppCompatActivity implements Administrador_Adapter.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            v.setPadding(0, insets.getSystemWindowInsetTop(), 0, 0);
            return insets;
        });

        // Configurar RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvFunciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lista de menús
        List<String> menuItems = Arrays.asList("Incidencias", "Administradores", "Técnicos", "Ciudadanos");

        // Configurar adaptador
        Administrador_Adapter adapter = new Administrador_Adapter(menuItems, this);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, Configuracion.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_user) {
            showPasswordDialog();
            return true;
        }
        if (item.getItemId() == R.id.action_logout) {
            showLogoutConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPasswordDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alerta_password, null);
        EditText passwordInput = dialogView.findViewById(R.id.password_input);

        new AlertDialog.Builder(this)
                .setTitle("Introduzca su contraseña:")
                .setView(dialogView)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = passwordInput.getText().toString();
                        // Aquí puedes agregar la lógica para verificar la contraseña
                        if (verifyPassword(password)) {
                            Intent intent = new Intent(Activity_Administrador.this, Usuario.class);
                            startActivity(intent);
                        } else {
                            // Mostrar un mensaje de error si la contraseña es incorrecta
                            new AlertDialog.Builder(Activity_Administrador.this)
                                    .setTitle("Error")
                                    .setMessage("Contraseña incorrecta")
                                    .setPositiveButton("Aceptar", null)
                                    .show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private boolean verifyPassword(String password) {
        String userType = getIntent().getStringExtra("userType");

        if ("admin".equals(userType)) {
            return "1234".equals(password);
        } else if ("ciudadano".equals(userType)) {
            return "4321".equals(password);
        }else {
            return false; // Contraseña incorrecta
        }

    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrando Sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Activity_Administrador.this, Login.class);
                        startActivity(intent);
                        finish(); // Finaliza la actividad principal para que no se pueda volver atrás
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    public void mostrarIncidencias() {
        // Implementar lógica para mostrar incidencias
    }

    @Override
    public void nuevoAdministrador() {
        // Implementar lógica para nuevo administrador
    }

    @Override
    public void modificarAdministrador() {
        // Implementar lógica para modificar administrador
    }

    @Override
    public void mostrarAdministradores() {
        // Implementar lógica para mostrar administradores
    }

    @Override
    public void nuevoTecnico() {
        // Implementar lógica para nuevo técnico
    }

    @Override
    public void modificarTecnico() {
        // Implementar lógica para modificar técnico
    }

    @Override
    public void mostrarTecnicos() {
        // Implementar lógica para mostrar técnicos
    }

    @Override
    public void modificarCiudadano() {
        // Implementar lógica para modificar ciudadano
    }

    @Override
    public void mostrarCiudadanos() {
        // Implementar lógica para mostrar ciudadanos
    }
}