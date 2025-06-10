package com.example.cityfixapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityfixapp.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MenuAdministradorAdapter extends RecyclerView.Adapter<MenuAdministradorAdapter.ViewHolder> {

    private final List<String> secciones = Arrays.asList("Incidencias", "Administradores", "Técnicos", "Ciudadanos");
    private final List<Boolean> visibilidadSubmenus = Arrays.asList(false, false, false, false);
    private final OnMenuClickListener listener;

    // Interfaz para manejar los clicks en el menú
    public interface OnMenuClickListener {
        void mostrarIncidencias();
        void nuevoAdministrador();
        void modificarAdministrador();
        void mostrarAdministradores();
        void nuevoTecnico();
        void modificarTecnico();
        void mostrarTecnicos();
        void modificarCiudadano();
        void mostrarCiudadanos();
    }

    // Constructor que recibe el listener
    public MenuAdministradorAdapter(OnMenuClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_funciones, parent, false);
        return new ViewHolder(vista);
    }


    // Método para enlazar los datos a cada ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String nombreSeccion = secciones.get(position);
        holder.titulo.setText(nombreSeccion);

        holder.menuIncidencias.setVisibility(View.GONE);
        holder.menuAdministradores.setVisibility(View.GONE);
        holder.menuTecnicos.setVisibility(View.GONE);
        holder.menuCiudadanos.setVisibility(View.GONE);

        // Configurar la visibilidad del menú según la sección
        holder.titulo.setOnClickListener(v -> {
            boolean visible = visibilidadSubmenus.get(position);
            Collections.fill(visibilidadSubmenus, false);
            visibilidadSubmenus.set(position, !visible);
            notifyDataSetChanged();
        });

        // Mostrar el submenu correspondiente si está visible
        if (visibilidadSubmenus.get(position)) {
            switch (nombreSeccion) {
                case "Incidencias":
                    holder.menuIncidencias.setVisibility(View.VISIBLE);
                    break;
                case "Administradores":
                    holder.menuAdministradores.setVisibility(View.VISIBLE);
                    break;
                case "Técnicos":
                    holder.menuTecnicos.setVisibility(View.VISIBLE);
                    break;
                case "Ciudadanos":
                    holder.menuCiudadanos.setVisibility(View.VISIBLE);
                    break;
            }
        }

        // Configurar los listeners de los botones
        holder.btMostrarIncidencias.setOnClickListener(v -> listener.mostrarIncidencias());
        holder.btNuevoAdministrador.setOnClickListener(v -> listener.nuevoAdministrador());
        holder.btModificarAdministrador.setOnClickListener(v -> listener.modificarAdministrador());
        holder.btMostrarAdministradores.setOnClickListener(v -> listener.mostrarAdministradores());
        holder.btNuevoTecnico.setOnClickListener(v -> listener.nuevoTecnico());
        holder.btModificarTecnico.setOnClickListener(v -> listener.modificarTecnico());
        holder.btMostrarTecnicos.setOnClickListener(v -> listener.mostrarTecnicos());
        holder.btModificarCiudadano.setOnClickListener(v -> listener.modificarCiudadano());
        holder.btMostrarCiudadanos.setOnClickListener(v -> listener.mostrarCiudadanos());
    }

    @Override
    public int getItemCount() {
        return secciones.size();
    }

    // ViewHolder para cada elemento del RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Elementos de la vista
        TextView titulo;
        ConstraintLayout menuIncidencias, menuAdministradores, menuTecnicos, menuCiudadanos;
        Button btMostrarIncidencias, btNuevoAdministrador, btModificarAdministrador, btMostrarAdministradores;
        Button btNuevoTecnico, btModificarTecnico, btMostrarTecnicos, btModificarCiudadano, btMostrarCiudadanos;

        // Constructor del ViewHolder
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_funcion);
            menuIncidencias = itemView.findViewById(R.id.MenuIncidencias);
            menuAdministradores = itemView.findViewById(R.id.MenuAdministradores);
            menuTecnicos = itemView.findViewById(R.id.MenuTecnicos);
            menuCiudadanos = itemView.findViewById(R.id.MenuCiudadanos);
            btMostrarIncidencias = itemView.findViewById(R.id.btMostrarIncidencias);
            btNuevoAdministrador = itemView.findViewById(R.id.btNuevoAdministrador);
            btModificarAdministrador = itemView.findViewById(R.id.btModificarAdministrador);
            btMostrarAdministradores = itemView.findViewById(R.id.btMostrarAdministradores);
            btNuevoTecnico = itemView.findViewById(R.id.btNuevoTecnico);
            btModificarTecnico = itemView.findViewById(R.id.btModicarTecnico);
            btMostrarTecnicos = itemView.findViewById(R.id.btMostrarTecnicos);
            btModificarCiudadano = itemView.findViewById(R.id.btModificarCiudadano);
            btMostrarCiudadanos = itemView.findViewById(R.id.btMostrarCiudadanos);
        }
    }
}
