package be.howest.nmct.android.kookhet.database;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import be.howest.nmct.android.kookhet.Contract;

public class CategorieenLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;

    public CategorieenLoader(Context context) {
       super(context);
    }

    @Override
    public Cursor loadInBackground() {
        DatabaseHelper helper = DatabaseHelper.getInstance(getContext());

        SQLiteDatabase database = helper.getWritableDatabase();

        mCursor = database.query(
                Contract.Categorieen.CONTENT_DIRECTORY + " C ",
                new String[] {
                        "C." + Contract.Categorieen._ID,
                        "( SELECT COUNT(*) FROM " + Contract.Recepten.CONTENT_DIRECTORY + " R, " + Contract.ReceptCategorie.CONTENT_DIRECTORY + " RC WHERE RC." + Contract.ReceptCategorie.ReceptId + " = R." + Contract.Recepten._ID + " AND RC." + Contract.ReceptCategorie.CategorieId + " = C." + Contract.Categorieen._ID + " ) AS " + Contract.Categorieen._COUNT,
                        "C." + Contract.Categorieen.Naam},
                null,
                null,
                null,
                null,
                Contract.Categorieen.Naam + " ASC"
        );

        mCursor.getCount();

        return mCursor;
    }

    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            if (cursor != null) {
                cursor.close();
            }
            return;
        }

        Cursor oldCursor = mCursor;
        mCursor = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {
        if(mCursor != null) {
            deliverResult(mCursor);
        }

        if(takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }

        mCursor = null;
    }
}
