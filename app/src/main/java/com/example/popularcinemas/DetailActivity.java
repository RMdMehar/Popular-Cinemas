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

import com.example.popularcinemas.model.Video;
import com.example.popularcinemas.utilities.NetworkUtils;
import com.example.popularcinemas.utilities.VideoAdapter;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {
    ListView videoListView;
    private VideoAdapter videoAdapter;
    ArrayList<Video> videoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Cinema Details");

        Intent intent = getIntent();
        int cinemaId = intent.getIntExtra("cinemaId", -1);
        String title = intent.getStringExtra("title");
        String plot = intent.getStringExtra("plot");
        String date = intent.getStringExtra("date");
        String poster = intent.getStringExtra("poster");
        String voteAvg = intent.getStringExtra("voteAvg");

        TextView title_textview = findViewById(R.id.title_textview);
        title_textview.setText(title);

        /*TextView plot_textview = findViewById(R.id.plot_textview);
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
                .into(poster_imageview);*/

        videoListView = findViewById(R.id.video_list);
        videoAdapter = new VideoAdapter(this, videoArrayList);
        videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Video currentVideo = videoAdapter.getItem(position);
                String videoKey = currentVideo.getVideoKey();
                Intent videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoKey));
                startActivity(videoIntent);
            }
        });
        queryForVideos(cinemaId);
    }

    public void queryForVideos(int cinemaId) {
        Uri builtUri = Uri.parse(NetworkUtils.baseUrl).buildUpon()
                .appendPath(String.valueOf(cinemaId))
                .appendPath("videos")
                .appendQueryParameter("api_key", NetworkUtils.apiKey)
                .build();
        URL url = NetworkUtils.buildUrl(builtUri.toString());
        new VideoTask().execute(url);
    }

    public class VideoTask extends AsyncTask<URL, Void, ArrayList<Video>> {

        @Override
        protected ArrayList<Video> doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            return NetworkUtils.extractVideo(searchUrl);
        }

        @Override
        protected void onPostExecute(ArrayList<Video> videoList) {
            videoAdapter.addAll(videoList);
            videoListView.setAdapter(videoAdapter);
        }
    }
}
