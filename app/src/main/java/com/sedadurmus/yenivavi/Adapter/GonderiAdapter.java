package com.sedadurmus.yenivavi.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Fragments.ProfileFragment;
import com.sedadurmus.yenivavi.Model.Gonderi;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.R;
import com.sedadurmus.yenivavi.TakipcilerActivity;
import com.sedadurmus.yenivavi.YorumlarActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GonderiAdapter extends RecyclerView.Adapter<GonderiAdapter.ViewHolder> {

    Uri uri;
    public Context mContext;
    public List<Gonderi> mGonderi;


    private FirebaseUser mevcutFirebaseUser;

    public GonderiAdapter(Context mContext, List<Gonderi> mGonderi) {
        this.mContext = mContext;
        this.mGonderi = mGonderi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.post_home, viewGroup, false);
        return new GonderiAdapter.ViewHolder(view);

    }

    boolean isOkey;

    String simdikiTarih;

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        mevcutFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Gonderi gonderi = mGonderi.get(i);

        if (gonderi.getGonderiHakkinda().equals("")) {
            viewHolder.txt_gonderi_hakkinda.setVisibility(View.GONE);
        } else {
            viewHolder.txt_gonderi_hakkinda.setVisibility(View.VISIBLE);
            viewHolder.txt_gonderi_hakkinda.setText(gonderi.getGonderiHakkinda());
        }

        if (gonderi.getGonderiResmi().length() < 0) {
            viewHolder.gonderiResmi.setVisibility(View.GONE);
        } else {
            viewHolder.gonderiResmi.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(gonderi.getGonderiResmi()).into(viewHolder.gonderiResmi);
        }

        //        görev olunca profilde ve anasayfada bi simge çıksın yanındaaaa
        if(gonderi.isOnayDurumu() == true){
            viewHolder.gorevMi.setVisibility(View.VISIBLE);
        }else {
            viewHolder.gorevMi.setVisibility(View.GONE);
        }
        viewHolder.gorevMi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Bu bir görev paylaşımıdır.\nSende görevlere katıl hem puan kazan hem rozet!", Toast.LENGTH_LONG).show();
            }
        });

        viewHolder.txt_zaman.setText(gonderi.getTarih() != null ? gonderi.getTarih().toString() : "");
        Date simdi = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        simdikiTarih = dateFormat.format(simdi);
        Date tarih = null;
        try {
            tarih = (gonderi.getTarih());
        } catch (Exception e) {
        }
        if (tarih != null) {
            int fark = (int) (simdi.getTime() - tarih.getTime());
            int gun = fark / (1000 * 60 * 60 * 24);
            int saat = fark / (1000 * 60 * 60);
            int dakika = fark / (1000 * 60);
            int saniye = fark / (1000);
            if (saniye == 0)
                viewHolder.txt_zaman.setText("şimdi");
            if (saniye > 0 && dakika == 0)
                viewHolder.txt_zaman.setText(saniye + "sn");
            if (dakika > 0 && saat == 0)
                viewHolder.txt_zaman.setText(dakika + "dk");
            if (saat > 0 && gun == 0)
                viewHolder.txt_zaman.setText(saat + "sa");
            if (gun > 0)
                viewHolder.txt_zaman.setText(gun + "g");
            viewHolder.txt_zaman.setVisibility(View.VISIBLE);
        }

//        Metotları çağırmak için
        gonderenBilgileri(viewHolder.profil_resmi, viewHolder.txt_kullanici_adi, viewHolder.txt_gonderen, gonderi.getGonderen());
        begenildi(gonderi.getGonderiId(), viewHolder.begeniResmi);
        begeniSayisi(viewHolder.txt_begeni, gonderi.getGonderiId());
        yorumlariAl(gonderi.getGonderiId(), viewHolder.txt_yorumlar);

        viewHolder.txt_kullanici_adi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOkey = true;
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", gonderi.getGonderen());
                editor.apply();
                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment()).commit();

//                Query sorgu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").orderByChild("id");
//                sorgu.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (!isOkey) return;
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            Kullanici kullanici = snapshot.getValue(Kullanici.class);
//                            if (kullanici.getId().equals(gonderi.getGonderen())) {
//                                BaskaProfilActivity.kullanici = kullanici;
//                                Intent intent = new Intent(mContext, BaskaProfilActivity.class);
//                                intent.putExtra("baslik", "Profil");
//                                mContext.startActivity(intent);
//                            }
//                        }
//                        isOkey = false;
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                    }
//                });
            }
        });

        viewHolder.profil_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOkey = true;
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", gonderi.getGonderen());
                editor.apply();
                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment()).commit();
            }
        });

//beğeni resmi tıklama olayı
        viewHolder.begeniResmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.begeniResmi.getTag().equals("beğen")) {
                    FirebaseDatabase.getInstance().getReference().child("Begeniler").child(gonderi.getGonderiId())
                            .child(mevcutFirebaseUser.getUid()).setValue(true);

                    bildirimleriEkle(gonderi.getGonderen(), gonderi.getGonderiId());

                } else {
                    FirebaseDatabase.getInstance().getReference().child("Begeniler").child(gonderi.getGonderiId())
                            .child(mevcutFirebaseUser.getUid()).removeValue();
                }
            }
        });
//        burdan sonraki viewHolder gönderiye yorum yapabilmek için
        viewHolder.yorumResmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, YorumlarActivity.class);
                intent.putExtra("gonderiId", gonderi.getGonderiId());
                intent.putExtra("gonderenId", gonderi.getGonderen());
                mContext.startActivity(intent);
            }
        });
        viewHolder.txt_yorumlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, YorumlarActivity.class);
                intent.putExtra("gonderiId", gonderi.getGonderiId());
                intent.putExtra("gonderenId", gonderi.getGonderen());
                mContext.startActivity(intent);
            }
        });
// beğeniler textine tıkladığında
        viewHolder.txt_begeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TakipcilerActivity.class);
                intent.putExtra("id", gonderi.getGonderiId());
                intent.putExtra("baslik", "Beğeniler");
                mContext.startActivity(intent);
            }
        });

        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.duzenle:
                                editPost(gonderi.getGonderiId());
                            return true;
                            case R.id.sil:
                                FirebaseDatabase.getInstance().getReference("Gonderiler")
                                        .child(gonderi.getGonderiId()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(mContext, "Gönderi başarıyla silindi.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                return true;
                            case R.id.report:
                                Toast.makeText(mContext, "Şikayetiniz incelenecektir.", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.post_menu);
                if (!gonderi.getGonderen().equals(mevcutFirebaseUser.getUid())){
                    popupMenu.getMenu().findItem(R.id.duzenle).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.sil).setVisible(false);
                }
                else  {
                    popupMenu.getMenu().findItem(R.id.report).setVisible(true);
                }
                popupMenu.show();
            }
        });

    }

    View.OnClickListener profilegit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    public int getItemCount() {
        return mGonderi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profil_resmi, gonderiResmi, begeniResmi, yorumResmi, kaydetmeResmi, silResmi, more, gorevMi;
        public TextView txt_kullanici_adi, txt_begeni, txt_gonderen, txt_gonderi_hakkinda, txt_yorumlar, txt_zaman;
        CardView cardView;
//        public VideoView videoView;

        public ViewHolder(View itemView) {
            super(itemView);
//           videoView = itemView.findViewById(R.id.videoView);
            profil_resmi = itemView.findViewById(R.id.profil_resmi_txtGonderiActivity);
            gonderiResmi = itemView.findViewById(R.id.image_textGonderiActivity);
            begeniResmi = itemView.findViewById(R.id.begeni_txtGonderiActivity);
            yorumResmi = itemView.findViewById(R.id.yorum_txtGonderiActivity);
            cardView = itemView.findViewById(R.id.card_view);
            gorevMi = itemView.findViewById(R.id.buBirGorev);

            cardView.setBackgroundResource(R.drawable.backrground_post);

            more = itemView.findViewById(R.id.more);

            txt_kullanici_adi = itemView.findViewById(R.id.txt_kullaniciadi_txtGonderiActivity);
            txt_begeni = itemView.findViewById(R.id.txt_begeniler_txtGonderiActivity);
            txt_gonderi_hakkinda = itemView.findViewById(R.id.txt_gonderiHakkinda_txtGonderiActivity);
            txt_yorumlar = itemView.findViewById(R.id.txt_yorum_txtGonderiActivity);
            txt_zaman = itemView.findViewById(R.id.txt_zaman);
        }
    }

//    yorum için metot

    private void yorumlariAl(String gonderiId, final TextView yorumlar) {

        DatabaseReference yorumlariAlmaYolu = FirebaseDatabase.getInstance().getReference("Yorumlar").child(gonderiId);

        yorumlariAlmaYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                yorumlar.setText(dataSnapshot.getChildrenCount() + " ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    //beğeni ekleme için yaptıklarım
    private void begenildi(String gonderiId, final ImageView imageView) {
        final FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference begeniVeriTabaniYolu = FirebaseDatabase.getInstance().getReference()
                .child("Begeniler").child(gonderiId);

        begeniVeriTabaniYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(mevcutKullanici.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.hand_like_green);
                    imageView.setTag("beğenildi");
                } else {
                    imageView.setImageResource(R.drawable.ic_hand_like);
                    imageView.setTag("beğen");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void begeniSayisi(final TextView begeniler, String gonderiId) {
        DatabaseReference begeniSayisiVeriTabaniYolu = FirebaseDatabase.getInstance().getReference()
                .child("Begeniler").child(gonderiId);
        begeniSayisiVeriTabaniYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                begeniler.setText(dataSnapshot.getChildrenCount() + " ");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void gonderenBilgileri(final ImageView profil_resmi, final TextView txt_kullanici_adi, final TextView gonderen, String kullaniciId) {
        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(kullaniciId);
        veriYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                assert kullanici != null;
                if (kullanici.getResimurl().length() > 0)

                    Glide.with(mContext).load(kullanici.getResimurl()).into(profil_resmi);
                    txt_kullanici_adi.setText(kullanici.getKullaniciadi());

//aşağıdakini ben yukardakini yorum satırı yapıp yazdım.
                Gonderi gonderi = dataSnapshot.getValue(Gonderi.class);
//                gonderen.setText(gonderi.getGonderen());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void bildirimleriEkle(String kullaniciId, String gonderiId) {

        DatabaseReference bildirimEklemeYolu = FirebaseDatabase.getInstance().getReference("Bildirimler").child(kullaniciId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("kullaniciid", mevcutFirebaseUser.getUid());
        hashMap.put("text", "Gönderini Beğendi");
        hashMap.put("gonderiid", gonderiId);
        hashMap.put("ispost", true);
        hashMap.put("bildirimTarihi", simdikiTarih);

        bildirimEklemeYolu.push().setValue(hashMap);
    }

    private void editPost(final String gonderiId){
        AlertDialog.Builder alertdialog =new AlertDialog.Builder(mContext);
        alertdialog.setTitle("Gönderiyi Düzenle");
        final EditText editText = new EditText(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editText.setLayoutParams(lp);
        alertdialog.setView(editText);

        getText(gonderiId,editText);

        alertdialog.setPositiveButton("Gönderiyi Düzenle",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("gonderiHakkinda", editText.getText().toString());

                        FirebaseDatabase.getInstance().getReference("Gonderiler").
                                child(gonderiId).updateChildren(hashMap);
                    }
                });
        alertdialog.setNegativeButton("Kapat",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        alertdialog.show();
    }

    private  void getText (String gonderiId, final EditText editText){
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference("Gonderiler").child(gonderiId);
       reference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               editText.setText(dataSnapshot.getValue(Gonderi.class).getGonderiHakkinda());
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }


}


