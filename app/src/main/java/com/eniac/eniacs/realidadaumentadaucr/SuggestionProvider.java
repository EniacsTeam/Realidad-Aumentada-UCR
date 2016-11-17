package com.eniac.eniacs.realidadaumentadaucr;


import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import java.util.HashMap;

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
    }

    //Base de datos
    private EdificiosSqliteHelper edbh;
    private static final String BD_NOMBRE = "DBEdificios";
    private static final int BD_VERSION = 1;
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
    }

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

        /*//Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if(uriMatcher.match(uri) == EDIFICIOS_ID){
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = edbh.getWritableDatabase();

        Cursor c = db.query(TABLA_EDIFICIOS, projection, where,
                selectionArgs, null, null, sortOrder);*/

        Cursor c = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
