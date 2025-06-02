package com.example.cityfixapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.cityfixapp.Modelo.Incidencia;

import java.util.ArrayList;
import java.util.List;

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
    private static final String CREAR_TABLA_INCIDENCIAS = "CREATE TABLE incidencias (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "titulo TEXT NOT NULL, " +
            "descripcion TEXT NOT NULL, " +
            "ubicacion TEXT, " +
            "foto BLOB, " +
            "fecha_hora TEXT NOT NULL, " +
            "estado TEXT NOT NULL, " +
            "id_ciudadano INTEGER NOT NULL, " +
            "id_tecnico INTEGER NULL);";


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
        try {
            valores.put(ADMIN_USUARIO, "admin");
            valores.put(ADMIN_PASSWORD, DBEncriptacion.encrypt("1234")); // Ahora cifrado
            db.insert(TABLA_ADMINISTRADOR, null, valores);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Contraseña predefinida
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
                String passwordDesencriptada = DBEncriptacion.decrypt(passwordCifrada);
                Log.d("LOGIN_DEBUG", "Desencriptada: " + passwordDesencriptada + " vs ingresada: " + password);
                if (passwordDesencriptada.equals(password)) {
                    cursor.close();
                    return true;
                }
            } catch (Exception e) {
                Log.e("LOGIN_ERROR", "Error desencriptando: " + e.getMessage());
            }
        }

        cursor.close();
        return false;
    }
    public int obtenerIdCiudadano(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM ciudadano WHERE usuario = ?", new String[]{usuario});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }
    public List<Incidencia> obtenerTodasLasIncidencias() {
        List<Incidencia> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT _id, titulo, descripcion, ubicacion, estado, fecha_hora, foto FROM incidencias ORDER BY _id DESC",
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String titulo = cursor.getString(1);
            String descripcion = cursor.getString(2);
            String ubicacion = cursor.getString(3);
            String estado = cursor.getString(4);
            String fechaHora = cursor.getString(5);
            byte[] foto = cursor.getBlob(6);

            lista.add(new Incidencia(id, titulo, descripcion, ubicacion, estado, fechaHora, foto));
        }

        cursor.close();
        return lista;
    }

    public void asignarTecnicoAIncidencia(int incidenciaId, int tecnicoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("id_tecnico", tecnicoId);
        db.update("incidencias", valores, "_id = ?", new String[]{String.valueOf(incidenciaId)});
    }

    public List<String> obtenerNombresTecnicos() {
        List<String> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM tecnicos ORDER BY nombre", null);
        while(cursor.moveToNext()) {
            lista.add(cursor.getString(0));
        }
        cursor.close();
        return lista;
    }

    public int obtenerIdTecnicoPorNombre(String nombre) {
        int id = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM tecnicos WHERE nombre = ?", new String[]{nombre});
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    public int obtenerIdTecnicoAsignado(int incidenciaId) {
        int tecnicoId = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id_tecnico FROM incidencias WHERE _id = ?", new String[]{String.valueOf(incidenciaId)});
        if (cursor.moveToFirst()) {
            tecnicoId = cursor.isNull(0) ? -1 : cursor.getInt(0);
        }
        cursor.close();
        return tecnicoId;
    }


    public boolean insertarAdministrador(String usuario, String password) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("admin_usuario", usuario);
            valores.put("admin_password", DBEncriptacion.encrypt(password));
            long id = db.insert("administrador", null, valores);
            return id != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // Método para obtener lista de usuarios administradores
    public List<String> obtenerUsuariosAdministradores() {
        List<String> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT admin_usuario FROM administrador ORDER BY admin_usuario", null);
        while (cursor.moveToNext()) {
            lista.add(cursor.getString(0));
        }
        cursor.close();
        return lista;
    }



    // Método para modificar un administrador
    public boolean modificarAdministrador(String usuarioOriginal, String nuevoUsuario, String nuevaPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();

        if (!nuevoUsuario.isEmpty()) {
            valores.put(ADMIN_USUARIO, nuevoUsuario);
        }
        if (!nuevaPassword.isEmpty()) {
            try {
                valores.put(ADMIN_PASSWORD, DBEncriptacion.encrypt(nuevaPassword));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        if (valores.size() == 0) return false; // No hay cambios

        int rows = db.update(TABLA_ADMINISTRADOR, valores, ADMIN_USUARIO + " = ?", new String[]{usuarioOriginal});
        return rows > 0;
    }


    // Inserta un técnico nuevo
    public boolean insertarTecnico(String nombre, String sector, String usuario, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("sector", sector);
        valores.put("usuario", usuario);
        try {
            valores.put("password", DBEncriptacion.encrypt(password));
        } catch (Exception e) {
            e.printStackTrace();
            return false; // si hay error al encriptar, no insertar
        }
        valores.put("clave", "defaultClave"); // o valor válido
        long resultado = db.insert("tecnicos", null, valores);
        return resultado != -1;
    }



    // Modifica un técnico existente (buscando por usuario actual)
    public boolean modificarTecnico(String usuarioActual, String nuevoNombre, String nuevoSector, String nuevoUsuario, String nuevaPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        if (nuevoNombre != null) valores.put("nombre", nuevoNombre);
        if (nuevoSector != null) valores.put("sector", nuevoSector);
        if (nuevoUsuario != null) valores.put("usuario", nuevoUsuario);
        try {
            valores.put("password", DBEncriptacion.encrypt(nuevaPassword));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        int filas = db.update(TABLA_TECNICOS, valores, "usuario = ?", new String[]{usuarioActual});
        return filas > 0;
    }

    // Obtiene una lista de nombres de usuarios técnicos (para spinners o listados)
    public List<String> obtenerUsuariosTecnicos() {
        List<String> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT usuario FROM tecnicos ORDER BY usuario", null);
        while(cursor.moveToNext()) {
            lista.add(cursor.getString(0));
        }
        cursor.close();
        return lista;
    }




    // Obtener lista de usuarios ciudadanos (solo nombres o usuarios)
    public List<String> obtenerUsuariosCiudadanos() {
        List<String> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT usuario FROM ciudadano ORDER BY usuario", null);
        while (cursor.moveToNext()) {
            lista.add(cursor.getString(0));
        }
        cursor.close();
        return lista;
    }

    // Modificar ciudadano
    public boolean modificarCiudadano(String usuarioActual, String nuevoNombre, String nuevoEmail, String nuevaPassword, String nuevoTelefono, String nuevoUsuario, String nuevoDni) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();

        if (!nuevoNombre.isEmpty()) valores.put("nombre", nuevoNombre);
        if (!nuevoEmail.isEmpty()) valores.put("email", nuevoEmail);
        if (!nuevaPassword.isEmpty()) {
            try {
                valores.put("password", DBEncriptacion.encrypt(nuevaPassword));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        if (!nuevoTelefono.isEmpty()) valores.put("telefono", nuevoTelefono);
        if (!nuevoUsuario.isEmpty()) valores.put("usuario", nuevoUsuario);
        if (!nuevoDni.isEmpty()) valores.put("dni", nuevoDni);

        int filas = db.update("ciudadano", valores, "usuario = ?", new String[]{usuarioActual});
        return filas > 0;
    }


    public List<Incidencia> obtenerIncidenciasPorTecnico(int tecnicoId) {
        List<Incidencia> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT _id, titulo, descripcion, ubicacion, estado, fecha_hora, foto FROM incidencias WHERE id_tecnico = ? ORDER BY _id DESC",
                new String[]{String.valueOf(tecnicoId)}
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String titulo = cursor.getString(1);
            String descripcion = cursor.getString(2);
            String ubicacion = cursor.getString(3);
            String estado = cursor.getString(4);
            String fechaHora = cursor.getString(5);
            byte[] foto = cursor.getBlob(6);

            lista.add(new Incidencia(id, titulo, descripcion, ubicacion, estado, fechaHora, foto));
        }

        cursor.close();
        return lista;
    }


    public boolean actualizarEstadoIncidencia(int idIncidencia, String nuevoEstado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("estado", nuevoEstado);
        int filasAfectadas = db.update("incidencias", valores, "_id = ?", new String[]{String.valueOf(idIncidencia)});
        return filasAfectadas > 0;
    }

}