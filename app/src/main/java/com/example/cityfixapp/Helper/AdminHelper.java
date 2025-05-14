package com.example.cityfixapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cityfixapp.DB.DBConexion;
import com.example.cityfixapp.DB.DB_Encriptacion;

public class AdminHelper {

    public static void insertarAdminPorDefecto(Context context) {
        DBConexion dbConexion = new DBConexion(context);
        SQLiteDatabase db = dbConexion.getWritableDatabase();

        try {
            ContentValues valores = new ContentValues();
            valores.put("admin_usuario", "admin");
            valores.put("admin_password", DB_Encriptacion.encrypt("1234"));

            db.insert("administrador", null, valores);
            Log.d("AdminHelper", "Administrador insertado correctamente");
        } catch (Exception e) {
            Log.e("AdminHelper", "Error al insertar admin: " + e.getMessage());
        } finally {
            db.close();
        }
    }
}
