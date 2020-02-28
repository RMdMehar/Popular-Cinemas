package com.example.popularcinemas;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class NetworkUtils {
    public static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    public static List<Cinema> extractCinema(URL requestURL) {
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(requestURL);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Custom Log: Error making HTTP request", e);
        }
        return extractCinemaFromJSON(jsonResponse);
    }

    private static List<Cinema> extractCinemaFromJSON(String cinemaJSON) {
        ArrayList<Cinema> cinemas = new ArrayList<>();
        if (TextUtils.isEmpty(cinemaJSON)) {
            return null;
        }

        try {
            int i;
            JSONObject root = new JSONObject(cinemaJSON);
            JSONArray array = root.getJSONArray("results");
            for (i=0; i<array.length(); i++) {
                JSONObject arrayItem = array.getJSONObject(i);
                String poster = arrayItem.getString("poster_path");
                String title = arrayItem.getString("title");
                double voteAvg = arrayItem.getDouble("vote_average");
                String plot = arrayItem.getString("overview");
                String releaseDate = arrayItem.getString("release_date");

                cinemas.add(new Cinema(title, releaseDate, poster, voteAvg, plot));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Custom Log: Problem parsing JSON results", e);
        }
        return cinemas;
    }

    public static URL buildUrl(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Custom Log: Error with creating URL", e);
        }

        return url;
    }

    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code:" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Custom Log: Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
