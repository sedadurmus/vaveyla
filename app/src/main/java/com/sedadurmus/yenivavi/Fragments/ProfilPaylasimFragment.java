package com.sedadurmus.yenivavi.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Adapter.FotografAdapter;
import com.sedadurmus.yenivavi.AyarlarActivity;
import com.sedadurmus.yenivavi.MessageActivity;
import com.sedadurmus.yenivavi.Model.Gonderi;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.ProfilDuzenleActivity;
import com.sedadurmus.yenivavi.R;
import com.sedadurmus.yenivavi.TakipcilerActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.sedadurmus.yenivavi.LoginActivity.kullanici;


public class ProfilPaylasimFragment extends Fragment {
    //    fotoğrafları profilde görme reyclerı

    RecyclerView recyclerViewFotograflar;
    FotografAdapter fotoAdapter;
    List<Gonderi> gonderiList = new ArrayList<>();
    private TextView txt_gonderiler, txt_takipciler, txt_takipEdilenler, txt_Ad, txt_bio, txt_kullaniciAdi, txt_puan;
    private SwipeRefreshLayout refreshLayout;
    FirebaseUser mevcutKullanici;
    private Button btn_profili_düzenle;
    ImageView profil_resmi, ayarlar, mesaj, puan_resim;
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

        profil_resmi = view.findViewById(R.id.profil_resmi_profilCercevesi);
        puan_resim = view.findViewById(R.id.txt_puan_resim);
        ayarlar = view.findViewById(R.id.profil_ayarlar);
        mesaj = view.findViewById(R.id.profil_message);
        txt_gonderiler = view.findViewById(R.id.txt_gonderiler_profilCercevesi);
        txt_takipciler = view.findViewById(R.id.txt_takipciler_profilCercevesi);
        txt_takipEdilenler = view.findViewById(R.id.txt_takip_profilCercevesi);

        txt_Ad = view.findViewById(R.id.txt_ad_profilCercevesi);
        txt_puan = view.findViewById(R.id.txt_puan_profilCercevesi);
        txt_kullaniciAdi = view.findViewById(R.id.kullaniciAdi_toolbar);
        btn_profili_düzenle = view.findViewById(R.id.btn_profiliDuzenle_profilCercevesi);
        refreshLayout = view.findViewById(R.id.refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                gonderiList.clear();
                kullaniciBilgisiRefresh();
            }
        });
        //metotlar
        kullaniciBilgisi();
        takipcileriAl();
        gonderiSayisiAl();
        fotograflarim();
//        fotograflarimRefresh();

        //profilde paylaştıklarının görünmesi için
        recyclerViewFotograflar = view.findViewById(R.id.recyler_view_profilPaylasim);
        recyclerViewFotograflar.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewFotograflar.setLayoutManager(linearLayoutManager);
        gonderiList = new ArrayList<>();
        gonderiList.clear();
        fotoAdapter = new FotografAdapter(getContext(), gonderiList);
        recyclerViewFotograflar.setAdapter(fotoAdapter);

        ayarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AyarlarActivity.class));
            }
        });

        // profil id ile mevcutKullanici Uid si eşitse butonda Profili düzenle yazsın. Değilse kaydet butonu görünmez olsun.
        if (profilId.equals(mevcutKullanici.getUid())) {
            btn_profili_düzenle.setText("Profili Düzenle");
        } else {
            takipKontrolu();
        }

        btn_profili_düzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = btn_profili_düzenle.getText().toString();
                if (btn.equals("Profili Düzenle")) {
                    //profili düzenlemeye gitsin.
                    startActivity(new Intent(getContext(), ProfilDuzenleActivity.class));
                } else if (btn.equals("takip et")) {
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(mevcutKullanici.getUid())
                            .child("takipEdilenler").child(profilId).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(profilId)
                            .child("takipciler").child(mevcutKullanici.getUid()).setValue(true);

                    bildirimleriEkle();

                } else if (btn.equals("takip ediliyor")) {
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(mevcutKullanici.getUid())
                            .child("takipEdilenler").child(profilId).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(profilId)
                            .child("takipciler").child(mevcutKullanici.getUid()).removeValue();
                }
            }
        });

        txt_takipciler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TakipcilerActivity.class);
                intent.putExtra("id", profilId);
                intent.putExtra("baslik", "Takipçiler");
                startActivity(intent);
            }
        });

        txt_takipEdilenler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TakipcilerActivity.class);
                intent.putExtra("id", profilId);
                intent.putExtra("baslik", "Takip Edilenler");
                startActivity(intent);

            }
        });

//        profil puanı 0-100 arası ise gümüş,
//        100- 200 ise bronz,
//        200 - 300 arası ile altın kuş tüyü olsun.
//        if (txt_puan.getText().toString().length() <= 100 ){
//            puan_resim.setImageResource(R.drawable.logo_silver);
//        } else if (txt_puan.getText().toString().length() <= 200){
//            puan_resim.setImageResource(R.drawable.logo_bronze);
//        } else {
//            puan_resim.setImageResource(R.drawable.coin);
//        }

//        başaksının profiline girince ayarlar butonunu gizleyip, mesaj atma ikonu koydum.
        if (profilId.equals(mevcutKullanici.getUid())){
            ayarlar.setVisibility(View.VISIBLE);
        }else {
            ayarlar.setVisibility(View.INVISIBLE);
        }
        if (profilId.equals(mevcutKullanici.getUid())){
            mesaj.setVisibility(View.INVISIBLE);
        }else {
            mesaj.setVisibility(View.VISIBLE);
        }
        mesaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MessageActivity.class);
                intent.putExtra("profileid", kullanici.getId());
                startActivity(intent);
            }
        });



        return view;

    }
    private void kullaniciBilgisi() {
        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(profilId);
        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() == null) {
                    return;
                }
                Kullanici kullanici =dataSnapshot.getValue(Kullanici.class);
                Glide.with(getContext()).load(kullanici.getResimurl()).into(profil_resmi);
                txt_Ad.setText(kullanici.getAd());
                txt_puan.setText(String.valueOf((int) kullanici.getProfilPuan()));
                txt_kullaniciAdi.setText(kullanici.getKullaniciadi());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void takipKontrolu() {
        DatabaseReference takipYolu = FirebaseDatabase.getInstance().getReference().child("Takip").child(mevcutKullanici.getUid())
                .child("takipEdilenler");

        takipYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profilId).exists()) {
                    btn_profili_düzenle.setText("takip ediliyor");
                } else {
                    btn_profili_düzenle.setText("takip et");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void takipcileriAl() {
        //Takipçi saysını alır
        DatabaseReference takipciYolu = FirebaseDatabase.getInstance().getReference().child("Takip").child(profilId)
                .child("takipciler");
        takipciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txt_takipciler.setText("" + dataSnapshot.getChildrenCount());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //Takip edilen sayısını alır
        DatabaseReference takipedilenYolu = FirebaseDatabase.getInstance().getReference().child("Takip").child(profilId)
                .child("takipEdilenler");
        takipedilenYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txt_takipEdilenler.setText("" + dataSnapshot.getChildrenCount());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void gonderiSayisiAl() {
        DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");
        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Gonderi gonderi = snapshot.getValue(Gonderi.class);
                    if (gonderi.isGorevmi()) {
                        continue;
                    }
                    if (gonderi == null && gonderi.getGonderen().length() == 0) {
                        txt_gonderiler.setText(String.valueOf(0));
                        return;
                    }
                    if (gonderi.getGonderen().equals(profilId)) {
                        i++;
                    }
                }
                txt_gonderiler.setText(String.valueOf(i));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


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

    private void kullaniciBilgisiRefresh() {

        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(profilId);
        refreshLayout.setRefreshing(false);
        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() == null) {
                    return;
                }

                Kullanici kullanici =dataSnapshot.getValue(Kullanici.class);
                Glide.with(getContext()).load(kullanici.getResimurl()).into(profil_resmi);
                txt_Ad.setText(kullanici.getAd());
                txt_puan.setText(String.valueOf((int) kullanici.getProfilPuan()));
                takipcileriAlRefresh();
                gonderiSayisiAlRefresh();
//                fotograflarimRefresh();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
    private void takipcileriAlRefresh() {
        //Takipçi saysını alır
        DatabaseReference takipciYolu = FirebaseDatabase.getInstance().getReference().child("Takip").child(profilId)
                .child("takipciler");
        refreshLayout.setRefreshing(false);
        takipciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txt_takipciler.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Takip edilen sayısını alır
        DatabaseReference takipedilenYolu = FirebaseDatabase.getInstance().getReference().child("Takip").child(profilId)
                .child("takipEdilenler");

        takipedilenYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                txt_takipEdilenler.setText("" + dataSnapshot.getChildrenCount());
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void gonderiSayisiAlRefresh() {

        DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");
        refreshLayout.setRefreshing(false);
        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Gonderi gonderi = snapshot.getValue(Gonderi.class);
                    if (gonderi.isGorevmi()) {
                        continue;
                    }
                    if (gonderi == null && gonderi.getGonderen().length() == 0) {
                        txt_gonderiler.setText(String.valueOf(0));
                        return;
                    }
                    if (gonderi.getGonderen().equals(profilId)) {
                        i++;
                    }
                }
                txt_gonderiler.setText(String.valueOf(i));
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

