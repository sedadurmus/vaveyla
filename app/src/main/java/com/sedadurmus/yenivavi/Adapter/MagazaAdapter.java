package com.sedadurmus.yenivavi.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Fragments.DukkanFragment;
import com.sedadurmus.yenivavi.LoginActivity;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.Model.MagazaFire;
import com.sedadurmus.yenivavi.Model.WriteFile;
import com.sedadurmus.yenivavi.R;
import com.sedadurmus.yenivavi.System.MailSender;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.sedadurmus.yenivavi.Fragments.DukkanFragment.dialog;
import static com.sedadurmus.yenivavi.Fragments.DukkanFragment.seciliUrun;
import static com.sedadurmus.yenivavi.Fragments.DukkanFragment.urunAdet;

public class MagazaAdapter extends RecyclerView.Adapter<MagazaAdapter.ViewHolder> {

    private Context mContext;
    private List<MagazaFire> shopList;

    boolean isOkey, urunAlindi;
    DatabaseReference magazaYolu;
    DatabaseReference kullaniciYolu;
    String email;
    String message;
    String subject;
    private static final String username="vaveylayardim@gmail.com";
    private static final String password ="sedat4825";

    public MagazaAdapter(Context mContext, List<MagazaFire> shopList) {
        this.mContext = mContext;
        this.shopList = shopList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_shop_two, viewGroup, false);
        return new MagazaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        isOkey = true;
        urunAlindi = true;
       final MagazaFire magazaFire = shopList.get(i);
        viewHolder.shop_baslik.setText(magazaFire.getMagazaBasligi());
        viewHolder.shop_tanim.setText(magazaFire.getMagazaHakkinda());
        viewHolder.shop_point.setText("Puan: " +(int) magazaFire.getMagazaPuani());
        Glide.with(mContext).load(magazaFire.getMagazaResmi()).into(viewHolder.resim_magaza);
        viewHolder.shop_biletAdeti.setText("Bilet Adedi: " + (int)magazaFire.getMagazaAdet());
        viewHolder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DukkanFragment.showDialog(magazaFire);
                dialog = new AlertDialog.Builder(v.getContext());
                View view1 = LayoutInflater.from(mContext).inflate(
                        R.layout.alert_dialog,
                        (ConstraintLayout)v.findViewById(R.id.layoutDialogContainer)
                );
                dialog.setView(view1);
                ((TextView)view1.findViewById(R.id.textTitle)).setText(mContext.getResources().getString(R.string.alertTitle));
                ((TextView)view1.findViewById(R.id.textMessage)).setText(seciliUrun.getMagazaBasligi() + " isimli alışverişin gerçekleşmesini istiyor musunuz?");
                ((Button)view1.findViewById(R.id.buttonNo)).setText(mContext.getResources().getString(R.string.alertButtonNo));
                ((Button)view1.findViewById(R.id.buttonYes)).setText(mContext.getResources().getString(R.string.alertButtonYes));
                ((ImageView)view1.findViewById(R.id.imageicon)).setImageResource(R.drawable.ic_info);
                final AlertDialog alertDialog = dialog.create();
                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                        if (seciliUrun == null) return;
                        if (LoginActivity.getKullanici().getProfilPuan() >= seciliUrun.getMagazaPuani()) {
                            kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            kullaniciYolu.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!isOkey) return;
                                    Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                                    HashMap<String, Object> kullaniciGuncelleHashMap = new HashMap<>();
                                    kullaniciGuncelleHashMap.put("profilPuan", kullanici.getProfilPuan() - seciliUrun.getMagazaPuani());
                                    kullaniciYolu.updateChildren(kullaniciGuncelleHashMap);
                                    DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("AlinanUrunler");
                                    String gonderiId = veriYolu.push().getKey();
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("magazaId", seciliUrun.getMagazaId());
                                    hashMap.put("gonderen", kullanici.getId());
                                    hashMap.put("tarih", new Date().toString());
                                    veriYolu.child(gonderiId).setValue(hashMap);
                                    veriYolu = null;
                                    String userData = WriteFile.Instance.ReadDataFromFile(mContext);
                                    if (userData != null && userData.length() > 0) {
                                        String[] users = userData.split("(?<=-)", 2);
                                        email = users[0];
                                    }
                                    message = "Sayın kullanıcımız " + LoginActivity.kullanici.getAd() + "," + " \n " + seciliUrun.getMagazaBasligi() + " isimli alışverişiniz başarıyla gerçekleşti. Alışveriş bilgileri için bu maile geri dönüş yapmanızı rica ediyoruz. ";
                                    subject = "Tebrikler";
//                                    sendMail(email, subject, message);

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            MailSender mail = new MailSender("vaveylayardim@gmail.com", "sedat4825",email.replace("-",""),subject,message);
                                            try {
                                                mail.createEmailMessage(mContext);
                                                mail.sendEmail();
                                            } catch (Exception e) {
                                                String s=e.getMessage();
                                            }
                                            //           tMailSender.send(null, "", subject, message, email);
                                        }
                                    }).start();
//                                    veriYolu = FirebaseDatabase.getInstance().getReference("Magaza");
//                                    hashMap = new HashMap<>();
//                                    hashMap.put("magazaAdet", seciliUrun.getMagazaAdet() - 1);
//                                    veriYolu.updateChildren(seciliUrun.getMagazaId()).setValue(hashMap);
                                    isOkey = false;
                                    magazaYolu = FirebaseDatabase.getInstance().getReference("Magaza").child(seciliUrun.getUid());
                                    magazaYolu.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (!urunAlindi) return;
                                            MagazaFire urun = dataSnapshot.getValue(MagazaFire.class);
                                            HashMap<String, Object> magazaYoluGuncelleHashMap = new HashMap<>();
                                            magazaYoluGuncelleHashMap.put("magazaAdet", urunAdet);
                                            if (urunAlindi)
                                                magazaYolu.updateChildren(magazaYoluGuncelleHashMap);
                                            isOkey = false;
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                            Toast.makeText(mContext, "Tebrikler! Hemen mail adresine bakmalısın!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(mContext, "Puanın yetersiz! \n Görevleri gözden geçirmeye ne dersin?", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                view1.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "Diğer görevlere gözatmaya ne dersin?", Toast.LENGTH_SHORT).show();
                        alertDialog.cancel();
                    }
                });
                if (alertDialog.getWindow() != null){
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();



            }
        });

    }



    @Override
    public int getItemCount() {
        return shopList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView resim_magaza;
        public TextView shop_baslik, shop_tanim, shop_point, shop_biletAdeti;
RelativeLayout content;
        public ViewHolder(View itemView) {
            super(itemView);
            resim_magaza = itemView.findViewById(R.id.resim_magaza);
            shop_baslik = itemView.findViewById(R.id.shop_title);
            shop_tanim = itemView.findViewById(R.id.shop_description);
            shop_point = itemView.findViewById(R.id.shop_point);
            shop_biletAdeti = itemView.findViewById(R.id.shop_bilet_adeti);
            content=itemView.findViewById(R.id.content);


        }
    }
}


















