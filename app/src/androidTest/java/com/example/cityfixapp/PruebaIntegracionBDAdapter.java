package com.example.cityfixapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.cityfixapp.Adapter.TecnicoIncidenciasAdapter;
import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.Modelo.Incidencia;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class PruebaIntegracionBDAdapter {

    private DBConexion dbConexion;
    private Context contexto;

    @Before
    public void setup() {
        contexto = ApplicationProvider.getApplicationContext();
        dbConexion = new DBConexion(contexto);
        // Limpiar tablas para empezar limpio
        dbConexion.getWritableDatabase().execSQL("DELETE FROM administrador");
        dbConexion.getWritableDatabase().execSQL("DELETE FROM tecnicos");
        dbConexion.getWritableDatabase().execSQL("DELETE FROM incidencias");
    }

    @After
    public void tearDown() {
        dbConexion.close();
    }

    @Test
    public void testInsertarIncidenciaYVerEnAdaptador() {
        // Insertar técnico
        boolean tecnicoInsertado = dbConexion.insertarTecnico("Tecnico1", "Sector1", "tec1usuario", "pass123");
        assertTrue(tecnicoInsertado);

        // Obtener id del técnico insertado
        List<String> tecnicos = dbConexion.obtenerUsuariosTecnicos();
        assertTrue(tecnicos.contains("tec1usuario"));
        int idTecnico = dbConexion.obtenerIdTecnicoPorNombre("Tecnico1");

        // Insertar incidencia asignada a técnico
        SQLiteDatabase db = dbConexion.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("titulo", "Incidencia de Prueba");
        valores.put("descripcion", "Descripción de prueba");
        valores.put("ubicacion", "Lat: 10, Lng: 10");
        valores.put("fecha_hora", "2025-06-10 12:00");
        valores.put("estado", "Pendiente");
        valores.put("id_ciudadano", 1);
        valores.put("id_tecnico", idTecnico);
        long idIncidencia = db.insert("incidencias", null, valores);
        assertTrue(idIncidencia != -1);

        // Cargar incidencias asignadas a técnico
        List<Incidencia> incidencias = dbConexion.obtenerIncidenciasPorTecnico(idTecnico);
        assertFalse(incidencias.isEmpty());
        assertEquals("Incidencia de Prueba", incidencias.get(0).titulo);

        // Crear adaptador con incidencias
        TecnicoIncidenciasAdapter adaptador = new TecnicoIncidenciasAdapter(contexto, incidencias);
        assertEquals(1, adaptador.getItemCount());
    }
}
