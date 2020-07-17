package com.sedadurmus.yenivavi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.sedadurmus.yenivavi.Model.Movie;

import java.util.ArrayList;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    TextView filmAdi, filmBegeni, filmHakkinda, txtTarih, puan, oySayisi;
    ImageView filmGorsel, backPoster, Favorite, create;
    private FirebaseUser mevcutFirebaseUser;
    Movie movie;
    Context mContext;
    String poster;
    private ArrayList<Movie> mMovies;

    @SuppressLint("WrongViewCast")
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

        filmAdi=findViewById(R.id.movie_title);
        txtTarih=findViewById(R.id.tvReleaseDate);
        filmHakkinda=findViewById(R.id.movie_hakkinda);
        filmGorsel=findViewById(R.id.film_poster);
        puan = findViewById(R.id.puan_movie);
        oySayisi = findViewById(R.id.tvVote);
        backPoster = findViewById(R.id.film_poster2);
        Favorite = findViewById(R.id.imgFavorite);
        create = findViewById(R.id.create_gonderi);


        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("title")){
            poster = Objects.requireNonNull(getIntent().getExtras()).getString("poster_path");
            String movieName = getIntent().getExtras().getString("title");
            String hakkinda = getIntent().getExtras().getString("overview");
            String puan_ort = getIntent().getExtras().getString("vote_average");
            String puan_oy = getIntent().getExtras().getString("vote_count");
            String tarih = getIntent().getExtras().getString("release_date");

            Glide.with(this).load("https://image.tmdb.org/t/p/w500/" + poster).into(filmGorsel);
            Glide.with(this).load("https://image.tmdb.org/t/p/w500/" + poster).into(backPoster);
            filmAdi.setText(movieName);
            filmHakkinda.setText(hakkinda);
            puan.setText(puan_ort);
            oySayisi.setText(puan_oy);
            txtTarih.setText(tarih);
        }else {
            Toast.makeText(this, "Api bulunamadı", Toast.LENGTH_SHORT).show();
        }

//        viewModel.getMovieTrailers(movieDetailResponse?.movieId)
//        viewModel.movieTrailers.observe(viewLifecycleOwner, Observer{
//            it?.let {
//                recyclerviewTrailer.adapter =
//                        TrailerAdapter(
//                                it
//                        ) {
//                    val intent = Intent(Intent.ACTION_VIEW)
//                    intent.data = Uri.parse(Constant.YOUTUBE_WATCH_URL + it.key)
//                    startActivity(intent)
//                }
//            }
//        })




//        gönderide paylaştırabilmek için
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GonderiActivity.class);
                intent.putExtra("poster", poster);
                startActivity(intent);
               // startActivity(new Intent(DetailActivity.this, GonderiActivity.class));
            }
        });
    }

}











