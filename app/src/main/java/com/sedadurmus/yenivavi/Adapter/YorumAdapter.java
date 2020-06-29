package com.sedadurmus.yenivavi.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.sedadurmus.yenivavi.MainActivity;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.Model.Yorum;
import com.sedadurmus.yenivavi.R;

import java.util.List;

public class YorumAdapter extends RecyclerView.Adapter<YorumAdapter.ViewHolder> {

    private Context mContext;
    private List<Yorum> mYorumListesi;
    String GonderiId;
    String gonderiSahibi;

    FirebaseUser firebaseUser;

    public YorumAdapter(Context mContext, List<Yorum> mYorumListesi, String AGonderiId, String AgonderiSahibi) {
        this.mContext = mContext;
        this.mYorumListesi = mYorumListesi;
        this.gonderiSahibi=AgonderiSahibi;
        this.GonderiId = AGonderiId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.yorum_ogesi, viewGroup, false);
        return new YorumAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Yorum yorum = mYorumListesi.get(i);

        viewHolder.txt_yorum.setText(yorum.getYorum());
        kullaniciBilgisiAl(viewHolder.profil_resmi, viewHolder.txt_kullanici_adi, yorum.getGonderen());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

//        viewHolder.yorumCercevesi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if ((!yorum.getGonderen().equals(LoginActivity.getKullanici().getId())
//                        &&!yorum.getYorumGonderi().equals(gonderiSahibi)))
//                    return;
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                    builder.setCancelable(false);
//                    builder.setTitle("Uyarı");
//                    builder.setMessage("Yorumu silmek istediğinize emin misiniz?");
//                    builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Evet'e basılınca yapılacak işlemleri yazınız
//
//                        FirebaseDatabase.getInstance().getReference().child("Yorumlar").child(yorum.getYorumGonderi()).child(yorum.getYorumUid())
//                                .removeValue();
//
//                        Toast.makeText(mContext, "Yorum başarıyla silindi.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Hayır'a baslınca yapılacak işmeleri yazınız
//                        dialog.cancel();
//                        Toast.makeText(mContext, "Gönderi silinemedi!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                AlertDialog alert = builder.create();
//                alert.show();
//
//            }
//        });

        viewHolder.profil_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("gonderenId", yorum.getGonderen());
                mContext.startActivity(intent);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (yorum.getGonderen().equals(firebaseUser.getUid()))
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("Yorumu silmek istediğinize emin misiniz?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Hayır",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Evet",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseDatabase.getInstance().getReference("Yorumlar").child(yorum.getYorumGonderi())
                                            .child(yorum.getYorumUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(mContext, "Yorum başarıyla silindi!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    dialogInterface.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mYorumListesi.size();
    }

//    yorum_ogesinde recyeler view kullanacağım için adapter olmalı

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profil_resmi;
        public TextView txt_kullanici_adi, txt_yorum;
        LinearLayout yorumCercevesi;

        public ViewHolder(View itemView) {
            super(itemView);
            yorumCercevesi=itemView.findViewById(R.id.yorumCercevesi);
            profil_resmi = itemView.findViewById(R.id.profil_resmi_yorumOgesi);
            txt_kullanici_adi = itemView.findViewById(R.id.txt_kullaniciadi_yorumOgesi);
            txt_yorum = itemView.findViewById(R.id.txt_yorum_yorumOgesi);

        }
    }

    private void kullaniciBilgisiAl(final ImageView imageView, final TextView kullaniciadi, String gonderenId) {

        DatabaseReference gonderenIdYolu = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(gonderenId);
        gonderenIdYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                Glide.with(mContext).load(kullanici.getResimurl()).into(imageView);
                kullaniciadi.setText(kullanici.getAd());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
















