package be.howest.nmct.android.kookhet;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    public static final String AUTHORITY = "be.howest.nmct.android.kookhet";

    public interface CategorieenColumns extends BaseColumns {
        public static final String Naam = "cNaam";
        public static final String Omschrijving = "cOmschrijving";
    }

    public static class Categorieen implements CategorieenColumns {
        public static final String CONTENT_DIRECTORY = "Categorieen";
        public static final String DEFAULT_SORT_ORDER = Naam +" ASC";
        public static final int CATEGORIE_ID_PATH_POSITION = 1 ;
        public static final String CONTENT_TYPE = "android.cursor.dir/kookhet.DB.Categorie";
        public static final String CONTENT_ITEM_TYPE= "android.cursor.item/kookhet.DB.Categorie";
        public static final String ITEM_CONTENT_PATH = "/" + CONTENT_DIRECTORY +"/";
        public static final Uri ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + ITEM_CONTENT_PATH);
        public static final String CONTENT_PATH = "/" + CONTENT_DIRECTORY;
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + CONTENT_PATH);
    }

    public interface ReceptCategorieColumns extends BaseColumns {
        public static final String ReceptId = "rcReceptId";
        public static final String CategorieId = "rcCategorieId";
    }

    public static class ReceptCategorie implements ReceptCategorieColumns {
        public static final String CONTENT_DIRECTORY = "ReceptCategorie";
    }

    public interface ReceptenColumns extends BaseColumns {
        public static final String Naam = "rNaam";
        public static final String Bereidingswijze = "rBereidingswijze";
        public static final String Bereidingstijd = "rBereidingstijd";
        public static final String IsVegetarisch = "rIsVegetarisch";
        public static final String Image = "rImage";
    }

    public static class Recepten implements ReceptenColumns {
        public static final String CONTENT_DIRECTORY = "Recepten";
        public static final String DEFAULT_SORT_ORDER = Naam + " ASC";
        public static final int RECEPT_ID_PATH_POSITION = 1;
        public static final String CONTENT_TYPE = ""; //in te vullen
        public static final String CONTENT_ITEM_TYPE = ""; //in te vullen
        public static final String ITEM_CONTENT_PATH = "/" + CONTENT_DIRECTORY + "/";
        public static final Uri ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + ITEM_CONTENT_PATH);
        public static final String CONTENT_PATH = "/" + CONTENT_DIRECTORY;
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + CONTENT_PATH);
    }

    public interface ReceptIngredientColumns extends BaseColumns {
        public static final String ReceptId = "riReceptId";
        public static final String IngredientId = "riIngredientId";
        public static final String HoeveelheidId = "riHoeveelheid";
    }

    public static class ReceptIngredient implements ReceptIngredientColumns {
        public static final String CONTENT_DIRECTORY = "ReceptIngredient";
    }

    public interface IngredientenColumns extends BaseColumns {
        public static final String Naam = "iNaam";
    }

    public static class Ingredienten implements IngredientenColumns {
        public static final String CONTENT_DIRECTORY = "Ingredienten";
        public static final String DEFAULT_SORT_ORDER = Naam + " ASC";
        public static final int INGREDIENT_ID_PATH_POSITION = 1;
        public static final String CONTENT_TYPE = ""; //in te vullen
        public static final String CONTENT_ITEM_TYPE = ""; //in te vullen
        public static final String ITEM_CONTENT_PATH = "/" + CONTENT_DIRECTORY + "/";
        public static final Uri ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + ITEM_CONTENT_PATH);
        public static final String CONTENT_PATH = "/" + CONTENT_DIRECTORY;
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + CONTENT_PATH);
    }
}
