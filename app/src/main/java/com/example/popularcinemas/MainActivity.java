package com.example.popularcinemas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PosterAdapter.GridItemClickListener {
    private static final String baseUrl = "https://api.themoviedb.org/3/movie/";
    private static final String apiKey = "1b36e1b2f2bacb56b80a5bab3aa001a2";

    private PosterAdapter mAdapter;
    private RecyclerView mPosterGrid;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPosterGrid = findViewById(R.id.posters_recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mPosterGrid.setLayoutManager(layoutManager);
        mPosterGrid.setHasFixedSize(true);

        mAdapter = new PosterAdapter(new ArrayList<Cinema>(), this);
        mPosterGrid.setAdapter(mAdapter);

        makeQuery();
    }

    public void loadSortOrderFromPreferences(SharedPreferences sharedPreferences) {
        String key_sort = getString(R.string.key_sort);
        String default_value = getString(R.string.value_sort_by_popularity);
        path = sharedPreferences.getString(key_sort, default_value);
    }

    public void makeQuery() {
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendPath(path)
                .appendQueryParameter("api_key", apiKey)
                .build();
        URL url = NetworkUtils.buildUrl(builtUri.toString());
        new CinemaTask().execute(url);
    }

    @Override
    public void onGridItemClick(int clickedItemIndex) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        Cinema cinema = mAdapter.getCinemaList().get(clickedItemIndex);
        String title = cinema.getTitle();
        String plot = cinema.getPlot();
        String date = cinema.getReleaseDate();
        String voteAvg = String.valueOf(cinema.getVoteAvg());
        String poster = cinema.getPoster();
        intent.putExtra("title", title);
        intent.putExtra("plot", plot);
        intent.putExtra("date", date);
        intent.putExtra("voteAvg", voteAvg);
        intent.putExtra("poster", poster);
        startActivity(intent);
    }

    public
    class CinemaTask extends AsyncTask<URL, Void, ArrayList<Cinema>> {
        @Override
        protected ArrayList<Cinema> doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            return NetworkUtils.extractCinema(searchUrl);
        }

        @Override
        protected void onPostExecute(ArrayList<Cinema> cinemaList) {
            mAdapter.updateCinemaList(cinemaList);
            mPosterGrid.setAdapter(mAdapter);
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
        int id = item.getItemId();
        if (id == R.id.sort_menu_item) {
            Intent intent = new Intent(MainActivity.this, SortActivity.class);
            startActivity(intent);
        }

        makeQuery();
        return super.onOptionsItemSelected(item);
    }
}