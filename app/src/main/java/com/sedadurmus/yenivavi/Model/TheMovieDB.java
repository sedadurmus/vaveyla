package com.sedadurmus.yenivavi.Model;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class TheMovieDB implements Serializable {
    @SerializedName("page")
    Integer page;

    @SerializedName("total_results")
    Integer total_results;

    @SerializedName("total_pages")
    Integer total_pages;

    @SerializedName("results")
    List<Movie> results;


    public Integer getPage() {
        return this.page;
    }
    public Integer getTotalResults() {
        return this.total_results;
    }
    public Integer getTotalPages() {
        return this.total_pages;
    }
    public List<Movie> getResults() {
        return this.results;
    }
}