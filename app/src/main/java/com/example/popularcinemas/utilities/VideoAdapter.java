package com.example.popularcinemas.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.popularcinemas.R;
import com.example.popularcinemas.model.Video;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class VideoAdapter extends ArrayAdapter<Video> {
    public VideoAdapter(@NonNull Context context, ArrayList<Video> videos) {
        super(context, 0, videos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View videoListItemView = convertView;
        if (videoListItemView == null) {
            videoListItemView = LayoutInflater.from(getContext()).inflate(R.layout.video_list_item, parent, false);
        }
        Video currentListItem = getItem(position);
        TextView videoLabel = videoListItemView.findViewById(R.id.video_label);
        videoLabel.setText(currentListItem.getName());
        return videoListItemView;
    }
}
