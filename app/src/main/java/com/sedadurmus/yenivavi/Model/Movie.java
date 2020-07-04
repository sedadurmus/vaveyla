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

    @SerializedName("title")
    String title;

    @SerializedName("overview")
    String overview;

    @SerializedName("release_date")
    String release_date;

    @SerializedName("vote_average")
    Double vote_average;


    public double getPopularity() {
        return this.popularity;
    }
    public int getVoteCount() {
        return this.vote_count;
    }
    public boolean getVideo() {
        return this.video;
    }
    public String getPosterPath() {
        return this.poster_path;
    }
    public String getTitle() {
        return this.title;
    }
    public String getOverview() {
        return this.overview;
    }
    public String getRelease_date() {
        return this.release_date;
    }
    public double getVote_average() {
        return this.vote_average;
    }

}
