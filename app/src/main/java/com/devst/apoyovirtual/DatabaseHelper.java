package com.devst.apoyovirtual;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // --- Constantes Generales de la Base de Datos ---
    private static final String DATABASE_NAME = "apoyo_virtual.db";
    private static final int DATABASE_VERSION = 2; // Si cambias la estructura, incrementa este número.

    // ---------------------------------------------------------------------------
    // --- Definición de la Tabla USUARIOS ---
    // ---------------------------------------------------------------------------
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NOMBRE = "nombre";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";

    // Sentencia SQL para construir la tabla de usuarios usando las constantes de arriba
    private static final String CREATE_TABLE_USUARIOS =
            "CREATE TABLE " + TABLE_USUARIOS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_NOMBRE + " TEXT NOT NULL, " +
                    COLUMN_USER_EMAIL + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_USER_PASSWORD + " TEXT NOT NULL);";

    // ---------------------------------------------------------------------------
    // --- Definición de la Tabla HABITOS_SALUDABLES ---
    // ---------------------------------------------------------------------------
    public static final String TABLE_HABITOS_SALUDABLES = "habitos_saludables";
    public static final String COLUMN_HABITO_SALUDABLE_ID = "id";

    public static final String COLUMN_HABITO_SALUDABLE_DESCRIPCION = "descripcion";
    public static final String COLUMN_HABITO_SALUDABLE_USER_ID_FK = "usuario_id";
    // Sentencia SQL para construir la tabla de hábitos saludables
    private static final String CREATE_TABLE_HABITOS_SALUDABLES =
            "CREATE TABLE " + TABLE_HABITOS_SALUDABLES + " (" +
                    COLUMN_HABITO_SALUDABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_HABITO_SALUDABLE_DESCRIPCION + " TEXT," +
                    COLUMN_HABITO_SALUDABLE_USER_ID_FK + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_HABITO_SALUDABLE_USER_ID_FK + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_USER_ID + "));" ;

    // ---------------------------------------------------------------------------
    // --- Definición de la Tabla HABITOS_NO_SALUDABLES ---
    // ---------------------------------------------------------------------------
    public static final String TABLE_HABITOS_NO_SALUDABLES = "habitos_no_saludables";
    public static final String COLUMN_HABITO_NO_SALUDABLE_ID = "id";
    public static final String COLUMN_HABITO_NO_SALUDABLE_DESCRIPCION = "descrpcion_no_saludable";
    public static final String COLUMN_HABITO_NO_SALUDABLE_USER_ID_FK = "usuario_id";
    // Sentencia SQL para construir la tabla de hábitos no saludables
    private static final String CREATE_TABLE_HABITOS_NO_SALUDABLES =
            "CREATE TABLE " + TABLE_HABITOS_NO_SALUDABLES + " (" +
                    COLUMN_HABITO_NO_SALUDABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_HABITO_NO_SALUDABLE_DESCRIPCION + " TEXT, " +
                    COLUMN_HABITO_NO_SALUDABLE_USER_ID_FK + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_HABITO_NO_SALUDABLE_USER_ID_FK + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_USER_ID + "));";


    public static final String TABLE_EMOCIONES = "emociones";
    public static final String COLUMN_EMOCION_CATALOGO_ID = "id";
    public static final String COLUMN_EMOCION_CATALOGO_NOMBRE = "nombre";
    public static final String COLUMN_EMOCION_CATALOGO_COLOR = "color_hex";
    public static final String COLUMN_EMOCION_CATALOGO_DESCRIPCION = "descripcion";

    // Sentencia SQL para crear el catálogo de emociones
    private static final String CREATE_TABLE_EMOCIONES =
            "CREATE TABLE " + TABLE_EMOCIONES + " (" +
                    COLUMN_EMOCION_CATALOGO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMOCION_CATALOGO_NOMBRE + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_EMOCION_CATALOGO_COLOR + " TEXT NOT NULL, " +
                    COLUMN_EMOCION_CATALOGO_DESCRIPCION + " TEXT);";

    // ---------------------------------------------------------------------------
    // --- Definición de la Tabla REGISTROS_EMOCIONES (IoT) ---
    // ---------------------------------------------------------------------------
    public static final String TABLE_REGISTROS_EMOCIONES = "registros_emociones";
    public static final String COLUMN_EMOCION_ID = "id";
    public static final String COLUMN_EMOCION_NOMBRE = "emocion_detectada";
    public static final String COLUMN_EMOCION_FECHA = "fecha";
    public static final String COLUMN_EMOCION_USER_ID_FK = "usuario_id";

    public static final String COLUMN_REGISTRO_EMOCION_ID_FK = "emocion_id"; // <-- MODIFICADO
    // Sentencia SQL para construir la tabla de registros de emociones
    private static final String CREATE_TABLE_REGISTROS_EMOCIONES =
            "CREATE TABLE " + TABLE_REGISTROS_EMOCIONES + " (" +
                    COLUMN_EMOCION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMOCION_NOMBRE + " TEXT NOT NULL, " +
                    COLUMN_EMOCION_FECHA + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    COLUMN_EMOCION_USER_ID_FK + " INTEGER, " +
                    COLUMN_REGISTRO_EMOCION_ID_FK + " INTEGER, " + // <-- COLUMNA AÑADIDA
                    "FOREIGN KEY(" + COLUMN_EMOCION_USER_ID_FK + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_USER_ID + ")," +
                    "FOREIGN KEY(" + COLUMN_REGISTRO_EMOCION_ID_FK + ") REFERENCES " + TABLE_EMOCIONES + "(" + COLUMN_EMOCION_CATALOGO_ID + "));";


    // --- Constructor ---
    // Es necesario para inicializar el Helper
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Este método se llama UNA SOLA VEZ, la primera vez que se necesita la base de datos.
     * Aquí se ejecutan las sentencias SQL para crear la estructura inicial.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Ejecuta las sentencias de creación para cada tabla
        db.execSQL(CREATE_TABLE_USUARIOS);
        db.execSQL(CREATE_TABLE_HABITOS_SALUDABLES);
        db.execSQL(CREATE_TABLE_HABITOS_NO_SALUDABLES);
        db.execSQL(CREATE_TABLE_EMOCIONES);
        db.execSQL(CREATE_TABLE_REGISTROS_EMOCIONES);
    }

    /**
     * Este método se llama si incrementas DATABASE_VERSION.
     * Sirve para actualizar la estructura de la base de datos en futuras versiones de tu app.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Para una actualización simple, borramos las tablas viejas y las volvemos a crear.
        // ¡CUIDADO! Esto elimina todos los datos existentes.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HABITOS_SALUDABLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HABITOS_NO_SALUDABLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMOCIONES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTROS_EMOCIONES);
        // Llama a onCreate de nuevo para crear la nueva estructura.
        onCreate(db);
    }

    public long addUser(String nombre, String email, String password) {
        // Obtiene una referencia a la base de datos en modo escritura.
        SQLiteDatabase db = this.getWritableDatabase();

        // ContentValues se usa para almacenar los pares de columna-valor que se van a insertar.
        ContentValues values = new ContentValues();
        // Usa las constantes que ya definiste para asegurar que los nombres de columna son correctos.
        values.put(COLUMN_USER_NOMBRE, nombre);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);

        // Inserta la nueva fila en la tabla de usuarios.
        // El 'null' (nullColumnHack) significa que si 'values' está vacío, no se inserta nada.
        // La restricción UNIQUE en la columna email se encargará de prevenir duplicados automáticamente.
        long result = db.insert(TABLE_USUARIOS, null, values);

        // Es buena práctica cerrar la base de datos cuando terminas la operación.
        db.close();

        return result; // Devuelve el resultado de la operación.
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // La cláusula WHERE ahora buscará por la columna del nombre de usuario.
        String selection = COLUMN_USER_NOMBRE + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // Los valores para la búsqueda ahora son el username y el password.
        String[] selectionArgs = { username, password };

        // Ejecuta la consulta
        Cursor cursor = db.query(TABLE_USUARIOS,
                new String[]{COLUMN_USER_ID}, // Solo necesitamos saber si existe una fila
                selection,
                selectionArgs,
                null,
                null,
                null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        // Si count > 0, significa que se encontró al usuario.
        return count > 0;
    }

    // ========== Métodos CRUD para la tabla EMOCIONES ==========

    public long addEmocion(String nombre, String color, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMOCION_CATALOGO_NOMBRE, nombre);
        values.put(COLUMN_EMOCION_CATALOGO_COLOR, color);
        values.put(COLUMN_EMOCION_CATALOGO_DESCRIPCION, descripcion);
        long result = db.insert(TABLE_EMOCIONES, null, values);
        db.close();
        return result;
    }

    public Cursor getAllEmociones() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_EMOCION_CATALOGO_ID + " as _id", // Renombramos 'id' a '_id' en el resultado
                COLUMN_EMOCION_CATALOGO_NOMBRE,
                COLUMN_EMOCION_CATALOGO_COLOR,
                COLUMN_EMOCION_CATALOGO_DESCRIPCION
        };

        return db.query(TABLE_EMOCIONES, projection, null, null, null, null, null);    }

    public int updateEmocion(int id, String nombre, String color, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMOCION_CATALOGO_NOMBRE, nombre);
        values.put(COLUMN_EMOCION_CATALOGO_COLOR, color);
        values.put(COLUMN_EMOCION_CATALOGO_DESCRIPCION, descripcion);
        int result = db.update(TABLE_EMOCIONES, values, COLUMN_EMOCION_CATALOGO_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

    public int deleteEmocion(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_EMOCIONES, COLUMN_EMOCION_CATALOGO_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
}
