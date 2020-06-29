package com.sedadurmus.yenivavi;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class ProfilDuzenleActivity extends AppCompatActivity {

    ImageView resim_kapatma, resim_profil;
    TextView txt_kaydet, txt_fotograf_degistir, hesabiSil;
    EditText mEdt_ad, mEdt_kullaniciadi, mEdt_biyografi;
    Kullanici kullanici;
    FirebaseUser mevcutKullanici;
    Context mContext;
    private StorageTask yuklemeGorevi;
    private Uri mResimUri;
    StorageReference depolamaYolu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_duzenle);
//        resimler
//        resim_kapatma = findViewById(R.id.kapat_resmi_profilDuzenleActivity);
        resim_profil = findViewById(R.id.profil_resmi_profilDuzenleActiviy);
//TextViewler
        txt_kaydet = findViewById(R.id.txt_kaydet_profilDuzenleActivity);
        txt_fotograf_degistir = findViewById(R.id.txt_fotograf_degistir_profilDuzenleActivity);
//        material EditText
        mEdt_ad =(EditText) findViewById(R.id.material_edt_text_Ad_profilDuzenleActivity);
        mEdt_kullaniciadi = (EditText) findViewById(R.id.material_edt_text_kullaniciAdi_profilDuzenleActivity);
//        mEdt_biyografi = findViewById(R.id.material_edt_text_biyografi_profilDuzenleActivity);
//        hesabiSil = findViewById(R.id.hesabi_sil);
        Toolbar toolbar =findViewById(R.id.toolbar_profilDuzenleActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profil Düzenle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfilDuzenleActivity.this, "Hiçbir değişiklik yapılmadı!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        depolamaYolu = FirebaseStorage.getInstance().getReference("yuklemeler");
        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(mevcutKullanici.getUid());
        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                mEdt_ad.setText(kullanici.getAd());
                mEdt_kullaniciadi.setText(kullanici.getKullaniciadi());
//                mEdt_biyografi.setText(kullanici.getBio());
                Glide.with(getApplicationContext()).load(kullanici.getResimurl()).into(resim_profil);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

//// Kapata tıklama kodu
//        resim_kapatma.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(ProfilDuzenleActivity.this, "Hiçbir değişiklik yapılmadı!", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });

//        fotoğraf değiştir yazısına tıkladığında yapılacaklar
        txt_fotograf_degistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(mResimUri)
                        .setAspectRatio(1, 1)
                        .start(ProfilDuzenleActivity.this);
            }
        });

//profil resmine tıkladığında da fotoğraf değiştir şeyi açılsın diye.
        resim_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(ProfilDuzenleActivity.this);
            }
        });
//kaydete tıklandığında
        txt_kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str_kullaniciAdi = mEdt_kullaniciadi.getText().toString();

                Query kullaniciAdiQuery = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar")
                        .orderByChild("kullaniciadi").equalTo(str_kullaniciAdi);
                kullaniciAdiQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount()>0){
                            Toast.makeText(ProfilDuzenleActivity.this, "Başka bir kullanıcı adı seçiniz!", Toast.LENGTH_LONG).show();
                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfilDuzenleActivity.this, R.style.AlertDialogTheme);
                            View view = LayoutInflater.from(ProfilDuzenleActivity.this).inflate(
                                    R.layout.alert_dialog,
                                    (ConstraintLayout)findViewById(R.id.layoutDialogContainer)
                            );
                            builder.setCancelable(false);
                            builder.setView(view);
                            ((TextView)view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.alertTitle));
                            ((TextView)view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.alertMessageProfilDuzenle));
                            ((Button)view.findViewById(R.id.buttonNo)).setText(getResources().getString(R.string.alertButtonNo));
                            ((Button)view.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.alertButtonYes));
                            ((ImageView)view.findViewById(R.id.imageicon)).setImageResource(R.drawable.ic_info);
                            final AlertDialog alertDialog1 = builder.create();
                            view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    resimYukle();
                                    profiliguncelle(mEdt_ad.getText().toString(), mEdt_kullaniciadi.getText().toString());
                                    Toast.makeText(ProfilDuzenleActivity.this, "Değişiklikler başarıyla kaydedildi!", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                            view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog1.cancel();
                                }
                            });
                            if (alertDialog1.getWindow() != null){
                                alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                            }
                            alertDialog1.show();

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
    }
//     String biyografi
    private void profiliguncelle(String ad, String kullaniciadi) {
        DatabaseReference guncellemeYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(mevcutKullanici.getUid());
        HashMap<String, Object> kullaniciGuncelleHashMap = new HashMap<>();
        kullaniciGuncelleHashMap.put("ad", ad);
        kullaniciGuncelleHashMap.put("kullaniciadi", kullaniciadi);
//        kullaniciGuncelleHashMap.put("bio", biyografi);
        guncellemeYolu.updateChildren(kullaniciGuncelleHashMap);
    }
    private String dosyaUzantisiAl(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void resimYukle() {
        if (mResimUri != null) {
            final StorageReference dosyaYolu = depolamaYolu.child(System.currentTimeMillis()
                    + "." + dosyaUzantisiAl(mResimUri));

            yuklemeGorevi = dosyaYolu.putFile(mResimUri);
            yuklemeGorevi.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                    }
                    return dosyaYolu.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri indirmeurisi = task.getResult();
                        String benimUrim = indirmeurisi.toString();
                        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar")
                                .child(mevcutKullanici.getUid());

                        HashMap<String, Object> resimHashMap = new HashMap<>();
                        resimHashMap.put("resimurl", "" + benimUrim);
                        kullaniciYolu.updateChildren(resimHashMap);
                    } else {
                        Toast.makeText(ProfilDuzenleActivity.this, "Yükleme başarısız!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfilDuzenleActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, " Resim seçilemedi! ", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mResimUri = result.getUri();
            resim_profil.setImageURI(mResimUri);
            Glide.with(ProfilDuzenleActivity.this).load(mResimUri).into(resim_profil);
        } else {
            Toast.makeText(this, "Birşeyler yanlış gitti!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        Toast.makeText(ProfilDuzenleActivity.this, "Hiçbir değişiklik yapılmadı!", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilDuzenleActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(ProfilDuzenleActivity.this).inflate(
                R.layout.alert_dialog,
                (ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setCancelable(false);
        builder.setView(view);
        ((TextView)view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.alertTitle));
        ((TextView)view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.alertMessageProfilDuzenlemeden));
        ((Button)view.findViewById(R.id.buttonNo)).setText(getResources().getString(R.string.alertButtonNo));
        ((Button)view.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.alertButtonYes));
        ((ImageView)view.findViewById(R.id.imageicon)).setImageResource(R.drawable.ic_info);
        final AlertDialog alertDialog1 = builder.create();
        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfilDuzenleActivity.this, "Değişiklik yapılmadı!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.cancel();
            }
        });
        if (alertDialog1.getWindow() != null){
            alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog1.show();
    }
}











