package com.sedadurmus.yenivavi.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.sedadurmus.yenivavi.Adapter.KullaniciAdapter;
import com.sedadurmus.yenivavi.Adapter.MovieAdapter;
import com.sedadurmus.yenivavi.Api.ApiClient;
import com.sedadurmus.yenivavi.Api.ApiInterface;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.Model.Movie;
import com.sedadurmus.yenivavi.Model.TheMovieDB;
import com.sedadurmus.yenivavi.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieSearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> mMovies;
    String searchWord;
    EditText kullanıcıAra;
    public MovieSearchFragment(MovieAdapter movieAdapter, String searchWord, List<Movie> mMovies) {
        // Required empty public constructor
        this.movieAdapter = movieAdapter;
        this.searchWord = searchWord;
        this.mMovies = mMovies;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_movie_search, container, false);

//        kullanıcıAra=view.findViewById(R.id.edit_kullanici_arama_bar);
        recyclerView =view.findViewById(R.id.reycler_view_movie_ara);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        recyclerView.setAdapter(movieAdapter);

//        kullanicileriOku();

        return view;
    }



//        private void kullanicileriOku (){
//        DatabaseReference kullanicilerYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar");
//
//        kullanicilerYolu.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (arama_bar.getText().toString().equals(""))
//                {
//                    mKullaniciler.clear();
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
//                    {
//                        Kullanici kullanici =snapshot.getValue(Kullanici.class);
//                        mKullaniciler.add(kullanici);
//                    }
//                    kullaniciAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }



}