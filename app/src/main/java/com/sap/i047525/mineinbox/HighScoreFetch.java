package com.sap.i047525.mineinbox;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.sap.i047525.mineinbox.data.GameContract;
import com.sap.i047525.mineinbox.data.HighScore;

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
public class HighScoreFetch extends AsyncTask<String, Void, HighScore[]> {

    private final String LOG_TAG = HighScoreFetch.class.getSimpleName();

    private ArrayAdapter<String> mHighScoreAdapter;
    private final Context mContext;

    public HighScoreFetch(Context context, ArrayAdapter<String> highScoreAdapter) {
        mContext = context;
        mHighScoreAdapter = highScoreAdapter;
    }

    @Override
    protected HighScore[] doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String highScoreJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String FORECAST_BASE_URL =
                    "https://api.parse.com/1/classes/TopScores";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL);
            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("X-Parse-Application-Id", "2Ec0BJbqZaQyq33s6GGKvcpQYIG2hsUSXdYtDpXk");
            urlConnection.setRequestProperty("X-Parse-REST-API-Key", "gd8mcZqZCZNfgqWAPJv6jpCSeGtUSHcnZQKCgbeO");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            highScoreJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getHighScoreDataFromJson(highScoreJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        // This will only happen if there was an error getting or parsing the forecast.
        return null;

    }

    private HighScore[] getHighScoreDataFromJson(String highScoreJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.

        // Location information
        final String OWM_RESULTS = "results";
        final String OWM_CREATEDAT = "createdAt";
        final String OWM_UPDATEDAT = "updatedAt";
        final String OWM_NAME = "name";
        final String OWM_OBJECTID = "objectId";
        final String OWM_TABLE = "table";
        final String OWM_TIME = "time";
        

        JSONObject resultsJson = new JSONObject(highScoreJsonStr);
        JSONArray resultArray = resultsJson.getJSONArray(OWM_RESULTS);

        // Get and insert the new weather information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(resultArray.length());
        //String[] resultStrs = new String[resultArray.length()];
        HighScore[] resultObjs = new HighScore[resultArray.length()];

        for(int i = 0; i < resultArray.length(); i++) {
            // These are the values that will be collected.

            HighScore highScore = new HighScore();

            JSONObject highScoreJson = resultArray.getJSONObject(i);

            highScore.setCreatedAt(highScoreJson.getString(OWM_CREATEDAT));
            highScore.setUpdatedAt(highScoreJson.getString(OWM_UPDATEDAT));
            highScore.setName(highScoreJson.getString(OWM_NAME));
            highScore.setObjectId(highScoreJson.getString(OWM_OBJECTID));
            highScore.setTable(highScoreJson.getString(OWM_TABLE));
            highScore.setTime(highScoreJson.getLong(OWM_TIME));

            ContentValues highScoreValues = new ContentValues();

            highScoreValues.put(GameContract.HighScoreEntry.COLUMN_NAME, highScore.getName());
            highScoreValues.put(GameContract.HighScoreEntry.COLUMN_UPDATEDAT, highScore.getUpdatedAt());
            highScoreValues.put(GameContract.HighScoreEntry.COLUMN_CREATEDAT,highScore.getCreatedAt());
            highScoreValues.put(GameContract.HighScoreEntry.COLUMN_OBJECTID,highScore.getObjectId());
            highScoreValues.put(GameContract.HighScoreEntry.COLUMN_TABLE,highScore.getTable());
            highScoreValues.put(GameContract.HighScoreEntry.COLUMN_TIME, highScore.getTime());

            cVVector.add(highScoreValues);
            //resultStrs[i] = day + " - " + description + " - " + highAndLow;
            resultObjs[i] = highScore;
        }
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            int rowsInserted = mContext.getContentResolver()
                    .bulkInsert(GameContract.HighScoreEntry.CONTENT_URI, cvArray);
            Log.v(LOG_TAG, "inserted " + rowsInserted + " rows of highScore data");
            // Use a DEBUG variable to gate whether or not you do this, so you can easily
            // turn it on and off, and so that it's easy to see what you can rip out if
            // you ever want to remove it.
/*
            if (DEBUG) {
                Cursor weatherCursor = mContext.getContentResolver().query(
                        WeatherEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );

                if (weatherCursor.moveToFirst()) {
                    ContentValues resultValues = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(weatherCursor, resultValues);
                    Log.v(LOG_TAG, "Query succeeded! **********");
                    for (String key : resultValues.keySet()) {
                        Log.v(LOG_TAG, key + ": " + resultValues.getAsString(key));
                    }
                } else {
                    Log.v(LOG_TAG, "Query failed! :( **********");
                }
            }
*/
        }
        return resultObjs;
    }

    @Override
    protected void onPostExecute(HighScore[] result) {
        if (result != null) {
            mHighScoreAdapter.clear();
            for(HighScore highScore : result) {
                String s = highScore.getName()+" ("+highScore.getTable()+", "+highScore.getTimeString()+")";
                mHighScoreAdapter.add(s);
            }
            // New data is back from the server.  Hooray!
        }
    }

}
