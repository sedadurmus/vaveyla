package com.sedadurmus.yenivavi.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Adapter.MagazaAdapter;
import com.sedadurmus.yenivavi.Model.MagazaFire;
import com.sedadurmus.yenivavi.R;

import java.util.ArrayList;
import java.util.List;

public class DukkanFragment extends Fragment {

//    private Multipart _multipart;

    private SwipeRefreshLayout refreshLayout;
    private static final String username="vaveylayardim@gmail.com";
    private static final String password ="sedat4825";

    RecyclerView profileListView;
    List<MagazaFire> shopList = new ArrayList<MagazaFire>();

    MagazaAdapter magazaAdapter;

    public static Context GetContext;

    public static MagazaFire seciliUrun;

    public static AlertDialog.Builder dialog;
    boolean isOkey, urunAlindi;
    DatabaseReference magazaYolu;
    DatabaseReference kullaniciYolu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_dukkan, container, false);
        LoadMissions();
        isOkey = true;
        urunAlindi = true;
        magazaAdapter = new MagazaAdapter(getContext(), shopList);
//        dialog = new AlertDialog.Builder(getContext());
//        dialog.setTitle("Uyarı")
//                .setMessage(seciliUrun != null ? seciliUrun.getMagazaBasligi() : "" + " isimli alışverişin gerçekleşmesini istiyor musunuz?")
//                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (seciliUrun == null) return;
//                        if (LoginActivity.getKullanici().getProfilPuan() >= seciliUrun.getMagazaPuani()) {
//
//                            kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                            kullaniciYolu.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    if (!isOkey) return;
//                                    Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
//                                    HashMap<String, Object> kullaniciGuncelleHashMap = new HashMap<>();
//                                    kullaniciGuncelleHashMap.put("profilPuan", kullanici.getProfilPuan() - seciliUrun.getMagazaPuani());
//                                    kullaniciYolu.updateChildren(kullaniciGuncelleHashMap);
//
//                                    DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("AlinanUrunler");
//                                    String gonderiId = veriYolu.push().getKey();
//                                    HashMap<String, Object> hashMap = new HashMap<>();
//                                    hashMap.put("magazaId", seciliUrun.getMagazaId());
//                                    hashMap.put("gonderen", kullanici.getId());
//                                    hashMap.put("tarih", new Date().toString());
//                                    veriYolu.child(gonderiId).setValue(hashMap);
//                                    veriYolu = null;
//
//                                    String userData = WriteFile.Instance.ReadDataFromFile(getContext());
//                                    if (userData != null && userData.length() > 0) {
//                                        String[] users = userData.split("(?<=-)", 2);
//                                        email = users[0];
//                                    }
//                                    message = "Sayın kullanıcımız " + LoginActivity.kullanici.getAd() + "," + " \n " + seciliUrun.getMagazaBasligi() + " isimli alışverişiniz başarıyla gerçekleşti. Alışveriş bilgileri için bu maile geri dönüş yapmanızı rica ediyoruz. ";
//                                    subject = "Tebrikler";
////                                    sendMail(email, subject, message);
//                                    new Thread(new Runnable() {
//
//                                        @Override
//                                        public void run() {
//                                            MailSender mail = new MailSender("vaveylayardim@gmail.com", "sedat4825",email.replace("-",""),subject,message);
//                                            try {
//                                                mail.createEmailMessage(getContext());
//                                                mail.sendEmail();
//                                            } catch (Exception e) {
//                                                String s=e.getMessage();
//                                            }
//      //           tMailSender.send(null, "", subject, message, email);
//
//                                        }
//                                    }).start();
//
////                                    veriYolu = FirebaseDatabase.getInstance().getReference("Magaza");
////                                    hashMap = new HashMap<>();
////                                    hashMap.put("magazaAdet", seciliUrun.getMagazaAdet() - 1);
////                                    veriYolu.updateChildren(seciliUrun.getMagazaId()).setValue(hashMap);
//                                    isOkey = false;
//
//
//                                    magazaYolu = FirebaseDatabase.getInstance().getReference("Magaza").child(seciliUrun.getUid());
//                                    magazaYolu.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            if (!urunAlindi) return;
//
//                                            MagazaFire urun = dataSnapshot.getValue(MagazaFire.class);
//
//                                            HashMap<String, Object> magazaYoluGuncelleHashMap = new HashMap<>();
//                                            magazaYoluGuncelleHashMap.put("magazaAdet", urunAdet);
//
//                                            if (urunAlindi)
//                                                magazaYolu.updateChildren(magazaYoluGuncelleHashMap);
//                                            isOkey = false;
//
//                                        }
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                                        }
//                                    });
//                                }
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                }
//                            });
//                            Toast.makeText(getContext(), "Tebrikler! Hemen mail adresine bakmalısın!", Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(getContext(), "Puanın yetersiz! \n Görevleri gözden geçirmeye ne dersin?", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                Toast.makeText(getContext(), "Dükkana gözatmaya devam etmelisin..", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        }).create();

        GetContext = getContext();
        profileListView =view.findViewById(R.id.shop_list);
        profileListView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        profileListView.setLayoutManager(linearLayoutManager);

        refreshLayout =view.findViewById(R.id.refresh);
        profileListView.setAdapter(magazaAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shopList.clear();
                LoadMissionsRefresh();
            }
        });
        return view;
    }

    public static void showDialog(MagazaFire urun) {
        seciliUrun = urun;
        if (seciliUrun == null) return;
        urunAdet = (int) seciliUrun.getMagazaAdet() - 1;
//        dialog.setMessage(seciliUrun.getMagazaBasligi() + " isimli alışverişin gerçekleşmesini istiyor musunuz?");
//        dialog.show();
    }

    public static int urunAdet = 0;
    String email;
    String message;
    String subject;

    void LoadMissions() {
        final DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Magaza");
        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MagazaFire magaza = new MagazaFire();
                    magaza.setMagazaBasligi(((String) snapshot.child("magazaBasligi").getValue()));
                    magaza.setMagazaAdet((Long) snapshot.child("magazaAdet").getValue());
                    magaza.setMagazaDurumu(((boolean) snapshot.child("magazaDurumu").getValue()));
                    magaza.setUid(snapshot.getKey());
                    magaza.setMagazaHakkinda(((String) snapshot.child("magazaHakkinda").getValue()));
                    magaza.setMagazaId(((String) snapshot.child("magazaId").getValue()));
                    magaza.setMagazaPuani(((Long) snapshot.child("magazaPuani").getValue()));
                    magaza.setMagazaResmi(((String) snapshot.child("magazaResmi").getValue()));

                    if (magaza.isMagazaDurumu() && magaza.getMagazaAdet() > 0) {
                        shopList.add(magaza);
                    }
                }
                magazaAdapter = new MagazaAdapter(getContext(), shopList);
                profileListView.setAdapter(magazaAdapter);
                magazaAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

//REFRESHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHhhhh
    private void LoadMissionsRefresh() {
        final DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Magaza");
        refreshLayout.setRefreshing(false);
        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MagazaFire magaza = new MagazaFire();
                    magaza.setMagazaBasligi(((String) snapshot.child("magazaBasligi").getValue()));
                    magaza.setMagazaAdet((Long) snapshot.child("magazaAdet").getValue());
                    magaza.setMagazaDurumu(((boolean) snapshot.child("magazaDurumu").getValue()));
                    magaza.setUid(snapshot.getKey());
                    magaza.setMagazaHakkinda(((String) snapshot.child("magazaHakkinda").getValue()));
                    magaza.setMagazaId(((String) snapshot.child("magazaId").getValue()));
                    magaza.setMagazaPuani(((Long) snapshot.child("magazaPuani").getValue()));
                    magaza.setMagazaResmi(((String) snapshot.child("magazaResmi").getValue()));

                    if (magaza.isMagazaDurumu() && magaza.getMagazaAdet() > 0) {
                        shopList.add(magaza);
                    }
                }
                magazaAdapter = new MagazaAdapter(getContext(), shopList);
                profileListView.setAdapter(magazaAdapter);
                magazaAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
