package com.example.popularcinemas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {
    private static final String LOG_TAG = PosterAdapter.class.getSimpleName();
    private ArrayList<Cinema> cinemaList;

    public PosterAdapter(ArrayList<Cinema> arrayList) {
        cinemaList = arrayList;
    }

    public void updateCinemaList(ArrayList<Cinema> updatedList) {
        cinemaList.clear();
        cinemaList.addAll(updatedList);
    }

    @NonNull
    @Override
    public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.poster_grid_item, parent, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PosterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if ((cinemaList == null)||(cinemaList.isEmpty())) {
            return 0;
        }
        return cinemaList.size();
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder {
        ImageView posterUnit;

        public PosterViewHolder(View itemView) {
            super(itemView);

            posterUnit = itemView.findViewById(R.id.poster_unit);
        }

        public void bind(int position) {
            Cinema cinema = cinemaList.get(position);
            String posterPath = cinema.getPoster();
            Log.v(LOG_TAG, "Custom Log: Poster path is " + posterPath);
            Picasso.get()
                    .load(posterPath)
                    .into(posterUnit);
        }
    }
}
