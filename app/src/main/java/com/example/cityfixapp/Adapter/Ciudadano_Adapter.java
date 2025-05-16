package com.example.cityfixapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.Modelo.Incidencia;
import com.example.cityfixapp.R;

import java.util.ArrayList;
import java.util.List;

public class Ciudadano_Adapter extends RecyclerView.Adapter<Ciudadano_Adapter.ViewHolder> {

    private List<Incidencia> listaOriginal;
    private List<Incidencia> listaFiltrada;

    private Context context;
    private DBConexion dbConexion;

    public Ciudadano_Adapter(Context context, List<Incidencia> lista) {
        this.context = context;
        this.dbConexion = new DBConexion(context);
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

        // Colorear el estado
        switch (inc.estado.toLowerCase()) {
            case "completada":
                holder.tvEstado.setTextColor(Color.parseColor("#4CAF50"));
                break;
            case "pendiente":
                holder.tvEstado.setTextColor(Color.parseColor("#FFC107"));
                break;
            case "denegada":
                holder.tvEstado.setTextColor(Color.parseColor("#F44336"));
                break;
            default:
                holder.tvEstado.setTextColor(Color.BLACK);
                break;
        }

        holder.btnVerDetalles.setOnClickListener(v -> mostrarDialogoDetalles(inc));
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
            String textoMin = texto.toLowerCase();
            for (Incidencia inc : listaOriginal) {
                if (inc.titulo.toLowerCase().contains(textoMin)) {
                    listaFiltrada.add(inc);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void mostrarDialogoDetalles(Incidencia inc) {
        View vista = LayoutInflater.from(context).inflate(R.layout.dialog_detalle_incidencia, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(vista);
        AlertDialog dialog = builder.create();

        TextView tvTitulo = vista.findViewById(R.id.tvDetalleTitulo);
        TextView tvDescripcion = vista.findViewById(R.id.tvDetalleDescripcion);
        TextView tvUbicacion = vista.findViewById(R.id.tvDetalleUbicacion);
        TextView tvFecha = vista.findViewById(R.id.tvDetalleFecha);
        TextView tvEstado = vista.findViewById(R.id.tvDetalleEstado);
        Button btnEliminar = vista.findViewById(R.id.btnEliminar);
        ImageView ivFoto = vista.findViewById(R.id.ivDetalleFoto);

        tvTitulo.setText("Título: " + inc.titulo);
        tvDescripcion.setText("Descripción: " + inc.descripcion);
        tvUbicacion.setText("Ubicación: " + inc.ubicacion);
        tvFecha.setText("Fecha: " + inc.fechaHora);
        tvEstado.setText("Estado: " + inc.estado);

        // ✅ Consulta adicional SOLO para la foto
        byte[] fotoBytes = cargarFotoDesdeBD(inc.id);
        if (fotoBytes != null && fotoBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
            ivFoto.setImageBitmap(bitmap);
        } else {
            ivFoto.setImageResource(R.drawable.baseline_no_photography_24);
        }

        btnEliminar.setOnClickListener(v -> {
            eliminarIncidenciaDeBD(inc.id);
            listaOriginal.remove(inc);
            listaFiltrada.remove(inc);
            notifyDataSetChanged();
            Toast.makeText(context, "Incidencia eliminada", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }


    private void eliminarIncidenciaDeBD(int id) {
        SQLiteDatabase db = dbConexion.getWritableDatabase();
        db.delete("incidencias", "_id = ?", new String[]{String.valueOf(id)});
    }

    private byte[] cargarFotoDesdeBD(int id) {
        SQLiteDatabase db = dbConexion.getReadableDatabase();
        byte[] foto = null;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT foto FROM incidencias WHERE _id = ? LIMIT 1",
                    new String[]{String.valueOf(id)}
            );

            if (cursor.moveToFirst()) {
                // Extrae en fragmentos si es posible
                foto = cursor.getBlob(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }

        return foto;
    }





}
