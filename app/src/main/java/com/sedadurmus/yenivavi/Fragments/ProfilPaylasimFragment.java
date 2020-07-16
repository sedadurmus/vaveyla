package com.sedadurmus.yenivavi.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Adapter.FotografAdapter;
import com.sedadurmus.yenivavi.Model.Gonderi;
import com.sedadurmus.yenivavi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ProfilPaylasimFragment extends Fragment {
    //    fotoğrafları profilde görme reyclerı

    RecyclerView recyclerViewFotograflar;
    FotografAdapter fotoAdapter;
    List<Gonderi> gonderiList = new ArrayList<>();

    private SwipeRefreshLayout refreshLayout;
    FirebaseUser mevcutKullanici;

    String profilId;

    public ProfilPaylasimFragment (){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profil_paylasim, container, false);

        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profilId = prefs.getString("profileid", "none");

        refreshLayout = view.findViewById(R.id.refresh);

        //metotlar
//        kullaniciBilgisi();
        fotograflarim();
        fotograflarimRefresh();

        //profilde paylaştıklarının görünmesi için
        recyclerViewFotograflar = view.findViewById(R.id.recyler_view_profilPaylasim);
        recyclerViewFotograflar.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewFotograflar.setLayoutManager(linearLayoutManager);
        gonderiList = new ArrayList<>();
        gonderiList.clear();
        fotoAdapter = new FotografAdapter(getContext(), gonderiList);
        recyclerViewFotograflar.setAdapter(fotoAdapter);

        return view;

    }
//    private void kullaniciBilgisi() {
//        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(profilId);
//        kullaniciYolu.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (getContext() == null) {
//                    return;
//                }
//                Kullanici kullanici =dataSnapshot.getValue(Kullanici.class);
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//    }


    //fotograflarımızı profilde göstermek için yapılanlar
    private void fotograflarim() {
        final DatabaseReference fotografYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");

        fotografYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gonderiList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Gonderi gonderi = snapshot.getValue(Gonderi.class);
                    if (gonderi != null && gonderi.getGonderen().equals(profilId)) {
                        gonderi.setGonderiUid(snapshot.getKey());
                        Object tarih = snapshot.child("gonderiTarihi").getValue();

                        gonderi.setTarih(tarih != null ? tarih.toString() : "");
                        Object onay = snapshot.child("onaydurumu").getValue();
//                        Uri uri=Uri.parse(dataSnapshot.child("gonderiVideo").getValue(true).toString());
                        gonderi.setOnayDurumu(onay != null && (boolean) onay);
                        if ((gonderi.isGorevmi() && gonderi.isOnayDurumu())
                                || (!gonderi.isGorevmi() && !gonderi.isOnayDurumu()))
                            gonderiList.add(gonderi);
                    }
                }
                Collections.reverse(gonderiList);
                fotoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    String simdikiTarih;
    private void bildirimleriEkle() {

        Date simdi = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        simdikiTarih = dateFormat.format(simdi);
        DatabaseReference bildirimEklemeYolu = FirebaseDatabase.getInstance().getReference("Bildirimler").child(profilId);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("kullaniciid", mevcutKullanici.getUid());
        hashMap.put("text", "Takip etmeye başladı");
        hashMap.put("gonderiid", "");
        hashMap.put("ispost", false);
        hashMap.put("bildirimTarihi", simdikiTarih);
        bildirimEklemeYolu.push().setValue(hashMap);
    }


    //fotograflarımızı profilde göstermek için yapılanlar
    private void fotograflarimRefresh() {
        final DatabaseReference fotografYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");

        fotografYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gonderiList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Gonderi gonderi = snapshot.getValue(Gonderi.class);
                    if (gonderi != null && gonderi.getGonderen().equals(profilId)) {
                        gonderi.setGonderiUid(snapshot.getKey());
                        Object tarih = snapshot.child("gonderiTarihi").getValue();

                        gonderi.setTarih(tarih != null ? tarih.toString() : "");
                        Object onay = snapshot.child("onaydurumu").getValue();

                        gonderi.setOnayDurumu(onay != null && (boolean) onay);
                        if ((gonderi.isGorevmi() && gonderi.isOnayDurumu())
                                || (!gonderi.isGorevmi() && !gonderi.isOnayDurumu()))
                            gonderiList.add(gonderi);
                    }
                }
                Collections.reverse(gonderiList);
                fotoAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}

