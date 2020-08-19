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

    @SerializedName("books")
    List<Book> books;

    public List<Book> getBooks(){
        return this.books;
    }

    public String getName() {
        return this.name;
    }
    public Integer getId() {
        return this.id;
    }
}
