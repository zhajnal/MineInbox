package com.sap.i047525.mineinbox;

import android.test.AndroidTestCase;

import com.sap.i047525.mineinbox.data.HighScore;

/**
 * Created by i035921 on 2015.03.13..
 */
public class HighScoreRESTTest extends AndroidTestCase {

    public void testPostHighScore(){
        HighScorePost hp = new HighScorePost(mContext);
        HighScore h = new HighScore();
        h.setName("Krisztian teszt 1");
        h.setTable("medium");
        h.setTime(1);
        hp.doInBackground(h);

    }

}
