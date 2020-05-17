package com.example.popularcinemas.utilities;

import android.text.TextUtils;
import android.util.Log;

import com.example.popularcinemas.model.Cinema;
import com.example.popularcinemas.model.Video;

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

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    public static final String baseUrl = "https://api.themoviedb.org/3/movie/";
    public static final String apiKey = "1b36e1b2f2bacb56b80a5bab3aa001a2";

    public static ArrayList<Cinema> extractCinema(URL requestURL) {
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(requestURL);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Custom Log: Error making HTTP request", e);
        }
        return extractCinemaFromJSON(jsonResponse);
    }

    public static ArrayList<Video> extractVideo(URL requestURL) {
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(requestURL);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Custom Log: Error making HTTP request", e);
        }
        return extractVideoFromJSON(jsonResponse);
    }

    private static ArrayList<Cinema> extractCinemaFromJSON(String cinemaJSON) {
        ArrayList<Cinema> cinemas = new ArrayList<>();
        if (TextUtils.isEmpty(cinemaJSON)) {
            return null;
        }

        try {
            int i;
            JSONObject root = new JSONObject(cinemaJSON);
            JSONArray cinemaArray = root.getJSONArray("results");
            for (i=0; i<cinemaArray.length(); i++) {
                JSONObject arrayItem = cinemaArray.getJSONObject(i);
                String poster = arrayItem.getString("poster_path");
                int cinemaId = arrayItem.getInt("id");
                String title = arrayItem.getString("title");
                double voteAvg = arrayItem.getDouble("vote_average");
                String plot = arrayItem.getString("overview");
                String releaseDate = arrayItem.getString("release_date");

                cinemas.add(new Cinema(cinemaId, title, releaseDate, poster, voteAvg, plot));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Custom Log: Problem parsing JSON results", e);
        }
        return cinemas;
    }

    private static ArrayList<Video> extractVideoFromJSON(String videoJSON) {
        ArrayList<Video> videos= new ArrayList<>();
        if (TextUtils.isEmpty(videoJSON)) {
            return null;
        }

        try {
            int i;
            JSONObject root = new JSONObject(videoJSON);
            JSONArray videoArray = root.getJSONArray("results");
            for (i=0; i<videoArray.length(); i++) {
                JSONObject arrayItem = videoArray.getJSONObject(i);
                String name = arrayItem.getString("name");
                String videoKey = arrayItem.getString("key");

                videos.add(new Video(name, videoKey));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Custom Log: Problem parsing JSON results", e);
        }
        return videos;
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
