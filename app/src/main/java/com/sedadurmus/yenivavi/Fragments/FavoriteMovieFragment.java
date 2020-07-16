package com.sedadurmus.yenivavi.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sedadurmus.yenivavi.Adapter.FotografAdapter;
import com.sedadurmus.yenivavi.Model.Gonderi;
import com.sedadurmus.yenivavi.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMovieFragment extends Fragment {

    RecyclerView recyclerViewFotograflar;
    FotografAdapter movieAdapter;
    List<Gonderi> movieList = new ArrayList<>();
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

//        //profilde paylaştıklarının görünmesi için
//        recyclerViewFotograflar = view.findViewById(R.id.recyler_view_profilCercevesi);
//        recyclerViewFotograflar.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        recyclerViewFotograflar.setLayoutManager(linearLayoutManager);
//        movieList = new ArrayList<>();
//        movieList.clear();
//        movieAdapter =new MovieAdapter(getContext(), movieList);
//        recyclerViewFotograflar.setAdapter(movieAdapter);

        return view;

    }
}
