package com.sedadurmus.yenivavi.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Movie implements Serializable {
    @SerializedName("popularity")
    double popularity;

    @SerializedName("vote_count")
    int vote_count;

    @SerializedName("video")
    boolean video;

    @SerializedName("poster_path")
    String poster_path;


    public double getPopularity() {
        return this.popularity;
    }
    public int getVoteCount() {
        return this.vote_count;
    }
    public boolean getVideo() { return this.video; }
    public String getPosterPath() { return this.poster_path; }

}
