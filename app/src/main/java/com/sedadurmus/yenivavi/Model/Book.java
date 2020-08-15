package com.sedadurmus.yenivavi.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Book implements Serializable {

    @SerializedName("name")
    String name;

    @SerializedName("id")
    Integer id;

    @SerializedName("slug")
    String slug;

    @SerializedName("description")
    String description;

    @SerializedName("img_url")
    String img_url;
    @SerializedName("isbn")
    String isbn;

    @SerializedName("author")
    String author;

    @SerializedName("page_count")
    Integer page_count;
    @SerializedName("translator")
    String translator;
    @SerializedName("printing_office")
    String printing_office;

    @SerializedName("price")
    Double price;
    @SerializedName("published_year")
    Integer published_year;

    public String getName() {
        return this.name;
    }
    public Integer getId() {
        return this.id;
    }

    public String getSlug() {
        return this.slug;
    }
    public String getImg_url() {
        return this.img_url;
    }
    public String getIsbn() {
        return this.isbn;
    }
    public String getAuthor() {
        return this.author;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPage_count() {
        return page_count;
    }

    public String getTranslator() {
        return translator;
    }

    public String getPrinting_office() {
        return printing_office;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getPublished_year() {
        return published_year;
    }
}
