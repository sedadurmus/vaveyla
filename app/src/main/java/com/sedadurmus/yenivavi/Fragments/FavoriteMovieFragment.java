package com.sedadurmus.yenivavi.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.sedadurmus.yenivavi.Adapter.MovieAdapter;
import com.sedadurmus.yenivavi.Model.Movie;
import com.sedadurmus.yenivavi.R;

import java.util.Collections;
import java.util.List;

public class FavoriteMovieFragment extends Fragment {

    RecyclerView recyclerViewFilmler;
    MovieAdapter movieAdapter;
    private List<Movie> movieList;
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
        movieAdapter =new MovieAdapter(getContext());
        recyclerViewFilmler.setAdapter(movieAdapter);

        loadFavorite();
        return view;

    }

    private void loadFavorite() {

        final FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference favoriYolu = FirebaseDatabase.getInstance().getReference("Favoriler").child(mevcutKullanici.getUid());

        favoriYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Movie movie = snapshot.getValue(Movie.class);

                    movieList.add(movie);

                }
                Collections.reverse(movieList);
                movieAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
