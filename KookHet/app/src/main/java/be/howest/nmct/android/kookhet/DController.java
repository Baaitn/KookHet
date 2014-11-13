package be.howest.nmct.android.kookhet;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Tom on 11/13/2014.
 */
public class DController {
    public static final String AUTHORITY = "be.howest.nmct.android.kookhet";
    public static final String ACCOUNT_TYPE = "be.howest.nmct.android.kookhet";
    public interface CategorieColumns extends BaseColumns{
        public static final String NAME = "name";


    }
    public static class Categorie implements CategorieColumns{
        public static final String CONTENT_DIRECTORY ="orders";
        public static final String DEFAULT_SORT_ORDER = NAME +" ASC";
        public static final int CAT_ID_PATH_POSITION = 1 ;
        public static final String CONTENT_TYPE = "android.cursor.dir/kookhet.DB.Categorie";
        public static final String CONTENT_ITEM_TYPE= "android.cursor.item/kookhet.DB.Categorie";
        public static final String ITEM_CONTENT_PATH = "/" + CONTENT_DIRECTORY +"/";
        public static final Uri ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + ITEM_CONTENT_PATH);
        public static final String CONTENT_PATH = "/" + CONTENT_DIRECTORY;
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + CONTENT_PATH);
        //verder aanvullen met wat we nodig hebben
        // de  content type en content item type goed path geven

    }
}
