package com.sap.i047525.mineinbox.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by i035921 on 2015.03.04..
 */
public class GameHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mineinboxgame.db";

    public GameHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_BOARD_TABLE = "CREATE TABLE " + GameContract.CellEntry.TABLE_NAME + " (" +
                GameContract.CellEntry._ID + " INTEGER PRIMARY KEY," +
                GameContract.CellEntry.COLUMN_COORD_X + " INTEGER NOT NULL, " +
                GameContract.CellEntry.COLUMN_COORD_Y + " INTEGER NOT NULL, " +
                GameContract.CellEntry.COLUMN_STATE + " INTEGER NOT NULL );";

        final String SQL_CREATE_HIGHSCORE_TABLE = "CREATE TABLE " + GameContract.CellEntry.TABLE_NAME + " (" +
                GameContract.CellEntry._ID + " INTEGER PRIMARY KEY," +
                GameContract.HighScoreEntry.COLUMN_OBJECTID + " TEXT NOT NULL, " +
                GameContract.HighScoreEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                GameContract.HighScoreEntry.COLUMN_TABLE+ " TEXT NOT NULL " +
                GameContract.HighScoreEntry.COLUMN_TIME + " INTEGER NOT NULL, " +
                GameContract.HighScoreEntry.COLUMN_CREATEDAT + " TEXT NOT NULL, " +
                GameContract.HighScoreEntry.COLUMN_UPDATEDAT + " TEXT NOT NULL, " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_BOARD_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_HIGHSCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GameContract.CellEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
