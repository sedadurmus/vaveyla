package com.sedadurmus.yenivavi.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AllBookCategory implements Serializable {
    @SerializedName("name")
    String name;

    @SerializedName("id")
    Integer id;

    @SerializedName("slug")
    String slug;

    @SerializedName("results")
    List<Book> results;

    public List<Book> getResults(){
        return this.results;
    }

    public String getName() {
        return this.name;
    }
    public Integer getId() {
        return this.id;
    }
}
