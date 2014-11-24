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

//        mCursor = database.query(
//                Contract.Recepten.CONTENT_DIRECTORY + " R Inner Join " + Contract.Categorieen.CONTENT_DIRECTORY
//                        + " C On R. " + Contract.ReceptenColumns.CategorieId + " = C." + Contract.CategorieenColumns._ID,
//                new String[] {
//                        "R." + Contract.ReceptenColumns._ID,
//                        /*"COUNT(*) AS " + Contract.ReceptenColumns._COUNT,*/
//                        "R." + Contract.ReceptenColumns.Naam,
//                        "R." + Contract.ReceptenColumns.CategorieId},
//                "( " + "C." +Contract.CategorieenColumns.Naam + " = ? )",
//                new String[] { "" + mCategorieNaam },
//                null,
//                null,
//                Contract.ReceptenColumns.Naam + " ASC"
//        );

//        mCursor = database.rawQuery("Select * From Recepten R Inner Join Categorieen C On (R.rCategorieId = C._id) Where C.cNaam = ?", new String[] { "" + mCategorieNaam });

        mCursor = database.rawQuery("SELECT recepten.naam, recepten.bereidingsWijze, recepten.bereidingsTijd, recepten.isVegetarisch, recepten.image" +
                " FROM KookHetDB.dbo.recepten" +
                " INNER JOIN KookHetDB.dbo.receptCategorie ON recepten.id = receptCategorie.receptID" +
                " WHERE receptCategorie.categorieID = ?",new String[] {"" + mCategorieId});

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
