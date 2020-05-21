package com.example.popularcinemas.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.popularcinemas.R;
import com.example.popularcinemas.model.Review;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ReviewAdapter extends ArrayAdapter<Review> {
    public ReviewAdapter(@NonNull Context context, ArrayList<Review> reviews) {
        super(context, 0, reviews);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View reviewListItemView = convertView;
        if (reviewListItemView == null) {
            reviewListItemView = LayoutInflater.from(getContext()).inflate(R.layout.review_list_item, parent, false);
        }
        Review currentListItem = getItem(position);
        TextView authorTextView = reviewListItemView.findViewById(R.id.author_textview);
        authorTextView.setText(currentListItem.getAuthor());

        TextView contentTextView = reviewListItemView.findViewById(R.id.content_textview);
        contentTextView.setText(currentListItem.getContent());
        return reviewListItemView;
    }
}
