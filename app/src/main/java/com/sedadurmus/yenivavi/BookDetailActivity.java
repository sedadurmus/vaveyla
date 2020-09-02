package com.sedadurmus.yenivavi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.sedadurmus.yenivavi.Model.Book;

import java.util.ArrayList;
import java.util.Objects;

public class BookDetailActivity extends AppCompatActivity {

    TextView kitapAdi, kitapHakkinda, txtTarih, yazar, tCevirmen;
    ImageView kitapGorsel, create;
    String poster;
    private ArrayList<Book> mBooks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kitap");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

         kitapAdi=findViewById(R.id.kitap_title);
         yazar =findViewById(R.id.yazar);
         tCevirmen =findViewById(R.id.cevirmen);
        txtTarih=findViewById(R.id.BookDate);
        kitapHakkinda=findViewById(R.id.kitap_hakkinda);
        kitapGorsel=findViewById(R.id.kitap_poster);
        create = findViewById(R.id.create_book_gonderi);


        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("name")){
            poster = Objects.requireNonNull(getIntent().getExtras()).getString("img_url");
            String kitapName = getIntent().getExtras().getString("name");
            String yazarAd = getIntent().getExtras().getString("author");
            String hakkinda = getIntent().getExtras().getString("description");
            String cevirmen = getIntent().getExtras().getString("translator");
            String tarih = getIntent().getExtras().getString("published_year");

            Glide.with(this).load(poster).into(kitapGorsel);
            kitapAdi.setText(kitapName);
            yazar.setText(yazarAd);
            kitapHakkinda.setText(hakkinda);
            tCevirmen.setText(cevirmen);
            txtTarih.setText(tarih);
        }else {
            Toast.makeText(this, "Api bulunamadı", Toast.LENGTH_SHORT).show();
        }


        //        gönderide paylaştırabilmek için
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PostBookActivity.class);
                intent.putExtra("img_url", poster);
                intent.putExtra("name",   getIntent().getExtras().getString("name"));
                intent.putExtra("author",   getIntent().getExtras().getString("author"));
                startActivity(intent);
            }
        });

    }
}