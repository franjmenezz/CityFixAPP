package com.example.cityfixapp;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.Modelo.Incidencia;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class PruebasDeSistema {

    private DBConexion dbConexion;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbConexion = new DBConexion(context);
    }

    @Test
    public void testCiudadanoReportaIncidenciaYAdministradorLaVe() {
        Incidencia incidencia = new Incidencia(
                0,
                "Tubería rota",
                "Fuga en la calle",
                "Lat: 10.0, Lng: 20.0",
                "pendiente",
                "2024-01-01 00:00:00",
                null
        );
        incidencia.idCiudadano = 1;
        incidencia.idTecnico = null;


        boolean insertado = dbConexion.insertarIncidencia(incidencia);


        assertTrue(insertado);

        List<Incidencia> lista = dbConexion.obtenerTodasLasIncidencias();
        assertFalse(lista.isEmpty());

        Incidencia nueva = lista.get(0);
        assertEquals("Tubería rota", nueva.titulo);
        assertEquals("pendiente", nueva.estado);
    }

    @Test
    public void testAdministradorAsignaTecnicoYTecnicoVeIncidencia() {
        dbConexion.insertarTecnico("Pedro", "Fontanería", "pedro", "clave123");
        int idTecnico = dbConexion.obtenerIdTecnicoPorNombre("Pedro");

        Incidencia incidencia = new Incidencia(
                0,
                "Avería luz",
                "Apagón total",
                "Lat: 1, Lng: 2",
                "pendiente",
                "2024-01-01 00:00:00",
                null
        );
        incidencia.idCiudadano = 1;

        dbConexion.insertarIncidencia(incidencia);

        int idIncidencia = dbConexion.obtenerTodasLasIncidencias().get(0).id;

        dbConexion.asignarTecnicoAIncidencia(idIncidencia, idTecnico);

        List<Incidencia> tecnicosIncidencias = dbConexion.obtenerIncidenciasPorTecnico(idTecnico);
        assertFalse(tecnicosIncidencias.isEmpty());
        assertEquals("Avería luz", tecnicosIncidencias.get(0).titulo);
    }

    @Test
    public void testTecnicoActualizaEstadoDeIncidencia() {
        Incidencia incidencia = new Incidencia(
                0,
                "Semáforo roto",
                "No cambia de color",
                "Lat: 5.0, Lng: 6.0",
                "pendiente",
                "2024-01-01 00:00:00", // Usa una fecha dummy válida para la prueba
                null
        );
        incidencia.idCiudadano = 1;
        incidencia.idTecnico = null;

        dbConexion.insertarIncidencia(incidencia);

        int idIncidencia = dbConexion.obtenerTodasLasIncidencias().get(0).id;

        boolean actualizado = dbConexion.actualizarEstadoIncidencia(idIncidencia, "completada");
        assertTrue(actualizado);

        List<Incidencia> lista = dbConexion.obtenerTodasLasIncidencias();
        assertEquals("completada", lista.get(0).estado);
    }


}
