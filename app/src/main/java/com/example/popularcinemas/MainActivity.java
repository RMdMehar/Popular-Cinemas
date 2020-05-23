package com.example.popularcinemas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.popularcinemas.database.AppDatabase;
import com.example.popularcinemas.model.Cinema;
import com.example.popularcinemas.utilities.NetworkUtils;
import com.example.popularcinemas.utilities.PosterAdapter;
import com.example.popularcinemas.viewmodel.MainViewModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements PosterAdapter.GridItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private PosterAdapter mAdapter;
    private RecyclerView mPosterGrid;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        mPosterGrid = findViewById(R.id.posters_recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mPosterGrid.setLayoutManager(layoutManager);
        mPosterGrid.setHasFixedSize(true);

        mAdapter = new PosterAdapter(new ArrayList<Cinema>(), this);
        mPosterGrid.setAdapter(mAdapter);

        loadSortOrderFromPreferences(sharedPreferences);
        if (!path.equals(getString(R.string.value_sort_by_favourites))) {
            makeQuery();
        } else {
            setupViewModel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void loadSortOrderFromPreferences(SharedPreferences sharedPreferences) {
        String key_sort = getString(R.string.key_sort);
        String default_value = getString(R.string.value_sort_by_popularity);
        path = sharedPreferences.getString(key_sort, default_value);
    }

    public void makeQuery() {
        Uri builtUri = Uri.parse(NetworkUtils.baseUrl).buildUpon()
                .appendPath(path)
                .appendQueryParameter("api_key", NetworkUtils.apiKey)
                .build();
        URL url = NetworkUtils.buildUrl(builtUri.toString());
        new CinemaTask().execute(url);
    }

    @Override
    public void onGridItemClick(int clickedItemIndex) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        Cinema cinema = mAdapter.getCinemaList().get(clickedItemIndex);
        int cinemaId = cinema.getCinemaId();
        String title = cinema.getTitle();
        String plot = cinema.getPlot();
        String date = cinema.getReleaseDate();
        String voteAvg = String.valueOf(cinema.getVoteAvg());
        String poster = cinema.getPoster();
        intent.putExtra("cinemaId", cinemaId);
        intent.putExtra("title", title);
        intent.putExtra("plot", plot);
        intent.putExtra("date", date);
        intent.putExtra("voteAvg", voteAvg);
        intent.putExtra("poster", poster);
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_sort))) {
            path = sharedPreferences.getString(key, getString(R.string.value_sort_by_popularity));
            if (!path.equals(getString(R.string.value_sort_by_favourites))) {
                makeQuery();
            } else {
                setupViewModel();
            }
        }
    }

    public class CinemaTask extends AsyncTask<URL, Void, List<Cinema>> {
        @Override
        protected List<Cinema> doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            return NetworkUtils.extractCinema(searchUrl);
        }

        @Override
        protected void onPostExecute(List<Cinema> cinemaList) {
            mAdapter.updateCinemaList(cinemaList);
            mPosterGrid.setAdapter(mAdapter);
            super.onPostExecute(cinemaList);
        }
    }

    private void setupViewModel() {
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getFavouriteCinemas().observe(this, new Observer<List<Cinema>>() {
            @Override
            public void onChanged(List<Cinema> cinemaList) {
                Log.d(LOG_TAG, "Custom Log: Updating list of favourite cinemas in ViewModel");
                Log.v(LOG_TAG, "Custom Log: no of cinemas = " + cinemaList.size());

                mAdapter.updateCinemaList(cinemaList);
                mPosterGrid.setAdapter(mAdapter);
            }
        });
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

        return super.onOptionsItemSelected(item);
    }
}