package com.sap.i047525.mineinbox.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by i035921 on 2015.03.05..
 */
public class GameProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private GameDbHelper mOpenHelper;

    private static final int CELL = 100;
    private static final int CELLS = 101;

    @Override
    public boolean onCreate() {
        mOpenHelper = new GameDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case CELL:
                return GameContract.CellEntry.CONTENT_ITEM_TYPE;
            case CELLS:
                return GameContract.CellEntry.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case CELLS: {
                long _id = db.insert(GameContract.CellEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = GameContract.CellEntry.buildCellUri(values.getAsInteger(GameContract.CellEntry.COLUMN_COORD_X), values.getAsInteger(GameContract.CellEntry.COLUMN_COORD_Y));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case CELL:
                rowsUpdated = db.update(
                        GameContract.CellEntry.TABLE_NAME,
                        values,
                        GameContract.CellEntry.COLUMN_COORD_X + "= ? AND " + GameContract.CellEntry.COLUMN_COORD_Y + "= ?",
                        new String[]{
//                                Integer.toString(values.getAsInteger(GameContract.CellEntry.COLUMN_COORD_X)),
//                                Integer.toString(values.getAsInteger(GameContract.CellEntry.COLUMN_COORD_Y))
                                uri.getPathSegments().get(1),   //get x,y from uri
                                uri.getPathSegments().get(2)
                        });
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case CELLS:
                rowsDeleted = db.delete(
                        GameContract.CellEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case CELL: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        GameContract.CellEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case CELLS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        GameContract.CellEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = GameContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, GameContract.PATH_CELL + "/#/#", CELL);
        matcher.addURI(authority, GameContract.PATH_CELL, CELLS);
        return matcher;
    }







}
