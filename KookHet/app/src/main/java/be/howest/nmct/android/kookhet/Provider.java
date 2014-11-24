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

// Created by Tom on 11/13/2014.

public class Provider extends ContentProvider{

    private static HashMap<String,String> sCategorieProjectionMap;
    private static HashMap<String,String> sReceptenProjectionMap;

    private static final UriMatcher sUriMatcher;
    private static final int CATEGORIE = 1;
    private static final int CATEGORIE_ID = 2;
    private static final int RECEPTEN = 3;
    private static final int RECEPT_ID = 4;

    private DatabaseHelper mOpenHelper;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(Contract.AUTHORITY, "categorie",CATEGORIE);
        sUriMatcher.addURI(Contract.AUTHORITY, "categorie/#",CATEGORIE_ID);
        sUriMatcher.addURI(Contract.AUTHORITY, "recepten", RECEPTEN);
        sUriMatcher.addURI(Contract.AUTHORITY, "recepten/#", RECEPT_ID);

        sCategorieProjectionMap = new HashMap<String, String>();
        sCategorieProjectionMap.put(Contract.Categorieen._ID, Contract.Categorieen._ID);
        sCategorieProjectionMap.put(Contract.Categorieen.Naam, Contract.Categorieen.Naam);

        sReceptenProjectionMap = new HashMap<String, String>();
        sReceptenProjectionMap.put(Contract.Recepten._ID, Contract.Recepten._ID);
        sReceptenProjectionMap.put(Contract.Recepten.Naam, Contract.Recepten.Naam);
        sReceptenProjectionMap.put(Contract.Recepten.Bereidingswijze, Contract.Recepten.Bereidingswijze);
        sReceptenProjectionMap.put(Contract.Recepten.Bereidingstijd, Contract.Recepten.Bereidingstijd);
        sReceptenProjectionMap.put(Contract.Recepten.IsVegetarisch, Contract.Recepten.IsVegetarisch);
        sReceptenProjectionMap.put(Contract.Recepten.Image, Contract.Recepten.Image);
    }

    public Provider(){}

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case CATEGORIE:
                return Contract.Categorieen.CONTENT_TYPE;
            case CATEGORIE_ID:
                return Contract.Categorieen.CONTENT_ITEM_TYPE;
            case RECEPTEN:
                return Contract.Recepten.CONTENT_TYPE;
            case RECEPT_ID:
                return Contract.Recepten.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = DatabaseHelper.getInstance(getContext());
        return true ;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        String orderBy = sortOrder;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)){
            case CATEGORIE:
                qb.setTables(Contract.Categorieen.CONTENT_DIRECTORY);
                qb.setProjectionMap(sCategorieProjectionMap);

                if (TextUtils.isEmpty(sortOrder)){
                    orderBy = Contract.Categorieen.DEFAULT_SORT_ORDER;
                }
                break;
            case CATEGORIE_ID:
                qb.setTables(Contract.Categorieen.CONTENT_DIRECTORY);
                qb.setProjectionMap(sCategorieProjectionMap);
                qb.appendWhere("(" + Contract.Categorieen._ID + "=" + uri.getPathSegments().get(Contract.Categorieen.CATEGORIE_ID_PATH_POSITION)+")");

                if (TextUtils.isEmpty(sortOrder)){
                    orderBy = Contract.Categorieen.DEFAULT_SORT_ORDER;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }

        Cursor c = qb.query(db,projection,selection,selectionArgs,null,null,orderBy);
        c.setNotificationUri(getContext().getContentResolver(),uri);
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        long RowId;

        switch (sUriMatcher.match(uri)){
            case CATEGORIE:
                if (!contentValues.containsKey(Contract.Categorieen.Naam)) {
                    throw new IllegalArgumentException(Contract.Categorieen.Naam + " is required for " + Contract.Categorieen.CONTENT_DIRECTORY);
                }

                RowId = db.insert(
                        Contract.Categorieen.CONTENT_DIRECTORY,
                        Contract.Categorieen.Naam,
                        contentValues
                );

                if (RowId > 0 ) {
                    Uri categorieUri = ContentUris.withAppendedId(Contract.Categorieen.ITEM_CONTENT_URI, RowId);
                    getContext().getContentResolver().notifyChange(categorieUri,null);
                    return categorieUri;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        throw new IllegalArgumentException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        String finalWhere;
        int count;

        switch(sUriMatcher.match(uri)){
            case CATEGORIE:
                count = db.update(
                        Contract.Categorieen.CONTENT_DIRECTORY,
                        values,
                        where,
                        whereArgs
                );
                break;
            case CATEGORIE_ID:
                String catId = uri.getPathSegments().get(Contract.Categorieen.CATEGORIE_ID_PATH_POSITION);
                finalWhere = Contract.Categorieen._ID + "=" + catId;

                if (where != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, where);
                }

                count = db.update(
                        Contract.Categorieen.CONTENT_DIRECTORY,
                        values,
                        finalWhere,
                        whereArgs
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        String finalWhere;
        int count;

        switch (sUriMatcher.match (uri)){
            case CATEGORIE:
                count = db.delete(
                        Contract.Categorieen.CONTENT_DIRECTORY,
                        where,
                        whereArgs
                );
                break;
            case CATEGORIE_ID:
                String categorieId = uri.getPathSegments().get(Contract.Categorieen.CATEGORIE_ID_PATH_POSITION);
                finalWhere = Contract.Categorieen._ID + "=" + categorieId;

                if (where != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, where);
                }

                count = db.delete(
                        Contract.Categorieen.CONTENT_DIRECTORY,
                        finalWhere,
                        whereArgs
                );
                break;
            case RECEPTEN:
                count = db.delete(
                        Contract.Recepten.CONTENT_DIRECTORY,
                        where,
                        whereArgs
                );
                break;
            case RECEPT_ID:
                String receptenId = uri.getPathSegments().get(Contract.Recepten.RECEPT_ID_PATH_POSITION);
                finalWhere = Contract.Recepten._ID + "=" + receptenId;

                if (where != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, where);
                }

                count = db.delete(
                        Contract.Recepten.CONTENT_DIRECTORY,
                        finalWhere,
                        whereArgs
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}