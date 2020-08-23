package com.sedadurmus.yenivavi.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;
import com.sedadurmus.yenivavi.Adapter.KitapAdapter;
import com.sedadurmus.yenivavi.Api.ApiClient;
import com.sedadurmus.yenivavi.Api.ApiInterface;
import com.sedadurmus.yenivavi.Api.Client;
import com.sedadurmus.yenivavi.Model.AllBookCategory;
import com.sedadurmus.yenivavi.Model.Book;
import com.sedadurmus.yenivavi.R;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class KitapFragment extends Fragment {
    private RecyclerView recyclerView;
    private KitapAdapter kitapAdapter;
    private List<Book> books;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view=  inflater.inflate(R.layout.fragment_kitap, container, false);
//            recyclerView =view.findViewById(R.id.kitap_list);
//            recyclerView.setHasFixedSize(true);
//            GridLayoutManager gridLayoutManager =new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL , false);
//            recyclerView.setLayoutManager(gridLayoutManager);
////            loadBooks();
        loadPopular();
//            kitapAdapter =new KitapAdapter(getContext());
//            recyclerView.setAdapter(kitapAdapter);
            return view;
    }

    private final void loadBooks() {

        ApiInterface var10000 = (ApiInterface) ApiClient.createService(ApiInterface.class);
        if (var10000 != null) {
            ApiInterface apiService = var10000;
            Call call = apiService.getBook("http://kitap.bildirimler.com/api/books");
            call.enqueue((Callback)(new Callback() {
                public void onResponse(@NotNull Call call, @NotNull Response response) {

                    AllBookCategory allBookCategory = (AllBookCategory) response.body();
                    assert allBookCategory != null;
                    books = allBookCategory.getResults();
                    Integer len = books.toArray().length;
                    kitapAdapter.notifyDataSetChanged();
                    loadDataAction(allBookCategory.getResults());
                    Log.e("Boooks", len.toString());
                }

                public void onFailure(@NotNull Call call, @NotNull Throwable t) {

                    Log.e("KITAPFRAGMENT", "request fail");
                }
            }));
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


    private void loadPopular(){

        try{

            Client Client = new Client();
            ApiInterface apiService = Client.getClient().create(ApiInterface.class);
            Call<AllBookCategory> call = apiService.getBook("http://kitap.bildirimler.com/api/books");
            call.enqueue(new Callback<AllBookCategory>() {
                @Override
                public void onResponse(Call<AllBookCategory> call, Response<AllBookCategory> response) {
                    List<Book> movies = response.body().getResults();
                    if (response.isSuccessful()){
                        if (response.body() != null){
                            KitapAdapter firstAdapter = new KitapAdapter(getContext());
                            RecyclerView firstRecyclerView = recyclerView.findViewById(R.id.first_recycler_view);
                            LinearLayoutManager firstManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            firstRecyclerView.setLayoutManager(firstManager);
                            firstRecyclerView.setAdapter(firstAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AllBookCategory> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(getContext(), "Error fetching trailer data", Toast.LENGTH_SHORT).show();

                }
            });

        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


}