package com.sedadurmus.yenivavi.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;
import com.sedadurmus.yenivavi.Adapter.MovieAdapter;
import com.sedadurmus.yenivavi.Api.ApiClient;
import com.sedadurmus.yenivavi.Api.ApiInterface;
import com.sedadurmus.yenivavi.Model.Movie;
import com.sedadurmus.yenivavi.Model.TheMovieDB;
import com.sedadurmus.yenivavi.R;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        recyclerView =view.findViewById(R.id.movie_list);
        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        GridLayoutManager gridLayoutManager =new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL , false);
        recyclerView.setLayoutManager(gridLayoutManager);
        loadMovies();

        movieAdapter =new MovieAdapter(getContext());
        recyclerView.setAdapter(movieAdapter);



        return view;
    }

    private final void loadMovies() {
        ApiInterface var10000 = (ApiInterface)ApiClient.createService(ApiInterface.class);
        if (var10000 != null) {
            ApiInterface apiService = var10000;
            Call call = apiService.getMovies("https://api.themoviedb.org/3/movie/popular?api_key=b7ee738bdfe5a91a0cec31c619d58968&language=tr");
            call.enqueue((Callback)(new Callback() {
                public void onResponse(@NotNull Call call, @NotNull Response response) {

                    TheMovieDB theMovieDB = (TheMovieDB)response.body();
                    movies = theMovieDB.getResults();
                    Integer len = movies.toArray().length;
                    movieAdapter.notifyDataSetChanged();
                    loadDataAction(theMovieDB.getResults());
                    Log.e("TheMovieDB", len.toString());
                }

                public void onFailure(@NotNull Call call, @NotNull Throwable t) {

                    Log.e("MOVIES", "request fail");
                }
            }));
        }
    }
    private void loadDataAction( List<Movie> items) {

        if (items != null) {
            List<Movie> models = items;
            Log.e("EKLEME", models.toArray().toString());
            Collections.reverse(items);
            movieAdapter.addAll(items);

            movieAdapter.notifyDataSetChanged();



        }
    }
}
