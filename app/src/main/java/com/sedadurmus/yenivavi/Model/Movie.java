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

    @SerializedName("BackdropPath")
    String  BackdropPath;

    public String getBackdropPath() {
        return BackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        BackdropPath = backdropPath;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

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
