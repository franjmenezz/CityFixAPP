package com.example.cityfixapp;

import static org.junit.Assert.assertTrue;

import android.content.ContentValues;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.Modelo.Incidencia;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class PruebaActualizarEstadoIncidencia {

    private DBConexion dbConexion;
    private Context contexto;
    private int idTecnico;
    private int idIncidencia;

    @Before
    public void setup() {
        contexto = ApplicationProvider.getApplicationContext();
        dbConexion = new DBConexion(contexto);
        // Limpiar tablas
        dbConexion.getWritableDatabase().execSQL("DELETE FROM incidencias");
        dbConexion.getWritableDatabase().execSQL("DELETE FROM tecnicos");

        // Insertar técnico
        dbConexion.insertarTecnico("TecnicoEstado", "SectorEstado", "tecEstadoUsuario", "passEstado");
        idTecnico = dbConexion.obtenerIdTecnicoPorNombre("TecnicoEstado");

        // Insertar incidencia
        ContentValues valores = new ContentValues();
        valores.put("titulo", "Incidencia Estado");
        valores.put("descripcion", "Descripción Estado");
        valores.put("ubicacion", "Lat: 0, Lng: 0");
        valores.put("fecha_hora", "2025-06-10 13:00");
        valores.put("estado", "Pendiente");
        valores.put("id_ciudadano", 1);
        valores.put("id_tecnico", idTecnico);
        idIncidencia = (int) dbConexion.getWritableDatabase().insert("incidencias", null, valores);
    }

    @After
    public void tearDown() {
        dbConexion.close();
    }

    @Test
    public void testActualizarEstadoIncidencia() {
        // Actualizar estado
        boolean actualizado = dbConexion.actualizarEstadoIncidencia(idIncidencia, "Completada");
        assertTrue(actualizado);

        // Verificar en BD
        List<Incidencia> lista = dbConexion.obtenerIncidenciasPorTecnico(idTecnico);
        boolean encontrado = false;
        for (Incidencia inc : lista) {
            if (inc.id == idIncidencia && inc.estado.equals("Completada")) {
                encontrado = true;
                break;
            }
        }
        assertTrue(encontrado);
    }
}
