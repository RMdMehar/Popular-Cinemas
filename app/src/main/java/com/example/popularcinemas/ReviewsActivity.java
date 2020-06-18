package com.example.popularcinemas;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.popularcinemas.model.Review;
import com.example.popularcinemas.utilities.NetworkUtils;
import com.example.popularcinemas.utilities.ReviewAdapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ReviewsActivity extends AppCompatActivity {
    ListView reviewListView;
    TextView emptyReviewsView;
    private ReviewAdapter reviewAdapter;
    ArrayList<Review> reviewArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        emptyReviewsView = findViewById(R.id.empty_reviews_view);

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
            if (reviewList.isEmpty()) {
                emptyReviewsView.setVisibility(View.VISIBLE);
                reviewListView.setVisibility(View.GONE);
            } else {
                emptyReviewsView.setVisibility(View.GONE);
                reviewListView.setVisibility(View.VISIBLE);
                reviewAdapter.addAll(reviewList);
                reviewListView.setAdapter(reviewAdapter);
            }
        }
    }
}