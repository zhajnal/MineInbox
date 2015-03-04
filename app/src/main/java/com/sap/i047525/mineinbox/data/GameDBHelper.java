package com.sap.i047525.mineinbox.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by i035921 on 2015.03.04..
 */
public class GameDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mineinboxgame.db";

    public GameDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_BOARD_TABLE = "CREATE TABLE " + GameContract.Board.TABLE_NAME + " (" +
                GameContract.Board._ID + " INTEGER PRIMARY KEY," +
                GameContract.Board.COLUMN_COORD_X + " INTEGER NOT NULL, " +
                GameContract.Board.COLUMN_COORD_Y + " INTEGER NOT NULL, " +
                GameContract.Board.COLUMN_STATE + " INTEGER NOT NULL );";

//        sqLiteDatabase.execSQL(SQL_CREATE_BOARD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GameContract.Board.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
