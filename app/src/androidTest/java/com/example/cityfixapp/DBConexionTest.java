package com.example.cityfixapp;

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

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DBConexionTest {

    // Instancia de la clase DBConexion para pruebas
    private DBConexion dbConexion;
    // Configuración inicial antes de cada prueba
    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbConexion = new DBConexion(context);

        // Limpiar tablas para pruebas limpias
        dbConexion.getWritableDatabase().execSQL("DELETE FROM administrador");
        dbConexion.getWritableDatabase().execSQL("DELETE FROM tecnicos");
        dbConexion.getWritableDatabase().execSQL("DELETE FROM incidencias");
    }

    // Cierre de la conexión a la base de datos después de cada prueba
    @After
    public void tearDown() {
        dbConexion.close();
    }

    // Test de inserción y obtención de usuarios
    @Test
    public void testInsertarYObtenerAdministrador() {
        boolean inserted = dbConexion.insertarAdministrador("adminTest", "12345");
        assertTrue(inserted);

        List<String> admins = dbConexion.obtenerUsuariosAdministradores();
        assertTrue(admins.contains("adminTest"));
    }

    // Test de inserción y obtención de técnicos
    @Test
    public void testInsertarYObtenerTecnico() {
        boolean inserted = dbConexion.insertarTecnico("Técnico1", "Sector1", "tecnico1", "pass123");
        assertTrue(inserted);

        List<String> tecnicos = dbConexion.obtenerUsuariosTecnicos();
        assertTrue(tecnicos.contains("tecnico1"));
    }

    // Test de inserción y obtención de incidencias
    @Test
    public void testInsertarYActualizarIncidencia() {
        Incidencia incidencia = new Incidencia(0, "Test Incidencia", "Descripción prueba", "Ubicación prueba", "pendiente", "2025-06-10", null);
        incidencia.idCiudadano = 1;  // Ajusta según tu modelo
        incidencia.idTecnico = 1;

        boolean inserted = dbConexion.insertarIncidencia(incidencia);
        assertTrue(inserted);

        List<Incidencia> incidencias = dbConexion.obtenerTodasLasIncidencias();
        assertFalse(incidencias.isEmpty());

        int idIncidencia = incidencias.get(0).id;
        boolean updated = dbConexion.actualizarEstadoIncidencia(idIncidencia, "completada");
        assertTrue(updated);

        List<Incidencia> incidenciasActualizadas = dbConexion.obtenerTodasLasIncidencias();
        assertEquals("completada", incidenciasActualizadas.get(0).estado);
    }
}
