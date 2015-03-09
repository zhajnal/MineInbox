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
import android.widget.BaseAdapter;
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
    private static final int FLAGGED_EMPTY = 11;
    private static final int FLAGGED_BOMB = 12;

    //global
    RelativeLayout rl;
    GridView grid;
    List<String> list;
    int[][] mineField;

    int rows_num;// = 8;
    int cols_num;// = 8;
    //todo: read nr of bombs based on settings
    int bomb_num = 20;
    int unpickedFields;

    int bombsOnField = bomb_num;

    List<Integer> updatedFields = new ArrayList<>();


    //Begin Timer
    TextView timerView;
    long startTime = 0;

    TextView bombView;

    //runs without a timer by re-posting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerView.setText(String.format("Timer: %d:%02d", minutes, seconds));

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

        //nr of bombs
        bombView = (TextView)rootView.findViewById(R.id.bombView);
        bombView.setText(String.format("Bombs on Field: %d", bombsOnField));

        //unpicked field
        unpickedFields = rows_num * cols_num - bomb_num;


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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

                if (isUnpicked(position)) {
                    if (isBomb(position)) {
                        //Game Over
                        stopTimer();
                        Toast.makeText(getActivity(), R.string.bomb_picked,
                                Toast.LENGTH_SHORT).show();
                        view.setBackgroundColor(Color.RED);
                    } else {
                        //Game
                        recalculateMineField(position);
                        //update adapter
                        ((BaseAdapter)grid.getAdapter()).notifyDataSetChanged();
                        //view.setBackgroundColor(Color.GREEN);

                        //int firstPosition = grid.getFirstVisiblePosition();
                        //int lastPosition = grid.getLastVisiblePosition();

                        //if ((position < firstPosition) || (position > lastPosition)) {
                            //return null;}

                        //View viewTmp = grid.getChildAt(position);
                        //viewTmp.setBackgroundColor(Color.GREEN);
                        updateFieldsColor();
                        if (unpickedFields <= 0) {
                            win();
                        }

                    }
                } else {
                    //already picked
                    Toast.makeText(getActivity(), R.string.already_picked,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(getActivity(), "Item Long Clicked",
                        Toast.LENGTH_SHORT).show();*/
                changeFlag(position);
                ((BaseAdapter)grid.getAdapter()).notifyDataSetChanged();
                if (!isUnpicked(position)) {
                    view.setBackgroundColor(Color.LTGRAY);
                } else {
                    view.setBackgroundColor(Color.WHITE);
                }
                bombView.setText(String.format("Bombs on Field: %d", bombsOnField));

                return true;
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
                mineField[getRowForListPos(listPos)][getColForListPos(listPos)] == BOMB) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBomb(int listPos) {
        return mineField[getRowForListPos(listPos)][getColForListPos(listPos)] == BOMB;
    }

    public boolean isFlagged(int listPos) {
        if (mineField[getRowForListPos(listPos)][getColForListPos(listPos)] == FLAGGED_EMPTY ||
                mineField[getRowForListPos(listPos)][getColForListPos(listPos)] == FLAGGED_BOMB) {
            return true;
        } else {
            return false;
        }
    }
    
    public void changeFlag(int listPos) {
        if (isFlagged(listPos)) {
            //change to either BOMB or UNPICKED
            if (mineField[getRowForListPos(listPos)][getColForListPos(listPos)] == FLAGGED_EMPTY) {
                mineField[getRowForListPos(listPos)][getColForListPos(listPos)] = UNPICKED;
                list.set(listPos, "10");
            } else {
                mineField[getRowForListPos(listPos)][getColForListPos(listPos)] = BOMB;
                list.set(listPos, "9");
            }
            bombsOnField++;
        } else {
            //flag for FLAG_BOMB or FLAG_EMPTY
            if (mineField[getRowForListPos(listPos)][getColForListPos(listPos)] == BOMB) {
                mineField[getRowForListPos(listPos)][getColForListPos(listPos)] = FLAGGED_BOMB;
                list.set(listPos, "12");
            } else {
                mineField[getRowForListPos(listPos)][getColForListPos(listPos)] = FLAGGED_EMPTY;
                list.set(listPos, "11");
            }
            bombsOnField--;
        }
    //else Toast: Already FLAGGED_EMPTY?
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
            if (col_tmp > cols_num - 1) {
                continue;
            }
            for (int j = -1; j < 2; j++) {
                row_tmp = row + j;
                if (row_tmp < 0) {
                    continue;
                }
                if (row_tmp > rows_num - 1) {
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
        if (!isUnpicked(listPos)){
            return;
        }
        int bombNeighbours = 0;
        int[][] tmp;
        tmp = getNeighbours(listPos);
        for (int i = 0; i < tmp.length; i++) {
            if (mineField[tmp[i][0]][tmp[i][1]] == BOMB) {
                bombNeighbours ++;
            }
        }
        mineField[getRowForListPos(listPos)][getColForListPos(listPos)] = bombNeighbours;
        list.set(listPos, Integer.toString(bombNeighbours));
        updatedFields.add(listPos);
        unpickedFields--;

        //if there is no bomb neighbour
        if (bombNeighbours == 0) {
            int tmpListPos;
            for (int i = 0; i < tmp.length; i++) {
                tmpListPos = getListPos(tmp[i][0], tmp[i][1]);
                recalculateMineField(tmpListPos);
            }
        }
    }

    public void updateFieldsColor() {
        for (int i = 0; i < updatedFields.size(); i++) {
            int listPos = updatedFields.get(i);
            View view = grid.getChildAt(listPos);
            view.setBackgroundColor(Color.GREEN);
        }
        updatedFields.clear();
    }

    public void win(){
        stopTimer();
/*        Toast.makeText(getActivity(), R.string.bomb_picked,
                Toast.LENGTH_SHORT).show();
*/
        Toast.makeText(getActivity(), "Winner",
                Toast.LENGTH_SHORT).show();

    }
}
