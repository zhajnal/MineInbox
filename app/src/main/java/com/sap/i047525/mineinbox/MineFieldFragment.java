package com.sap.i047525.mineinbox;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by I047525 on 2015.03.04..
 */
public class MineFieldFragment extends android.support.v4.app.Fragment {

    RelativeLayout rl;
    GridView grid;
    List<String> list;
    int[][] mineField;

    int rows_num = 10;
    int cols_num = 15;
    int bomb_num = 10;

    public MineFieldFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        rl=(RelativeLayout) rootView.findViewById(R.id.rl);
        grid =new GridView(getActivity());
        list=new ArrayList<String> ();

        /*for (int i = 0; i < rows_num; i++) {
            for (int j = 0; j < cols_num; j++) {
                /*String str = Integer.toString(i) + ";" + Integer.toString(j);
                list.add(str);*/

                /*mineField[i][j] = 10;
                if (nrOfBomb < bomb_num) {
                    Boolean isBomb = new Random().nextBoolean();
                    if (isBomb == true) {
                        mineField[i][j] = 9;
                        nrOfBomb++;
                    }
                }
                list.add((String) Integer.toString(mineField[i][j]));
            }
        }*/

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
                int row = arg2 / cols_num;
                int col = arg2 % cols_num;

                /*Toast.makeText(getActivity(), list.get(arg2) + ": " +
                                Integer.toString(mineField[row][col]),
                        Toast.LENGTH_SHORT).show();*/
                Toast.makeText(getActivity(), list.get(arg2) + ": " +
                                Integer.toString(row) + "-" + Integer.toString(col),
                        Toast.LENGTH_SHORT).show();

            }
        });

        return rootView;
    }

    public void initMineField(int rows, int cols) {
        mineField = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mineField[i][j] = 10;
                list.add((String) Integer.toString(mineField[i][j]));
            }
        }

        for (int i = 0; i < bomb_num; i++) {
            Random r = new Random();
            int listPos = r.nextInt(rows_num * cols_num);
            list.set(listPos, "9");
            mineField[getRowForListPos(listPos)][getColForListPos(listPos)] = 9;
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
}
