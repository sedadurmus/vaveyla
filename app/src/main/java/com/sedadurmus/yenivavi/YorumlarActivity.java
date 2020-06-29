package com.sedadurmus.yenivavi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Adapter.YorumAdapter;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.Model.Yorum;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class YorumlarActivity extends AppCompatActivity {

    String id;
    String baslik;
    private RecyclerView recyclerView;
    private YorumAdapter yorumAdapter;
    private List<Yorum> yorumListesi;

    EditText edt_yorum_ekle;
    ImageView profil_resmi;
    TextView txt_gonder;

    String gonderiId;
    String gonderenId;
    String gonderiSahibi;
    FirebaseUser mevcutKullanici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yorumlar);

        recyclerView = findViewById(R.id.recyler_view_yorumlarActivity);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        yorumListesi = new ArrayList<>();

        edt_yorum_ekle = findViewById(R.id.edt_yorumEkle_yorumlarActivity);
        profil_resmi = findViewById(R.id.profil_resmi_yorumlarActicity);
        txt_gonder = findViewById(R.id.txt_gonder_yorumlarActivity);
        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();



// Toolbar ayarları
        Toolbar toolbar = findViewById(R.id.toolbar_yorumlarActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Yorumlar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        gonderiId = intent.getStringExtra("gonderiId");
        gonderiSahibi=intent.getStringExtra("gonderiSahibi");
        gonderenId = intent.getStringExtra("gonderenId");

        yorumAdapter = new YorumAdapter(YorumlarActivity.this, yorumListesi,gonderenId,gonderiSahibi);
        recyclerView.setAdapter(yorumAdapter);

        txt_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_yorum_ekle.getText().toString().equals("")) {
                    Toast.makeText(YorumlarActivity.this, "Boş yorum gönderemezsiniz..", Toast.LENGTH_SHORT).show();
                } else {
                    yorumEkle();
//                    yorumTelefon();
                }
            }
        });


//        metotları çağırma
        resimAl();
        yorumlarıOku();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(YorumlarActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();

    }

    private void yorumEkle() {

        DatabaseReference yorumlarYolu = FirebaseDatabase.getInstance().getReference("Yorumlar").child(gonderiId);

        String yorumUid = yorumlarYolu.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("yorum", edt_yorum_ekle.getText().toString());
        hashMap.put("gonderen", mevcutKullanici.getUid());
        hashMap.put("yorumid", yorumUid);

        yorumlarYolu.child(yorumUid).setValue(hashMap);
        edt_yorum_ekle.setText("");

//      Yorum ekle Bildirimleri ekle
        bildirimleriEkle();



    }

    private void resimAl() {

        DatabaseReference resimAlmaYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(mevcutKullanici.getUid());

        resimAlmaYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);

                Glide.with(getApplicationContext()).load(kullanici.getResimurl()).into(profil_resmi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void yorumlarıOku() {

        DatabaseReference yorumlarıOkumaYolu = FirebaseDatabase.getInstance().getReference("Yorumlar").child(gonderiId);

        yorumlarıOkumaYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                yorumListesi.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Yorum yorum = snapshot.getValue(Yorum.class);
                    yorum.setYorumUid(snapshot.getKey());
                    yorum.setYorumGonderi(gonderiId);

                    yorumListesi.add(yorum);
                }
                yorumAdapter=new YorumAdapter(YorumlarActivity.this,yorumListesi,gonderenId,gonderiSahibi);
                recyclerView.setAdapter(yorumAdapter);
                yorumAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    String simdikiTarih;
    private void bildirimleriEkle() {

        Date simdi = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        simdikiTarih = dateFormat.format(simdi);

        DatabaseReference bildirimEklemeYolu = FirebaseDatabase.getInstance().getReference("Bildirimler").child(gonderenId);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("kullaniciid", mevcutKullanici.getUid());
        hashMap.put("text", "Yorum yaptı: " + edt_yorum_ekle.getText().toString());
        hashMap.put("gonderiid", gonderiId);
        hashMap.put("ispost", true);
        hashMap.put("bildirimTarihi", simdikiTarih);

        bildirimEklemeYolu.push().setValue(hashMap);

    }

//    private void yorumTelefon ()
//    {
//        //        telefona bildirim gelmesi için yaptım. TELEFONANAAAAAAAAA BİLDİRİRİRİRİRİRİRİİRM
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification.Builder builder = new Notification.Builder(YorumlarActivity.this);
//        builder.setContentTitle("Bildirim");
//        builder.setContentText("Yorum yaptı"+edt_yorum_ekle.getText().toString() );
//        builder.setSmallIcon(R.drawable.ic_like);
//        builder.setAutoCancel(true);
//        builder.setTicker("Yorumunuz var");
//
//
//        Intent ıntent = new Intent(YorumlarActivity.this, BildirimFragment.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(YorumlarActivity.this, 1, ıntent, 0);
//        builder.setContentIntent(pendingIntent);
//
//        Notification notification = builder.getNotification();
//        manager.notify(1, notification);
//
//    }

}
















