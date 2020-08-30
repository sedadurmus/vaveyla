package com.sedadurmus.yenivavi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.sedadurmus.yenivavi.Adapter.KullaniciAdapter;
import com.sedadurmus.yenivavi.Adapter.MovieAdapter;
import com.sedadurmus.yenivavi.Adapter.SearchAdapter;
import com.sedadurmus.yenivavi.Api.ApiClient;
import com.sedadurmus.yenivavi.Api.ApiInterface;
import com.sedadurmus.yenivavi.Fragments.MovieSearchFragment;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.Model.Movie;
import com.sedadurmus.yenivavi.Model.TheMovieDB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private KullaniciAdapter kullaniciAdapter;
    private List<Kullanici> mKullaniciler;
    EditText Ara;
    private MovieAdapter movieAdapter;
    private List<Movie> mMovies;
    ViewPager viewPager;
    TabLayout tabLayout;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mMovies = new ArrayList<>();

        Toolbar toolbar =findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Ara= findViewById(R.id.edit_kullanici_arama_bar);

        viewPager =findViewById(R.id.viewPager);
        tabLayout =findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("Kişi"));
        tabLayout.addTab(tabLayout.newTab().setText("Film"));
        tabLayout.addTab(tabLayout.newTab().setText("Kitap"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final SearchAdapter adapter = new SearchAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        Ara.setHint("Kullanıcı Ara");
                        Ara.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                kullaniciAra(charSequence.toString().toLowerCase());
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });


                        return ;
                    case 1:
                        Ara.setHint("Film Ara");
                        movieAdapter = new MovieAdapter(getApplicationContext());
                        Ara.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                String search = charSequence.toString().toLowerCase();
                                Log.e("ARAMA", search);
                                filmAra(search);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });

                        return ;
                    case 2:
                        Ara.setHint("Kitap Ara");
                        return ;
                    default:
                        return ;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void kullaniciAra(String s) {
        Query sorgu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").orderByChild("kullaniciadi")
                .startAt(s)
                .endAt(s+"\uf8ff");

        sorgu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mKullaniciler.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Kullanici kullanici = snapshot.getValue(Kullanici.class);
                    mKullaniciler.add(kullanici);
                }
                kullaniciAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void filmAra(String search) {
        Log.e("ARAMA", search);
        Log.e("ARAMA", search);
        if(search.isEmpty())
            return;
        ApiInterface apiService = (ApiInterface) ApiClient.createService(ApiInterface.class);
        if (apiService != null) {
            Call call = apiService.getMovies("https://api.themoviedb.org/3/search/movie?api_key=b7ee738bdfe5a91a0cec31c619d58968&query=" + search + "&language=tr");
            call.enqueue((Callback)(new Callback() {
                public void onResponse(@NotNull Call call, @NotNull Response response) {

                    TheMovieDB theMovieDB = (TheMovieDB)response.body();
                    mMovies = theMovieDB.getResults();
                    Integer len = mMovies.toArray().length;
                    movieAdapter.notifyDataSetChanged();
                    loadDataAction(theMovieDB.getResults());
                    Log.e("TheMovieDB", len.toString());
                }

                public void onFailure(@NotNull Call call, @NotNull Throwable t) {
                    Log.e("MOVIES", "request fail");
                }
            }));
        }
    }
    private void loadDataAction( List<Movie> items) {

        if (items != null) {
            List<Movie> models = items;
            Log.e("EKLEME", models.toArray().toString());
            Collections.reverse(items);
            movieAdapter.addAll(items);
            movieAdapter.notifyDataSetChanged();
        }
    }



}