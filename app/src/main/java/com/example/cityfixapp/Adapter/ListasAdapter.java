package com.example.cityfixapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListasAdapter extends RecyclerView.Adapter<ListasAdapter.ViewHolder> {

    private final List<String> items;

    public ListasAdapter(List<String> items) {
        this.items = items;
    }

    // Método para refrescar el RecyclerView
    @NonNull
    @Override
    public ListasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    // Método para enlazar los datos a cada ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ListasAdapter.ViewHolder holder, int position) {
        holder.textView.setText(items.get(position));
    }

    // Método para filtrar los datos
    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}
