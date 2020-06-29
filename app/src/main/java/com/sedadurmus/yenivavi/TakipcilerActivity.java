package com.sedadurmus.yenivavi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Adapter.KullaniciAdapter;
import com.sedadurmus.yenivavi.Model.Kullanici;

import java.util.ArrayList;
import java.util.List;

public class TakipcilerActivity extends AppCompatActivity {

    String id;
    String baslik;

    List<String> idListesi;

    RecyclerView recyclerView;
    KullaniciAdapter kullaniciAdapter;
    List<Kullanici> kullaniciListesi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takipciler);

        Intent intent = getIntent();
        id= intent.getStringExtra("id");
        baslik= intent.getStringExtra("baslik");

// Toolbar ayarları
        Toolbar toolbar = findViewById(R.id.toolbar_takipcilerActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(baslik);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        Reycler ayarları
        recyclerView =findViewById(R.id.reycler_view_takipcilerActivity);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        kullaniciListesi = new ArrayList<>();
        kullaniciAdapter = new KullaniciAdapter(TakipcilerActivity.this, kullaniciListesi);
        recyclerView.setAdapter(kullaniciAdapter);


        idListesi = new ArrayList<>();
//aşağıda switch ile bu durumda bunu yap demek istiyoruz.
        switch (baslik)
        {
            case "Beğeniler":
                begenileriAl();
                break;
            case "Takip Edilenler":
                takipEdilenleriAl();
                break;
            case "Takipçiler":
                takipcileriAl();
                break;
        }

    }

    private void begenileriAl() {

        DatabaseReference begeniYolu = FirebaseDatabase.getInstance().getReference("Begeniler").child(id);
        begeniYolu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idListesi.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    idListesi.add(snapshot.getKey());
                }
                kullanicileriGoster();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void takipEdilenleriAl() {
        DatabaseReference takipEdilenlerYolu = FirebaseDatabase.getInstance().getReference("Takip").child(id).child("takipEdilenler");
        takipEdilenlerYolu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idListesi.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    idListesi.add(snapshot.getKey());
                }
                kullanicileriGoster();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void takipcileriAl() {
        DatabaseReference takipciYolu = FirebaseDatabase.getInstance().getReference("Takip").child(id).child("takipciler");
        takipciYolu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idListesi.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    idListesi.add(snapshot.getKey());
                }
                kullanicileriGoster();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void kullanicileriGoster () {
        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar");
        kullaniciYolu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kullaniciListesi.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Kullanici kullanici =snapshot.getValue(Kullanici.class);
                    for (String id: idListesi)
                    {
                        if (kullanici.getId().equals(id))
                        {
                            kullaniciListesi.add(kullanici);
                        }
                    }
                }
                kullaniciAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}











