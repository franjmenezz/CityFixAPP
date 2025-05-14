package com.example.cityfixapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityfixapp.R;

import java.util.ArrayList;
import java.util.List;

public class Ciudadano_Adapter extends RecyclerView.Adapter<Ciudadano_Adapter.ViewHolder> {

    private final List<String> listaOriginal;
    private List<String> listaFiltrada;

    public Ciudadano_Adapter(List<String> incidencias) {
        this.listaOriginal = new ArrayList<>(incidencias);
        this.listaFiltrada = incidencias;
    }

    @NonNull
    @Override
    public Ciudadano_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_incidencia, parent, false);
        return new Ciudadano_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Ciudadano_Adapter.ViewHolder holder, int position) {
        holder.tvIncidencia.setText(listaFiltrada.get(position));
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    public void filtrar(String texto) {
        if (texto.isEmpty()) {
            listaFiltrada = new ArrayList<>(listaOriginal);
        } else {
            List<String> filtrada = new ArrayList<>();
            for (String item : listaOriginal) {
                if (item.toLowerCase().contains(texto.toLowerCase())) {
                    filtrada.add(item);
                }
            }
            listaFiltrada = filtrada;
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIncidencia;

        ViewHolder(View itemView) {
            super(itemView);
            tvIncidencia = itemView.findViewById(R.id.tvIncidencia);
        }
    }
}