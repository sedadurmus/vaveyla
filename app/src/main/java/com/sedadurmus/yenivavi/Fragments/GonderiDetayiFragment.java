package com.sedadurmus.yenivavi.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Adapter.GonderiAdapter;
import com.sedadurmus.yenivavi.Model.Gonderi;
import com.sedadurmus.yenivavi.R;

import java.util.ArrayList;
import java.util.List;


public class GonderiDetayiFragment extends Fragment {

    public Context mContext;
    private RecyclerView recyclerView;
    private GonderiAdapter gonderiAdapter;
    private List<Gonderi> gonderiListesi;
    ImageView geri;
    String gonderiId;

    public GonderiDetayiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gonderi_detayi, container, false);

        SharedPreferences preferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        gonderiId = preferences.getString("postid","none");

        recyclerView = view.findViewById(R.id.reycler_view_gonderiDetayi);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager  =new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        gonderiListesi =new ArrayList<>();
        gonderiAdapter= new GonderiAdapter(getContext(),gonderiListesi);
        recyclerView.setAdapter(gonderiAdapter);
        geri = view.findViewById(R.id.geri_detay);

        gonderiOku();

        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HaberFragment()).commit();
            }
        });

        return view;
    }

    private void gonderiOku()
    {
        DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Gonderiler").child(gonderiId);

        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gonderiListesi.clear();

                Gonderi gonderi =dataSnapshot.getValue(Gonderi.class);
                gonderiListesi.add(gonderi);

                gonderiAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
