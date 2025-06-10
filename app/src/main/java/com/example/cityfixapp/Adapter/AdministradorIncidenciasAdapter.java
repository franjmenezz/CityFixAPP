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

import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.Modelo.Incidencia;

import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cityfixapp.R;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import java.util.ArrayList;
import java.util.List;

public class AdministradorIncidenciasAdapter extends RecyclerView.Adapter<AdministradorIncidenciasAdapter.ViewHolder> {
    private final Context context;

    private List<Incidencia> listaOriginal;
    private List<Incidencia> listaFiltrada;
    private DBConexion dbConexion;

    // Constructor
    public AdministradorIncidenciasAdapter(Context context, List<Incidencia> lista) {
        this.context = context;
        this.dbConexion = new DBConexion(context);

        this.listaOriginal = lista;
        this.listaFiltrada = new ArrayList<>(lista);

    }


    // Método para crear el ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_incidencia, parent, false);
        return new ViewHolder(view);
    }


    // Método para enlazar los datos al ViewHolder

    @Override
    public void onBindViewHolder(@NonNull AdministradorIncidenciasAdapter.ViewHolder holder, int position) {

        // Obtener la incidencia en la posición actual
        Incidencia inc = listaFiltrada.get(position);
        holder.tvTitulo.setText(inc.titulo);
        holder.tvEstado.setText("Estado: " + inc.estado);
        holder.tvFecha.setText("Fecha: " + inc.fechaHora);

        int tecnicoId = dbConexion.obtenerIdTecnicoAsignado(inc.id);

        // Mostrar el ID del técnico asignado o "Sin asignar" si no hay ninguno


        if (tecnicoId == -1) {
            holder.tvTecnico.setText("Técnico: Sin asignar");
        } else {
            holder.tvTecnico.setText("Técnico ID: " + tecnicoId);
        }


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


    // Método para obtener el número de elementos en la lista filtrada

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    // Clase ViewHolder para manejar las vistas de cada elemento de la lista

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvEstado, tvFecha, tvTecnico;
        Button btnVerDetalles;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvTecnico = itemView.findViewById(R.id.tvTecnico);
            btnVerDetalles = itemView.findViewById(R.id.btnVerDetalles);
        }
    }

    // Métodos para filtrar y ordenar la lista de incidencias
    /**
     * Filtra la lista de incidencias según el texto proporcionado.
     * Si el texto está vacío, muestra todas las incidencias.
     *
     */
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



    // Métodos para ordenar la lista de incidencias
    public void ordenarPorFecha() {
        Collections.sort(listaFiltrada, (a, b) -> b.fechaHora.compareTo(a.fechaHora));
        notifyDataSetChanged();
    }


    // Método para ordenar por nombre de incidencia de A a Z

    public void ordenarPorNombreAZ() {
        Collections.sort(listaFiltrada, Comparator.comparing(inc -> inc.titulo.toLowerCase()));
        notifyDataSetChanged();
    }

    // Método para ordenar por nombre de incidencia de Z a A
    public void ordenarPorNombreZA() {
        Collections.sort(listaFiltrada, (a, b) -> b.titulo.toLowerCase().compareTo(a.titulo.toLowerCase()));
        notifyDataSetChanged();
    }

    // Método para mostrar el diálogo de detalles de una incidencia
    private void mostrarDialogoDetalles(Incidencia inc) {
        View vista = LayoutInflater.from(context).inflate(R.layout.dialog_detalle_incidencia_administrador, null);
        // Crear el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(vista);
        AlertDialog dialog = builder.create();

        // Referencias a los elementos del diálogo

        TextView tvTitulo = vista.findViewById(R.id.tvDetalleTitulo);
        TextView tvDescripcion = vista.findViewById(R.id.tvDetalleDescripcion);
        MapView mapView = vista.findViewById(R.id.mapaUbicacion);
        TextView tvFecha = vista.findViewById(R.id.tvDetalleFecha);
        TextView tvEstado = vista.findViewById(R.id.tvDetalleEstado);
        Button btnEliminar = vista.findViewById(R.id.btnEliminar);
        Button btnAsignarTecnico = vista.findViewById(R.id.btnAsignarTecnico);
        TextView tvTecnico = vista.findViewById(R.id.tvDetalleTecnico);
        ImageView ivFoto = vista.findViewById(R.id.ivDetalleFoto);

        // Asignar los valores a los elementos del diálogo
        tvTitulo.setText(inc.titulo);
        tvDescripcion.setText(inc.descripcion);
        tvFecha.setText("Fecha: " + inc.fechaHora);
        tvEstado.setText("Estado: " + inc.estado);

        // Obtener el id del técnico asignado a la incidencia
        int tecnicoId = dbConexion.obtenerIdTecnicoAsignado(inc.id);

        // Mostrar el ID del técnico asignado o "Sin asignar" si no hay ninguno

        if (tecnicoId == -1) {
            tvTecnico.setText("Técnico: Sin asignar");
        } else {
            tvTecnico.setText("Técnico ID: " + tecnicoId);
        }

        // Cargar la foto de la incidencia desde la base de datos
        byte[] fotoBytes = cargarFotoDesdeBD(inc.id);

        // Si la foto existe, mostrarla; si no, mostrar un icono por defecto
        if (fotoBytes != null && fotoBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
            ivFoto.setImageBitmap(bitmap);
        } else {
            ivFoto.setImageResource(R.drawable.baseline_no_photography_24);
        }

        // Configurar los botones del diálogo
        btnEliminar.setOnClickListener(v -> {
            eliminarIncidenciaDeBD(inc.id);
            listaOriginal.remove(inc);
            listaFiltrada.remove(inc);
            notifyDataSetChanged();
            Toast.makeText(context, "Incidencia eliminada", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnAsignarTecnico.setOnClickListener(v -> {
            mostrarDialogoSeleccionarTecnico(inc, dialog, () -> {
                listaOriginal.clear();
                listaOriginal.addAll(dbConexion.obtenerTodasLasIncidencias());
                listaFiltrada.clear();
                listaFiltrada.addAll(listaOriginal);
                notifyDataSetChanged();
            });
        });


        mapView.onCreate(null);
        mapView.onResume(); // NECESARIO para que se muestre

        // Inicializar el MapView

        try {
            MapsInitializer.initialize(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Configurar el MapView para mostrar la ubicación de la incidencia

        mapView.getMapAsync(googleMap -> {
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            // Extraer las coordenadas de la ubicación de la incidencia
            // y agregar un marcador en el mapa
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

    // Método para eliminar una incidencia de la base de datos
    private void eliminarIncidenciaDeBD(int id) {
        SQLiteDatabase db = dbConexion.getWritableDatabase();
        db.delete("incidencias", "_id = ?", new String[]{String.valueOf(id)});
    }

    // Método para cargar la foto de una incidencia desde la base de datos
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

    // Método para mostrar un diálogo para seleccionar un técnico y asignarlo a una incidencia

    private void mostrarDialogoSeleccionarTecnico(Incidencia inc, AlertDialog dialogDetalles, Runnable onTecnicoAsignado) {
        List<String> nombresTecnicos = dbConexion.obtenerNombresTecnicos();
        final String[] tecnicosArray = nombresTecnicos.toArray(new String[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Selecciona un técnico");

        // Configurar el diálogo para mostrar los técnicos disponibles
        builder.setSingleChoiceItems(tecnicosArray, -1, (dialog, which) -> {
            int tecnicoId = dbConexion.obtenerIdTecnicoPorNombre(tecnicosArray[which]);
            dbConexion.asignarTecnicoAIncidencia(inc.id, tecnicoId);

            Toast.makeText(context, "Técnico asignado correctamente", Toast.LENGTH_SHORT).show();

            dialog.dismiss();
            dialogDetalles.dismiss();

            if (onTecnicoAsignado != null) {
                onTecnicoAsignado.run();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }



}
