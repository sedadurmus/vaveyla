package com.sedadurmus.yenivavi.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Kitap implements Serializable {
    @SerializedName("mTitle")
    String mTitle;

    @SerializedName("mAuthors")
    String mAuthors;

    @SerializedName("mPublishedDate")
    String mPublishedDate;

    @SerializedName("mDescription")
    String mDescription;

    @SerializedName("mCategories")
    String mCategories;

    @SerializedName("mThumbnail")
    String mThumbnail;

    @SerializedName("mPreview")
    String mPreview;

    @SerializedName("mPrice")
    String mPrice;

    @SerializedName("mUrl")
    String mUrl;

    @SerializedName("pageCount")
    Integer pageCount;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmAuthors() {
        return mAuthors;
    }

    public void setmAuthors(String mAuthors) {
        this.mAuthors = mAuthors;
    }

    public String getmPublishedDate() {
        return mPublishedDate;
    }

    public void setmPublishedDate(String mPublishedDate) {
        this.mPublishedDate = mPublishedDate;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmCategories() {
        return mCategories;
    }

    public void setmCategories(String mCategories) {
        this.mCategories = mCategories;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public String getmPreview() {
        return mPreview;
    }

    public void setmPreview(String mPreview) {
        this.mPreview = mPreview;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }
}
