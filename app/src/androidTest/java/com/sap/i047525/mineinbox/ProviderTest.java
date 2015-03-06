package com.sap.i047525.mineinbox;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.sap.i047525.mineinbox.data.GameContract;

/**
 * Created by i035921 on 2015.03.05..
 */
public class ProviderTest extends AndroidTestCase {

    public static final String LOG_TAG = ProviderTest.class.getSimpleName();

    // brings our database to an empty state
    public void deleteAllRecords() {
        mContext.getContentResolver().delete(
                GameContract.CellEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                GameContract.CellEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    public void setUp() {
        deleteAllRecords();
    }

    public void testInsertCells() throws Throwable{
        ContentValues cell0 = DBTest.createCellValues(1);

        ContentResolver resolver = mContext.getContentResolver();
        Uri cellUri = resolver.insert(GameContract.CellEntry.CONTENT_URI, cell0);
        Log.d(LOG_TAG, "Cell URI: " + cellUri);
//        long cellRowId = ContentUris.parseId(cellUri);
//
//        assertTrue(cellRowId != -1);
        String location = cellUri.toString();
        assertTrue(location != GameContract.CellEntry.CONTENT_URI.toString() + "/2/2");

        Cursor cursor = mContext.getContentResolver().query(
                GameContract.CellEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        DBTest.iterateAndLogCursor(cursor, LOG_TAG);
        DBTest.validateCursor(cursor, cell0);

        // Now see if we can successfully query if we include the coordinates
        cursor = mContext.getContentResolver().query(
                GameContract.CellEntry.buildCellUri(1, 1),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        DBTest.iterateAndLogCursor(cursor, LOG_TAG);
        DBTest.validateCursor(cursor, cell0);

    }

    public void testUpdateCell() {
        ContentValues values = DBTest.createCellValues(2);
        Uri cellUri = mContext.getContentResolver().
                insert(GameContract.CellEntry.CONTENT_URI, values);
        Log.d(LOG_TAG, "Cell URI: " + cellUri);
//        long cellRowId = ContentUris.parseId(cellUri);
//
//        assertTrue(cellRowId != -1);
        String location = cellUri.toString();
        assertTrue(location != GameContract.CellEntry.CONTENT_URI.toString() + "/1/1");

        values.put(GameContract.CellEntry.COLUMN_STATE, 9);
        int count = mContext.getContentResolver().update(
                cellUri,
                values,
                null,
                null
//                GameContract.CellEntry.COLUMN_COORD_X + "= ? AND " + GameContract.CellEntry.COLUMN_COORD_Y + "= ?",
//                new String[]{
//                        Integer.toString(values.getAsInteger(GameContract.CellEntry.COLUMN_COORD_X)),
//                        Integer.toString(values.getAsInteger(GameContract.CellEntry.COLUMN_COORD_Y))
//                }
        );

        assertEquals(count, 1);

        // Now see if we can successfully query if we include the coordinates
        Cursor cursor = mContext.getContentResolver().query(
                GameContract.CellEntry.buildCellUri(2, 2),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        DBTest.iterateAndLogCursor(cursor, LOG_TAG);
        DBTest.validateCursor(cursor, values);

    }



}
