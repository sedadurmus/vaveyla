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
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.sedadurmus.yenivavi.Model.Movie;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    TextView filmAdi, filmBegeni, filmHakkinda, txtTarih, puan;
    ImageView filmGorsel;
    Movie movie;
    private ArrayList<Movie> mMovies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Film");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        initCollapsingToolbar();

        filmAdi=findViewById(R.id.movie_title);
        filmHakkinda=findViewById(R.id.movie_hakkinda);
        filmGorsel=findViewById(R.id.film_poster);
        puan = findViewById(R.id.puan_movie);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("title")){
            String poster = getIntent().getExtras().getString("poster_path");
            String movieName = getIntent().getExtras().getString("title");
            String hakkinda = getIntent().getExtras().getString("overview");
            String puan_ort = getIntent().getExtras().getString("vote_average");

            Glide.with(this)
                    .load(poster)
                    .into(filmGorsel);
//            new DownLoadImageTask(filmGorsel).execute( "https://image.tmdb.org/t/p/w500/" +  mMovies.get(Integer.parseInt(poster)).getPosterPath());
            filmAdi.setText(movieName);
            filmHakkinda.setText(hakkinda);
            puan.setText(puan_ort);
        }else {
            Toast.makeText(this, "Api bulunamadı", Toast.LENGTH_SHORT).show();
        }
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout  =  (CollapsingToolbarLayout) findViewById(R.id.toolbar);

    }
}











