package com.sap.i047525.mineinbox;

import android.database.Cursor;
import android.test.AndroidTestCase;

import com.sap.i047525.mineinbox.data.GameContract;
import com.sap.i047525.mineinbox.data.PersistenceUtility;

/**
 * Created by I035921 on 2015.03.06..
 */
public class PersistenceUtilityTest extends AndroidTestCase {

    private PersistenceUtility mUtility;

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

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mUtility = new PersistenceUtility(mContext);
        deleteAllRecords();
    }

    public void testSaveAndGetBoard(){
        int[][] board = new int[4][3];
        board[0][0] = 11;
        board[0][1] = 12;
        board[0][2] = 13;
        board[1][0] = 21;
        board[1][1] = 22;
        board[1][2] = 23;
        board[2][0] = 31;
        board[2][1] = 32;
        board[2][2] = 33;
        board[3][0] = 41;
        board[3][1] = 42;
        board[3][2] = 43;

        int count = mUtility.saveBoard(board);
        assertEquals(12, count);

        int[][] boardNew = mUtility.getBoard();

        assertEquals(board.length, boardNew.length);
        for(int x = 0; x < boardNew.length ; x++){
            assertEquals(board[x].length, boardNew[x].length);
            for(int y = 0; y < boardNew[x].length ; y++){
                assertEquals(board[x][y], boardNew[x][y]);
            }
        }



    }
}
