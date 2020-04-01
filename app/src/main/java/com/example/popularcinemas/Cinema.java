package com.example.popularcinemas;

public class Cinema {
    private String mTitle;
    private String mReleaseDate;
    private String mPoster;
    private double mVoteAvg;
    private String mPlot;

    private final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/";

    public Cinema(String title, String releaseDate, String poster, double voteAvg, String plot) {
        mTitle = title;
        mReleaseDate = releaseDate;
        mPoster = poster;
        mVoteAvg = voteAvg;
        mPlot = plot;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getPoster() {
        return IMAGE_BASE_URL + mPoster;
    }

    public double getVoteAvg() {
        return mVoteAvg;
    }

    public String getPlot() {
        return mPlot;
    }
}
