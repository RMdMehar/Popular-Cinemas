package com.example.popularcinemas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.popularcinemas.model.Review;
import com.example.popularcinemas.model.Video;
import com.example.popularcinemas.utilities.NetworkUtils;
import com.example.popularcinemas.utilities.ReviewAdapter;
import com.example.popularcinemas.utilities.VideoAdapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity {
    ListView reviewListView;
    private ReviewAdapter reviewAdapter;
    ArrayList<Review> reviewArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Intent intent = getIntent();
        int cinemaId = intent.getIntExtra("cinemaId", -1);

        reviewListView = findViewById(R.id.reviews_list);
        reviewAdapter = new ReviewAdapter(this, reviewArrayList);

        queryForReviews(cinemaId);
    }

    public void queryForReviews(int cinemaId) {
        Uri builtUri = Uri.parse(NetworkUtils.baseUrl).buildUpon()
                .appendPath(String.valueOf(cinemaId))
                .appendPath("reviews")
                .appendQueryParameter("api_key", NetworkUtils.apiKey)
                .build();
        URL url = NetworkUtils.buildUrl(builtUri.toString());
        new ReviewTask().execute(url);
    }

    public class ReviewTask extends AsyncTask<URL, Void, List<Review>> {

        @Override
        protected List<Review> doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            return NetworkUtils.extractReview(searchUrl);
        }

        @Override
        protected void onPostExecute(List<Review> reviewList) {
            reviewAdapter.addAll(reviewList);
            reviewListView.setAdapter(reviewAdapter);
        }
    }
}
