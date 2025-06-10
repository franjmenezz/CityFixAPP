package com.example.cityfixapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.DB.DBEncriptacion;

public class AdminHelper {

    // Método para insertar un administrador por defecto en la base de datos
    public static void insertarAdminPorDefecto(Context context) {
        DBConexion dbConexion = new DBConexion(context);
        SQLiteDatabase db = dbConexion.getWritableDatabase();

        try { // Verificar si la tabla 'administrador' ya tiene un registro
            ContentValues valores = new ContentValues();
            valores.put("admin_usuario", "admin");
            valores.put("admin_password", DBEncriptacion.encrypt("1234"));

            // Intentar insertar el administrador por defecto
            db.insert("administrador", null, valores);
            Log.d("AdminHelper", "Administrador insertado correctamente");
        } catch (Exception e) {
            Log.e("AdminHelper", "Error al insertar admin: " + e.getMessage());
        } finally {
            db.close();
        }
    }
}
