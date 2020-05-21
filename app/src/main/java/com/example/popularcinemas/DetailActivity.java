package com.example.popularcinemas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.popularcinemas.database.AppDatabase;
import com.example.popularcinemas.model.Cinema;
import com.example.popularcinemas.model.Video;
import com.example.popularcinemas.utilities.AppExecutors;
import com.example.popularcinemas.utilities.NetworkUtils;
import com.example.popularcinemas.utilities.VideoAdapter;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {
    private boolean isFavourite;

    private AppDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Cinema Details");

        mDB = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        final int cinemaId = intent.getIntExtra("cinemaId", -1);
        final String title = intent.getStringExtra("title");
        final String plot = intent.getStringExtra("plot");
        final String date = intent.getStringExtra("date");
        final String poster = intent.getStringExtra("poster");
        final String voteAvg = intent.getStringExtra("voteAvg");

        TextView title_textview = findViewById(R.id.title_textview);
        title_textview.setText(title);

        TextView plot_textview = findViewById(R.id.plot_textview);
        plot_textview.setText(plot);

        TextView voteAvg_textview = findViewById(R.id.vote_avg_textview);
        voteAvg_textview.setText(voteAvg + "/10");

        TextView releaseDate_textview = findViewById(R.id.release_date_textview);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date dateobj;
        String year;
        try {
            dateobj = format.parse(date);
            year = (String) DateFormat.format("yyyy", dateobj);
        } catch (ParseException e) {
            year = "N/A";
        }
        releaseDate_textview.setText(year);

        ImageView poster_imageview = findViewById(R.id.poster_imageview);
        Picasso.get()
                .load(poster)
                .into(poster_imageview);

        TextView videoTextView = findViewById(R.id.video_textview);
        videoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, VideoActivity.class);
                intent.putExtra("cinemaId", cinemaId);
                startActivity(intent);
            }
        });

        TextView reviewsTextView = findViewById(R.id.reviews_textview);
        reviewsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ReviewsActivity.class);
                intent.putExtra("cinemaId", cinemaId);
                startActivity(intent);
            }
        });

        final ImageView favButton = findViewById(R.id.fav_button);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Cinema check = mDB.cinemaDao().loadCinemaById(cinemaId);
                if (check != null) {
                    isFavourite = true;
                    favButton.setImageResource(R.drawable.ic_star_white_48dp);
                } else {
                    isFavourite = false;
                    favButton.setImageResource(R.drawable.ic_star_border_white_48dp);
                }
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Cinema favouriteCinema = new Cinema(cinemaId, title, date, poster, Double.parseDouble(voteAvg), plot);
                if(isFavourite) {
                    isFavourite = false;
                    favButton.setImageResource(R.drawable.ic_star_border_white_48dp);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDB.cinemaDao().deleteFavouriteCinema(favouriteCinema);
                        }
                    });
                } else {
                    isFavourite = true;
                    favButton.setImageResource(R.drawable.ic_star_white_48dp);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDB.cinemaDao().insertFavouriteCinema(favouriteCinema);
                        }
                    });
                }
            }
        });
    }
}