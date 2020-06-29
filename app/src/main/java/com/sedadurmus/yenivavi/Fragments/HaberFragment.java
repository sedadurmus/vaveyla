package com.sedadurmus.yenivavi.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Adapter.BildirimAdapter;
import com.sedadurmus.yenivavi.Model.Bildiren;
import com.sedadurmus.yenivavi.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HaberFragment extends Fragment {
    private ViewPager viewPager;
    private RecyclerView recyclerView;
    private BildirimAdapter bildirimAdapter;
    private List<Bildiren> bildirimListesi;
    private SwipeRefreshLayout refreshLayout;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_haber, container, false);

        refreshLayout = view.findViewById(R.id.refresh);
        recyclerView =view.findViewById(R.id.reycler_bildirimCercevesi);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        bildirimListesi =new ArrayList<>();
        bildirimAdapter = new BildirimAdapter(getContext(), bildirimListesi);
        recyclerView.setAdapter(bildirimAdapter);

        viewPager = view.findViewById(R.id.viewPager);

        bildirimleriOku();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bildirimListesi.clear();
                bildirimleriOkuRefresh();
            }
        });

        return view;
    }

    // bunları bildirim olan sayfaya ekleeeeeeeeeeeeeeeeeeeeee
    private void bildirimleriOku() {
        FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference bildirimYolu = FirebaseDatabase.getInstance().getReference("Bildirimler").child(mevcutKullanici.getUid());
        bildirimYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bildirimListesi.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bildiren bildirim = snapshot.getValue(Bildiren.class);
                    bildirim.setTarih(snapshot.child("bildirimTarihi").getValue().toString());
                    bildirimListesi.add(bildirim);
                }
                Collections.reverse(bildirimListesi);
                bildirimAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    //    Refresh
    private void bildirimleriOkuRefresh() {
        FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference bildirimYolu = FirebaseDatabase.getInstance().getReference("Bildirimler").child(mevcutKullanici.getUid());
        refreshLayout.setRefreshing(false);
        bildirimYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bildirimListesi.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Bildiren bildirim = snapshot.getValue(Bildiren.class);
                    bildirim.setTarih(snapshot.child("bildirimTarihi").getValue().toString());
                    bildirimListesi.add(bildirim);
                }
                Collections.reverse(bildirimListesi);
                refreshLayout.setRefreshing(false);
                bildirimAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
