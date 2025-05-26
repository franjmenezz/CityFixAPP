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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import java.util.List;

public class TecnicoIncidenciasAdapter extends RecyclerView.Adapter<TecnicoIncidenciasAdapter.ViewHolder> {

    private final Context context;
    private final List<Incidencia> listaIncidencias;
    private final DBConexion dbConexion;

    public TecnicoIncidenciasAdapter(Context context, List<Incidencia> lista) {
        this.context = context;
        this.listaIncidencias = lista;
        this.dbConexion = new DBConexion(context); // Inicializar aquí
    }

    @NonNull
    @Override
    public TecnicoIncidenciasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_incidencia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TecnicoIncidenciasAdapter.ViewHolder holder, int position) {
        Incidencia inc = listaIncidencias.get(position);
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

        // ✅ PASAR POSICIÓN AQUÍ
        holder.btnVerDetalles.setOnClickListener(v -> mostrarDialogoDetalles(inc, position));
    }


    @Override
    public int getItemCount() {
        return listaIncidencias.size();
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

    private void mostrarDialogoDetalles(Incidencia inc, int position) {
        View vista = LayoutInflater.from(context).inflate(R.layout.dialog_detalle_incidencia_tecnico, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(vista);
        AlertDialog dialog = builder.create();

        MapView mapView = vista.findViewById(R.id.mapaUbicacion);
        ImageView ivFoto = vista.findViewById(R.id.ivDetalleFoto);
        TextView tvTitulo = vista.findViewById(R.id.tvDetalleTitulo);
        TextView tvDescripcion = vista.findViewById(R.id.tvDetalleDescripcion);
        TextView tvFecha = vista.findViewById(R.id.tvDetalleFecha);
        TextView tvEstado = vista.findViewById(R.id.tvDetalleEstado);
        Button btnCambiarEstado = vista.findViewById(R.id.btnCambiarEstado);

        tvTitulo.setText(inc.titulo);
        tvDescripcion.setText(inc.descripcion);
        tvFecha.setText("Fecha: " + inc.fechaHora);
        tvEstado.setText("Estado: " + inc.estado);

        // Colorear el estado
        colorearEstado(tvEstado, inc.estado);

        byte[] fotoBytes = cargarFotoDesdeBD(inc.id);
        if (fotoBytes != null && fotoBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
            ivFoto.setImageBitmap(bitmap);
        } else {
            ivFoto.setImageResource(R.drawable.baseline_no_photography_24);
        }



        btnCambiarEstado.setOnClickListener(v -> {
            final String[] estados = {"Pendiente", "Completada", "Denegada"};
            AlertDialog.Builder estadoDialog = new AlertDialog.Builder(context);
            estadoDialog.setTitle("Selecciona nuevo estado");
            estadoDialog.setItems(estados, (dialogInterface, i) -> {
                String nuevoEstado = estados[i];
                boolean exito = dbConexion.actualizarEstadoIncidencia(inc.id, nuevoEstado);
                if (exito) {
                    inc.estado = nuevoEstado;
                    tvEstado.setText("Estado: " + nuevoEstado);
                    colorearEstado(tvEstado, nuevoEstado);
                    Toast.makeText(context, "Estado actualizado a " + nuevoEstado, Toast.LENGTH_SHORT).show();

                    // 🔄 REFRESCAR el ítem del RecyclerView
                    notifyItemChanged(position);
                } else {
                    Toast.makeText(context, "Error al actualizar estado", Toast.LENGTH_SHORT).show();
                }
            });
            estadoDialog.show();
        });

        mapView.onCreate(null);
        mapView.onResume(); // NECESARIO para que se muestre

        try {
            MapsInitializer.initialize(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(googleMap -> {
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            // EXTRAER coordenadas desde inc.ubicacion
            try {
                String[] partes = inc.ubicacion.replace("Lat: ", "").replace("Lng: ", "").split(", ");
                double lat = Double.parseDouble(partes[0]);
                double lng = Double.parseDouble(partes[1]);

                com.google.android.gms.maps.model.LatLng coordenadas = new com.google.android.gms.maps.model.LatLng(lat, lng);
                googleMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions().position(coordenadas).title("Ubicación de la incidencia"));
                googleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(coordenadas, 15));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });



        dialog.show();
    }

    private void colorearEstado(TextView tv, String estado) {
        switch (estado.toLowerCase()) {
            case "completada":
                tv.setTextColor(Color.parseColor("#4CAF50")); // verde
                break;
            case "pendiente":
                tv.setTextColor(Color.parseColor("#FFC107")); // amarillo
                break;
            case "denegada":
                tv.setTextColor(Color.parseColor("#F44336")); // rojo
                break;
            default:
                tv.setTextColor(Color.BLACK);
                break;
        }
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
