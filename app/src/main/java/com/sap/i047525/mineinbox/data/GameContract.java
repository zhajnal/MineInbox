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
    public static final String PATH_HIGHSCORE = "highscore";

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

    public static final class HighScoreEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HIGHSCORE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_HIGHSCORE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_HIGHSCORE;

        public static final String TABLE_NAME = "highscore";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_TABLE = "table";
        public static final String COLUMN_CREATEDAT = "createdAt";
        public static final String COLUMN_UPDATEDAT = "updatedAt";
        public static final String COLUMN_OBJECTID = "objectId";

        public static Uri buildHighScoreUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}
