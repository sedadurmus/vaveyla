package com.sedadurmus.yenivavi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class PostActivity extends AppCompatActivity {

    ImageView filmPoster, filmposter2;
    EditText hakkinda;
    TextView paylas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar toolbar =findViewById(R.id.toolbar_movie);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Gönderi Oluştur");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        filmPoster =findViewById(R.id.film_img);
        filmposter2=findViewById(R.id.film_poster);
        hakkinda =findViewById(R.id.edit_movie_gonderi);
        paylas =findViewById(R.id.txt_gonder);




        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("title")){
            String poster = Objects.requireNonNull(getIntent().getExtras()).getString("poster_path");
            String movieName = getIntent().getExtras().getString("title");
            String hakkinda = getIntent().getExtras().getString("overview");
            String puan_ort = getIntent().getExtras().getString("vote_average");
            String puan_oy = getIntent().getExtras().getString("vote_count");
            String tarih = getIntent().getExtras().getString("release_date");

            Glide.with(this).load("https://image.tmdb.org/t/p/w500/" + poster).into(filmPoster);
            Glide.with(this).load("https://image.tmdb.org/t/p/w500/" + poster).into(filmposter2);

        }else {
            Toast.makeText(this, "Api bulunamadı", Toast.LENGTH_SHORT).show();
        }

    }
}
