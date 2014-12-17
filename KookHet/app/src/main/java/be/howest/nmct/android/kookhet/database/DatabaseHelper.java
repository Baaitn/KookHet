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

        database.execSQL("DROP TABLE IF EXISTS " + Contract.Categorieen.TABLE_NAME);
        createCategorieen(database);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.Recepten.TABLE_NAME);
        createRecepten(database);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.ReceptCategorie.TABLE_NAME);
        createReceptCategorie(database);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.Ingredienten.TABLE_NAME);
        createIngredienten(database);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.ReceptIngredient.TABLE_NAME);
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
        String sql = "CREATE TABLE " + Contract.Categorieen.TABLE_NAME + "("
                + Contract.CategorieenColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Contract.CategorieenColumns.Naam + " TEXT,"
                + Contract.CategorieenColumns.Omschrijving + " TEXT"
                + ");";
        database.execSQL(sql);
    }

    private void createRecepten(SQLiteDatabase database) {
        String sql = "CREATE TABLE " + Contract.Recepten.TABLE_NAME + "("
                + Contract.ReceptenColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Contract.ReceptenColumns.Naam + " TEXT,"
                + Contract.ReceptenColumns.Bereidingswijze + " TEXT,"
                + Contract.ReceptenColumns.Bereidingstijd + " TEXT,"
                + Contract.ReceptenColumns.IsVegetarisch + " BOOLEAN,"
                + Contract.ReceptenColumns.IsFavoriet + " BOOLEAN,"
                + Contract.ReceptenColumns.IsMenu + " BOOLEAN,"
                + Contract.ReceptenColumns.Image + " TEXT"
                + ");";
        database.execSQL(sql);
    }

    private void createReceptCategorie(SQLiteDatabase database) {
        String sql = "CREATE TABLE " + Contract.ReceptCategorie.TABLE_NAME + "("
                + Contract.ReceptCategorieColumns.ReceptId + " INTEGER,"
                + Contract.ReceptCategorieColumns.CategorieId + " INTEGER"
                + ");";
        database.execSQL(sql);
    }

    private void createIngredienten(SQLiteDatabase database) {
        String sql = "CREATE TABLE " + Contract.Ingredienten.TABLE_NAME + "("
                + Contract.IngredientenColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Contract.IngredientenColumns.Naam + " TEXT"
                + ");";
        database.execSQL(sql);
    }

    private void createReceptIngredient(SQLiteDatabase database) {
        String sql = "CREATE TABLE " + Contract.ReceptIngredient.TABLE_NAME + "("
                + Contract.ReceptIngredientColumns.ReceptId + " INTEGER,"
                + Contract.ReceptIngredientColumns.IngredientId + " INTEGER,"
                + Contract.ReceptIngredientColumns.Hoeveelheid + " TEXT"
                + ");";
        database.execSQL(sql);
    }

    private void fill(SQLiteDatabase database) {
        loadDummyData(database);
        loadDataBaseData(database);
    }

    private void loadDummyData(SQLiteDatabase database) {
        String sql;

        //Categorieen

        sql = "INSERT INTO " + Contract.Categorieen.TABLE_NAME
                + " ( " + Contract.CategorieenColumns.Naam + ", " + Contract.CategorieenColumns.Omschrijving + " ) "
                + " VALUES ( 'Voorgerechten', 'Omschrijving' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Categorieen.TABLE_NAME
                + " ( " + Contract.CategorieenColumns.Naam + ", " + Contract.CategorieenColumns.Omschrijving + " ) "
                + " VALUES ( 'Hoofdgerechten', 'Omschrijving' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Categorieen.TABLE_NAME + " ( "
                + Contract.CategorieenColumns.Naam + ", " + Contract.CategorieenColumns.Omschrijving + " ) "
                + " VALUES ( 'Deserts', 'Omschrijving' )";
        database.execSQL(sql);

        //Recepten

        sql = "INSERT INTO " + Contract.Recepten.TABLE_NAME
                + " ( " + Contract.ReceptenColumns.Naam + ", " + Contract.ReceptenColumns.Bereidingswijze + ", " + Contract.ReceptenColumns.Bereidingstijd + ", " + Contract.ReceptenColumns.IsVegetarisch + ", " + Contract.ReceptenColumns.IsMenu + " ) "
                + " VALUES ('Tomatensoep met balletjes', 'Bereidingswijze', '50 min', '0', '1' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Recepten.TABLE_NAME
                + " ( " + Contract.ReceptenColumns.Naam + ", " + Contract.ReceptenColumns.Bereidingswijze + ", " + Contract.ReceptenColumns.Bereidingstijd + ", " + Contract.ReceptenColumns.IsVegetarisch + " ) "
                + " VALUES ('Spaghetti Bolognese', 'Bereidingswijze', '30 min', 'false' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Recepten.TABLE_NAME
                + " ( " + Contract.ReceptenColumns.Naam + ", " + Contract.ReceptenColumns.Bereidingswijze + ", " + Contract.ReceptenColumns.Bereidingstijd + ", " + Contract.ReceptenColumns.IsVegetarisch + ", " + Contract.ReceptenColumns.IsFavoriet + " ) "
                + " VALUES ('Kip met appelmoes en kroketjes', 'Bereidingswijze', '40 min', 'false', 'true' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Recepten.TABLE_NAME
                + " ( " + Contract.ReceptenColumns.Naam + ", " + Contract.ReceptenColumns.Bereidingswijze + ", " + Contract.ReceptenColumns.Bereidingstijd + ", " + Contract.ReceptenColumns.IsVegetarisch + ", " + Contract.ReceptenColumns.IsMenu + " ) "
                + " VALUES ('Stoofvlees met frieten', 'Bereidingswijze', '120 min', 'false', 'true' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Recepten.TABLE_NAME
                + " ( " + Contract.ReceptenColumns.Naam + ", " + Contract.ReceptenColumns.Bereidingswijze + ", " + Contract.ReceptenColumns.Bereidingstijd + ", " + Contract.ReceptenColumns.IsVegetarisch + ", " + Contract.ReceptenColumns.IsMenu + " ) "
                + " VALUES ('Chocolademousse', 'Bereidingswijze', '30 min', 'false', 'true' )";
        database.execSQL(sql);

        //ReceptCategorie

        sql = "INSERT INTO " + Contract.ReceptCategorie.TABLE_NAME
                + " ( " + Contract.ReceptCategorie.CategorieId + ", " + Contract.ReceptCategorie.ReceptId + " ) "
                + " VALUES ('1', '1' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptCategorie.TABLE_NAME
                + " ( " + Contract.ReceptCategorie.CategorieId + ", " + Contract.ReceptCategorie.ReceptId + " ) "
                + " VALUES ('2', '2' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptCategorie.TABLE_NAME
                + " ( " + Contract.ReceptCategorie.CategorieId + ", " + Contract.ReceptCategorie.ReceptId + " ) "
                + " VALUES ('2', '3' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptCategorie.TABLE_NAME
                + " ( " + Contract.ReceptCategorie.CategorieId + ", " + Contract.ReceptCategorie.ReceptId + " ) "
                + " VALUES ('2', '4' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptCategorie.TABLE_NAME
                + " ( " + Contract.ReceptCategorie.CategorieId + ", " + Contract.ReceptCategorie.ReceptId + " ) "
                + " VALUES ('3', '5' )";
        database.execSQL(sql);

        //Ingredienten

        sql = "INSERT INTO " + Contract.Ingredienten.TABLE_NAME
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Tomatensoep');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.TABLE_NAME
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Balletjes');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.TABLE_NAME
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Spaghetti');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.TABLE_NAME
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Bolognesesaus');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.TABLE_NAME
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Kip');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.TABLE_NAME
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Appelmoes');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.TABLE_NAME
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Kroketjes');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.TABLE_NAME
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Stoofvlees');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.TABLE_NAME
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Frieten');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.TABLE_NAME
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Chocolademousse');";
        database.execSQL(sql);

        //ReceptIngredient

        sql = "INSERT INTO " + Contract.ReceptIngredient.TABLE_NAME
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('1', '1' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.TABLE_NAME
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('1', '2' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.TABLE_NAME
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('2', '3' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.TABLE_NAME
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('2', '4' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.TABLE_NAME
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('3', '5' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.TABLE_NAME
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('3', '6' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.TABLE_NAME
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('3', '7' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.TABLE_NAME
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('4', '8' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.TABLE_NAME
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('4', '9' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.TABLE_NAME
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('5', '10' )";
        database.execSQL(sql);
    }

    private void loadDataBaseData(SQLiteDatabase database) {
        //TODO: echte data inladen
    }
}
