package be.howest.nmct.android.kookhet.DB;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import be.howest.nmct.android.kookhet.DController;

/**
 * Created by Tom on 11/13/2014.
 */
public class CategorieënLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mData;
    public CategorieënLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
       DatabaseHelper helper = DatabaseHelper.getInstance(getContext());
        SQLiteDatabase db = helper .getWritableDatabase();
        mData = db.query(DController.Categorie.CONTENT_DIRECTORY,
                new String []{
                DController.CategorieColumns._ID,
                DController.CategorieColumns.NAME},

                        null,
                        null,
                        null,
                        null,
                DController.CategorieColumns.NAME + " ASC");
mData.getCount();
        return  mData;

        //to review
        // wss een foute query ingegeven
                }


    @Override
    protected void onStartLoading() {
       if (mData!=null){
           deliverResult(mData);
       }
        if(takeContentChanged() || mData==null){
            forceLoad();
        }
    }

    @Override
    public void deliverResult(Cursor cursor) {
      if (isReset()){
          if (cursor!=null){
              cursor.close();
          }
        return;
      }
        Cursor olddata = mData;
        mData = cursor;
        if (isStarted()){
            super.deliverResult(cursor);
        }
        if(olddata!=null && olddata!=cursor && !olddata.isClosed()){
            olddata.close();
        }
    }

    @Override
    protected void onStopLoading() {
     cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
       if (cursor!=null && !cursor.isClosed()){
        cursor.close();
       }
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if(mData != null && !mData.isClosed()){
            mData.close();
                    }
        mData = null ;
            }
}

