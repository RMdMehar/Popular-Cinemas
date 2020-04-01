package com.example.popularcinemas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Cinema Details");

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String plot = intent.getStringExtra("plot");
        String date = intent.getStringExtra("date");
        String poster = intent.getStringExtra("poster");
        String voteAvg = intent.getStringExtra("voteAvg");

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
    }
}
