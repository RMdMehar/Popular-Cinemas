package com.example.popularcinemas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String baseUrl = "https://api.themoviedb.org/3/movie/";
    private static final String apiKey = "1b36e1b2f2bacb56b80a5bab3aa001a2";
    private static final String PATH_POPULAR = "popular";
    private static final String PATH_TOP_RATED = "top_rated";

    private PosterAdapter mAdapter;
    private RecyclerView mPosterGrid;
    private String path = PATH_POPULAR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPosterGrid = findViewById(R.id.posters_recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mPosterGrid.setLayoutManager(layoutManager);
        mPosterGrid.setHasFixedSize(true);

        mAdapter = new PosterAdapter();
        mPosterGrid.setAdapter(mAdapter);
        makeQuery();
    }

    public void makeQuery() {
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendPath(path)
                .appendQueryParameter("api_key", apiKey)
                .build();
        URL url = NetworkUtils.buildUrl(builtUri.toString());
        new CinemaTask().execute(url);
    }


    public
    class CinemaTask extends AsyncTask<URL, Void, List<Cinema>> {

        @Override
        protected List<Cinema> doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            return NetworkUtils.extractCinema(searchUrl);
        }

        @Override
        protected void onPostExecute(List<Cinema> cinemaList) {
            mAdapter.setListItems(cinemaList);
            super.onPostExecute(cinemaList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_popular_item:
                path = PATH_POPULAR;
                break;
            case R.id.sort_ratings_item:
                path = PATH_TOP_RATED;
        }
        makeQuery();
        return super.onOptionsItemSelected(item);
    }
}
