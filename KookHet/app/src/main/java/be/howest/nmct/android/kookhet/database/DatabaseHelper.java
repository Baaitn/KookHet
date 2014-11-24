package be.howest.nmct.android.kookhet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import be.howest.nmct.android.kookhet.Contract;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper INSTANCE;
    private static Object lock = new Object();

    private static final String DB_NAME = "kookhet.db";
    private static final int DB_VERSION = 1;

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
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        onUpgrade(database, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

        database.execSQL("DROP TABLE IF EXISTS " + Contract.Categorieen.CONTENT_DIRECTORY);
        createCategorieen(database);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.Recepten.CONTENT_DIRECTORY);
        createRecepten(database);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.Ingredienten.CONTENT_DIRECTORY);
        createIngredienten(database);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.ReceptCategorie.CONTENT_DIRECTORY);
        createReceptCategorie(database);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.ReceptIngredient.CONTENT_DIRECTORY);
        createReceptIngredient(database);

        fill(database);

//        int currentVersion = oldVersion;
//        while(currentVersion < newVersion) {
//            switch (currentVersion) {
//                case 0:
//                    updateToV1();
//                    break;
//                case 1:
//                    updateToV2();
//                    break;
//            }
//            currentVersion++;
//        }
    }

    private void createCategorieen(SQLiteDatabase database) {
        String sql = "CREATE TABLE " + Contract.Categorieen.CONTENT_DIRECTORY + "("
                + Contract.CategorieenColumns._ID + " INTEGER PRIMARY KEY,"
                + Contract.CategorieenColumns.Naam + " TEXT,"
                + Contract.CategorieenColumns.Omschrijving + " TEXT"
                + ");";
        database.execSQL(sql);
    }

    private void createRecepten(SQLiteDatabase database) {
        String sql = "CREATE TABLE " + Contract.Recepten.CONTENT_DIRECTORY + "("
                + Contract.ReceptenColumns._ID + " INTEGER PRIMARY KEY,"
                + Contract.ReceptenColumns.Naam + " TEXT,"
                + Contract.ReceptenColumns.Bereidingstijd + " TEXT,"
                + Contract.ReceptenColumns.Bereidingstijd + " INTEGER,"
                + Contract.ReceptenColumns.IsVegetarisch + " BOOLEAN,"
                + Contract.ReceptenColumns.Image + " TEXT"
                + ");";
        database.execSQL(sql);
    }

    private void createIngredienten(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + Contract.Ingredienten.CONTENT_DIRECTORY + "("
                + Contract.IngredientenColumns._ID + " INTEGER PRIMARY KEY,"
                + Contract.IngredientenColumns.Naam + " TEXT,"
                + ");";
        db.execSQL(sql);
    }

    private void createReceptCategorie(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + Contract.ReceptCategorie.CONTENT_DIRECTORY+ "("
                + Contract.ReceptCategorieColumns.ReceptId + " INTEGER,"
                + Contract.ReceptCategorieColumns.CategorieId + " INTEGER"
                + ");";
        db.execSQL(sql);
    }

    private void createReceptIngredient(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + Contract.ReceptIngredient.CONTENT_DIRECTORY + "("
                + Contract.ReceptIngredientColumns.ReceptId + " INTEGER,"
                + Contract.ReceptIngredientColumns.IngredientId + " INTEGER,"
                + Contract.ReceptIngredientColumns.HoeveelheidId + " TEXT"
                + ");";
        db.execSQL(sql);
    }

    private void fill(SQLiteDatabase database) {
        loadDummyData(database);
        //loadDataBaseData(database);
    }

    private void loadDummyData(SQLiteDatabase database) {
        String sql = "INSERT INTO " + Contract.Recepten.CONTENT_DIRECTORY + "("
                + Contract.ReceptenColumns._ID + ", "
                + Contract.ReceptenColumns.Naam + ", "
                + Contract.ReceptenColumns.Bereidingswijze + ", "
                + Contract.ReceptenColumns.Bereidingstijd + ", "
                + Contract.ReceptenColumns.IsVegetarisch + " VALUES "
                + "( 0, 'stoofvlees', 'in een pot', 50, True);";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Categorieen.CONTENT_DIRECTORY + "("
                + Contract.CategorieenColumns._ID + ", "
                + Contract.CategorieenColumns.Naam + ", "
                + Contract.CategorieenColumns.Omschrijving + " VALUES"
                + "(0,'Hoofdgerechten','Met frietjes of aardappelen');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.CONTENT_DIRECTORY + "("
                + Contract.IngredientenColumns._ID + ", "
                + Contract.IngredientenColumns.Naam + ", "
                + "(0,'Frieten');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.CONTENT_DIRECTORY + "("
                + Contract.IngredientenColumns._ID + ", "
                + Contract.IngredientenColumns.Naam + ", "
                + "(1,'Stoofvlees');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptCategorie.CONTENT_DIRECTORY+ "("
                + Contract.ReceptCategorieColumns.ReceptId + ", "
                + Contract.ReceptCategorieColumns.CategorieId + " VALUES"
                + "(0,0);";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.CONTENT_DIRECTORY + "("
                + Contract.ReceptIngredientColumns.ReceptId + ", "
                + Contract.ReceptIngredientColumns.IngredientId + ", "
                + Contract.ReceptIngredientColumns.HoeveelheidId + " VALUES"
                + "(0,0, '30kg');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.CONTENT_DIRECTORY + "("
                + Contract.ReceptIngredientColumns.ReceptId + ", "
                + Contract.ReceptIngredientColumns.IngredientId + ", "
                + Contract.ReceptIngredientColumns.HoeveelheidId + " VALUES"
                + "(0,1, '10.000 stuks');";
        database.execSQL(sql);
    }

    private void loadDataBaseData(SQLiteDatabase database) {
        //TODO: echte data inladen
    }
}
