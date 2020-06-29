package com.sedadurmus.yenivavi.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Adapter.GonderiAdapter;
import com.sedadurmus.yenivavi.GonderiActivity;
import com.sedadurmus.yenivavi.GorevActivity;
import com.sedadurmus.yenivavi.LoginActivity;
import com.sedadurmus.yenivavi.Model.Gonderi;
import com.sedadurmus.yenivavi.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private GonderiAdapter gonderiAdapter;
    private List<Gonderi> gonderiListeleri;
    private List<String> takipListesi;

    private SwipeRefreshLayout refreshLayout;

    View myFragment;
    FloatingActionButton fab, fab1, fab2;
    TextView fab1_txt, fab2_txt;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    Boolean isOpen = false;

    ImageView gorunmez_resim;
    TextView yazi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);

        myFragment = inflater.inflate(R.layout.fragment_home, container, false);

        refreshLayout = myFragment.findViewById(R.id.refresh);

        yazi = myFragment.findViewById(R.id.gorunmez_yazi);
        gorunmez_resim = myFragment.findViewById(R.id.gorunmez_resim);

        recyclerView =myFragment.findViewById(R.id.recyler_view_HomeFragment);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        gonderiListeleri = new ArrayList<>();
        gonderiAdapter = new GonderiAdapter(getContext(), gonderiListeleri);
        recyclerView.setAdapter(gonderiAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


//        Metotları çağır
        takipKontrolu();
        gonderileriOku();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gonderiListeleri.clear();
                takipKontroluRefresh();
            }
        });

        fab = myFragment.findViewById(R.id.fab);
        fab1 = myFragment.findViewById(R.id.fab1);
        fab2 = myFragment.findViewById(R.id.fab2);
        fab1_txt = myFragment.findViewById(R.id.fab1_text);
        fab2_txt =myFragment.findViewById(R.id.fab2_text);

        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();

            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), GorevActivity.class));
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), GonderiActivity.class));
            }
        });

        return myFragment;
    }

    private void animateFab()
    {
        if (isOpen)
        {
            fab.startAnimation(rotateForward);
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab1_txt.startAnimation(fabClose);
            fab2_txt.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab1_txt.setClickable(false);
            fab2_txt.setClickable(false);
            isOpen=false;
        }
        else
        {
            fab.startAnimation(rotateBackward);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab1_txt.startAnimation(fabOpen);
            fab2_txt.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab1_txt.setClickable(true);
            fab2_txt.setClickable(true);
            isOpen=true;
        }
    }

    //TAKİP EDİLEN VE TAKİPÇİLER İLE İLGİLİ KISIM AŞAĞISI

    private void takipKontrolu() {
        FirebaseAuth fauth = FirebaseAuth.getInstance();
        if (fauth.getCurrentUser() == null) {
            Intent logoutIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(logoutIntent);
        } else {
            takipListesi = new ArrayList<>();
            final DatabaseReference takipYolu = FirebaseDatabase.getInstance().getReference("Takip")
                    .child(fauth.getCurrentUser().getUid())
                    .child("takipEdilenler");
            takipYolu.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    takipListesi.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        takipListesi.add(snapshot.getKey());
                    }
                    gonderileriOku();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void gonderileriOku() {
        final DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");

        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gonderiListeleri.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Gonderi gonderi = snapshot.getValue(Gonderi.class);
                    if (takipListesi != null) {
                        for (String id : takipListesi) {
                            String value = id.toString();
                            if (gonderi!=null&&gonderi.getGonderen().equals(value)) {
//                                if ( gonderi.isGonderiDurumu())
                                Object tarih = snapshot.child("gonderiTarihi").getValue();
                                gonderi.setTarih(tarih != null ? tarih.toString() : "");
                                Object onay = snapshot.child("onaydurumu").getValue();
                                gonderi.setOnayDurumu(onay != null && (boolean) onay);
                                if ((gonderi.isGorevmi() && gonderi.isOnayDurumu())
                                        || (!gonderi.isGorevmi() && !gonderi.isOnayDurumu()))
                                    gonderiListeleri.add(gonderi);
                            }
                        }
                        yazi.setVisibility(gonderiListeleri.size() > 0 ? View.GONE : View.VISIBLE);
                        gorunmez_resim.setVisibility(gonderiListeleri.size() > 0 ? View.GONE : View.VISIBLE);
                    } else {
                        yazi.setVisibility(gonderiListeleri.size() > 0 ? View.GONE : View.VISIBLE);
                        gorunmez_resim.setVisibility(gonderiListeleri.size() > 0 ? View.GONE : View.VISIBLE);
                    }
                }
                gonderiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //REFREEESSSSSHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
    private void takipKontroluRefresh() {
        FirebaseAuth fauth = FirebaseAuth.getInstance();
        if (fauth.getCurrentUser() == null) {
            Intent logoutIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(logoutIntent);


        } else {
            takipListesi = new ArrayList<>();
            final DatabaseReference takipYolu = FirebaseDatabase.getInstance().getReference("Takip")
                    .child(fauth.getCurrentUser().getUid())
                    .child("takipEdilenler");
            takipYolu.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    takipListesi.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        takipListesi.add(snapshot.getKey());
                    }
                    gonderileriOkuRefresh();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void gonderileriOkuRefresh() {
        final DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");
        refreshLayout.setRefreshing(false);
        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gonderiListeleri.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Gonderi gonderi = snapshot.getValue(Gonderi.class);
                    if (takipListesi != null) {
                        for (String id : takipListesi) {
                            String value = id.toString();
                            if (gonderi!=null&&gonderi.getGonderen().equals(value)) {
//                                if ( gonderi.isGonderiDurumu())
                                Object tarih = snapshot.child("gonderiTarihi").getValue();
                                gonderi.setTarih(tarih != null ? tarih.toString() : "");
                                Object onay = snapshot.child("onaydurumu").getValue();
                                gonderi.setOnayDurumu(onay != null && (boolean) onay);
                                if ((gonderi.isGorevmi() && gonderi.isOnayDurumu())
                                        || (!gonderi.isGorevmi() && !gonderi.isOnayDurumu()))
                                    gonderiListeleri.add(gonderi);
                            }
                        }
                        yazi.setVisibility(gonderiListeleri.size() > 0 ? View.GONE : View.VISIBLE);
                    } else {
                        yazi.setVisibility(gonderiListeleri.size() > 0 ? View.GONE : View.VISIBLE);
                    }
                }
                refreshLayout.setRefreshing(false);
                gonderiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



}
