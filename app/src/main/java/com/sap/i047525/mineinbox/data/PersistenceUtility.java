package com.sap.i047525.mineinbox.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by I035921 on 2015.03.06..
 */
public class PersistenceUtility {

    private class Cell{
        public int x;
        public int y;
        public int state;
    }

    private final String LOG_TAG = PersistenceUtility.class.getSimpleName();
    private final Context mContext;

    public PersistenceUtility(Context context){
        mContext = context;
    }

    public void deleteBoard() {
        mContext.getContentResolver().delete(
                GameContract.CellEntry.CONTENT_URI,
                null,
                null
        );
    }

//    public int updateCellState(int x, int y, int state){
//        ContentValues values = new ContentValues();
//        values.put(GameContract.CellEntry.COLUMN_COORD_X, x);
//        values.put(GameContract.CellEntry.COLUMN_COORD_Y, y);
//        values.put(GameContract.CellEntry.COLUMN_STATE, state);
//
//        Uri target = GameContract.CellEntry.buildCellUri(x, y);
//
//        int count = mContext.getContentResolver().update(
//                target,
//                values,
//                null,
//                null
//        );
//        return count;
//    }

    public int saveBoard(int[][] board){
        deleteBoard();
        ArrayList<ContentValues> values = new ArrayList<ContentValues>();

        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board[x].length; y++){
                ContentValues value = new ContentValues();
                value.put(GameContract.CellEntry.COLUMN_COORD_X, x);
                value.put(GameContract.CellEntry.COLUMN_COORD_Y, y);
                value.put(GameContract.CellEntry.COLUMN_STATE, board[x][y]);
                values.add(value);
            }
        }
        ContentValues[] valuesArray = new ContentValues[values.size()];
        valuesArray = values.toArray(valuesArray);
        int count = mContext.getContentResolver().bulkInsert(GameContract.CellEntry.CONTENT_URI, valuesArray);
        return count;
    }

    public int[][] getBoard(){
        ArrayList<Cell> cells = new ArrayList<Cell>();
        Set<Integer> xs = new HashSet<>();  //used to store distinct x values => x size of board
        Set<Integer> ys = new HashSet<>();

        Cursor cursor = mContext.getContentResolver().query(
                GameContract.CellEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        while (cursor.moveToNext()){
            int idx;
            Cell cell = new Cell();

            idx = cursor.getColumnIndex(GameContract.CellEntry.COLUMN_COORD_X);
            cell.x = cursor.getInt(idx);
            xs.add(cell.x);

            idx = cursor.getColumnIndex(GameContract.CellEntry.COLUMN_COORD_Y);
            cell.y = cursor.getInt(idx);
            ys.add(cell.y);

            idx = cursor.getColumnIndex(GameContract.CellEntry.COLUMN_STATE);
            cell.state = cursor.getInt(idx);

            cells.add(cell);
        }
        cursor.close();

        int[][] board = new int[xs.size()][ys.size()];

        for (Iterator<Cell> i = cells.iterator(); i.hasNext();){
            Cell c = i.next();
            board[c.x][c.y] = c.state;
        }

        return board;
    }


}
