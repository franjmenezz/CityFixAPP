package com.example.cityfixapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityfixapp.R;

import java.util.List;

public class Incidencia_Adapter extends RecyclerView.Adapter<Incidencia_Adapter.ViewHolder> {

    private final List<String> listaIncidencias;

    public Incidencia_Adapter(List<String> listaIncidencias) {
        this.listaIncidencias = listaIncidencias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_incidencia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String incidencia = listaIncidencias.get(position);
        holder.tvIncidencia.setText(incidencia);
    }

    @Override
    public int getItemCount() {
        return listaIncidencias.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvIncidencia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIncidencia = itemView.findViewById(R.id.tvIncidencia);
        }
    }
}