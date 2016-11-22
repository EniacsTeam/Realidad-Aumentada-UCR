package com.eniac.eniacs.realidadaumentadaucr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Esta clase representa la base de datos de la aplicacion, por lo que tiene metodos para crear una base de datos, en este caso una base de eficios.
 *
 * @author  EniacsTeam
 */
public class EdificiosSqliteHelper extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla de Edificios
    String sqlCreate = "CREATE TABLE Edificios " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " nombre TEXT )";

    private static final String edificios[] = {
            "Facultad de Derecho","Oficina de Becas",
            "Biblioteca Tinoco","Escuela de Arquitectura","Comedor Universitario","Facultad de Ingeniería",
            "Escuela de Física y Matemáticas","Escuela de Generales","Biblioteca Carlos Monge",
            "Educación Preescolar","Facultad de Letras","Centro de Informática","Escuela de Geología",
            "Ciencias Económicas","Computación e Informática","Facultad de Odontología","Facultad de Medicina",
            "Facultad de Farmacia","Facultad de Microbiología","Escuela de Biología","Escuela de Química",
            "Escuela de Artes Musicales","Escuela de Bellas Artes","Facultad de Educación","Bosque Leonel Oviedo",
            "Mariposario","Plaza 24 de Abril","El Pretil"};

    /**
     * Crea un objeto "helper" para crear, abrir, y/o manejar una base de datos.
     *
     * @param contexto para abrir o crear la base de datos.
     * @param nombre del archivo de la base de datos o {@code null} para una base "in-memory".
     * @param factory para crear objetos {@code Cursor} o {@code null} para el preestablecido
     * @param version numero de la base de datos.
     */
    public EdificiosSqliteHelper(Context contexto, String nombre,
                                 SQLiteDatabase.CursorFactory factory, int version) {

        super(contexto, nombre, factory, version);
    }

    /**
     * Llamado cuando la base de datos es creada por primera vez. Aqui sucede la creacion de la tabla y el poblamiento de respectivo.
     *
     * @param db la base de datos
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);

        //Insertamos los 28 edificios
        for(int i = 0; i < 28; i++)
        {
            //Insertamos los datos en la tabla Edificios
            db.execSQL("INSERT INTO Edificios (nombre) " +
                    "VALUES ('" + edificios[i] + "')");
        }
    }

    /**
     * Este metodo es llamado cuando el numero de version de la base es aumentado y se encarga de realizar las actualizaciones necesarias.
     *
     * @param db la base de datos.
     * @param versionAnterior la version vieja de la base.
     * @param versionNueva la nueva version de la base.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Edificios");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);

        //Insertamos los 28 edificios
        for(int i = 0; i < 28; i++)
        {
            //Insertamos los datos en la tabla Edificios
            db.execSQL("INSERT INTO Edificios (nombre) " +
                    "VALUES ('" + edificios[i] + "')");
        }
    }
}
