package com.example.popularcinemas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.popularcinemas.model.Video;
import com.example.popularcinemas.utilities.NetworkUtils;
import com.example.popularcinemas.utilities.VideoAdapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends AppCompatActivity {
    ListView videoListView;
    private VideoAdapter videoAdapter;
    ArrayList<Video> videoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent intent = getIntent();
        int cinemaId = intent.getIntExtra("cinemaId", -1);

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

    public class VideoTask extends AsyncTask<URL, Void, List<Video>> {

        @Override
        protected List<Video> doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            return NetworkUtils.extractVideo(searchUrl);
        }

        @Override
        protected void onPostExecute(List<Video> videoList) {
            videoAdapter.addAll(videoList);
            videoListView.setAdapter(videoAdapter);
        }
    }
}
