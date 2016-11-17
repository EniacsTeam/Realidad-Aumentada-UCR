package com.eniac.eniacs.realidadaumentadaucr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Xinia on 16/11/2016.
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

    public EdificiosSqliteHelper(Context contexto, String nombre,
                                 SQLiteDatabase.CursorFactory factory, int version) {

        super(contexto, nombre, factory, version);
    }

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
    }
}
