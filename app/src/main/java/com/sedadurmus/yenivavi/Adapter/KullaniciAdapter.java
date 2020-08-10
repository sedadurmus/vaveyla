package com.sedadurmus.yenivavi.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Fragments.ProfileFragment;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class KullaniciAdapter extends RecyclerView.Adapter<KullaniciAdapter.ViewHolder>{

    private Context mContext;
    private List<Kullanici> mKullanicilar;
    private FirebaseUser firebaseKullanici;

    public KullaniciAdapter(Context context, List<Kullanici> mKullaniciler) {
        this.mContext = context;
        this.mKullanicilar = mKullaniciler;
    }

    @NonNull
    @Override
    public KullaniciAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.kullanici_ogesi, viewGroup, false);
        return new KullaniciAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        firebaseKullanici = FirebaseAuth.getInstance().getCurrentUser();
        final Kullanici kullanici =mKullanicilar.get(i);
        viewHolder.btn_takipEt.setVisibility(View.VISIBLE);
        viewHolder.kullaniciadi.setText(kullanici.getKullaniciadi());
        viewHolder.ad.setText(kullanici.getAd());
        Glide.with(mContext).load(kullanici.getResimurl()).into(viewHolder.profil_resmi);

        takipEdiliyor(kullanici.getId(),viewHolder.btn_takipEt);

        if (kullanici.getId().equals(firebaseKullanici.getUid()))
        {
            viewHolder.btn_takipEt.setVisibility(View.GONE);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", kullanici.getId());
                    editor.apply();
                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ProfileFragment()).commit();
            }
        });
        viewHolder.btn_takipEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.btn_takipEt.getText().toString().equals("Takip Et")) {

                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseKullanici.getUid())
                            .child("takipEdilenler").child(kullanici.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(kullanici.getId())
                            .child("takipciler").child(firebaseKullanici.getUid()).setValue(true);
                    // bildirim ekleme metodu
                    bildirimleriEkle(kullanici.getId());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseKullanici.getUid())
                            .child("takipEdilenler").child(kullanici.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(kullanici.getId())
                            .child("takipciler").child(firebaseKullanici.getUid()).removeValue();

                    bildirimleriKaldir(kullanici.getId());
                }
            }
        });
    }
    String simdikiTarih;
    private void bildirimleriEkle (String kullaniciId)
    {
        Date simdi = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        simdikiTarih = dateFormat.format(simdi);
        DatabaseReference bildirimEklemeYolu = FirebaseDatabase.getInstance().getReference("Bildirimler").child(kullaniciId);
        HashMap<String, Object> hashMap =new HashMap<>();

        hashMap.put("kullaniciid", firebaseKullanici.getUid());
        hashMap.put("text", "Takibe başladı");
        hashMap.put("gonderiid", "");
        hashMap.put("ispost", false);
        hashMap.put("bildirimTarihi", simdikiTarih);

        bildirimEklemeYolu.child(firebaseKullanici.getUid()).setValue(hashMap);
    }


    private void bildirimleriKaldir(String kullaniciId) {

        DatabaseReference bildirimKaldirmaYolu = FirebaseDatabase.getInstance().getReference("Bildirimler")
                .child(kullaniciId)
                .child(firebaseKullanici.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("kullaniciid", firebaseKullanici.getUid());
        hashMap.put("text", "Takibe başladı");
        hashMap.put("gonderiid", "");
        hashMap.put("ispost", false);
        hashMap.put("bildirimTarihi", simdikiTarih);

        bildirimKaldirmaYolu.removeValue();
    }
    @Override
    public int getItemCount() {
        return mKullanicilar.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView kullaniciadi;
        public TextView ad;
        public CircleImageView profil_resmi;
        public Button btn_takipEt;
        public ViewHolder(View itemView) {
            super(itemView);
            kullaniciadi= itemView.findViewById(R.id.txt_kullaniciadi_oge);
            ad= itemView.findViewById(R.id.txt_ad_oge);
            profil_resmi= itemView.findViewById(R.id.profil_resmi_oge);
            btn_takipEt= itemView.findViewById(R.id.btn_takipEt_oge);
        }
    }
    private void takipEdiliyor (final String kullaniciId, final Button button)
    {
        DatabaseReference takipYolu = FirebaseDatabase.getInstance().getReference().child("Takip")
                .child(firebaseKullanici.getUid()).child("takipEdilenler");

        takipYolu.addValueEventListener(new ValueEventListener() {
            @SuppressLint({"ResourceAsColor", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(kullaniciId).exists())
                {
                    button.setText("Takip Ediliyor");
                    button.setBackgroundResource(R.drawable.btn_kullanici_style);
                    button.setTextColor(mContext.getResources().getColor(R.color.black));
                }
                else
                {
                    button.setText("Takip Et");
                    button.setBackgroundResource(R.drawable.btn_kullanici_takip);
                    button.setTextColor(mContext.getResources().getColor(R.color.white));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}
