package com.sap.i047525.mineinbox;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by I047525 on 2015.03.04..
 */
public class MineFieldFragment extends android.support.v4.app.Fragment {

    //Const
    private static final int BOMB = 9;
    private static final int UNPICKED = 10;
    private static final int FLAGGED = 11;

    //global
    RelativeLayout rl;
    GridView grid;
    List<String> list;
    int[][] mineField;

    int rows_num = 30;
    int cols_num = 16;
    int bomb_num = 10;


    //Begin Timer
    TextView timerView;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    //End Timer

    Utility mUtility = new Utility();

    public MineFieldFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rows_num = mUtility.getBoardSizeX(getActivity());
        cols_num = mUtility.getBoardSizeY(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //start timer
        timerView = (TextView)rootView.findViewById(R.id.timerView);
        startTimer();


        rl=(RelativeLayout) rootView.findViewById(R.id.rl);
        grid =new GridView(getActivity());

        grid.setVerticalSpacing(2);
        grid.setHorizontalSpacing(2);
        grid.setGravity(View.TEXT_ALIGNMENT_CENTER);
        grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);

        list=new ArrayList<String> ();

        initMineField(rows_num, cols_num);

        ArrayAdapter<String> adp=new ArrayAdapter<String> (getActivity(),
                R.layout.text_view_layout,list);
        grid.setNumColumns(cols_num);

        grid.setBackgroundColor(Color.BLUE);
        grid.setAdapter(adp);
        rl.addView(grid);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                /*int row = arg2 / cols_num;
                int col = arg2 % cols_num;

                //Toast.makeText(getActivity(), list.get(arg2) + ": " +
                //                Integer.toString(mineField[row][col]),
                //        Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), list.get(arg2) + ": " +
                                Integer.toString(row) + "-" + Integer.toString(col),
                        Toast.LENGTH_SHORT).show();
                arg1.setBackgroundColor(Color.RED);

                int test[][] = getNeighbours(arg2);*/

                if (isUnpicked(arg2)) {
                    if (isBomb(arg2)) {
                        //Game Over
                        stopTimer();
                        Toast.makeText(getActivity(), R.string.bomb_picked,
                                Toast.LENGTH_SHORT).show();
                        arg1.setBackgroundColor(Color.RED);
                    } else {

                    }
                } else {
                    //already picked
                    Toast.makeText(getActivity(), R.string.already_picked,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
    }

    public void initMineField(int rows, int cols) {
        mineField = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mineField[i][j] = UNPICKED;
                list.add((String) Integer.toString(mineField[i][j]));
                //list.add("");
            }
        }

        for (int i = 0; i < bomb_num; i++) {
            Random r = new Random();
            int listPos = r.nextInt(rows_num * cols_num);
            mineField[getRowForListPos(listPos)][getColForListPos(listPos)] = BOMB;
            list.set(listPos, "9");
        }
    }

    public int getRowForListPos(int listPos) {
        return listPos / cols_num;
    }

    public int getColForListPos(int listPos) {
        return listPos % cols_num;
    }

    public int getListPos(int row, int col) {
        return row * cols_num + col;
    }

    public boolean isUnpicked(int listPos) {
        if (mineField[getRowForListPos(listPos)][getColForListPos(listPos)] == UNPICKED ||
                mineField[getRowForListPos(listPos)][getColForListPos(listPos)] == FLAGGED ||
                mineField[getRowForListPos(listPos)][getColForListPos(listPos)] == BOMB) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBomb(int listPos) {
        return mineField[getRowForListPos(listPos)][getColForListPos(listPos)] == BOMB;
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    public void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    public int[][] getNeighbours(int listPos) {
        int[][] tmp = new int[8][2];
        int index = 0;
        int col = getColForListPos(listPos);
        int row = getRowForListPos(listPos);

        int col_tmp;
        int row_tmp;

        for (int i = -1; i < 2; i++) {
            col_tmp = col + i;
            if (col_tmp < 0) {
                continue;
            }
            if (col_tmp > cols_num) {
                continue;
            }
            for (int j = -1; j < 2; j++) {
                row_tmp = row + j;
                if (row_tmp < 0) {
                    continue;
                }
                if (row_tmp > rows_num) {
                    continue;
                }
                if (col == col_tmp && row == row_tmp) {continue;}

                tmp[index][0] = row_tmp;
                tmp[index][1] = col_tmp;
                index++;
            }
        }
        int[][] result = new int[index][2];
        for (int i = 0; i < index; i++) {
            result[i][0] = tmp[i][0];
            result[i][1] = tmp[i][1];
        }

        return result;
    }
    public void recalculateMineField(int listPos) {

    }
}
