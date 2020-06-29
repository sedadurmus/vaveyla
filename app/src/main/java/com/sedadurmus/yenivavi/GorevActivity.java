package com.sedadurmus.yenivavi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class GorevActivity extends AppCompatActivity {

    Uri resimUri;
    String benimUrim = "";

    StorageTask yuklemeGorevi;
    StorageReference resimYukleYolu;

    ImageView image_Kapat, image_Eklendi,btn_gonderiActivitye_git;
    TextView txt_Gonder, txt_tema;
    EditText gonderi_hakkinda;
    private Bundle savedInstanceState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorev);
        btn_gonderiActivitye_git=findViewById(R.id.btn_gonderiActivitye_git);
        image_Kapat=findViewById(R.id.close_gonderi);
        image_Eklendi=findViewById(R.id.imageView_gonderi);
        txt_Gonder=findViewById(R.id.txt_gonder);
        gonderi_hakkinda=findViewById(R.id.edit_gonderi_hakkinda_txtGonderiActivity);
        resimYukleYolu = FirebaseStorage.getInstance().getReference("Gonderiler");
        image_Kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GorevActivity.this, R.style.AlertDialogTheme);
                View view = LayoutInflater.from(GorevActivity.this).inflate(
                        R.layout.alert_dialog,
                        (ConstraintLayout)findViewById(R.id.layoutDialogContainer)
                );
//                AlertDialog.Builder builder = new AlertDialog.Builder(GonderiActivity.this);
                builder.setCancelable(false);
                builder.setView(view);
                ((TextView)view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.alertTitle));
                ((TextView)view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.alertMessageGorevCikis));
                ((Button)view.findViewById(R.id.buttonNo)).setText(getResources().getString(R.string.alertButtonNo));
                ((Button)view.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.alertButtonYes));
                ((ImageView)view.findViewById(R.id.imageicon)).setImageResource(R.drawable.ic_info);
//                builder.setTitle("Uyarı");
//                builder.setMessage("Gönderiyi kaydetmeden çıkmak istediğinize emin misiniz?");
                final AlertDialog alertDialog1 = builder.create();
                view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(GorevActivity.this, "Görev Paylaşılamadı!", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent ıntent = new Intent(GorevActivity.this, MainActivity.class);
                        startActivity(ıntent);
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
        });
        txt_Gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GorevActivity.this, R.style.AlertDialogTheme);
                View view = LayoutInflater.from(GorevActivity.this).inflate(
                        R.layout.alert_dialog,
                        (ConstraintLayout)findViewById(R.id.layoutDialogContainer)
                );
//                getResources().getString(R.string.alertTitle)
                builder.setView(view);
                ((TextView)view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.alertTitle));
                ((TextView)view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.alertMessageGorev));
                ((Button)view.findViewById(R.id.buttonNo)).setText(getResources().getString(R.string.alertButtonNo));
                ((Button)view.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.alertButtonYes));
                ((ImageView)view.findViewById(R.id.imageicon)).setImageResource(R.drawable.ic_info);
                builder.setCancelable(false);
//                builder.setTitle("Uyarı");
//                builder.setMessage("Gönderiyi paylaşmak istediğinize emin misiniz?");
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
                        Toast.makeText(GorevActivity.this, "Görev Paylaşımı Yapılamadı!", Toast.LENGTH_SHORT).show();

                    }
                });
                if (alertDialog.getWindow() != null){
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });

        btn_gonderiActivitye_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(resimUri)
                        .setAspectRatio(1,1)
                        .start(GorevActivity.this);

            }
        });
    }
    private String dosyaUzantisiAl (Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    String simdikiTarih;
    private void   resimYukle(){
        //resim yükle kodları
        final ProgressDialog progressDialog = new ProgressDialog(GorevActivity.this);
        progressDialog.setMessage("Gönderiliyor..");
        progressDialog.show();
        Date simdi = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        simdikiTarih = dateFormat.format(simdi);
        if (resimUri != null)
        {
            final StorageReference dosyaYolu = resimYukleYolu.child(System.currentTimeMillis()
                    +"."+dosyaUzantisiAl(resimUri));
            yuklemeGorevi=dosyaYolu.putFile(resimUri);
            yuklemeGorevi.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    progressDialog.dismiss();
                    return dosyaYolu.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri indirmeUrisi = task.getResult();
                        benimUrim = indirmeUrisi.toString();
                        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");
                        String gonderiId = veriYolu.push().getKey();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("gonderiId", gonderiId);
                        hashMap.put("gorevDurumu", false);
                        hashMap.put("gonderiResmi", benimUrim);
                        hashMap.put("gonderiHakkinda",gonderi_hakkinda.getText().toString() );
                        hashMap.put("gonderen", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("gonderiTarihi",simdikiTarih);
                        hashMap.put("gorevmi", true);
                        hashMap.put("onaydurumu", false);
                        veriYolu.child(gonderiId).setValue(hashMap);
                        startActivity(new Intent(GorevActivity.this, MainActivity.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(GorevActivity.this, "Gönderme başarısız!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GorevActivity.this, " Seçilen resim yok.."+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");
            String gonderiId = veriYolu.push().getKey();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("gonderiId", gonderiId);
            hashMap.put("gorevDurumu", false);
            hashMap.put("gonderiResmi", "");
            hashMap.put("gonderiHakkinda",gonderi_hakkinda.getText().toString() );
            hashMap.put("gonderen", FirebaseAuth.getInstance().getCurrentUser().getUid());
            hashMap.put("gonderiTarihi",simdikiTarih);
            hashMap.put("gorevmi", true);
            hashMap.put("onaydurumu", false);
            veriYolu.child(gonderiId).setValue(hashMap);
            veriYolu.child(gonderiId).setValue(hashMap);
            startActivity(new Intent(GorevActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            resimUri = result.getUri();

            image_Eklendi.setImageURI(resimUri);
        }
        else {
            Toast.makeText(this, "Resim seçilemedi!", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(GorevActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(GorevActivity.this).inflate(
                R.layout.alert_dialog,
                (ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setCancelable(false);
        builder.setView(view);
        ((TextView)view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.alertTitle));
        ((TextView)view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.alertMessageGorevCikis));
        ((Button)view.findViewById(R.id.buttonNo)).setText(getResources().getString(R.string.alertButtonNo));
        ((Button)view.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.alertButtonYes));
        ((ImageView)view.findViewById(R.id.imageicon)).setImageResource(R.drawable.ic_info);
        final AlertDialog alertDialog1 = builder.create();
        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GorevActivity.this, "Paylaşım yapmadınız..", Toast.LENGTH_LONG).show();
                startActivity(new Intent(GorevActivity.this, MainActivity.class));
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
