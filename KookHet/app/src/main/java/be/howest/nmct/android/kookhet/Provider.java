package be.howest.nmct.android.kookhet;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

import be.howest.nmct.android.kookhet.database.DatabaseHelper;

public class Provider extends ContentProvider{

    private DatabaseHelper mOpenHelper;

    private SQLiteDatabase database;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static HashMap<String,String> sCategorieenProjectionMap = new HashMap<String, String>();
    private static HashMap<String,String> sReceptenProjectionMap = new HashMap<String, String>();

    private static final int CATEGORIEEN = 1;
    private static final int CATEGORIE_ID = 2;
    private static final int RECEPTEN = 3;
    private static final int RECEPT_ID = 4;

    static {
        sCategorieenProjectionMap.put(Contract.Categorieen._ID, Contract.Categorieen._ID);
        sCategorieenProjectionMap.put(Contract.Categorieen.Naam, Contract.Categorieen.Naam);
        sCategorieenProjectionMap.put(Contract.Categorieen.Omschrijving, Contract.Categorieen.Omschrijving);

        sReceptenProjectionMap.put(Contract.Recepten._ID, Contract.Recepten._ID);
        sReceptenProjectionMap.put(Contract.Recepten.Naam, Contract.Recepten.Naam);
        sReceptenProjectionMap.put(Contract.Recepten.Bereidingswijze, Contract.Recepten.Bereidingswijze);
        sReceptenProjectionMap.put(Contract.Recepten.Bereidingstijd, Contract.Recepten.Bereidingstijd);
        sReceptenProjectionMap.put(Contract.Recepten.IsVegetarisch, Contract.Recepten.IsVegetarisch);
        sReceptenProjectionMap.put(Contract.Recepten.IsFavoriet, Contract.Recepten.IsFavoriet);
        sReceptenProjectionMap.put(Contract.Recepten.IsMenu, Contract.Recepten.IsMenu);
        sReceptenProjectionMap.put(Contract.Recepten.Image, Contract.Recepten.Image);

        sUriMatcher.addURI(Contract.AUTHORITY, Contract.Categorieen.TABLE_NAME, CATEGORIEEN);
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.Categorieen.TABLE_NAME + "/#", CATEGORIE_ID);
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.Recepten.TABLE_NAME, RECEPTEN);
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.Recepten.TABLE_NAME + "/#", RECEPT_ID);
    }

    public Provider(){}

    @Override
    public boolean onCreate() {
        mOpenHelper = DatabaseHelper.getInstance(getContext());
        return true;

//        boolean ret = true;
//        mOpenHelper = DatabaseHelper.getInstance(getContext());
//        database = mOpenHelper.getWritableDatabase();
//        if (database == null) {
//            ret = false;
//        }
//        if (database.isReadOnly()) {
//            database.close();
//            database = null;
//            ret = false;
//        }
//        return ret;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String orderBy = sortOrder;

        switch (sUriMatcher.match(uri)){
            case CATEGORIEEN:
                qb.setTables(Contract.Categorieen.TABLE_NAME);
                qb.setProjectionMap(sCategorieenProjectionMap);
                if (TextUtils.isEmpty(sortOrder)){
                    orderBy = Contract.Categorieen.DEFAULT_SORT_ORDER;
                }
                break;
            case CATEGORIE_ID:
                qb.setTables(Contract.Categorieen.TABLE_NAME);
                qb.setProjectionMap(sCategorieenProjectionMap);
                qb.appendWhere(Contract.Categorieen._ID + " = " + uri.getLastPathSegment());
                if (TextUtils.isEmpty(sortOrder)){
                    orderBy = Contract.Categorieen.DEFAULT_SORT_ORDER;
                }
                break;
            case RECEPTEN:
                qb.setTables(Contract.Recepten.TABLE_NAME);
                qb.setProjectionMap(sReceptenProjectionMap);
                if (TextUtils.isEmpty(sortOrder)){
                    orderBy = Contract.Recepten.DEFAULT_SORT_ORDER;
                }
                break;
            case RECEPT_ID:
                qb.setTables(Contract.Recepten.TABLE_NAME);
                qb.setProjectionMap(sReceptenProjectionMap);
                qb.appendWhere(Contract.Recepten._ID + " = " + uri.getLastPathSegment());
                if (TextUtils.isEmpty(sortOrder)){
                    orderBy = Contract.Recepten.DEFAULT_SORT_ORDER;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

        /*qb.appendWhere("(" + Contract.Categorieen._ID + " = " + uri.getPathSegments().get(Contract.Categorieen.CATEGORIE_ID_PATH_POSITION)+")");*/
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case CATEGORIEEN:
                return Contract.Categorieen.CONTENT_TYPE;
            case CATEGORIE_ID:
                return Contract.Categorieen.CONTENT_ITEM_TYPE;
            case RECEPTEN:
                return Contract.Recepten.CONTENT_TYPE;
            case RECEPT_ID:
                return Contract.Recepten.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        long id;

        switch (sUriMatcher.match(uri)){
            case CATEGORIEEN:
                if (!values.containsKey(Contract.Categorieen.Naam)) {
                    throw new IllegalArgumentException(Contract.Categorieen.Naam + " is required for " + Contract.Categorieen.TABLE_NAME);
                }
                id = db.insert(Contract.Categorieen.TABLE_NAME, Contract.Categorieen.Naam, values);
                if (id > 0 ) {
                    //Uri categorieUri = ContentUris.withAppendedId(Contract.Categorieen.ITEM_CONTENT_URI, id);
                    Uri categorieUri = ContentUris.withAppendedId(uri, id);
                    getContext().getContentResolver().notifyChange(categorieUri, null);
                    return categorieUri;
                }
                break;
            case CATEGORIE_ID:

                break;
            case RECEPTEN:
                if (!values.containsKey(Contract.Recepten.Naam)) {
                    throw new IllegalArgumentException(Contract.Recepten.Naam + " is required for " + Contract.Recepten.TABLE_NAME);
                }
                id = db.insert(Contract.Recepten.TABLE_NAME, Contract.Recepten.Naam, values);
                if (id > 0 ) {
                    //Uri receptUri = ContentUris.withAppendedId(Contract.Recepten.ITEM_CONTENT_URI, id);
                    Uri receptUri = ContentUris.withAppendedId(uri, id);
                    getContext().getContentResolver().notifyChange(receptUri, null);
                    return receptUri;
                }
                break;
            case RECEPT_ID:

                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        throw new IllegalArgumentException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String finalWhere;
        int count = 0;

        switch (sUriMatcher.match(uri)){
            case CATEGORIEEN:
                count = db.update(Contract.Categorieen.TABLE_NAME, values, where, whereArgs);
                break;
            case CATEGORIE_ID:
                String categorieId = uri.getLastPathSegment();
                //String categorieId = uri.getPathSegments().get(Contract.Categorieen.CATEGORIE_ID_PATH_POSITION);
                finalWhere = Contract.Categorieen._ID + " = " + categorieId;
                if (where != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, where);
                }
                count = db.update(Contract.Categorieen.TABLE_NAME, values, finalWhere, whereArgs);
                break;
            case RECEPTEN:
                count = db.update(Contract.Recepten.TABLE_NAME, values, where, whereArgs);
                break;
            case RECEPT_ID:
                String receptId = uri.getLastPathSegment();
                //String receptId = uri.getPathSegments().get(Contract.Recepten.RECEPT_ID_PATH_POSITION);
                finalWhere = Contract.Recepten._ID + " = " + receptId;
                if (where != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, where);
                }
                count = db.update(Contract.Recepten.TABLE_NAME, values, finalWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String finalWhere;
        int count = 0;

        switch (sUriMatcher.match(uri)){
            case CATEGORIEEN:
                count = db.delete(Contract.Categorieen.TABLE_NAME, where, whereArgs);
                break;
            case CATEGORIE_ID:
                String categorieId = uri.getLastPathSegment();
                //String categorieId = uri.getPathSegments().get(Contract.Categorieen.CATEGORIE_ID_PATH_POSITION);
                finalWhere = Contract.Categorieen._ID + " = " + categorieId;
                if (where != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, where);
                }
                count = db.delete(Contract.Categorieen.TABLE_NAME, finalWhere, whereArgs);
                break;
            case RECEPTEN:
                count = db.delete(Contract.Recepten.TABLE_NAME, where, whereArgs);
                break;
            case RECEPT_ID:
                String receptId = uri.getLastPathSegment();
                //String receptId = uri.getPathSegments().get(Contract.Recepten.RECEPT_ID_PATH_POSITION);
                finalWhere = Contract.Categorieen._ID + " = " + receptId;
                if (where != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, where);
                }
                count = db.delete(Contract.Recepten.TABLE_NAME, finalWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}