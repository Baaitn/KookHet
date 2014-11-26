package be.howest.nmct.android.kookhet.database;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReceptenLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;
    private final String mCategorieNaam;

    public ReceptenLoader(Context context, String CategorieNaam) {
        super(context);
        this.mCategorieNaam = CategorieNaam;
    }

    @Override
    public Cursor loadInBackground() {
        DatabaseHelper helper = DatabaseHelper.getInstance(getContext());

        SQLiteDatabase database = helper.getWritableDatabase();

        mCursor = database.rawQuery(
                " SELECT *" +
                " FROM Recepten R" +
                " INNER JOIN ReceptCategorie RC ON (RC.rcReceptId = R._ID)" +
                " INNER JOIN Categorieen C ON (RC.rcCategorieId = C._ID)" +
                " WHERE C.cNaam = ?", new String[] {"" + mCategorieNaam});

//        mCursor = database.query(
//                Contract.Recepten.CONTENT_DIRECTORY + " R " +
//                        "INNER JOIN " + Contract.ReceptCategorie.CONTENT_DIRECTORY + " RC ON (RC." + Contract.ReceptCategorie.ReceptId + " = R." + Contract.ReceptCategorie._ID + ") " +
//                        "INNER JOIN " + Contract.Categorieen.CONTENT_DIRECTORY + " C ON (RC." + Contract.ReceptCategorie.ReceptId + " = C." + Contract.CategorieenColumns._ID + ")",
//                new String[] {
//                        "R." + Contract.ReceptenColumns._ID,
//                        "R." + Contract.ReceptenColumns.Naam,
//                        "R." + Contract.ReceptenColumns.Bereidingswijze,
//                        "R." + Contract.ReceptenColumns.Bereidingstijd,
//                        "R." + Contract.ReceptenColumns.IsVegetarisch,
//                        "R." + Contract.ReceptenColumns.Image},
//                "C." + Contract.CategorieenColumns.Naam + " = ?",
//                new String[] { "" + mCategorieNaam },
//                null,
//                null,
//                Contract.ReceptenColumns.Naam + " ASC"
//        );

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
