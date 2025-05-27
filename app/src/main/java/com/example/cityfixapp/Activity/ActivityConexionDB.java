package com.example.cityfixapp.Activity;

import android.app.Application;
import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.Helper.AdminHelper;

public class ActivityConexionDB extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Inicializar la base de datos
        DBConexion dbConexion = new DBConexion(this);
        dbConexion.getWritableDatabase(); // Esto asegura que la base de datos se cree si no existe
        AdminHelper.insertarAdminPorDefecto(this);
    }
}