package com.sedadurmus.yenivavi;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class AyarlarActivity extends AppCompatActivity {

    LinearLayout yardim, kullanici, hakkinda, cikis;
    Fragment selectedFragment = null;
    ImageView geri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ayarlar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        yardim = findViewById(R.id.yardim_linear);
        kullanici = findViewById(R.id.kullanici_sozlesmesi_linear);
//        hakkinda = findViewById(R.id.hakkinda_linear);
        cikis = findViewById(R.id.cikis_linear);
//        geri = findViewById(R.id.geri_ayarlar);

        cikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AyarlarActivity.this, R.style.AlertDialogTheme);
                View view = LayoutInflater.from(AyarlarActivity.this).inflate(
                        R.layout.alert_dialog,
                        (ConstraintLayout)findViewById(R.id.layoutDialogContainer)
                );
                builder.setCancelable(false);
                builder.setView(view);
                ((TextView)view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.alertTitle));
                ((TextView)view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.alertMessageHesapCikis));
                ((Button)view.findViewById(R.id.buttonNo)).setText(getResources().getString(R.string.alertButtonNo));
                ((Button)view.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.alertButtonYes));
                ((ImageView)view.findViewById(R.id.imageicon)).setImageResource(R.drawable.ic_info);
                final AlertDialog alertDialog1 = builder.create();
                view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        Intent logoutIntent = new Intent(AyarlarActivity.this, LoginActivity.class);
                        startActivity(logoutIntent);
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
//                new AlertDialog.Builder(AyarlarActivity.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Çıkış Yap")
//                        .setMessage("Çıkış yapmak istediğinize emin misiniz?")
//                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                FirebaseAuth.getInstance().signOut();
//                                Intent logoutIntent = new Intent(AyarlarActivity.this, LoginActivity.class);
//                                startActivity(logoutIntent);
//                                finish();
//                            }
//                        }).setNegativeButton("Hayır", null).show();
            }
        });

        yardim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AyarlarActivity.this, YardimActivity.class));

            }
        });

        kullanici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AyarlarActivity.this, KullaniciSozlesmesiActivity.class));
            }
        });
    }
}
