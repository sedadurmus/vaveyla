package com.sedadurmus.yenivavi.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

public class FotografAdapter extends RecyclerView.Adapter<FotografAdapter.ViewHolder> {
    String profilId;
    Uri uri;

    private Context mContext;
    private List<Gonderi> mGonderiler;

    private FirebaseUser mevcutFirebaseUser;

    public FotografAdapter(Context context, List<Gonderi> mGonderiler) {
        this.mContext = context;
        this.mGonderiler = mGonderiler;
    }

    @NonNull
    @Override
    public FotografAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.profil_ogesi, viewGroup, false);
//        return new FotografAdapter.ViewHolder(view);
        mevcutFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Gonderi gonderi = mGonderiler.get(viewType);

        if (gonderi.getGonderiTuru().equals("film")) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.profil_ogesi, viewGroup, false);
            return new FotografAdapter.ViewHolder(view);
        } else  {
            View view = LayoutInflater.from(mContext).inflate(R.layout.dene_profil_ogesi, viewGroup, false);
            return new FotografAdapter.ViewHolder(view);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        mevcutFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Gonderi gonderi = mGonderiler.get(i);
        Glide.with(mContext).load(gonderi.getGonderiResmi()).into(viewHolder.gonderiResmi);


        if (gonderi.getGonderiHakkinda().equals("")) {
            viewHolder.txt_gonderi_hakkinda.setVisibility(View.GONE);
        } else {
            viewHolder.txt_gonderi_hakkinda.setVisibility(View.VISIBLE);
            viewHolder.txt_gonderi_hakkinda.setText(gonderi.getGonderiHakkinda());
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

        Date simdi = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date tarih = null;
        try {
            tarih = gonderi.getTarih();
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
                viewHolder.txt_zaman.setText(saniye + "s");
            if (dakika > 0 && saat == 0)
                viewHolder.txt_zaman.setText(dakika + "dk");
            if (saat > 0 && gun == 0)
                viewHolder.txt_zaman.setText(saat + "sa");
            if (gun > 0)
                viewHolder.txt_zaman.setText(gun + "g");
            viewHolder.txt_zaman.setVisibility(View.VISIBLE);
        }

        if (gonderi.getGonderiResmi().length() < 0) {
            viewHolder.gonderiResmi.setVisibility(View.GONE);

        } else {
            viewHolder.gonderiResmi.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(gonderi.getGonderiResmi()).into(viewHolder.gonderiResmi);
        }
//
//        if (gonderi.getGonderiVideo() != null){
//            viewHolder.videoView.setVisibility(View.VISIBLE);
//            MediaController media = new MediaController(mContext);
//            viewHolder.videoView.setMediaController(media);
//          //  viewHolder.videoView.setVideoURI(Uri.parse(gonderi.getGonderiVideo()));
//            viewHolder.setVideo(Uri.parse(String.valueOf(uri)),gonderi.getGonderiVideo());
//        }else {
//            viewHolder.videoView.setVisibility(View.GONE);
//        }

//        if (gonderi.getGonderiVideo().length() > 0) {
//            viewHolder.videoView.setVisibility(View.GONE);
//            MediaController media = new MediaController(mContext);
//            viewHolder.videoView.setMediaController(media);
//            viewHolder.videoView.setVideoURI(Uri.parse(gonderi.getGonderiVideo()));
//        }

        gonderenBilgileri(viewHolder.profil_resmi, viewHolder.txt_kullanici_adi, viewHolder.txt_gonderen, gonderi.getGonderen());
        begenildi(gonderi.getGonderiId(), viewHolder.begeniResmi);
        begeniSayisi(viewHolder.txt_begeni, gonderi.getGonderiId());
        yorumlariAl(gonderi.getGonderiId(), viewHolder.txt_yorumlar);

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
                intent.putExtra("gonderiSahibi", gonderiSahibi);
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
            public void onClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.duzenle:
                                editPost(gonderi.getGonderiId());
                                return true;
                            case R.id.sil:
                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                LayoutInflater factory = LayoutInflater.from(view.getContext());
                                final View view = factory.inflate(R.layout.alert_dialog, null);
                                builder.setView(view);
                                builder.setCancelable(false);
                                ((TextView)view.findViewById(R.id.textTitle)).setText("Uyarı");
                                ((TextView)view.findViewById(R.id.textMessage)).setText("Gönderiyi silmek istediğinize emin misiniz?");
                                ((Button)view.findViewById(R.id.buttonNo)).setText("HAYIR");
                                ((Button)view.findViewById(R.id.buttonYes)).setText("EVET");
                                ((ImageView)view.findViewById(R.id.imageicon)).setImageResource(R.drawable.ic_info);
                                final AlertDialog alertDialog = builder.create();
                                view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // Evet'e basılınca yapılacak işlemleri yazınız
                                        FirebaseDatabase.getInstance().getReference().child("Gonderiler").child(gonderi.getGonderiUid())
                                                .removeValue();
                                        isOkey = true;
                                        final DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(mevcutFirebaseUser.getUid());
                                        kullaniciYolu.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (!isOkey) return;
                                                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                                                HashMap<String, Object> kullaniciGuncelleHashMap = new HashMap<>();
                                                kullaniciGuncelleHashMap.put("profilPuan", kullanici.getProfilPuan() - 3);
                                                kullaniciYolu.updateChildren(kullaniciGuncelleHashMap);
                                                isOkey = false;
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        Toast.makeText(mContext, "Gönderi başarıyla silindi.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertDialog.cancel();
                                        Toast.makeText(mContext, "Gönderi silinemedi!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                if (alertDialog.getWindow() != null){
                                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                }
                                alertDialog.show();
                                return true;
                            case R.id.report:

                                Toast.makeText(mContext, "Şikayetiniz incelenecektir", Toast.LENGTH_SHORT).show();
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
                    popupMenu.getMenu().findItem(R.id.report).setVisible(false);
                }
                popupMenu.show();
            }
        });
    }

    boolean isOkey = true;

    @Override
    public int getItemCount() {
        return mGonderiler.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profil_resmi, gonderiResmi, begeniResmi, yorumResmi, silResmi,more, gorevMi;
        public TextView txt_kullanici_adi, txt_begeni, txt_gonderen, txt_gonderi_hakkinda, txt_yorumlar, txt_zaman;
        CardView cardView;
        public VideoView videoView;

        public ViewHolder(View itemView) {
            super(itemView);

            profil_resmi = itemView.findViewById(R.id.profil_resmi_profil_ogesi);
            gonderiResmi = itemView.findViewById(R.id.gonderi_resmi_profil_ogesi);

            begeniResmi = itemView.findViewById(R.id.begeni_profil_ogesi);
            yorumResmi = itemView.findViewById(R.id.yorum_profil_ogesi);
//            silResmi = itemView.findViewById(R.id.sil_profil_ogesi);
            gorevMi= itemView.findViewById(R.id.buBirGorev);

            cardView = itemView.findViewById(R.id.card_view);
            cardView.setBackgroundResource(R.drawable.backrground_post);

            more = itemView.findViewById(R.id.more);

            txt_kullanici_adi = itemView.findViewById(R.id.txt_kullaniciadi_profil_ogesi);
            txt_begeni = itemView.findViewById(R.id.txt_begeniler_profil_ogesi);
            txt_gonderi_hakkinda = itemView.findViewById(R.id.txt_gonderiHakkinda_profil_ogesi);
            txt_yorumlar = itemView.findViewById(R.id.txt_yorum_profil_ogesi);
            txt_zaman = itemView.findViewById(R.id.txt_zaman);
//            videoView = itemView.findViewById(R.id.video_profil_ogesi);
        }

//        public void   setVideo(Uri parse, final String video){
//            DatabaseReference videoYolu = FirebaseDatabase.getInstance().getReference("Gonderiler").child("gonderiVideo");
//            videoYolu.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    Uri uri=Uri.parse(dataSnapshot.child(video).child("gonderiVideo").getValue(true).toString());
//                    videoView.setVideoURI(uri);
//                    videoView.requestFocus();
//                    videoView.start();
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
    }

//    public void   setVideo (final String video){
//        DatabaseReference videoYolu = FirebaseDatabase.getInstance().getReference("Gonderiler").child("gonderiVideo");
//        videoYolu.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Uri uri=Uri.parse(dataSnapshot.child(video).child("gonderiVideo").getValue(true).toString());
//                videoView.setVideoURI(uri);
//                videoView.requestFocus();
//                videoView.start();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }


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

    String gonderiSahibi;

    private void gonderenBilgileri(final ImageView profil_resmi, final TextView txt_kullanici_adi, final TextView gonderen, String kullaniciId) {
        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(kullaniciId);
        veriYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                    if (kullanici == null) return;
                    gonderiSahibi = kullanici.getId();
                    if (kullanici.getResimurl().length() > 0 && profil_resmi != null)
                        Glide.with(mContext).load(kullanici.getResimurl()).into(profil_resmi);
                    txt_kullanici_adi.setText(kullanici.getKullaniciadi());
                } catch (Exception ignored) {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private String simdikiTarih;

    private void bildirimleriEkle(String kullaniciId, String gonderiId) {
        Date simdi = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        simdikiTarih = dateFormat.format(simdi);
        DatabaseReference bildirimEklemeYolu = FirebaseDatabase.getInstance().getReference("Bildirimler").child(kullaniciId);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("kullaniciid", mevcutFirebaseUser.getUid());
        hashMap.put("text", "Gönderini Beğendi");
        hashMap.put("gonderiid", gonderiId);
        hashMap.put("ispost", true);
        hashMap.put("bildirimTarihi", simdikiTarih);
        bildirimEklemeYolu.push().setValue(hashMap);
    }

    @SuppressLint("SetTextI18n")
    private void editPost(final String gonderiId){

//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        LayoutInflater factory = LayoutInflater.from(mContext);
//        @SuppressLint("InflateParams") final View view = factory.inflate(R.layout.alert_dialog, null);
//        builder.setView(view);
//        builder.setCancelable(false);
//        ((TextView)view.findViewById(R.id.textTitle)).setText("Gönderiyi Düzenle");
//        ((Button)view.findViewById(R.id.buttonNo)).setText("KAPAT");
//        ((Button)view.findViewById(R.id.buttonYes)).setText("KAYDET");
//        ((ImageView)view.findViewById(R.id.imageicon)).setImageResource(R.drawable.ic_info);
//        final EditText editText = new EditText(mContext);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT
//        );
//
//        final AlertDialog alertdialog = builder.create();
//        editText.setLayoutParams(lp);
//        alertdialog.setView(editText);
//        getText(gonderiId,editText);


        AlertDialog.Builder alertdialog =new AlertDialog.Builder(mContext);
        final EditText editText = new EditText(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editText.setLayoutParams(lp);
        alertdialog.setView(editText);
        getText(gonderiId,editText);
        alertdialog.setPositiveButton("KAYDET",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("gonderiHakkinda", editText.getText().toString());
                        FirebaseDatabase.getInstance().getReference("Gonderiler").
                                child(gonderiId).updateChildren(hashMap);
                    }
                });

        alertdialog.setNegativeButton("KAPAT",
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
