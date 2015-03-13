package com.sap.i047525.mineinbox;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.sap.i047525.mineinbox.data.GameContract;
import com.sap.i047525.mineinbox.data.GameHelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by i035921 on 2015.03.05..
 */
public class DBTest extends AndroidTestCase {

    public static final String LOG_TAG = DBTest.class.getSimpleName();

    private static final String DATABASE_NAME = "mineinboxgame.db";

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(DATABASE_NAME);
        SQLiteDatabase db = new GameHelper(this.mContext).getWritableDatabase();

        assertEquals(true, db.isOpen());

        db.close();
    }

    public void testInsertQueryDb(){
        Log.d(LOG_TAG, "----------------");

        GameHelper dbHelper = new GameHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = createCellValues(0);

        long locationRowId;
        locationRowId = db.insert(GameContract.CellEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        Cursor cursor = db.query(
                GameContract.CellEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        iterateAndLogCursor(cursor, LOG_TAG);
        validateCursor(cursor, testValues);

        db.close();

    }

    public static ContentValues createCellValues(int number) {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(GameContract.CellEntry.COLUMN_COORD_X, number);
        testValues.put(GameContract.CellEntry.COLUMN_COORD_Y, number);
        testValues.put(GameContract.CellEntry.COLUMN_STATE, number);

        return testValues;
    }

    static void iterateAndLogCursor(Cursor valueCursor, String logTag) {

        while (valueCursor.moveToNext()){
            int idx = valueCursor.getColumnIndex(GameContract.CellEntry.COLUMN_COORD_X);
            Log.d(logTag, GameContract.CellEntry.COLUMN_COORD_X + "(" + idx + ") = " + valueCursor.getString(idx));
            idx = valueCursor.getColumnIndex(GameContract.CellEntry.COLUMN_COORD_Y);
            Log.d(logTag, GameContract.CellEntry.COLUMN_COORD_Y + "(" + idx + ") = " + valueCursor.getString(idx));
            idx = valueCursor.getColumnIndex(GameContract.CellEntry.COLUMN_STATE);
            Log.d(logTag, GameContract.CellEntry.COLUMN_STATE + "(" + idx + ") = " + valueCursor.getString(idx));
        }
    }

    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();
    }


}
