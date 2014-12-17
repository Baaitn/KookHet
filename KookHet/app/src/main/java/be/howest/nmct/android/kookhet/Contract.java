package be.howest.nmct.android.kookhet;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    public static final String AUTHORITY = "be.howest.nmct.android.kookhet";

    public interface CategorieenColumns extends BaseColumns {
        public static final String Naam = "cNaam";
        public static final String Omschrijving = "cOmschrijving";
    }

    public interface ReceptenColumns extends BaseColumns {
        public static final String Naam = "rNaam";
        public static final String Bereidingswijze = "rBereidingswijze";
        public static final String Bereidingstijd = "rBereidingstijd";
        public static final String IsVegetarisch = "rIsVegetarisch";
        public static final String IsFavoriet = "rIsFavoriet";
        public static final String IsMenu = "rIsMenu";
        public static final String Image = "rImage";
    }

    public interface ReceptCategorieColumns extends BaseColumns {
        public static final String ReceptId = "rcReceptId";
        public static final String CategorieId = "rcCategorieId";
    }

    public interface IngredientenColumns extends BaseColumns {
        public static final String Naam = "iNaam";
    }

    public interface ReceptIngredientColumns extends BaseColumns {
        public static final String ReceptId = "riReceptId";
        public static final String IngredientId = "riIngredientId";
        public static final String Hoeveelheid = "riHoeveelheid";
    }

    public static class Categorieen implements CategorieenColumns {
        public static final String TABLE_NAME = "Categorieen";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/categorieen"; //vnd.android.cursor.item/categorieen
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/categorie"; //vnd.android.cursor.item/categorie
        public static final String DEFAULT_SORT_ORDER = Naam + " ASC";

//        public static final String CONTENT_PATH = "/" + TABLE_NAME;
//        public static final int CATEGORIE_ID_PATH_POSITION = 1 ;
//        public static final String ITEM_CONTENT_PATH = "/" + TABLE_NAME +"/";
//        public static final Uri ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + ITEM_CONTENT_PATH);
    }

    public static class Recepten implements ReceptenColumns {
        public static final String TABLE_NAME = "Recepten";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/recepten"; //vnd.android.cursor.item/recepten
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/recept"; //vnd.android.cursor.item/recept
        public static final String DEFAULT_SORT_ORDER = Naam + " ASC";

//        public static final String CONTENT_PATH = "/" + TABLE_NAME;
//        public static final int RECEPT_ID_PATH_POSITION = 1;
//        public static final String ITEM_CONTENT_PATH = "/" + TABLE_NAME + "/";
//        public static final Uri ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + ITEM_CONTENT_PATH);
    }

    public static class ReceptCategorie implements ReceptCategorieColumns {
        public static final String TABLE_NAME = "ReceptCategorie";
    }

    public static class Ingredienten implements IngredientenColumns {
        public static final String TABLE_NAME = "Ingredienten";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/ingredienten"; //vnd.android.cursor.item/ingredienten
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/ingredient"; //vnd.android.cursor.item/ingredient
        public static final String DEFAULT_SORT_ORDER = Naam + " ASC";

//        public static final String CONTENT_PATH = "/" + TABLE_NAME;
//        public static final int INGREDIENT_ID_PATH_POSITION = 1;
//        public static final String ITEM_CONTENT_PATH = "/" + TABLE_NAME + "/";
//        public static final Uri ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + ITEM_CONTENT_PATH);
    }

    public static class ReceptIngredient implements ReceptIngredientColumns {
        public static final String TABLE_NAME = "ReceptIngredient";
    }
}
