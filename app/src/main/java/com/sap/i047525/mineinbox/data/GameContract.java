package com.sap.i047525.mineinbox.data;

import android.provider.BaseColumns;

/**
 * Created by i035921 on 2015.03.04..
 */
public class GameContract {

    public static final String CONTENT_AUTHORITY = "com.sap.i047525.mineinbox";

    public static final class Board implements BaseColumns{
        public static final String TABLE_NAME = "board";
        public static final String COLUMN_COORD_X = "x";
        public static final String COLUMN_COORD_Y = "y";
        public static final String COLUMN_STATE = "state";
    }

}
