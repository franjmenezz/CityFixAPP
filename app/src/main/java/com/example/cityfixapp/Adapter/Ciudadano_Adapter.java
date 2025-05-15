package com.example.cityfixapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityfixapp.Modelo.Incidencia;
import com.example.cityfixapp.R;

import java.util.ArrayList;
import java.util.List;

public class Ciudadano_Adapter extends RecyclerView.Adapter<Ciudadano_Adapter.ViewHolder> {

    private List<Incidencia> listaOriginal;
    private List<Incidencia> listaFiltrada;

    public Ciudadano_Adapter(List<Incidencia> lista) {
        this.listaOriginal = lista;
        this.listaFiltrada = new ArrayList<>(lista);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incidencia, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Incidencia inc = listaFiltrada.get(position);
        holder.tvTitulo.setText(inc.titulo);
        holder.tvEstado.setText("Estado: " + inc.estado);
        holder.tvFecha.setText("Fecha: " + inc.fechaHora);
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvEstado, tvFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvFecha = itemView.findViewById(R.id.tvFecha);
        }
    }

    public void filtrar(String texto) {
        listaFiltrada.clear();
        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaOriginal);
        } else {
            String textoMin = texto.toLowerCase();
            for (Incidencia inc : listaOriginal) {
                if (inc.titulo.toLowerCase().contains(textoMin)) {
                    listaFiltrada.add(inc);
                }
            }
        }
        notifyDataSetChanged();
    }
}
