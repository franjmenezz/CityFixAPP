package com.example.cityfixapp;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;


import android.content.Context;
import android.content.Intent;


import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.cityfixapp.Activity.ActivityTecnico;

import com.example.cityfixapp.DB.DBConexion;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
@RunWith(AndroidJUnit4.class)
public class PruebaLoginYNavegacion {

    private DBConexion dbConexion;
    private Context contexto;

    @Before
    public void setup() {
        contexto = ApplicationProvider.getApplicationContext();
        dbConexion = new DBConexion(contexto);
        // Insertar técnico para login
        dbConexion.insertarTecnico("TecnicoLogin", "SectorLogin", "tecLoginUsuario", "contrasenaLogin");
    }

    @After
    public void tearDown() {
        dbConexion.close();
    }

    @Test
    public void testLoginYNavigation() {
        // Verificar credenciales correctas
        boolean valido = dbConexion.verificarCredenciales("tecnicos", "usuario", "password", "tecLoginUsuario", "contrasenaLogin");
        assertTrue(valido);

        // Obtener id técnico
        int idTecnico = dbConexion.obtenerIdTecnicoPorNombre("TecnicoLogin");
        assertTrue(idTecnico > 0);

        // Simular Intent y paso de datos
        Intent intent = new Intent(contexto, ActivityTecnico.class);
        intent.putExtra("id_tecnico", idTecnico);

        assertEquals(idTecnico, intent.getIntExtra("id_tecnico", -1));
    }
}

