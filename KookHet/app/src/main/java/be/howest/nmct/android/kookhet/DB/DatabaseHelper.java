package be.howest.nmct.android.kookhet.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tom on 11/13/2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ribbons.db";
    private static final int DB_VERSION = 1;
    private static DatabaseHelper INSTANCE;
    private static Object lock = new Object();

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    public static DatabaseHelper getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (lock) {
                if(INSTANCE == null) {
                    INSTANCE = new DatabaseHelper(context.getApplicationContext());
                }
            }
        }

        return  INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        onUpgrade(sqLiteDatabase,0,0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
       // db.execSQL("DROP TABLE IF EXISTS " + Contra  );
    }
}
