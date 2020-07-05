package com.sedadurmus.yenivavi.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Movie implements Serializable {
    @SerializedName("popularity")
    private
    double popularity;

    @SerializedName("vote_count")
    private
    Integer vote_count;

    @SerializedName("video")
    boolean video;

    @SerializedName("poster_path")
    private
    String poster_path;

    @SerializedName("title")
    String title;

    @SerializedName("id")
    String id;

    @SerializedName("overview")
    String overview;

    @SerializedName("release_date")
    String release_date;

    @SerializedName("vote_average")
    Double vote_average;



    public double getPopularity() {
        return this.popularity;
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
    public Integer getVote_count() {
        return this.vote_count;
    }
    public String getId() {
        return this.id;
    }

}
