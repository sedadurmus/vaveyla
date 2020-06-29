package com.sedadurmus.yenivavi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText edt_kullaniciAdi, edt_Ad, edt_Email, edt_Sifre, edt_Tarih;
    Button btn_Kaydol;
    TextView txt_girisSayfasina_git;
    ProgressDialog pd;
    DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int year, month, dayOfMonth;

    private FirebaseAuth yetki;
    private DatabaseReference yol;

    private long backPressedTime;
    private Toast backToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_kullaniciAdi = (EditText) findViewById(R.id.edt_kullaniciAdi);
        edt_Ad = (EditText) findViewById(R.id.edt_Ad);
        edt_Email = (EditText) findViewById(R.id.edt_email);
        edt_Sifre = (EditText) findViewById(R.id.edt_sifre);
        edt_Tarih = (EditText) findViewById(R.id.edt_Tarih);

        edt_Tarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                edt_Tarih.setText(day + "/" + (month+1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        btn_Kaydol= findViewById(R.id.btn_kaydol_activity);

        txt_girisSayfasina_git= findViewById(R.id.txt_girisSayfasina_git);

        yetki = FirebaseAuth.getInstance();

        txt_girisSayfasina_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        btn_Kaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd =new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Lütfen Bekleyin..");
                pd.show();
                 final String str_kullaniciAdi = edt_kullaniciAdi.getText().toString();
                 final String str_Ad = edt_Ad.getText().toString();
                 final String str_Email = edt_Email.getText().toString();
                 final String str_Sifre = edt_Sifre.getText().toString();

                Query kullaniciAdiQuery = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar")
                        .orderByChild("kullaniciadi").equalTo(str_kullaniciAdi);
                kullaniciAdiQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount()>0){
                            Toast.makeText(RegisterActivity.this, "Başka bir kullanıcı adı seçiniz!", Toast.LENGTH_LONG).show();
                        }else {
                            if (TextUtils.isEmpty(str_kullaniciAdi)||TextUtils.isEmpty(str_Ad)||TextUtils.isEmpty(str_Email)
                                    ||TextUtils.isEmpty(str_Sifre))
                            {
                                pd.dismiss();
                                Toast.makeText(RegisterActivity.this, "Lütfen bütün alanları doldurun!", Toast.LENGTH_LONG).show();
                            }
                            else if (str_Sifre.length()<6)
                            {
                                pd.dismiss();
                                Toast.makeText(RegisterActivity.this, "Şifreniz min. 6 karakter olmalıdır!", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                //Yeni Kullanıcı kaydetmet kodlarını çağır
                                kaydet(str_kullaniciAdi, str_Ad, str_Email, str_Sifre);

                            }
                        }
                        pd.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }

        });
    }

    private void kaydet (final String kullaniciadi, final String ad, String email, String sifre)
    {
        //Yeni Kullanıcı kaydetme kodları
        yetki.createUserWithEmailAndPassword(email,sifre)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser firebaseKullanici = yetki.getCurrentUser();

                            String kullaniciId = firebaseKullanici.getUid();

                            yol = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar")
                                    .child(kullaniciId);

                            HashMap<String , Object> hashMap  = new HashMap<>();
                            hashMap.put("id", kullaniciId);
                            hashMap.put("kullaniciadi", kullaniciadi.toLowerCase());
                            hashMap.put("ad", ad);
                            hashMap.put("bio", "");
                            hashMap.put("profilPuan", 20);
                            hashMap.put("resimurl","https://firebasestorage.googleapis.com/v0/b/vaveyla-b2ca8.appspot.com/o/placeholder.jpg?alt=media&token=8085a983-5920-4e9e-ba89-d6b2b0752a24");
                            yol.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                    {
                                        pd.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(RegisterActivity.this, "Kayıt başarılı! Lütfen giriş yapınız.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "Bu email veya şifre ile kayıt başarısız!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            backToast.cancel();
        } else {
            backToast = Toast.makeText(getBaseContext(), "Çıkmak için iki kere basınız", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
