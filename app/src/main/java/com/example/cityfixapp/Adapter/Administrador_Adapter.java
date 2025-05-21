package com.example.cityfixapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cityfixapp.Modelo.Incidencia;

import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cityfixapp.R;

import java.util.ArrayList;
import java.util.List;

public class Administrador_Adapter extends RecyclerView.Adapter<Administrador_Adapter.ViewHolder> {
    private final Context context;
    private List<Incidencia> listaOriginal;
    private List<Incidencia> listaFiltrada;

    public Administrador_Adapter(Context context, List<Incidencia> lista) {
        this.context = context;
        this.listaOriginal = lista;
        this.listaFiltrada = new ArrayList<>(lista);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_incidencia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Incidencia inc = listaFiltrada.get(position);

        holder.tvTitulo.setText(inc.titulo);
        holder.tvEstado.setText("Estado: " + inc.estado);
        holder.tvFecha.setText("Fecha: " + inc.fechaHora);

        // Cambiar el color del texto del estado según su valor
        switch (inc.estado.toLowerCase()) {
            case "completada":
                holder.tvEstado.setTextColor(Color.parseColor("#4CAF50")); // Verde
                break;
            case "pendiente":
                holder.tvEstado.setTextColor(Color.parseColor("#FFC107")); // Amarillo
                break;
            case "denegada":
                holder.tvEstado.setTextColor(Color.parseColor("#F44336")); // Rojo
                break;
            default:
                holder.tvEstado.setTextColor(Color.BLACK); // Negro por defecto
                break;
        }

        // El resto de la configuración del ViewHolder...
    }


    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvEstado, tvFecha;
        Button btnVerDetalles;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            btnVerDetalles = itemView.findViewById(R.id.btnVerDetalles);
        }
    }

    public void filtrar(String texto) {
        listaFiltrada.clear();
        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaOriginal);
        } else {
            String textoLower = texto.toLowerCase();
            for (Incidencia inc : listaOriginal) {
                if (inc.titulo.toLowerCase().contains(textoLower)) {
                    listaFiltrada.add(inc);
                }
            }
        }
        notifyDataSetChanged();
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

    public void ordenarPorFecha() {
        Collections.sort(listaFiltrada, (a, b) -> b.fechaHora.compareTo(a.fechaHora));
        notifyDataSetChanged();
    }

    public void ordenarPorNombreAZ() {
        Collections.sort(listaFiltrada, Comparator.comparing(inc -> inc.titulo.toLowerCase()));
        notifyDataSetChanged();
    }

    public void ordenarPorNombreZA() {
        Collections.sort(listaFiltrada, (a, b) -> b.titulo.toLowerCase().compareTo(a.titulo.toLowerCase()));
        notifyDataSetChanged();
    }
}
