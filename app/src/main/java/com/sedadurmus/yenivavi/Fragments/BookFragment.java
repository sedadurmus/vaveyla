package com.sedadurmus.yenivavi.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;
import com.sedadurmus.yenivavi.Adapter.MainRecyclerAdapter;
import com.sedadurmus.yenivavi.Api.ApiClient;
import com.sedadurmus.yenivavi.Api.ApiInterface;
import com.sedadurmus.yenivavi.Model.AllBookCategory;
import com.sedadurmus.yenivavi.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookFragment extends Fragment {


    private RecyclerView mainCategoryRecycler;
    private MainRecyclerAdapter mainRecyclerAdapter;
    private List<AllBookCategory> allBookCategoryList;
    private ProgressBar bar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_books, container, false);

        ArrayList<AllBookCategory> allBookCategoryList =new ArrayList<>();
        allBookCategoryList.add(new AllBookCategory());


        mainCategoryRecycler = view.findViewById(R.id.main_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mainCategoryRecycler.setLayoutManager(layoutManager);

        loadPopularBooks();

        mainRecyclerAdapter = new MainRecyclerAdapter(getContext(), allBookCategoryList);
        mainCategoryRecycler.setAdapter(mainRecyclerAdapter);



        return view;
    }




    private void loadPopularBooks() {
        ApiInterface var10000 = (ApiInterface) ApiClient.createService(ApiInterface.class);
        Log.e("BookCategoryresponse", "loadpopularbooks");
        if (var10000 != null) {
            ApiInterface apiService = var10000;
            Call call = apiService.getCategories("https://kitap.vaveyla.app/api/categories?with=books");
            call.enqueue((Callback)(new Callback() {
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    Log.e("BookCategoryresponse", response.body().toString());
                    List<AllBookCategory> categories = (List<AllBookCategory>)response.body();
                    allBookCategoryList = categories;
                    Integer len = allBookCategoryList.toArray().length;
                    mainRecyclerAdapter.notifyDataSetChanged();
                    loadDataAction(allBookCategoryList);
                    Log.e("BookCategory", len.toString());
                }

                public void onFailure(@NotNull Call call, @NotNull Throwable t) {

                    Log.e("BookCategory", "request fail");
                }
            }));
        }
    }

    private void loadDataAction( List<AllBookCategory> items) {

        if (items != null) {
            List<AllBookCategory> models = items;
            Log.e("EKLEME", models.toArray().toString());
            Collections.reverse(items);
            mainRecyclerAdapter.addAll(items);
            mainRecyclerAdapter.notifyDataSetChanged();
        }
    }


}