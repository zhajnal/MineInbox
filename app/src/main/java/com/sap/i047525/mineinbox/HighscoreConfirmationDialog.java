package com.sap.i047525.mineinbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.sap.i047525.mineinbox.data.HighScore;

/**
 * Created by I035921 on 2015.04.08..
 */
public class HighscoreConfirmationDialog extends DialogFragment {

    public long time;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You won! Record your highscore?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HighScorePost highScorePost = new HighScorePost(getActivity());
                        HighScore h = new HighScore();

                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String playerName = settings.getString("pref_player_name", "default user");
                        String boardSize = settings.getString("pref_board size", "medium");
                        h.setName(playerName);
                        h.setTable(boardSize);
                        h.setTime(time);

                        new HighScorePost(getActivity()).execute(h);

//                        highScorePost.doInBackground(h);
//                        Toast.makeText(getActivity(), "Hmmokay",
//                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
