package com.example.popularcinemas.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_cinemas")
public class Cinema {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String mTitle;
    private String mReleaseDate;
    private String mPoster;
    private double mVoteAvg;
    private String mPlot;

    @Ignore
    public Cinema(String title, String releaseDate, String poster, double voteAvg, String plot) {
        mTitle = title;
        mReleaseDate = releaseDate;
        mPoster = poster;
        mVoteAvg = voteAvg;
        mPlot = plot;
    }

    public Cinema(int id, String title, String releaseDate, String poster, double voteAvg, String plot) {
        mTitle = title;
        mReleaseDate = releaseDate;
        mPoster = poster;
        mVoteAvg = voteAvg;
        mPlot = plot;
    }

    public int getId() {
        return id;
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