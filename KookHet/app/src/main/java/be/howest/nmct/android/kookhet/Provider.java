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

import be.howest.nmct.android.kookhet.DB.DatabaseHelper;

/**
 * Created by Tom on 11/13/2014.
 */
public class Provider extends ContentProvider{
private static HashMap<String,String> sKookhetProjectionMap;


    private static final UriMatcher sUriMatcher;
    private static final int CATEGORIE = 1;
    private static final int CATEGORIE_ID = 2;
private DatabaseHelper mOpenHelper;
    public Provider(){}
    static {
    sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(DController.AUTHORITY, "Categorie",CATEGORIE);
sUriMatcher.addURI(DController.AUTHORITY, "Categorie/#",CATEGORIE_ID);

        sKookhetProjectionMap = new HashMap<String, String>();
        sKookhetProjectionMap.put (DController.Categorie._ID,DController.Categorie._ID);
        sKookhetProjectionMap.put(DController.Categorie.NAME,DController.Categorie.NAME);

    }

    @Override
    public boolean onCreate() {
      mOpenHelper = DatabaseHelper.getInstance(getContext());
        return true ;

    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {
    String orderBy = s2;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)){
            case CATEGORIE:
                qb.setTables(DController.Categorie.CONTENT_DIRECTORY);
                qb.setProjectionMap(sKookhetProjectionMap);
                if (TextUtils.isEmpty(s2)){
                    orderBy = DController.Categorie.DEFAULT_SORT_ORDER;
                   }
            break;
            case CATEGORIE_ID:
                qb.setTables(DController.Categorie.CONTENT_DIRECTORY);
                qb.setProjectionMap(sKookhetProjectionMap);
                qb.appendWhere(
                        "("  + DController.Categorie._ID + "=" + uri.getPathSegments().get(DController.Categorie.CAT_ID_PATH_POSITION)+")"
                );
                if (TextUtils.isEmpty(s2)){
                    orderBy = DController.Categorie.DEFAULT_SORT_ORDER;

                }
                break;
            default:
                throw new IllegalArgumentException("Unkown URI" + uri);

        }
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db,strings,s,strings2,null,null,orderBy);
        c.setNotificationUri(getContext().getContentResolver(),uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
       switch (sUriMatcher.match(uri)){
           case CATEGORIE:
               return DController.Categorie.CONTENT_TYPE;
           case CATEGORIE_ID:
               return DController.Categorie.CONTENT_ITEM_TYPE;


default:
    throw new IllegalArgumentException("Unknown URI " + uri);
       }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
   SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long RowId;
        switch (sUriMatcher.match(uri)){
            case CATEGORIE:
                if (!contentValues.containsKey(DController.Categorie.NAME))
                    throw new IllegalArgumentException(DController.Categorie.NAME
                    +" is required for " + DController.Categorie.CONTENT_DIRECTORY);
                RowId = db.insert(
                        DController.Categorie.CONTENT_DIRECTORY,
                        DController.Categorie.NAME,
                        contentValues

                );
        if (RowId > 0 ) {
            Uri categorieUri = ContentUris.withAppendedId(DController.Categorie.ITEM_CONTENT_URI,RowId);
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
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        String finalWhere;
        int count;
                switch (sUriMatcher.match (uri)){
                    case CATEGORIE:
                        count = db.delete(DController.Categorie.CONTENT_DIRECTORY,
                             s,
                                strings


                        );
                    break;
                    case CATEGORIE_ID:
                        String orderId = uri.getPathSegments().get(DController.Categorie.CAT_ID_PATH_POSITION);
                        finalWhere = DController.Categorie._ID + "=" + orderId;

                        count = db.delete(DController.Categorie.CONTENT_DIRECTORY,s,strings);
                            break;

                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);
                }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;


    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        String FinalWhere;

        switch(sUriMatcher.match(uri)){
            case CATEGORIE:
                count = db.update(
                        DController.Categorie.CONTENT_DIRECTORY,
                        contentValues,
                        s,
                        strings

                );
                break;
            case CATEGORIE_ID:
                String catId = uri.getPathSegments().get(DController.Categorie.CAT_ID_PATH_POSITION);
                FinalWhere = DController.Categorie._ID + "=" + catId;
                if (s!=null){
                    FinalWhere = DatabaseUtils.concatenateWhere(FinalWhere,s);
                }
            count = db.update(
                    DController.Categorie.CONTENT_DIRECTORY,
                    contentValues,
                    s,
                    strings
            );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
