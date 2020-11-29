package com.sedadurmus.yenivavi.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Adapter.KullaniciAdapter;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.R;

import java.util.ArrayList;
import java.util.List;

public class AramaBarFragment extends Fragment {

    private RecyclerView recyclerView;
    private KullaniciAdapter kullaniciAdapter;
    private List<Kullanici> mKullaniciler;
    private SwipeRefreshLayout refreshLayout;
    SearchView ara;
    EditText arama_bar;
    ImageView geri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView =view.findViewById(R.id.recyler_view_Arama);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        refreshLayout =view.findViewById(R.id.refresh);
//        arama_bar =view.findViewById(R.id.edit_arama_bar);
        ara = view.findViewById(R.id.searchView);
        geri = view.findViewById(R.id.geri);
        mKullaniciler = new ArrayList<>();
        kullaniciAdapter =new KullaniciAdapter(getContext(), mKullaniciler);
        recyclerView.setAdapter(kullaniciAdapter);

        kullanicileriOku();


        ara.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                kullaniciAra(s);
                return false;
            }
        });



        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment()).commit();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mKullaniciler.clear();
                kullanicileriOkuRefresh();
            }
        });

        return  view;
    }

        private void kullaniciAra (String s)
    {
        Query sorgu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").orderByChild("kullaniciadi")
                .startAt(s)
                .endAt(s+"\uf8ff");

        sorgu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mKullaniciler.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Kullanici kullanici = snapshot.getValue(Kullanici.class);
                    mKullaniciler.add(kullanici);
                }
                kullaniciAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void kullanicileriOku ()
    {
        DatabaseReference kullanicilerYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar");

        kullanicilerYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (ara.getQuery().toString().equals(""))
                {
                    mKullaniciler.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Kullanici kullanici =snapshot.getValue(Kullanici.class);
                        mKullaniciler.add(kullanici);
                    }
                    kullaniciAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void kullanicileriOkuRefresh ()
    {
        DatabaseReference kullanicilerYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar");

        refreshLayout.setRefreshing(false);

        kullanicilerYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (ara.getQuery().toString().equals(""))
                {
                    mKullaniciler.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Kullanici kullanici =snapshot.getValue(Kullanici.class);
                        mKullaniciler.add(kullanici);
                    }
                    kullaniciAdapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}