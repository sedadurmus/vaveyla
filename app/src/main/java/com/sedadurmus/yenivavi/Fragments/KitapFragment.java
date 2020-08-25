package com.sedadurmus.yenivavi.Fragments;

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
import com.sedadurmus.yenivavi.Adapter.KitapAdapter;
import com.sedadurmus.yenivavi.Api.ApiInterface;
import com.sedadurmus.yenivavi.Api.Client;
import com.sedadurmus.yenivavi.Model.AllBookCategory;
import com.sedadurmus.yenivavi.Model.Book;
import com.sedadurmus.yenivavi.R;

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
            recyclerView =view.findViewById(R.id.first_recycler_view);
            recyclerView.setHasFixedSize(true);
            GridLayoutManager gridLayoutManager =new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL , false);
            recyclerView.setLayoutManager(gridLayoutManager);
////            loadBooks();
        loadPopular();
            kitapAdapter =new KitapAdapter(getContext());
            recyclerView.setAdapter(kitapAdapter);
            return view;
    }

//    private final void loadBooks() {
//        Log.e("Boooks", "len.toString()");
//        ApiInterface var10000 = (ApiInterface) ApiClient.createService(ApiInterface.class);
//        if (var10000 != null) {
//            ApiInterface apiService = var10000;
//            Call call = apiService.getBook("https://kitap.bildirimler.com/api/books");
//            call.enqueue((Callback)(new Callback() {
//                public void onResponse(@NotNull Call call, @NotNull Response response) {
//
//                    AllBookCategory allBookCategory = (AllBookCategory) response.body();
//                    assert allBookCategory != null;
//                    books = allBookCategory.getBooks();
//                    Integer len = books.toArray().length;
//                    kitapAdapter.notifyDataSetChanged();
//                    loadDataAction(allBookCategory.getBooks());
//                    Log.e("Boooks", len.toString());
//                }
//
//                public void onFailure(@NotNull Call call, @NotNull Throwable t) {
//
//                    Log.e("KITAPFRAGMENT", "request fail");
//                }
//            }));
//        }
//    }
//    private void loadDataAction( List<Book> items) {
//
//        if (items != null) {
//            List<Book> models = items;
//            Log.e("EKLEME", models.toArray().toString());
//            Collections.reverse(items);
//            kitapAdapter.addAll(items);
//            kitapAdapter.notifyDataSetChanged();
//        }
//    }


    private void loadPopular(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="http://kitap.bildirimler.com/api/categories?with=books";
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
            Call<List<AllBookCategory>> call = apiService.getCategories("http://kitap.bildirimler.com/api/categories?with=books");
            call.enqueue(new Callback<List<AllBookCategory>>() {
                @Override
                public void onResponse(Call<List<AllBookCategory>> call, Response<List<AllBookCategory>> response) {
                    List<AllBookCategory> categories = (List<AllBookCategory>)response.body();
//                    AllBookCategory category = categories.get(0);
                    Log.e("BURASI", categories.get(0).getSlug());
                    Log.e("BURASI", categories.get(0).getId().toString());

//                  if (response.isSuccessful()){
//                        if (response.body() != null){
//                            KitapAdapter firstAdapter = new KitapAdapter(getContext());
//                            RecyclerView firstRecyclerView = Objects.requireNonNull(getView()).findViewById(R.id.first_recycler_view);
//                            LinearLayoutManager firstManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//                            firstRecyclerView.setLayoutManager(firstManager);
//                            firstRecyclerView.setAdapter(firstAdapter);
//                        }
//                    }
                }

                @Override
                public void onFailure(Call<List<AllBookCategory>> call, Throwable t) {
                    Log.d("BURASI", t.getMessage());
                    Toast.makeText(getContext(), "Error fetching trailer data", Toast.LENGTH_SHORT).show();

                }
            });

        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


}