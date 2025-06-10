package com.example.cityfixapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityfixapp.Adapter.ListasAdapter;
import com.example.cityfixapp.Adapter.MenuAdministradorAdapter;
import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.R;

import java.util.List;

public class ActivityAdministrador extends AppCompatActivity implements MenuAdministradorAdapter.OnMenuClickListener {

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

    // Manejo de opciones del menú

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


    // Método para mostrar un diálogo de confirmación antes de cerrar sesión
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrando sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    startActivity(new Intent(ActivityAdministrador.this, Login.class));
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    // Implementación del listener del menú
    @Override public void mostrarIncidencias() {
        startActivity(new Intent(this, ActivityAdministrarIncidencias.class));
    }

    @Override public void nuevoAdministrador() {
        mostrarDialogoNuevoAdministrador();
    }

    @Override public void modificarAdministrador() {
        mostrarDialogoModificarAdministrador();
    }

    @Override public void mostrarAdministradores() {
        mostrarDialogoMostrarAdministradores();
    }

    @Override public void nuevoTecnico() {
        mostrarDialogoNuevoTecnico();
    }

    @Override public void modificarTecnico() {
        mostrarDialogoModificarTecnico();
    }

    @Override public void mostrarTecnicos() {
        mostrarDialogoMostrarTecnicos();
    }

    @Override public void modificarCiudadano() {
        mostrarDialogoModificarCiudadano();
    }

    @Override public void mostrarCiudadanos() {
        mostrarDialogoMostrarCiudadanos();
    }

    // Métodos para mostrar diálogos de creación, modificación y visualización de administradores, técnicos y ciudadanos

    // Método para mostrar diálogo de creación de nuevo administrador
    private void mostrarDialogoNuevoAdministrador() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Administrador");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_nuevo_administrador, null);
        final EditText inputUsuario = viewInflated.findViewById(R.id.etUsuario);
        final EditText inputPassword = viewInflated.findViewById(R.id.etPassword);
        Button btnGuardar = viewInflated.findViewById(R.id.btnGuardar);
        Button btnCancelar = viewInflated.findViewById(R.id.btnCancelar);

        builder.setView(viewInflated);
        AlertDialog dialog = builder.create();
        btnGuardar.setOnClickListener(v -> {
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

        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }

    // Método para mostrar diálogo de modificación de administrador
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
        List<String> admins = dbConexion.obtenerUsuariosAdministradores();

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

    // Método para mostrar diálogo con lista de administradores registrados
    private void mostrarDialogoMostrarAdministradores() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Administradores registrados");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_mostrar_administradores, null);
        TextView tvCantidad = viewInflated.findViewById(R.id.tvCantidadAdmins);
        RecyclerView rvAdministradores = viewInflated.findViewById(R.id.rvAdministradores);
        Button btnCerrar = viewInflated.findViewById(R.id.btnCerrar);

        DBConexion dbConexion = new DBConexion(this);
        List<String> administradores = dbConexion.obtenerUsuariosAdministradores();

        // Mostrar cantidad total de administradores
        tvCantidad.setText("Existen " + administradores.size() + " usuarios administradores");

        // Configurar RecyclerView
        rvAdministradores.setLayoutManager(new LinearLayoutManager(this));
        // Adaptador simple para mostrar lista de strings
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, administradores);
        // RecyclerView no usa ArrayAdapter, sino RecyclerView.Adapter, así que necesitamos uno simple:

        rvAdministradores.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                return new RecyclerView.ViewHolder(itemView) {};
            }
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                TextView tv = (TextView) holder.itemView;
                tv.setText(administradores.get(position));
            }
            @Override
            public int getItemCount() {
                return administradores.size();
            }
        });

        builder.setView(viewInflated);
        // Crear el diálogo y guardarlo en una variable
        AlertDialog dialog = builder.create();
        // Ahora puedes cerrar el diálogo desde el botón
        btnCerrar.setOnClickListener(v -> dialog.dismiss());
        dialog.show(); // Mostrar el diálogo
    }

    // Método para mostrar diálogo de creación de nuevo técnico
    private void mostrarDialogoNuevoTecnico() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Técnico");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_nuevo_tecnico, null);
        final EditText inputNombre = viewInflated.findViewById(R.id.etNombre);
        final EditText inputSector = viewInflated.findViewById(R.id.etSector);
        final EditText inputUsuario = viewInflated.findViewById(R.id.etUsuario);
        final EditText inputPassword = viewInflated.findViewById(R.id.etPassword);
        Button btnGuardar = viewInflated.findViewById(R.id.btnGuardar);
        Button btnCancelar = viewInflated.findViewById(R.id.btnCancelar);

        builder.setView(viewInflated);
        AlertDialog dialog = builder.create();

        btnGuardar.setOnClickListener(v -> {
            String nombre = inputNombre.getText().toString().trim();
            String sector = inputSector.getText().toString().trim();
            String usuario = inputUsuario.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (nombre.isEmpty() || sector.isEmpty() || usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            DBConexion dbConexion = new DBConexion(this);
            boolean exito = dbConexion.insertarTecnico(nombre, sector, usuario, password);

            if (exito) {
                Toast.makeText(this, "Técnico creado correctamente", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Error al crear técnico", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // Método para mostrar diálogo de modificación de técnico

    private void mostrarDialogoModificarTecnico() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modificar Técnico");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_modificar_tecnico, null);

        Spinner spinnerTecnicos = viewInflated.findViewById(R.id.spinnerTecnicos);
        EditText etNuevoNombre = viewInflated.findViewById(R.id.etNuevoNombre);
        EditText etNuevoSector = viewInflated.findViewById(R.id.etNuevoSector);
        EditText etNuevoUsuario = viewInflated.findViewById(R.id.etNuevoUsuario);
        EditText etNuevaPassword = viewInflated.findViewById(R.id.etNuevaPassword);
        Button btnGuardar = viewInflated.findViewById(R.id.btnGuardar);
        Button btnCancelar = viewInflated.findViewById(R.id.btnCancelar);

        DBConexion dbConexion = new DBConexion(this);
        List<String> tecnicos = dbConexion.obtenerUsuariosTecnicos();

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tecnicos);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTecnicos.setAdapter(adapterSpinner);

        AlertDialog dialog = builder.setView(viewInflated).create();

        btnGuardar.setOnClickListener(v -> {
            String tecnicoActual = (String) spinnerTecnicos.getSelectedItem();
            String nuevoNombre = etNuevoNombre.getText().toString().trim();
            String nuevoSector = etNuevoSector.getText().toString().trim();
            String nuevoUsuario = etNuevoUsuario.getText().toString().trim();
            String nuevaPassword = etNuevaPassword.getText().toString().trim();

            if (tecnicoActual == null) {
                Toast.makeText(this, "Selecciona un técnico para modificar", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nuevaPassword.isEmpty()) {
                Toast.makeText(this, "La contraseña es obligatoria", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean exito = dbConexion.modificarTecnico(
                    tecnicoActual,
                    nuevoNombre.isEmpty() ? null : nuevoNombre,
                    nuevoSector.isEmpty() ? null : nuevoSector,
                    nuevoUsuario.isEmpty() ? null : nuevoUsuario,
                    nuevaPassword
            );

            if (exito) {
                Toast.makeText(this, "Técnico modificado correctamente", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Error al modificar técnico", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // Método para mostrar diálogo con lista de técnicos registrados

    private void mostrarDialogoMostrarTecnicos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Técnicos registrados");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_mostrar_tecnicos, null);
        TextView tvCantidad = viewInflated.findViewById(R.id.tvCantidadTecnicos);
        RecyclerView rvTecnicos = viewInflated.findViewById(R.id.rvTecnicos);
        Button btnCerrar = viewInflated.findViewById(R.id.btnCerrar);

        DBConexion dbConexion = new DBConexion(this);
        List<String> tecnicos = dbConexion.obtenerUsuariosTecnicos();

        tvCantidad.setText("Existen " + tecnicos.size() + " técnicos registrados");

        rvTecnicos.setLayoutManager(new LinearLayoutManager(this));
        rvTecnicos.setAdapter(new ListasAdapter(tecnicos));

        builder.setView(viewInflated);
        // Crear el diálogo y guardarlo en una variable
        AlertDialog dialog = builder.create();
        // Ahora puedes cerrar el diálogo desde el botón
        btnCerrar.setOnClickListener(v -> dialog.dismiss());
        dialog.show(); // Mostrar el diálogo

    }

    // Método para mostrar diálogo con lista de ciudadanos registrados

    private void mostrarDialogoMostrarCiudadanos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ciudadanos registrados");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_mostrar_ciudadanos, null);
        TextView tvCantidad = viewInflated.findViewById(R.id.tvCantidadCiudadanos);
        Button btnCerrar = viewInflated.findViewById(R.id.btnCerrar);
        RecyclerView rvCiudadanos = viewInflated.findViewById(R.id.rvCiudadanos);

        DBConexion dbConexion = new DBConexion(this);
        List<String> ciudadanos = dbConexion.obtenerUsuariosCiudadanos();

        tvCantidad.setText("Existen " + ciudadanos.size() + " ciudadanos registrados");

        rvCiudadanos.setLayoutManager(new LinearLayoutManager(this));
        rvCiudadanos.setAdapter(new ListasAdapter(ciudadanos));

        builder.setView(viewInflated);

        // Crear el diálogo y guardarlo en una variable
        AlertDialog dialog = builder.create();

        // Ahora puedes cerrar el diálogo desde el botón
        btnCerrar.setOnClickListener(v -> dialog.dismiss());

        dialog.show(); // Mostrar el diálogo
    }



    // Método para mostrar diálogo de modificación de ciudadano

    private void mostrarDialogoModificarCiudadano() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modificar Ciudadano");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_modificar_ciudadano, null);

        Spinner spinnerCiudadanos = viewInflated.findViewById(R.id.spinnerCiudadanos);
        EditText etNuevoNombre = viewInflated.findViewById(R.id.etNuevoNombre);
        EditText etNuevoEmail = viewInflated.findViewById(R.id.etNuevoEmail);
        EditText etNuevoTelefono = viewInflated.findViewById(R.id.etNuevoTelefono);
        EditText etNuevoUsuario = viewInflated.findViewById(R.id.etNuevoUsuario);
        EditText etNuevaPassword = viewInflated.findViewById(R.id.etNuevaPassword);
        EditText etNuevoDni = viewInflated.findViewById(R.id.etNuevoDNI);
        Button btnGuardar = viewInflated.findViewById(R.id.btnGuardar);
        Button btnCancelar = viewInflated.findViewById(R.id.btnCancelar);

        DBConexion dbConexion = new DBConexion(this);
        List<String> ciudadanos = dbConexion.obtenerUsuariosCiudadanos();

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ciudadanos);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCiudadanos.setAdapter(adapterSpinner);

        AlertDialog dialog = builder.setView(viewInflated).create();

        btnGuardar.setOnClickListener(v -> {
            String ciudadanoActual = (String) spinnerCiudadanos.getSelectedItem();
            String nuevoNombre = etNuevoNombre.getText().toString().trim();
            String nuevoEmail = etNuevoEmail.getText().toString().trim();
            String nuevoTelefono = etNuevoTelefono.getText().toString().trim();
            String nuevoUsuario = etNuevoUsuario.getText().toString().trim();
            String nuevaPassword = etNuevaPassword.getText().toString().trim();
            String nuevoDni = etNuevoDni.getText().toString().trim();

            if (ciudadanoActual == null) {
                Toast.makeText(this, "Selecciona un ciudadano para modificar", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nuevaPassword.isEmpty()) {
                Toast.makeText(this, "La contraseña es obligatoria", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean exito = dbConexion.modificarCiudadano(
                    ciudadanoActual,
                    nuevoNombre.isEmpty() ? ciudadanoActual : nuevoNombre,
                    nuevoEmail.isEmpty() ? "" : nuevoEmail,
                    nuevaPassword.isEmpty() ? "" : nuevaPassword,
                    nuevoTelefono.isEmpty() ? "" : nuevoTelefono,
                    nuevoUsuario.isEmpty() ? ciudadanoActual : nuevoUsuario,
                    nuevoDni.isEmpty() ? "" : nuevoDni
            );

            if (exito) {
                Toast.makeText(this, "Ciudadano modificado correctamente", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Error al modificar ciudadano", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


}
