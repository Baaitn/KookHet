package be.howest.nmct.android.kookhet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import be.howest.nmct.android.kookhet.Contract;
import be.howest.nmct.android.kookhet.RestAPI;

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
        database.execSQL("DROP TABLE IF EXISTS " + Contract.ReceptCategorie.CONTENT_DIRECTORY);
        createReceptCategorie(database);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.Ingredienten.CONTENT_DIRECTORY);
        createIngredienten(database);
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
                + Contract.CategorieenColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Contract.CategorieenColumns.Naam + " TEXT,"
                + Contract.CategorieenColumns.Omschrijving + " TEXT"
                + ");";
        database.execSQL(sql);
    }

    private void createRecepten(SQLiteDatabase database) {
        String sql = "CREATE TABLE " + Contract.Recepten.CONTENT_DIRECTORY + "("
                + Contract.ReceptenColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Contract.ReceptenColumns.Naam + " TEXT,"
                + Contract.ReceptenColumns.Bereidingswijze + " TEXT,"
                + Contract.ReceptenColumns.Bereidingstijd + " TEXT,"
                + Contract.ReceptenColumns.IsVegetarisch + " BOOLEAN,"
                + Contract.ReceptenColumns.Image + " TEXT"
                + ");";
        database.execSQL(sql);
    }

    private void createReceptCategorie(SQLiteDatabase database) {
        String sql = "CREATE TABLE " + Contract.ReceptCategorie.CONTENT_DIRECTORY+ "("
                + Contract.ReceptCategorieColumns.ReceptId + " INTEGER,"
                + Contract.ReceptCategorieColumns.CategorieId + " INTEGER"
                + ");";
        database.execSQL(sql);
    }

    private void createIngredienten(SQLiteDatabase database) {
        String sql = "CREATE TABLE " + Contract.Ingredienten.CONTENT_DIRECTORY + "("
                + Contract.IngredientenColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Contract.IngredientenColumns.Naam + " TEXT"
                + ");";
        database.execSQL(sql);
    }

    private void createReceptIngredient(SQLiteDatabase database) {
        String sql = "CREATE TABLE " + Contract.ReceptIngredient.CONTENT_DIRECTORY + "("
                + Contract.ReceptIngredientColumns.ReceptId + " INTEGER,"
                + Contract.ReceptIngredientColumns.IngredientId + " INTEGER,"
                + Contract.ReceptIngredientColumns.Hoeveelheid + " TEXT"
                + ");";
        database.execSQL(sql);
    }

    private void fill(SQLiteDatabase database) {
        //TODO: echte data inladen
        //loadDummyData(database);
        //loadDataBaseData(database);
        new LoadJson().execute();
    }
        private void insertCategorie(SQLiteDatabase database,Integer Id,String naam,String omschrijving) {
            String sql;
    sql = "INSERT INTO " + Contract.Categorieen.CONTENT_DIRECTORY
            + " ( " + Contract.CategorieenColumns._ID + "," + Contract.CategorieenColumns.Naam + ", " + Contract.CategorieenColumns.Omschrijving + " ) "
            + " VALUES ( '"+Id+"','"+naam+"','"+omschrijving+"' )";
    database.execSQL(sql);

    }
    private void insertCategorieRecept(SQLiteDatabase database,Integer receptId, Integer catId) {
        String sql;
        sql = "INSERT INTO " + Contract.ReceptCategorie.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptCategorie.CategorieId + ", " + Contract.ReceptCategorie.ReceptId + " ) "
                + " VALUES ('"+catId+"', '"+receptId+"' )";
        database.execSQL(sql);

    }
    private void insertReceptIngredient(SQLiteDatabase database,Integer receptID,Integer ingredientId, String hoeveelheid) {
        String sql;
        sql = "INSERT INTO " + Contract.ReceptIngredient.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId  + ", " + Contract.ReceptIngredient.Hoeveelheid + ") "
                + " VALUES ('"+receptID+"', '"+ingredientId+"', '"+hoeveelheid+"')";

    }

    private void insertRecept(SQLiteDatabase database,Integer Id, String naam, String bereidingswijze, Integer bereidingstijd, Boolean isvegitarisch) {
        String sql;
        sql = "INSERT INTO " + Contract.Recepten.CONTENT_DIRECTORY
                + " (" + Contract.ReceptenColumns._ID + ", " + Contract.ReceptenColumns.Naam + ", " + Contract.ReceptenColumns.Bereidingswijze + ", " + Contract.ReceptenColumns.Bereidingstijd + ", " + Contract.ReceptenColumns.IsVegetarisch + " ) "
                + " VALUES ('"+Id+"', '"+naam+"', '"+bereidingswijze+"', '"+bereidingstijd+"', '"+isvegitarisch+"' )";
        database.execSQL(sql);

    }

    private void insertIngredient(SQLiteDatabase database,Integer id,String naam) {
        String sql;
        sql = "INSERT INTO " + Contract.Ingredienten.CONTENT_DIRECTORY
                + "(" + Contract.ReceptenColumns._ID + "," + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('"+id+"', '"+naam+"');";
        database.execSQL(sql);

    }



    private void loadDummyData(SQLiteDatabase database) {
        String sql;

        //Categorieen

        sql = "INSERT INTO " + Contract.Categorieen.CONTENT_DIRECTORY
                + " ( " + Contract.CategorieenColumns.Naam + ", " + Contract.CategorieenColumns.Omschrijving + " ) "
                + " VALUES ( 'Voorgerechten', 'Omschrijving' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Categorieen.CONTENT_DIRECTORY
                + " ( " + Contract.CategorieenColumns.Naam + ", " + Contract.CategorieenColumns.Omschrijving + " ) "
                + " VALUES ( 'Hoofdgerechten', 'Omschrijving' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Categorieen.CONTENT_DIRECTORY + " ( "
                + Contract.CategorieenColumns.Naam + ", " + Contract.CategorieenColumns.Omschrijving + " ) "
                + " VALUES ( 'Deserts', 'Omschrijving' )";
        database.execSQL(sql);

        //Recepten

        sql = "INSERT INTO " + Contract.Recepten.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptenColumns.Naam + ", " + Contract.ReceptenColumns.Bereidingswijze + ", " + Contract.ReceptenColumns.Bereidingstijd + ", " + Contract.ReceptenColumns.IsVegetarisch + " ) "
                + " VALUES ('Tomatensoep met balletjes', 'Bereidingswijze', '50 min', 'False' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Recepten.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptenColumns.Naam + ", " + Contract.ReceptenColumns.Bereidingswijze + ", " + Contract.ReceptenColumns.Bereidingstijd + ", " + Contract.ReceptenColumns.IsVegetarisch + " ) "
                + " VALUES ('Spaghetti Bolognese', 'Bereidingswijze', '30 min', 'False' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Recepten.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptenColumns.Naam + ", " + Contract.ReceptenColumns.Bereidingswijze + ", " + Contract.ReceptenColumns.Bereidingstijd + ", " + Contract.ReceptenColumns.IsVegetarisch + " ) "
                + " VALUES ('Kip met appelmoes en kroketjes', 'Bereidingswijze', '40 min', 'False' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Recepten.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptenColumns.Naam + ", " + Contract.ReceptenColumns.Bereidingswijze + ", " + Contract.ReceptenColumns.Bereidingstijd + ", " + Contract.ReceptenColumns.IsVegetarisch + " ) "
                + " VALUES ('Stoofvlees met frieten', 'Bereidingswijze', '120 min', 'False' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Recepten.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptenColumns.Naam + ", " + Contract.ReceptenColumns.Bereidingswijze + ", " + Contract.ReceptenColumns.Bereidingstijd + ", " + Contract.ReceptenColumns.IsVegetarisch + " ) "
                + " VALUES ('Chocolademousse', 'Bereidingswijze', '30 min', 'False' )";
        database.execSQL(sql);

        //ReceptCategorie

        sql = "INSERT INTO " + Contract.ReceptCategorie.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptCategorie.CategorieId + ", " + Contract.ReceptCategorie.ReceptId + " ) "
                + " VALUES ('1', '1' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptCategorie.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptCategorie.CategorieId + ", " + Contract.ReceptCategorie.ReceptId + " ) "
                + " VALUES ('2', '2' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptCategorie.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptCategorie.CategorieId + ", " + Contract.ReceptCategorie.ReceptId + " ) "
                + " VALUES ('2', '3' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptCategorie.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptCategorie.CategorieId + ", " + Contract.ReceptCategorie.ReceptId + " ) "
                + " VALUES ('2', '4' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptCategorie.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptCategorie.CategorieId + ", " + Contract.ReceptCategorie.ReceptId + " ) "
                + " VALUES ('3', '5' )";
        database.execSQL(sql);

        //Ingredienten

        sql = "INSERT INTO " + Contract.Ingredienten.CONTENT_DIRECTORY
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Tomatensoep');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.CONTENT_DIRECTORY
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Balletjes');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.CONTENT_DIRECTORY
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Spaghetti');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.CONTENT_DIRECTORY
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Bolognesesaus');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.CONTENT_DIRECTORY
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Kip');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.CONTENT_DIRECTORY
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Appelmoes');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.CONTENT_DIRECTORY
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Kroketjes');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.CONTENT_DIRECTORY
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Stoofvlees');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.CONTENT_DIRECTORY
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Frieten');";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.Ingredienten.CONTENT_DIRECTORY
                + "(" + Contract.IngredientenColumns.Naam + " ) "
                + " VALUES ('Chocolademousse');";
        database.execSQL(sql);

        //ReceptIngredient

        sql = "INSERT INTO " + Contract.ReceptIngredient.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('1', '1' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('1', '2' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('2', '3' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('2', '4' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('3', '5' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('3', '6' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('3', '7' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('4', '8' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('4', '9' )";
        database.execSQL(sql);
        sql = "INSERT INTO " + Contract.ReceptIngredient.CONTENT_DIRECTORY
                + " ( " + Contract.ReceptIngredient.ReceptId + ", " + Contract.ReceptIngredient.IngredientId + " ) "
                + " VALUES ('5', '10' )";
        database.execSQL(sql);
    }

    private void loadDataBaseData(SQLiteDatabase database) {

    }

    private class LoadJson extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            RestAPI api;
            JSONArray arr;
            JSONObject jsonobj;
            try {


                api = new RestAPI();

                jsonobj =  api.GetCategorieen();
      arr =  jsonobj.getJSONArray("Value");

                    for(int i = 0 ; i < arr.length()-1 ; i++) {
                        String naam,omschrijving;
                        Integer id;
                JSONObject o = arr.getJSONObject(i);
                        id = o.getInt("ID");
                        omschrijving = o.getString("omschrijving");
                        naam = o.getString("naam");
                        insertCategorie(getWritableDatabase(),id,naam,omschrijving);
                    }



                api = new RestAPI();


                jsonobj=  api.GetReceptCategorie();
                arr  =  jsonobj.getJSONArray("Value");

                for(int i = 0 ; i < arr.length()-1 ; i++) {

                    Integer receptId,catId;
                    JSONObject o = arr.getJSONObject(i);
                    receptId = o.getInt("receptID");
                    catId = o.getInt("CategorieID");

                    insertCategorieRecept(getWritableDatabase(),receptId,catId);
                }




                api = new RestAPI();


                jsonobj=  api.GetRecepten();
                arr  =  jsonobj.getJSONArray("Value");

                for(int i = 0 ; i < arr.length()-1 ; i++) {

                    Integer Id,bereidingstijd;
                    String naam,bereidingswijze;
                    boolean isveg;

                    JSONObject o = arr.getJSONObject(i);
                    naam = o.getString("naam");
                    bereidingswijze = o.getString("bereidingsWijze");
                    Id=o.getInt("ID");
                    bereidingstijd=o.getInt("bereidingsTijd");
                    isveg = o.getBoolean("isVegitarisch");



                    insertRecept(getWritableDatabase(),Id,naam,bereidingswijze,bereidingstijd,isveg);
                }



                api = new RestAPI();


                jsonobj=  api.GetIngredienten();
                arr  =  jsonobj.getJSONArray("Value");

                for(int i = 0 ; i < arr.length()-1 ; i++) {

                    Integer Id;
                    String naam;
                               JSONObject o = arr.getJSONObject(i);
                    naam = o.getString("naam");

                    Id=o.getInt("ID");




                    insertIngredient(getWritableDatabase(),Id,naam);
                }



                api = new RestAPI();


                jsonobj=  api.GetReceptIngredient();
                arr  =  jsonobj.getJSONArray("Value");

                for(int i = 0 ; i < arr.length()-1 ; i++) {

                    Integer ingredientID,receptID;
                    String hoeveelheid;
                    JSONObject o = arr.getJSONObject(i);
                    hoeveelheid = o.getString("hoeveelheid");

                    receptID=o.getInt("receptID");
                    ingredientID=o.getInt("ingredientID");



                   insertReceptIngredient(getWritableDatabase(),receptID,ingredientID,hoeveelheid);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            return "test";

        }
    }
}
