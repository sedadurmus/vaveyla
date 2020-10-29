package com.sedadurmus.yenivavi.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sedadurmus.yenivavi.Adapter.KitapAdapter;
import com.sedadurmus.yenivavi.Api.ApiInterface;
import com.sedadurmus.yenivavi.Api.Client;
import com.sedadurmus.yenivavi.Model.Book;
import com.sedadurmus.yenivavi.R;
import com.sedadurmus.yenivavi.SearchBookActivity;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class KitapFragment extends Fragment {
    private RecyclerView recyclerView;
    private KitapAdapter kitapAdapter;
    private List<Book> books;
    private FloatingActionButton fabBook;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view=  inflater.inflate(R.layout.fragment_kitap, container, false);

            fabBook =view.findViewById(R.id.fabBook);
            recyclerView =view.findViewById(R.id.first_recycler_view);
            recyclerView.setHasFixedSize(true);
            GridLayoutManager gridLayoutManager =new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL , false);
            recyclerView.setLayoutManager(gridLayoutManager);
////            loadBooks();
            loadPopular();
            kitapAdapter =new KitapAdapter(getContext());
            recyclerView.setAdapter(kitapAdapter);

            fabBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), SearchBookActivity.class));
                }
            });


            return view;
    }

    private void loadPopular(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="http://kitap.bildirimler.com/api/books";
        Log.e("BURASI", "response");
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("BURASI", "success");
                        Log.e("BURASI", response);
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("BURASI", "error.getMessage()");
                Log.e("BURASI", error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        try{
            Log.e("BURASI", "loadpopularbooks");
            Client Client = new Client();
            ApiInterface apiService = Client.getClient().create(ApiInterface.class);
            Call<List<Book>> call = apiService.getBooks("http://kitap.bildirimler.com/api/books");
            call.enqueue(new Callback<List<Book>>() {
                @Override
                public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                    List<Book> books = (List<Book>)response.body();
//                    AllBookCategory category = categories.get(0);
                    Log.e("BURASI", books.get(0).getSlug());
                    Log.e("BURASI", books.get(0).getId().toString());

                    Integer len = books.toArray().length;
                    loadDataAction(books);
                    kitapAdapter.notifyDataSetChanged();

                    Log.e("TheMovieDB", len.toString());
                }

                @Override
                public void onFailure(Call<List<Book>> call, Throwable t) {
                    Log.d("BURASI", t.getMessage());
                    Toast.makeText(getContext(), "Error fetching trailer data", Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private void loadDataAction( List<Book> items) {

        if (items != null) {
            List<Book> models = items;
            Log.e("EKLEME", models.toArray().toString());
            Collections.reverse(items);
            kitapAdapter.addAll(items);
            kitapAdapter.notifyDataSetChanged();
        }
    }

}