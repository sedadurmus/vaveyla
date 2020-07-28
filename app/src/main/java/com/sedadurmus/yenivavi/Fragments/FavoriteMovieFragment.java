package com.sedadurmus.yenivavi.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.sedadurmus.yenivavi.Adapter.MovieAdapter;
import com.sedadurmus.yenivavi.Api.ApiClient;
import com.sedadurmus.yenivavi.Api.ApiInterface;
import com.sedadurmus.yenivavi.Model.Movie;
import com.sedadurmus.yenivavi.Model.TheMovieDB;
import com.sedadurmus.yenivavi.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteMovieFragment extends Fragment {

    RecyclerView recyclerViewFilmler;
    MovieAdapter movieAdapter;
    private List<Movie> movies;
    Context mContext;

    private SwipeRefreshLayout refreshLayout;
    FirebaseUser mevcutKullanici;

    String profilId;
    public FavoriteMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_favorite_movie, container, false);

        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profilId = prefs.getString("profileid", "none");

        recyclerViewFilmler =view.findViewById(R.id.recyler_view_filmCerceve);
        recyclerViewFilmler.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager =new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL , false);
        recyclerViewFilmler.setLayoutManager(gridLayoutManager);
        movies =new ArrayList<>();
        movieAdapter =new MovieAdapter(getContext());
        recyclerViewFilmler.setAdapter(movieAdapter);

        loadFavorite();
        return view;

    }

    private void loadFavorite() {

        final FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        assert mevcutKullanici != null;
        final DatabaseReference favoriYolu = FirebaseDatabase.getInstance().getReference("Favoriler");

        favoriYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movies.clear();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    final Movie movie = snapshot.getValue(Movie.class);
                    assert movie != null;

                    if (snapshot.child(mevcutKullanici.getUid()).child(movie.getId()).exists()) {
                        ApiInterface var10000 = (ApiInterface) ApiClient.createService(ApiInterface.class);
                        if (var10000 != null) {
                            ApiInterface apiService = var10000;
                            Call call = apiService.getMovies("https://api.themoviedb.org/3/movie/popular?api_key=b7ee738bdfe5a91a0cec31c619d58968&language=tr");
                            call.enqueue((Callback) (new Callback() {
                                public void onResponse(@NotNull Call call, @NotNull Response response) {

                                    TheMovieDB theMovieDB = (TheMovieDB) response.body();
                                    movies = theMovieDB.getResults();
                                    Integer len = movies.toArray().length;
                                    loadDataAction(theMovieDB.getResults());
                                    Log.e("TheMovieDB", len.toString());
                                    movies.add(movie);


                                }

                                public void onFailure(@NotNull Call call, @NotNull Throwable t) {

                                    Log.e("MOVIES", "request fail");
                                }
                            }));
                        }
                    }

                }
                Collections.reverse(movies);
                movieAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
