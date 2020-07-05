package com.sedadurmus.yenivavi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Model.Movie;

import java.util.ArrayList;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    TextView filmAdi, filmBegeni, filmHakkinda, txtTarih, puan, oySayisi;
    ImageView filmGorsel, backPoster, Favorite;
    private FirebaseUser mevcutFirebaseUser;
    Movie movie;
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


        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("title")){
            String poster = Objects.requireNonNull(getIntent().getExtras()).getString("poster_path");
            String poster2 = Objects.requireNonNull(getIntent().getExtras()).getString("poster_path");
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
//        favoriyeEkle(Favorite);
//
//        Favorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Favorite.getTag().equals("ekle")) {
//                    FirebaseDatabase.getInstance().getReference().child("Favoriler").child(movie.getId())
//                            .child(mevcutFirebaseUser.getUid()).setValue(true);
//
//                } else {
//                    FirebaseDatabase.getInstance().getReference().child("Favoriler").child(movie.getId())
//                            .child(mevcutFirebaseUser.getUid()).removeValue();
//                }
//            }
//        });


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



    }

    //favori ekleme için yaptıklarım
    private void favoriyeEkle( final ImageView imageView) {
        final FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference begeniVeriTabaniYolu = FirebaseDatabase.getInstance().getReference()
                .child("Favoriler");

        begeniVeriTabaniYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(mevcutKullanici.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_favorite_black);
                    imageView.setTag("eklendi");
                } else {
                    imageView.setImageResource(R.drawable.ic_favorite);
                    imageView.setTag("ekle");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}











