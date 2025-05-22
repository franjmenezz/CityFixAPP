package com.example.cityfixapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityfixapp.Adapter.MenuAdministradorAdapter;
import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.R;

import java.util.List;

public class Activity_Administrador extends AppCompatActivity implements MenuAdministradorAdapter.OnMenuClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            v.setPadding(0, insets.getSystemWindowInsetTop(), 0, 0);
            return insets;
        });

        // RecyclerView con adaptador de menú
        RecyclerView recyclerView = findViewById(R.id.rvFunciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MenuAdministradorAdapter adapter = new MenuAdministradorAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    // Menú superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, Configuracion.class));
            return true;
        }
        if (item.getItemId() == R.id.action_logout) {
            showLogoutConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrando sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    startActivity(new Intent(Activity_Administrador.this, Login.class));
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    // Implementación del listener del menú
    @Override public void mostrarIncidencias() {
        startActivity(new Intent(this, Activity_AdministrarIncidencias.class));
    }

    @Override public void nuevoAdministrador() {
        mostrarDialogoNuevoAdministrador();
    }

    @Override public void modificarAdministrador() {
        mostrarDialogoModificarAdministrador();
    }

    @Override public void mostrarAdministradores() {
        // Por implementar
    }

    @Override public void nuevoTecnico() {
        // Por implementar
    }

    @Override public void modificarTecnico() {
        // Por implementar
    }

    @Override public void mostrarTecnicos() {
        // Por implementar
    }

    @Override public void modificarCiudadano() {
        // Por implementar
    }

    @Override public void mostrarCiudadanos() {
        // Por implementar
    }

    private void mostrarDialogoNuevoAdministrador() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Administrador");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_nuevo_administrador, null);
        final EditText inputUsuario = viewInflated.findViewById(R.id.etUsuario);
        final EditText inputPassword = viewInflated.findViewById(R.id.etPassword);

        builder.setView(viewInflated);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String usuario = inputUsuario.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            DBConexion dbConexion = new DBConexion(this);
            boolean exito = dbConexion.insertarAdministrador(usuario, password);

            if (exito) {
                Toast.makeText(this, "Administrador creado correctamente", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Error al crear administrador", Toast.LENGTH_SHORT).show();
            }


        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    private void mostrarDialogoModificarAdministrador() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modificar Administrador");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_modificar_administrador, null);

        Spinner spinnerAdministradores = viewInflated.findViewById(R.id.spinnerAdministradores);
        EditText etNuevoUsuario = viewInflated.findViewById(R.id.etNuevoUsuario);
        EditText etNuevaPassword = viewInflated.findViewById(R.id.etNuevaPassword);
        Button btnGuardar = viewInflated.findViewById(R.id.btnGuardar);
        Button btnCancelar = viewInflated.findViewById(R.id.btnCancelar);

        // Cargar administradores en el spinner
        DBConexion dbConexion = new DBConexion(this);
        List<String> admins = dbConexion.obtenerUsuariosAdministrador();
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, admins);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdministradores.setAdapter(adapterSpinner);

        AlertDialog dialog = builder.setView(viewInflated).create();

        btnGuardar.setOnClickListener(v -> {
            String usuarioActual = (String) spinnerAdministradores.getSelectedItem();
            String nuevoUsuario = etNuevoUsuario.getText().toString().trim();
            String nuevaPassword = etNuevaPassword.getText().toString().trim();

            if (usuarioActual == null) {
                Toast.makeText(this, "Selecciona un administrador para modificar", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nuevaPassword.isEmpty()) {
                Toast.makeText(this, "La contraseña es obligatoria", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean exito = dbConexion.modificarAdministrador(
                    usuarioActual,
                    nuevoUsuario.isEmpty() ? usuarioActual : nuevoUsuario,
                    nuevaPassword
            );

            if (exito) {
                Toast.makeText(this, "Administrador modificado correctamente", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Error al modificar administrador", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }



}
