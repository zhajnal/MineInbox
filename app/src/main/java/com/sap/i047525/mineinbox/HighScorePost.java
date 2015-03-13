package com.sap.i047525.mineinbox;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.sap.i047525.mineinbox.data.GameContract;
import com.sap.i047525.mineinbox.data.HighScore;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by i035921 on 2015.03.13..
 */
public class HighScorePost extends AsyncTask<HighScore, Void, String> {

    private final String LOG_TAG = HighScorePost.class.getSimpleName();

    private ArrayAdapter<String> mHighScoreAdapter;
    private final Context mContext;

    public HighScorePost(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(HighScore... params) {

        final String FORECAST_BASE_URL = "https://api.parse.com/1/classes/TopScores";

        HighScore highScore = params[0];

        HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost(FORECAST_BASE_URL);
        JSONObject object = new JSONObject();
        try {

            object.put("name", highScore.getName());
            object.put("time", highScore.getTime());
            object.put("table", highScore.getTable());

            String message = object.toString();

            post.setEntity(new StringEntity(message, "UTF8"));
            post.setHeader("Content-type", "application/json");
            post.setHeader("Content-Type", "application/json");
            post.setHeader("X-Parse-Application-Id", "2Ec0BJbqZaQyq33s6GGKvcpQYIG2hsUSXdYtDpXk");
            post.setHeader("X-Parse-REST-API-Key", "gd8mcZqZCZNfgqWAPJv6jpCSeGtUSHcnZQKCgbeO");
            HttpResponse resp = hc.execute(post);
            if (resp != null) {
                if (resp.getStatusLine().getStatusCode() == 204)
                    return "" + resp.getStatusLine().getStatusCode();
            }

            Log.d(LOG_TAG, "POST respone code:" + resp.getStatusLine().getStatusCode());
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);

        }
        return null;

    }


}
