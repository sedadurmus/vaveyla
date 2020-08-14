package com.sedadurmus.yenivavi.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sedadurmus.yenivavi.Model.Chat;
import com.sedadurmus.yenivavi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    public static final int MSG_TYPE__LEFT=0;
    public static final int MSG_TYPE__RIGHT=1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageUrl;
    private FirebaseUser fuser;


    public MessageAdapter(Context context, List<Chat> mChat, String imageUrl) {
        this.mContext = context;
        this.mChat = mChat;
        this.imageUrl = imageUrl;
    }
    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == MSG_TYPE__RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }
    String simdikiTarih;


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder viewHolder, final int position) {

        Chat chat = mChat.get(position);

        viewHolder.show_msg.setText(chat.getMessage());

        if (imageUrl.equals("default")){
            viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(mContext).load(imageUrl).into(viewHolder.profile_image);
        }

        if (position == mChat.size()-1){
            if (chat.isIsseen()){
                viewHolder.txt_seen.setText("Görüldü");
            }else {
                viewHolder.txt_seen.setText("Teslim Edildi");
            }
        }else {
            viewHolder.txt_seen.setVisibility(View.GONE);
        }
        viewHolder.txt_zaman.setText(chat.getMessageTarihi() != null ? chat.getMessageTarihi().toString() : " ");
        Date simdi = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        simdikiTarih = dateFormat.format(simdi);
        Date tarih = null;
        try {
            tarih = chat.getTarih();
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
    }

//    String simdikiTarih;
//    private void bildirimleriEkle (String kullaniciId)
//    {
//        Date simdi = new Date();
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        simdikiTarih = dateFormat.format(simdi);
//
//        DatabaseReference bildirimEklemeYolu = FirebaseDatabase.getInstance().getReference("Bildirimler").child(kullaniciId);
//
//        HashMap<String, Object> hashMap =new HashMap<>();
//        hashMap.put("kullaniciid", firebaseKullanici.getUid());
//        hashMap.put("text", "Takibe başladı");
//        hashMap.put("gonderiid", "");
//        hashMap.put("ispost", false);
//        hashMap.put("bildirimTarihi", simdikiTarih);
//
//        bildirimEklemeYolu.push().setValue(hashMap);
//    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView show_msg;
        public CircleImageView profile_image;
        public  TextView txt_seen, txt_zaman;

        public ViewHolder(View itemView) {
            super(itemView);
            show_msg= itemView.findViewById(R.id.show_msg);
            profile_image= itemView.findViewById(R.id.profileImage);
            txt_seen= itemView.findViewById(R.id.txt_seen);
            txt_zaman= itemView.findViewById(R.id.txt_zaman);
        }
    }
    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE__RIGHT;
        }else {
            return MSG_TYPE__LEFT;
        }
    }
}