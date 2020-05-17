package com.example.popularcinemas.model;

public class Video {
    private String mName;
    private String mVideoKey;

    public Video(String name, String videoKey) {
        mName = name;
        mVideoKey = videoKey;
    }

    public String getName() {
        return mName;
    }

    public String getVideoKey() {
        return mVideoKey;
    }
}
