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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Administrador_Adapter extends RecyclerView.Adapter<Administrador_Adapter.ViewHolder> {
    private final List<String> nameList;
    private final OnItemClickListener listener;
    private final List<Boolean> subMenuVisibilityList;

    public Administrador_Adapter(List<String> nameList, OnItemClickListener listener) {
        this.nameList = nameList;
        this.listener = listener;
        this.subMenuVisibilityList = new ArrayList<>(Collections.nCopies(nameList.size(), false));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_funciones, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String itemName = nameList.get(position);
        holder.textView.setText(itemName);

        // Controlar la visibilidad de los submenús
        holder.menuIncidencias.setVisibility(View.GONE);
        holder.menuAdministradores.setVisibility(View.GONE);
        holder.menuTecnicos.setVisibility(View.GONE);
        holder.menuCiudadanos.setVisibility(View.GONE);

        if (subMenuVisibilityList.get(position)) {
            switch (itemName) {
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
                default:
                    break;
            }
        }

        // Alternar visibilidad al hacer clic en el título
        holder.textView.setOnClickListener(v -> {
            boolean isVisible = subMenuVisibilityList.get(position);
            subMenuVisibilityList.set(position, !isVisible);
            notifyItemChanged(position);
        });

        // Configurar los botones del submenú
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
        return nameList.size();
    }

    public interface OnItemClickListener {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ConstraintLayout menuIncidencias;
        private final ConstraintLayout menuAdministradores;
        private final ConstraintLayout menuTecnicos;
        private final ConstraintLayout menuCiudadanos;
        private final Button btMostrarIncidencias;
        private final Button btNuevoAdministrador;
        private final Button btModificarAdministrador;
        private final Button btMostrarAdministradores;
        private final Button btNuevoTecnico;
        private final Button btModificarTecnico;
        private final Button btMostrarTecnicos;
        private final Button btModificarCiudadano;
        private final Button btMostrarCiudadanos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_funcion);
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