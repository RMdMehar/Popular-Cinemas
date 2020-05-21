package com.example.popularcinemas.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_cinemas")
public class Cinema {
    @PrimaryKey
    private int mCinemaId;
    private String mTitle;
    private String mReleaseDate;
    private String mPoster;
    private double mVoteAvg;
    private String mPlot;


    public Cinema(int cinemaId, String title, String releaseDate, String poster, double voteAvg, String plot) {
        mCinemaId = cinemaId;
        mTitle = title;
        mReleaseDate = releaseDate;
        mPoster = poster;
        mVoteAvg = voteAvg;
        mPlot = plot;
    }

    public int getId() {
        return mCinemaId;
    }

    public int getCinemaId() {
        return mCinemaId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getPoster() {
        String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/";
        return IMAGE_BASE_URL + mPoster;
    }

    public double getVoteAvg() {
        return mVoteAvg;
    }

    public String getPlot() {
        return mPlot;
    }
}
