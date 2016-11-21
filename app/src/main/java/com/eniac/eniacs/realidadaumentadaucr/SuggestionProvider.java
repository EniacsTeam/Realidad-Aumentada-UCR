package com.eniac.eniacs.realidadaumentadaucr;


import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Esta clase representa la interfaz entre la base de datos y la aplicacion. Se encarga del acceso a la base y suministra los datos
 * pertinentes.
 *
 * @author  EniacsTeam
 */
public class SuggestionProvider extends ContentProvider {
    private SQLiteDatabase db;

    //Definición del CONTENT_URI
    private static final String uri =
            "content://com.eniac.eniacs.realidadaumentadaucr.SuggestionProvider/edificios";

    public static final Uri CONTENT_URI = Uri.parse(uri);

    //Clase interna para declarar las constantes de columna
    public static final class Edificios implements BaseColumns
    {
        private Edificios() {}

        //Nombres de columnas
        public static final String COL_NOMBRE = "nombre";
        public static final String COL_ID = "_id";
    }

    //Base de datos
    private EdificiosSqliteHelper edbh;
    private static final String BD_NOMBRE = "DBEdificios";
    private static final int BD_VERSION = 3;
    private static final String TABLA_EDIFICIOS = "Edificios";

    //UriMatcher
    private static final int EDIFICIOS = 1;
    private static final int EDIFICIOS_ID = 2;
    private static final int SEARCH = 3;
    private static final UriMatcher uriMatcher;

    //Inicializamos el UriMatcher
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.eniac.eniacs.realidadaumentadaucr.SuggestionProvider", "edificios", EDIFICIOS);
        uriMatcher.addURI("com.eniac.eniacs.realidadaumentadaucr.SuggestionProvider", "edificios/#", EDIFICIOS_ID);
        uriMatcher.addURI("com.eniac.eniacs.realidadaumentadaucr.SuggestionProvider", SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH);
        uriMatcher.addURI("com.eniac.eniacs.realidadaumentadaucr.SuggestionProvider", SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH);
    }

    /**
     * Este metodo es llamado como constructor del proveedor y liga la base de datos dada por {@value BD_NOMBRE}.
     *
     * @return verdadero si el true si el proveedor fue cargado de manera exitosa.
     */
    @Override
    public boolean onCreate() {
            edbh = new EdificiosSqliteHelper(getContext(), BD_NOMBRE, null, BD_VERSION);
            db = edbh.getReadableDatabase();
            return true;
    }

    private static final HashMap<String, String> PROJECTION_MAP = new HashMap();
    static {
        PROJECTION_MAP.put("_id", "_id");
        PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_TEXT_1, "nombre AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
        PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA, "_id AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA);
    }

    /**
     * Este metodo se encarga de consultar la base y retornar los resultados en un {@code Cursor}, realiza una consulta equivalente a:
     * SELECT projection FROM uri WHERE selection (tomando los valores de selectionArgs) ORDER BY sortOrder.
     *
     * @param uri el URI hacia la consulta.
     * @param projection la lista de columnas a colocar dentro del cursor.
     * @param selection un criterio de seleccion a aplicar al filtrar las filas.
     * @param selectionArgs los valores de reemplazo para los ? incluidos en la seleccion.
     * @param sortOrder como las filas en el {@code Cursor} deberian ser ordenadas.
     * @return un {@code Cursor} o {@code null}.
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(TABLA_EDIFICIOS);

        switch (uriMatcher.match(uri)) {
            case SEARCH:
                String query = uri.getLastPathSegment();
                builder.appendWhere("nombre LIKE '%" + query + "%'");
                builder.setProjectionMap(PROJECTION_MAP);
                break;
        }

        Cursor c = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        return c;
    }

    /**
     *
     * @param uri el URI hacia la consulta.
     * @return una cadena de caracteres de tipo MIME, o {@code null} si no hay tipo.
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     *
     * @param uri el content:// URI de la solicitud de insercion.
     * @param values un conjunto de pares nombre_columna/valor para añadir a la base.
     * @return el URI del item insertado recientemente.
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    /**
     *
     * @param uri el URI completo de la consulta, incluyendo el ID de la fila (si un registro particular es solicitado).
     * @param selection una restriccion opcional para aplicar a las filas al borrar.
     * @param selectionArgs los valores de reemplazo para los ? incluidos en la seleccion.
     * @return el numero de filas afectadas.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     *
     * @param uri el URI hacia la consulta.
     * @param values un conjunto de pares nombre_columna/valor para actualizar a la base.
     * @param selection un filtro opcional para encontrar las filas por actualizar.
     * @param selectionArgs los valores de reemplazo para los ? incluidos en la seleccion.
     * @return el numero de filas afectadas.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
