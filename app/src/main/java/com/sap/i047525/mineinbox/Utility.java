package com.sap.i047525.mineinbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by I035921 on 2015.03.06..
 */
public class Utility {


    public int getBoardSizeX(Context context){
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        String size = sharedPrefs.getString("pref_board size",
                context.getString(R.string.pref_title_board_medium));
        switch(size){
            case "1": {
                return 8;
            }
            case "2": {
                return 16;
            }
            case "3": {
                return 30;
            }
            default:
                return 16;
        }
    }

    public int getBoardSizeY(Context context){
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        String size = sharedPrefs.getString("pref_board size",
                context.getString(R.string.pref_title_board_medium));
        switch(size){
            case "1": {
                return 8;
            }
            case "2": {
                return 16;
            }
            case "3": {
                return 16;
            }
            default:
                return 16;
        }
    }

    public int getNrOfBombs(Context context){
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        String size = sharedPrefs.getString("pref_board size",
                context.getString(R.string.pref_title_board_medium));
        switch(size){
            case "1": {
                return 10;
            }
            case "2": {
                return 40;
            }
            case "3": {
                return 99;
            }
            default:
                return 99;
        }
    }


}
