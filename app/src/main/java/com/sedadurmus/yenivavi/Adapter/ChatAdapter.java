package com.sedadurmus.yenivavi.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.sedadurmus.yenivavi.MessageActivity;
import com.sedadurmus.yenivavi.Model.Chat;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    private Context mContext;
    private List<Kullanici> mKullanicilar;
    private FirebaseUser firebaseKullanici;

    String theLastMessage;

    public ChatAdapter(Context mContext, List<Kullanici> mKullanicilar) {
        this.mContext = mContext;
        this.mKullanicilar = mKullanicilar;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item, viewGroup, false);
        return new ChatAdapter.ViewHolder(view);
    }

    String simdikiTarih;
    @Override
    public void onBindViewHolder(@NonNull final ChatAdapter.ViewHolder viewHolder, final int i) {

        firebaseKullanici = FirebaseAuth.getInstance().getCurrentUser();
        final Kullanici kullanici =mKullanicilar.get(i);

        viewHolder.kullaniciadi.setText(kullanici.getKullaniciadi());
//        viewHolder.ad.setText(kullanici.getAd());
        Glide.with(mContext).load(kullanici.getResimurl()).into(viewHolder.profil_resmi);

        if (!kullanici.getId().equals(firebaseKullanici.getUid())){
            lastMessage(kullanici.getId(), viewHolder.last_msg);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", kullanici.getId());
                editor.apply();
                mContext.startActivity(new Intent(mContext, MessageActivity.class));
            }
        });

//        viewHolder.txt_zaman.setText(chat.getMessageTarihi() != null ? chat.getMessageTarihi().toString() : " ");
//        Date simdi = new Date();
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        simdikiTarih = dateFormat.format(simdi);
//        Date tarih = null;
//        try {
//            tarih = chat.getTarih();
//        } catch (Exception e) {
//        }
//        if (tarih != null) {
//            int fark = (int) (simdi.getTime() - tarih.getTime());
//            int gun = fark / (1000 * 60 * 60 * 24);
//            int saat = fark / (1000 * 60 * 60);
//            int dakika = fark / (1000 * 60);
//            int saniye = fark / (1000);
//            if (saniye == 0)
//                viewHolder.txt_zaman.setText("şimdi");
//            if (saniye > 0 && dakika == 0)
//                viewHolder.txt_zaman.setText(saniye + "s");
//            if (dakika > 0 && saat == 0)
//                viewHolder.txt_zaman.setText(dakika + "dk");
//            if (saat > 0 && gun == 0)
//                viewHolder.txt_zaman.setText(saat + "sa");
//            if (gun > 0)
//                viewHolder.txt_zaman.setText(gun + "g");
//            viewHolder.txt_zaman.setVisibility(View.VISIBLE);
//        }

// Mesajları silmek için
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                alertDialog.setTitle("Mektubu silmek istediğinize emin misiniz?");
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
                                FirebaseDatabase.getInstance().getReference("Chats")
                                        .child(kullanici.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(mContext, "Mektubunuz başarıyla silindi!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                dialogInterface.dismiss();
                            }
                        });
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mKullanicilar.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView kullaniciadi;
        public TextView last_msg, txt_zaman;
        public CircleImageView profil_resmi;

        public ViewHolder(View itemView) {
            super(itemView);
            kullaniciadi= itemView.findViewById(R.id.txt_kullaniciadi_oge);
//            txt_zaman= itemView.findViewById(R.id.txt_zaman);
            last_msg= itemView.findViewById(R.id.txt_ad_oge);
            profil_resmi= itemView.findViewById(R.id.profil_resmi_oge);
        }
    }

    private void lastMessage(final String kullaniciid, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getRecevier().equals(firebaseUser.getUid()) && chat.getSender().equals(kullaniciid) ||
                    chat.getRecevier().equals(kullaniciid) && chat.getSender().equals(firebaseUser.getUid())){
                        theLastMessage= chat.getMessage();
                    }
                }
                switch (theLastMessage){
                    case   "default" :
                        last_msg.setText("Mesaj Yok");
                        break;
                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
