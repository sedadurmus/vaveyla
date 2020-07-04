package com.sedadurmus.yenivavi.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sedadurmus.yenivavi.Model.DownLoadImageTask;
import com.sedadurmus.yenivavi.Model.Movie;
import com.sedadurmus.yenivavi.R;


public class MovieDetayFragment extends Fragment {

    TextView filmAdi, filmBegeni, filmHakkinda, txtTarih, puan;
    ImageView filmGorsel;
    Movie movie;

    public MovieDetayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_movie_detay, container, false);
       filmAdi=view.findViewById(R.id.movie_title);
        filmHakkinda=view.findViewById(R.id.movie_hakkinda);
        filmGorsel=view.findViewById(R.id.film_poster);
        puan = view.findViewById(R.id.puan_movie);


       filmAdi.setText(movie.getTitle());
       filmHakkinda.setText(movie.getOverview());
        new DownLoadImageTask(filmGorsel).execute( "https://image.tmdb.org/t/p/w500/" +  movie.getPosterPath());
        puan.setText((int) movie.getVote_average());

        return  view;
    }
}
