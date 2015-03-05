package com.sap.i047525.mineinbox.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by i035921 on 2015.03.04..
 */
public class GameContract {

    public static final String CONTENT_AUTHORITY = "com.sap.i047525.mineinbox";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CELL = "cell";

    public static final class CellEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CELL).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CELL;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CELL;

        public static final String TABLE_NAME = "board";
        public static final String COLUMN_COORD_X = "x";
        public static final String COLUMN_COORD_Y = "y";
        public static final String COLUMN_STATE = "state";

        public static Uri buildCellUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildCellUri(int x, int y) {
            return ContentUris.withAppendedId(ContentUris.withAppendedId(CONTENT_URI, x), y);
        }


    }

}
