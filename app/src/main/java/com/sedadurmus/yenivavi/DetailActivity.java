package com.sedadurmus.yenivavi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.sedadurmus.yenivavi.Api.ApiEndpoint;
import com.sedadurmus.yenivavi.Model.Movie;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    TextView filmAdi, filmBegeni, filmHakkinda, txtTarih, puan, oySayisi;
    ImageView filmGorsel, backPoster, Favorite, create;
    private FirebaseUser mevcutFirebaseUser;
    Movie movie;
    Context mContext;
    String poster;
    private ArrayList<Movie> mMovies;

    ProgressDialog progressDialog;
    Toolbar toolbar;
    TextView tvTitle, tvName, tvRating, tvRelease, tvPopularity, tvOverview;
    ImageView imgCover, imgPhoto;
    RecyclerView rvTrailer;
    MaterialFavoriteButton imgFavorite;
    FloatingActionButton fabShare;
    RatingBar ratingBar;
    String NameFilm, ReleaseDate, Popularity, Overview, Cover, Thumbnail, movieURL;
    int Id;
    double Rating;

    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Lütfen bekleyiniz...");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fragman gösteriliyor...");

        ratingBar = findViewById(R.id.ratingBar);
        imgCover = findViewById(R.id.imgCover);
        imgPhoto = findViewById(R.id.imgPhoto);
//        imgFavorite = findViewById(R.id.imgFavorite);
        tvTitle = findViewById(R.id.tvTitle);
        tvName = findViewById(R.id.tvName);
        tvRating = findViewById(R.id.tvRating);
        tvRelease = findViewById(R.id.tvRelease);
        tvPopularity = findViewById(R.id.tvPopularity);
        tvOverview = findViewById(R.id.tvOverview);
//        rvTrailer = findViewById(R.id.rvTrailer);
        fabShare = findViewById(R.id.fabShare);
        create = findViewById(R.id.create_gonderi);
//        filmAdi=findViewById(R.id.movie_title);
//        txtTarih=findViewById(R.id.tvReleaseDate);
//        filmHakkinda=findViewById(R.id.movie_hakkinda);
//        filmGorsel=findViewById(R.id.film_poster);
//        puan = findViewById(R.id.puan_movie);
//        oySayisi = findViewById(R.id.tvVote);
//        backPoster = findViewById(R.id.film_poster2);
//        Favorite = findViewById(R.id.imgFavorite);
//        create = findViewById(R.id.create_gonderi);


//        Intent intentThatStartedThisActivity = getIntent();
//        if (intentThatStartedThisActivity.hasExtra("title")){
//            poster = Objects.requireNonNull(getIntent().getExtras()).getString("poster_path");
//            String movieName = getIntent().getExtras().getString("title");
//            String hakkinda = getIntent().getExtras().getString("overview");
//            String puan_ort = getIntent().getExtras().getString("vote_average");
//            String puan_oy = getIntent().getExtras().getString("vote_count");
//            String tarih = getIntent().getExtras().getString("release_date");
//
//            Glide.with(this).load("https://image.tmdb.org/t/p/w500/" + poster).into(filmGorsel);
//            Glide.with(this).load("https://image.tmdb.org/t/p/w500/" + poster).into(backPoster);
//            filmAdi.setText(movieName);
//            filmHakkinda.setText(hakkinda);
//            puan.setText(puan_ort);
//            oySayisi.setText(puan_oy);
//            txtTarih.setText(tarih);
//        }else {
//            Toast.makeText(this, "Api bulunamadı", Toast.LENGTH_SHORT).show();
//        }

        movie = (Movie) getIntent().getSerializableExtra("detailMovie");
        if (movie != null) {

            Id = movie.getId();
            NameFilm = movie.getTitle();
            Rating = movie.getVote_average();
            ReleaseDate = movie.getRelease_date();
            Popularity = movie.getPopularity();
            Overview = movie.getOverview();
            Cover = movie.getBackdropPath();
            Thumbnail = movie.getPosterPath();
            movieURL = ApiEndpoint.URLFILM + "" + Id;

            tvTitle.setText(NameFilm);
            tvName.setText(NameFilm);
            tvRating.setText(Rating + "/10");
            tvRelease.setText(ReleaseDate);
            tvPopularity.setText(Popularity);
            tvOverview.setText(Overview);
            tvTitle.setSelected(true);
            tvName.setSelected(true);

            float newValue = (float)Rating;
            ratingBar.setNumStars(5);
            ratingBar.setStepSize((float) 0.5);
            ratingBar.setRating(newValue / 2);

            Glide.with(this)
                    .load(ApiEndpoint.URLIMAGE + Cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCover);

            Glide.with(this)
                    .load(ApiEndpoint.URLIMAGE + Thumbnail)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgPhoto);

//            rvTrailer.setHasFixedSize(true);
//            rvTrailer.setLayoutManager(new LinearLayoutManager(this));

//            getTrailer();

        }

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String subject = movie.getTitle();
                String description = movie.getOverview();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, subject + "\n\n" + description + "\n\n" + movieURL);
                startActivity(Intent.createChooser(shareIntent, "İle paylaş :"));
            }
        });


//        gönderide paylaştırabilmek için
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                intent.putExtra("poster", Thumbnail);
                intent.putExtra("title",  NameFilm);
                intent.putExtra("vote_average",   Rating);
                intent.putExtra("release_date",   ReleaseDate);
                startActivity(intent);
            }
        });
    }

}











