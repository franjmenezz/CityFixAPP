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
            INCIDENCIA_ESTADO + " TEXT NOT NULL, " +
            "id_ciudadano INTEGER NOT NULL, " +
            "FOREIGN KEY (id_ciudadano) REFERENCES " + TABLA_CIUDADANO + "(" + CIUDADANO_ID + "));";


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
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_INCIDENCIAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CIUDADANO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_TECNICOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_ADMINISTRADOR);
        onCreate(db);
    }

    // Métodos para insertar datos en las tablas
    public void insertarAdministrador(SQLiteDatabase db, String admin_usuario, String admin_password) {
        try {
            ContentValues valores = new ContentValues();
            valores.put(ADMIN_USUARIO, DB_Encriptacion.encrypt(admin_usuario));
            valores.put(ADMIN_PASSWORD, DB_Encriptacion.encrypt(admin_password));
            db.insert(TABLA_ADMINISTRADOR, null, valores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertarCiudadano(SQLiteDatabase db, String nombre, String email, String password, String telefono, String usuario, String dni) {
        try {
            ContentValues valores = new ContentValues();
            valores.put(CIUDADANO_NOMBRE, nombre); // No se encripta
            valores.put(CIUDADANO_EMAIL, DB_Encriptacion.encrypt(email));
            valores.put(CIUDADANO_PASSWORD, DB_Encriptacion.encrypt(password));
            valores.put(CIUDADANO_TELEFONO, DB_Encriptacion.encrypt(telefono));
            valores.put(CIUDADANO_USUARIO, usuario); // No se encripta
            valores.put(CIUDADANO_DNI, DB_Encriptacion.encrypt(dni));
            db.insert(TABLA_CIUDADANO, null, valores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Método para insertar datos en la tabla tecnicos
    public void insertarTecnico(SQLiteDatabase db, String nombre, String sector, String clave, String password, String usuario) {
        try {
            ContentValues valores = new ContentValues();
            valores.put(TECNICO_NOMBRE, nombre); // No se encripta
            valores.put(TECNICO_SECTOR, DB_Encriptacion.encrypt(sector));
            valores.put(TECNICO_CLAVE, DB_Encriptacion.encrypt(clave));
            valores.put(TECNICO_PASSWORD, DB_Encriptacion.encrypt(password));
            valores.put(TECNICO_USUARIO, usuario); // No se encripta
            db.insert(TABLA_TECNICOS, null, valores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertarIncidencia(SQLiteDatabase db, String titulo, String descripcion, String estado, int idCiudadano) {
        ContentValues valores = new ContentValues();
        valores.put(INCIDENCIA_TITULO, titulo);
        valores.put(INCIDENCIA_DESCRIPCION, descripcion);
        valores.put(INCIDENCIA_ESTADO, estado);
        valores.put("id_ciudadano", idCiudadano);
        db.insert(TABLA_INCIDENCIAS, null, valores);
    }

    public Cursor seleccionarIncidenciasPorCiudadano(SQLiteDatabase db, int idCiudadano) {
        return db.rawQuery("SELECT * FROM " + TABLA_INCIDENCIAS + " WHERE id_ciudadano = ?", new String[]{String.valueOf(idCiudadano)});
    }

    // Métodos para seleccionar datos de las tablas
    public Cursor seleccionarAdministradores(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_ADMINISTRADOR, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    int usuarioIndex = cursor.getColumnIndex(ADMIN_USUARIO);
                    int passwordIndex = cursor.getColumnIndex(ADMIN_PASSWORD);

                    if (usuarioIndex >= 0 && passwordIndex >= 0) {
                        String usuario = DB_Encriptacion.decrypt(cursor.getString(usuarioIndex));
                        String password = DB_Encriptacion.decrypt(cursor.getString(passwordIndex));
                        // Usa los datos desencriptados según sea necesario
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        return cursor;
    }

    public Cursor seleccionarCiudadanos(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_CIUDADANO, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    String email = DB_Encriptacion.decrypt(cursor.getString(cursor.getColumnIndex(CIUDADANO_EMAIL)));
                    String password = DB_Encriptacion.decrypt(cursor.getString(cursor.getColumnIndex(CIUDADANO_PASSWORD)));
                    String telefono = DB_Encriptacion.decrypt(cursor.getString(cursor.getColumnIndex(CIUDADANO_TELEFONO)));
                    String dni = DB_Encriptacion.decrypt(cursor.getString(cursor.getColumnIndex(CIUDADANO_DNI)));
                    // Usa los datos desencriptados según sea necesario
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        return cursor;
    }

    public Cursor seleccionarIncidencias(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM " + TABLA_INCIDENCIAS, null);
    }

    // Método para seleccionar datos de la tabla tecnicos
    public Cursor seleccionarTecnicos(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_TECNICOS, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    String sector = DB_Encriptacion.decrypt(cursor.getString(cursor.getColumnIndex(TECNICO_SECTOR)));
                    String clave = DB_Encriptacion.decrypt(cursor.getString(cursor.getColumnIndex(TECNICO_CLAVE)));
                    String password = DB_Encriptacion.decrypt(cursor.getString(cursor.getColumnIndex(TECNICO_PASSWORD)));
                    // Usa los datos desencriptados según sea necesario
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        return cursor;
    }

    public boolean verificarCredenciales(String tabla, String usuarioColumna, String passwordColumna, String usuario, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + passwordColumna + " FROM " + tabla + " WHERE " + usuarioColumna + " = ?", new String[]{usuario});

        if (cursor.moveToFirst()) {
            try {
                String encryptedPassword = cursor.getString(0);
                String decryptedPassword = DB_Encriptacion.decrypt(encryptedPassword);

                // Compara la contraseña desencriptada con la ingresada
                if (decryptedPassword.equals(password)) {
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