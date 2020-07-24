package com.sedadurmus.yenivavi.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Fragments.GonderiDetayiFragment;
import com.sedadurmus.yenivavi.Fragments.ProfileFragment;
import com.sedadurmus.yenivavi.Model.Bildiren;
import com.sedadurmus.yenivavi.Model.Gonderi;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;

public class BildirimAdapter extends RecyclerView.Adapter<BildirimAdapter.ViewHolder> {

    private Context mContext;
    private List<Bildiren> mBildirim;
    private List<Kullanici> mKullanicilar;

    public BildirimAdapter(Context mContext, List<Bildiren> mBildirim) {
        this.mContext = mContext;
        this.mBildirim = mBildirim;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.bildirim_ogesi, viewGroup, false);
        return new BildirimAdapter.ViewHolder(view);
    }

    boolean isOkey;
    String simdikiTarih;

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Bildiren bildirim = mBildirim.get(i);
        viewHolder.txt_yorum.setText(bildirim.getText());
        kullaniciBilgisiAl(viewHolder.profil_resmi, viewHolder.txt_kullaniciadi, bildirim.getKullaniciid());

        if (bildirim.isIspost()) {
            viewHolder.gonderi_resmi.setVisibility(View.VISIBLE);
            gonderiResmiAl(viewHolder.gonderi_resmi, bildirim.getGonderiid());
        } else {
            viewHolder.gonderi_resmi.setVisibility(GONE);
        }

        viewHolder.txt_zaman.setText(bildirim.getTarih() != null ? bildirim.getTarih().toString() : " ");
        Date simdi = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        simdikiTarih = dateFormat.format(simdi);
        Date tarih = null;
        try {
            tarih = bildirim.getTarih();
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
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bildirim.isIspost()) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("postid", bildirim.getGonderiid());
                    editor.apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new GonderiDetayiFragment()).commit();
                } else {

                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", bildirim.getKullaniciid());
                    editor.apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();
                }
            }
        });

////        mesajla ilgili. Adam kullanıcı  adaptere yazdı ama benim bildirim fragmentte olduğu için ben buraya yazdım.Hadi bakalım hayırlısı.
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, MessageActivity.class);
//                intent.putExtra("kullaniciid", kullanici.getId());
//                mContext.startActivity(intent);
//            }
//        });

    }


    @Override
    public int getItemCount() {
        return mBildirim.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profil_resmi, gonderi_resmi, bildirim_geldi;
        public TextView txt_kullaniciadi, txt_yorum, txt_zaman;

        public ViewHolder(View itemView) {
            super(itemView);

            profil_resmi = itemView.findViewById(R.id.profil_resmi_bildirim_ogesi);
            gonderi_resmi = itemView.findViewById(R.id.gonderi_resmi_bildirimOgesi);

            txt_kullaniciadi = itemView.findViewById(R.id.txt_kullaniciadi_bildirimOgesi);
            txt_yorum = itemView.findViewById(R.id.txt_yorum_bildirimOgesi);
            txt_zaman = itemView.findViewById(R.id.txt_zaman);

        }
    }

    private void kullaniciBilgisiAl(final ImageView imageView, final TextView kullaniciadi, String gonderenid) {
        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(gonderenid);

        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                Glide.with(mContext).load(kullanici.getResimurl()).into(imageView);
                kullaniciadi.setText(kullanici.getKullaniciadi());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void gonderiResmiAl(final ImageView imageView, String gonderiId) {
        DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Gonderiler").child(gonderiId);
        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Gonderi gonderi = dataSnapshot.getValue(Gonderi.class);
                if (gonderi != null && gonderi.getGonderiResmi().length() > 0) {
                    Glide.with(mContext).load(gonderi.getGonderiResmi()).into(imageView);
                }
                else {
                    imageView.setVisibility(GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    private void bildirimGeldi(String gonderiId, final ImageView imageView) {
//        final FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
//
//        DatabaseReference begeniVeriTabaniYolu = FirebaseDatabase.getInstance().getReference()
//                .child("Bildirimler").child(gonderiId);
//
//        begeniVeriTabaniYolu.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                Notification.InboxStyle style = new Notification.InboxStyle();
//                style.setBigContentTitle("Bildirim");
//                style.addLine("Gönderinizi Beğendi");
//                style.addLine("Sizi takip etti");
//                style.addLine("Bildirimmmm");
//
////                NotificationManager manager =  getSystemService(NOTIFICATION_SERVICE);
//                Notification.Builder builder = new Notification.Builder(mContext);
//                builder.setContentTitle("Bildirim");
//                builder.setContentText("Yorum yaptı");
//                builder.setSmallIcon(R.drawable.ic_like);
//                builder.setAutoCancel(true);
//                builder.setTicker("Yorumunuz var");
//                builder.setStyle(style);
//
//                Intent ıntent = new Intent(mContext, BildirimFragment.class);
//                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, ıntent, 0);
//                builder.setContentIntent(pendingIntent);
//
////                Notification notification = builder.getNotification();
////                manager.notify(1, notification);
//
//
//
//
////                if (dataSnapshot.child(mevcutKullanici.getUid()).exists()) {
////                    imageView.setImageResource(R.drawable.ic_bildirim_geldi);
////
////                } else {
////                    imageView.setImageResource(R.drawable.ic_bildirim);
////
////                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }
}
