package com.sedadurmus.yenivavi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.sedadurmus.yenivavi.Model.Kullanici;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class PostBookActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 111;
    Uri resimUri, videoUri;
    String benimUrim = "http://kitap.bildirimler.com/api/books";
    Intent videoIntent = new Intent();
    String posterUrl;
    ImageView filmPoster, filmposter2;
    EditText hakkinda;
    ConstraintLayout constraintMovie;
    StorageTask yuklemeGorevi;
    StorageReference resimYukleYolu;
    TextView txt_Gonder, yazarAd, kitapAdi;
    EditText gonderi_hakkinda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_book);

        posterUrl = getIntent().getExtras().getString("img_url");
        Toolbar toolbar =findViewById(R.id.toolbar_gonderi);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Gönderi Oluştur");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        kitapAdi =findViewById(R.id.book_title_paylasim);
        filmposter2=findViewById(R.id.book_poster);
        constraintMovie =findViewById(R.id.constraint_movie);
        txt_Gonder = findViewById(R.id.txt_gonder);
        yazarAd = findViewById(R.id.yazar_kitap_paylasim);
        gonderi_hakkinda = findViewById(R.id.edit_gonderi_hakkinda_txtGonderiActivity);
        resimYukleYolu = FirebaseStorage.getInstance().getReference("Gonderiler");

        constraintMovie.setVisibility(View.VISIBLE);
        String kitapname = getIntent().getExtras().getString("name");
        String yazar = getIntent().getExtras().getString("author");

        Glide.with(this).load( posterUrl).into(filmposter2);
        kitapAdi.setText(kitapname);
        yazarAd.setText(yazar);

        txt_Gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PostBookActivity.this, R.style.AlertDialogTheme);
                View view = LayoutInflater.from(PostBookActivity.this).inflate(
                        R.layout.alert_dialog,
                        (ConstraintLayout)findViewById(R.id.layoutDialogContainer)
                );

                builder.setView(view);
                ((TextView)view.findViewById(R.id.textTitle)).setText( getResources().getString(R.string.alertTitle));
                ((TextView)view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.alertMessageGonderi));
                ((Button)view.findViewById(R.id.buttonNo)).setText(getResources().getString(R.string.alertButtonNo));
                ((Button)view.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.alertButtonYes));
                ((ImageView)view.findViewById(R.id.imageicon)).setImageResource(R.drawable.ic_info);
                builder.setCancelable(false);
                final AlertDialog alertDialog = builder.create();
                view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resimYukle();
                    }
                });
                view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                        Toast.makeText(PostBookActivity.this, "Gönderi Paylaşılamadı!", Toast.LENGTH_SHORT).show();

                    }
                });

                if (alertDialog.getWindow() != null){
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });
    }
    boolean isOkey = true;

    private String dosyaUzantisiAl(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    String simdikiTarih;

    private void resimYukle() {
        isOkey = true;

        //resim yükle kodları
        final ProgressDialog progressDialog = new ProgressDialog(PostBookActivity.this);
        progressDialog.setMessage("Gönderiliyor..");
        progressDialog.show();

        Date simdi = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        simdikiTarih = dateFormat.format(simdi);

        if (resimUri != null || videoUri != null) {
            Uri uri = videoUri != null ? videoUri : resimUri;
            if (uri == null)
                return;

            final StorageReference dosyaYolu = resimYukleYolu.child(System.currentTimeMillis()
                    + "." + dosyaUzantisiAl(uri));
            yuklemeGorevi = dosyaYolu.putFile(uri);
            yuklemeGorevi.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    progressDialog.dismiss();
                    return dosyaYolu.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri indirmeUrisi = task.getResult();
//                        benimUrim = indirmeUrisi.toString();
                        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");
                        String gonderiId = veriYolu.push().getKey();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("gonderiId", gonderiId);
                        hashMap.put("gonderiResmi", posterUrl);
                        hashMap.put("gonderiVideo", "");
                        hashMap.put("gonderiHakkinda", gonderi_hakkinda.getText().toString());
                        hashMap.put("gonderen", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("gorevmi", false);
                        hashMap.put("gonderiTarihi", simdikiTarih);
                        hashMap.put("gonderiTuru", "kitap");
                        hashMap.put("gonderiAdi", kitapAdi.getText().toString());
                        hashMap.put("onaydurumu", false);

                        veriYolu.child(gonderiId).setValue(hashMap);
                        final DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        kullaniciYolu.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!isOkey) return;
                                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);

                                HashMap<String, Object> kullaniciGuncelleHashMap = new HashMap<>();
                                kullaniciGuncelleHashMap.put("profilPuan", kullanici.getProfilPuan() + 3);
                                kullaniciYolu.updateChildren(kullaniciGuncelleHashMap);
                                isOkey = false;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        resimUri=null;
                        videoUri=null;
                        startActivity(new Intent(PostBookActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(PostBookActivity.this, "Gönderme başarısız!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostBookActivity.this, " Seçilen resim yok.." + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {

            DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");
            String gonderiId = veriYolu.push().getKey();
            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("gonderiId", gonderiId);
            hashMap.put("gonderiResmi", posterUrl);
            hashMap.put("gonderiVideo", "");
            hashMap.put("gonderiHakkinda", gonderi_hakkinda.getText().toString());
            hashMap.put("gonderen", FirebaseAuth.getInstance().getCurrentUser().getUid());
            hashMap.put("gorevmi", false);
            hashMap.put("gonderiTarihi", simdikiTarih);
            hashMap.put("gonderiTuru", "kitap");
            hashMap.put("gonderiAdi", kitapAdi.getText().toString());
            hashMap.put("onaydurumu", false);
            veriYolu.child(gonderiId).setValue(hashMap);


            final DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            kullaniciYolu.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!isOkey) return;
                    Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);

                    HashMap<String, Object> kullaniciGuncelleHashMap = new HashMap<>();
                    kullaniciGuncelleHashMap.put("profilPuan", kullanici.getProfilPuan() + 3);


                    kullaniciYolu.updateChildren(kullaniciGuncelleHashMap);
                    isOkey = false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            startActivity(new Intent(PostBookActivity.this, MainActivity.class));
            finish();
        }

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}