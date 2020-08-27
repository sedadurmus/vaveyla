package com.sedadurmus.yenivavi.Api;

import com.sedadurmus.yenivavi.Model.AllBookCategory;
import com.sedadurmus.yenivavi.Model.Book;
import com.sedadurmus.yenivavi.Model.Movie;
import com.sedadurmus.yenivavi.Model.TheMovieDB;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiInterface {
    @GET
    Call<TheMovieDB> getMovies(@Url String url);
    @GET
    Call<Movie> getMovie(@Url String url);

    @GET
    Call<List<AllBookCategory>> getCategories(@Url String url);

    @GET
    Call<List<Book>> getBooks(@Url String url);
//    @GET
//    Call<List<ProductModel>> getProducts(@Url String url);
//    @GET
//    Call<List<NewsModel>> getNews(@Url String url);

//    @POST
//    Call<List<Item>> postItems(@Url String url);

    }