package com.sedadurmus.yenivavi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Adapter.KullaniciAdapter;
import com.sedadurmus.yenivavi.Model.Kullanici;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sedadurmus.yenivavi.Api.Client.BASE_URL;

public class SearchActivity extends AppCompatActivity {

    private KullaniciAdapter kullaniciAdapter;
    private List<Kullanici> mKullaniciler;
    SearchView Ara;
//    private MovieAdapter movieAdapter;
//    private List<Movie> mMovies;
    ViewPager viewPager;
    TabLayout tabLayout;

    private RequestQueue mRequestQueue;
    RecyclerView recyclerViewSearch;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tek);
        mKullaniciler = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Ara = findViewById(R.id.edit_kullanici_arama_bar);
        recyclerViewSearch= findViewById(R.id.arama_recycler);
        recyclerViewSearch.setHasFixedSize(true);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        mKullaniciler = new ArrayList<>();

        kullaniciAdapter =new KullaniciAdapter(getApplicationContext(), mKullaniciler);
        recyclerViewSearch.setAdapter(kullaniciAdapter);
        mRequestQueue = Volley.newRequestQueue(this);

        kullanicileriOku();
//        loadMovies();
        Ara.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // collapse the view ?
                //menu.findItem(R.id.menu_search).collapseActionView();
                Log.e("queryText",query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // search goes here !!
                // listAdapter.getFilter().filter(query);
                kullaniciAra(newText);
                Log.e("queryText",newText);
                return false;
            }


        });




   /*     viewPager =findViewById(R.id.viewPager);
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


    */


    }

        private void kullaniciAra (String s)
    {
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

    private void kullanicileriOku ()
    {
        DatabaseReference kullanicilerYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar");

        kullanicilerYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (Ara.getQuery().toString().equals(""))
                {
                    mKullaniciler.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Kullanici kullanici =snapshot.getValue(Kullanici.class);
                        mKullaniciler.add(kullanici);
                    }
                    kullaniciAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void parseJson(String key) {

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, key.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String title ="";
                        String author ="";
                        String publishedDate = "NoT Available";
                        String description = "No Description";
                        int pageCount = 1000;
                        String categories = "No categories Available ";
                        String buy ="";

                        String price = "NOT_FOR_SALE";
                        try {
                            JSONArray items = response.getJSONArray("items");

                            for (int i = 0 ; i< items.length() ;i++){
                                JSONObject item = items.getJSONObject(i);
                                JSONObject volumeInfo = item.getJSONObject("volumeInfo");



                                try{
                                    title = volumeInfo.getString("title");

                                    JSONArray authors = volumeInfo.getJSONArray("authors");
                                    if(authors.length() == 1){
                                        author = authors.getString(0);
                                    }else {
                                        author = authors.getString(0) + "|" +authors.getString(1);
                                    }


                                    publishedDate = volumeInfo.getString("publishedDate");
                                    pageCount = volumeInfo.getInt("pageCount");



                                    JSONObject saleInfo = item.getJSONObject("saleInfo");
                                    JSONObject listPrice = saleInfo.getJSONObject("listPrice");
                                    price = listPrice.getString("amount") + " " +listPrice.getString("currencyCode");
                                    description = volumeInfo.getString("description");
                                    buy = saleInfo.getString("buyLink");
                                    categories = volumeInfo.getJSONArray("categories").getString(0);

                                }catch (Exception e){

                                }
                                String thumbnail = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");

                                String previewLink = volumeInfo.getString("previewLink");
                                String url = volumeInfo.getString("infoLink");


//                                mMovies.add(new Movie(title,  ));


//                                movieAdapter = new MovieAdapter(getApplicationContext());
//                                recyclerViewSearch.setAdapter(movieAdapter);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TAG" , e.toString());

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }


    private boolean Read_network_state(Context context)
    {    boolean is_connected;
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        is_connected=info!=null&&info.isConnectedOrConnecting();
        return is_connected;
    }
    private void search()
    {
        String search_query = Ara.getQuery().toString();

        boolean is_connected = Read_network_state(this);
        if(!is_connected)
        {
//            error_message.setText(R.string.Failed_to_Load_data);
//            mRecyclerView.setVisibility(View.INVISIBLE);
//            error_message.setVisibility(View.VISIBLE);
            return;
        }

        //  Log.d("QUERY",search_query);


        if(search_query.equals(""))
        {
            Toast.makeText(this,"Please enter your query",Toast.LENGTH_SHORT).show();
            return;
        }
        String final_query=search_query.replace(" ","+");
        Uri uri=Uri.parse(BASE_URL+final_query);
        Uri.Builder buider = uri.buildUpon();

        parseJson(buider.toString());
    }

}