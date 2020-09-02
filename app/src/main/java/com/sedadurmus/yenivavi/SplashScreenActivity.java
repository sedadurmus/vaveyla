package com.sedadurmus.yenivavi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.Model.WriteFile;

import java.util.ArrayList;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    String mail, sifre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) { // check user session
            MainActivity.mevcutKullanici = firebaseUser;
            final DatabaseReference yolGiris = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar")
                    .child(mAuth.getCurrentUser().getUid());

            yolGiris.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    LoginActivity.kullanici = dataSnapshot.getValue(Kullanici.class);
                    if (LoginActivity.kullanici== null) return;
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });


        } else {
            String userData = WriteFile.Instance.ReadDataFromFile(getApplicationContext());
            if (userData != null && userData.length() > 0) {
                String[] users = userData.split("(?<=-)", 2);
                mail = users[0];
                sifre = users[1];
                loginFunc();
            }
            else {
                Intent i = new Intent(SplashScreenActivity.this, IntroActivity.class);
                startActivity(i);
                finish();
            }
        }

//        üstteki else nin içinde splashden LoginAcvitivtye git olaraktı ama introyu eklemek için bu şekil yaptım.
    }

    private void loginFunc() {

        mAuth.signInWithEmailAndPassword(mail,sifre)
                .addOnCompleteListener(SplashScreenActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String asd=task.getException().getMessage();
                        if (task.isSuccessful())
                        {
                            final DatabaseReference yolGiris = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar")
                                    .child(mAuth.getCurrentUser().getUid());

                            yolGiris.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    LoginActivity.kullanici = dataSnapshot.getValue(Kullanici.class);
                                    if (LoginActivity.kullanici== null) return;
                                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                        else
                        {
                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                             Toast.makeText(SplashScreenActivity.this, "Lütfen giriş yapın veya kaydolun", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    int grantedCount = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            grantedCount++;
                        }
                    }
                    if (grantedCount == 0) {
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                }
                return;
            }
        }
    }

}
