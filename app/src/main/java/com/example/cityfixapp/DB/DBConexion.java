package com.example.cityfixapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DBConexion extends SQLiteOpenHelper {
    private static final String DB_NAME = "CityFixDB";
    private static final int DB_VERSION = 1;

    // Tabla administrador
    private static final String TABLA_ADMINISTRADOR = "administrador";
    private static final String ADMIN_ID = "_id";
    private static final String ADMIN_USUARIO = "admin_usuario";
    private static final String ADMIN_PASSWORD = "admin_password";

    // Tabla ciudadano
    private static final String TABLA_CIUDADANO = "ciudadano";
    private static final String CIUDADANO_ID = "_id";
    private static final String CIUDADANO_NOMBRE = "nombre";
    private static final String CIUDADANO_EMAIL = "email";
    private static final String CIUDADANO_PASSWORD = "password";
    private static final String CIUDADANO_TELEFONO = "telefono";
    private static final String CIUDADANO_USUARIO = "usuario";
    private static final String CIUDADANO_DNI = "dni";


    // Tabla tecnicos
    private static final String TABLA_TECNICOS = "tecnicos";
    private static final String TECNICO_ID = "_id";
    private static final String TECNICO_NOMBRE = "nombre";
    private static final String TECNICO_SECTOR = "sector";
    private static final String TECNICO_CLAVE = "clave";
    private static final String TECNICO_PASSWORD = "password";
    private static final String TECNICO_USUARIO = "usuario";



    // Tabla incidencias
    private static final String TABLA_INCIDENCIAS = "incidencias";
    private static final String INCIDENCIA_ID = "_id";
    private static final String INCIDENCIA_TITULO = "titulo";
    private static final String INCIDENCIA_DESCRIPCION = "descripcion";
    private static final String INCIDENCIA_ESTADO = "estado";

    // Sentencias SQL para la creación de tablas
    private static final String CREAR_TABLA_ADMINISTRADOR = "CREATE TABLE " + TABLA_ADMINISTRADOR +
            " (" + ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ADMIN_USUARIO + " TEXT NOT NULL, " +
            ADMIN_PASSWORD + " TEXT NOT NULL);";

    private static final String CREAR_TABLA_CIUDADANO = "CREATE TABLE " + TABLA_CIUDADANO +
            " (" + CIUDADANO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CIUDADANO_NOMBRE + " TEXT NOT NULL, " +
            CIUDADANO_EMAIL + " TEXT NOT NULL, " +
            CIUDADANO_PASSWORD + " TEXT NOT NULL, " +
            CIUDADANO_TELEFONO + " TEXT NOT NULL, " +
            CIUDADANO_USUARIO + " TEXT NOT NULL, " +
            CIUDADANO_DNI + " TEXT NOT NULL);";
    private static final String CREAR_TABLA_INCIDENCIAS = "CREATE TABLE " + TABLA_INCIDENCIAS +
            " (" + INCIDENCIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            INCIDENCIA_TITULO + " TEXT NOT NULL, " +
            INCIDENCIA_DESCRIPCION + " TEXT NOT NULL, " +
            INCIDENCIA_ESTADO + " TEXT NOT NULL);";


    // Sentencia SQL para la creación de la tabla tecnicos
    private static final String CREAR_TABLA_TECNICOS = "CREATE TABLE " + TABLA_TECNICOS +
            " (" + TECNICO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TECNICO_NOMBRE + " TEXT NOT NULL, " +
            TECNICO_SECTOR + " TEXT NOT NULL, " +
            TECNICO_CLAVE + " TEXT NOT NULL, " +
            TECNICO_PASSWORD + " TEXT NOT NULL, " +
            TECNICO_USUARIO + " TEXT NOT NULL);";

    public DBConexion(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_ADMINISTRADOR);
        db.execSQL(CREAR_TABLA_CIUDADANO);
        db.execSQL(CREAR_TABLA_INCIDENCIAS);
        db.execSQL(CREAR_TABLA_TECNICOS);

        // Insertar un administrador predefinido
        ContentValues valores = new ContentValues();
        valores.put(ADMIN_USUARIO, "admin"); // Usuario predefinido
        valores.put(ADMIN_PASSWORD, "1234"); // Contraseña predefinida
        db.insert(TABLA_ADMINISTRADOR, null, valores);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_ADMINISTRADOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CIUDADANO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_INCIDENCIAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_TECNICOS); // Nueva tabla
        onCreate(db);
    }

    // Métodos para insertar datos en las tablas


    public boolean verificarCredenciales(String tabla, String usuarioColumna, String passwordColumna, String usuario, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + passwordColumna + " FROM " + tabla + " WHERE " + usuarioColumna + " = ?",
                new String[]{usuario});

        if (cursor.moveToFirst()) {
            String passwordCifrada = cursor.getString(0);
            try {
                String passwordDesencriptada = DB_Encriptacion.decrypt(passwordCifrada);
                if (passwordDesencriptada.equals(password)) {
                    cursor.close();
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return false;
    }

}