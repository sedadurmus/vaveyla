package com.sedadurmus.yenivavi.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;
import com.sedadurmus.yenivavi.Adapter.BookAdapterCategory;
import com.sedadurmus.yenivavi.Adapter.BookHorizontalAdapter;
import com.sedadurmus.yenivavi.Api.ApiInterface;
import com.sedadurmus.yenivavi.Api.Client;
import com.sedadurmus.yenivavi.DetailActivity;
import com.sedadurmus.yenivavi.Model.Book;
import com.sedadurmus.yenivavi.Model.BookSearch;
import com.sedadurmus.yenivavi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;


public class FragmentBook extends Fragment implements BookHorizontalAdapter.onSelectData, BookAdapterCategory.onSelectData{

    private RecyclerView rvNowBookPlaying, rvBookRecommend;
    private BookHorizontalAdapter bookHorizontalAdapter;
    private BookAdapterCategory bookAdapter;
    private ProgressDialog progressDialog;
    private SearchView searchBook;
    private List<Book> bookPlayNow = new ArrayList<>();
    private List<BookSearch> bookPopular = new ArrayList<>();

    private static  final  String BASE_URL="https://www.googleapis.com/books/v1/volumes?q=";

    private RequestQueue mRequestQueue;
    private TextView error_message;

    public FragmentBook() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_book, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Lütfen bekleyiniz...");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Yükleniyor...");

        mRequestQueue = Volley.newRequestQueue(getContext());

        searchBook = rootView.findViewById(R.id.searchBook);
        searchBook.setQueryHint(getString(R.string.search_book));
        searchBook.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                boolean is_connected = Read_network_state(getContext());
                if(!is_connected)
                {
                    error_message.setText(R.string.Failed_to_Load_data);
                    rvBookRecommend.setVisibility(View.INVISIBLE);
                    error_message.setVisibility(View.VISIBLE);
                }

                String final_query=query.replace(" ","+");
                Uri uri=Uri.parse(BASE_URL+final_query);
                Uri.Builder buider = uri.buildUpon();

                setSearchBook(buider.toString());
//                setSearchBook(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals(""))

                    getBook(newText);
                return false;
            }
        });

        int searchPlateId = searchBook.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchBook.findViewById(searchPlateId);
        if (searchPlate != null) {
            searchPlate.setBackgroundColor(Color.TRANSPARENT);
        }
// horizontal gösterim için
        rvNowBookPlaying = rootView.findViewById(R.id.rvNowBookPlaying);
        rvNowBookPlaying.setHasFixedSize(true);
        rvNowBookPlaying.setLayoutManager(new CardSliderLayoutManager(getActivity()));
        new CardSnapHelper().attachToRecyclerView(rvNowBookPlaying);

//        search için
        rvBookRecommend = rootView.findViewById(R.id.rvBookRecommend);
        rvBookRecommend.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvBookRecommend.setHasFixedSize(true);

        getBookHorizontal();
//        getBook();


        return rootView;
    }

    private void setSearchBook(String key) {
        progressDialog.show();
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, key.toString(), null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        progressDialog.dismiss();
                        String title ="";
                        String author ="";
                        String publishedDate = "Yayım Tarihi Bulunamadı";
                        String description = "Açıklama Bulunamadı";
                        int pageCount = 1000;
                        String categories = "Kategori Bulunamadı ";
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


                                bookPopular.add(new BookSearch(title , author , publishedDate , description ,categories
                                        ,thumbnail,buy,previewLink,price,pageCount,url));


//                                bookAdapter = new BookAdapterCategory(getActivity(), bookPopular, this);
//                                rvBookRecommend.setAdapter(bookAdapter);
                                showBook();
                                progressDialog.dismiss();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TAG" , e.toString());

                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                progressDialog.dismiss();
            }
        });
        mRequestQueue.add(request);
    }

    private void getBookHorizontal() {
        progressDialog.show();
//        AndroidNetworking.get(ApiEndpoint.BASEURL + ApiEndpoint.MOVIE_PLAYNOW + ApiEndpoint.APIKEY + ApiEndpoint.LANGUAGE)
//                .setPriority(Priority.HIGH)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            progressDialog.dismiss();
//                            JSONArray jsonArray = response.getJSONArray("results");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                Kitap dataApi = new Kitap();
//                                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
//                                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
//                                String datePost = jsonObject.getString("release_date");
//
////                                dataApi.setId(jsonObject.getInt(""));
//                                dataApi.setmTitle(jsonObject.getString("mTitle"));
//                                dataApi.setmDescription(jsonObject.getString("mDescription"));
//                                dataApi.setmAuthors(jsonObject.getString("mAuthors"));
//                                dataApi.setmPublishedDate(formatter.format(dateFormat.parse(datePost)));
//                                dataApi.setmThumbnail(jsonObject.getString("mThumbnail"));
////                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
////                                dataApi.setPopularity(jsonObject.getString("popularity"));
//                                bookPlayNow.add(dataApi);
//                                showBookHorizontal();
//                            }
//                        } catch (JSONException | ParseException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getActivity(), "Bulunamadı!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        progressDialog.dismiss();
//                        Toast.makeText(getActivity(), "İnternet bağlantınızı kontrol ediniz!", Toast.LENGTH_SHORT).show();
//                    }
//                });
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
                    bookHorizontalAdapter.notifyDataSetChanged();

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
            progressDialog.dismiss();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getBook(String key) {
        progressDialog.show();
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, key.toString(), null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        progressDialog.dismiss();
                        String title ="";
                        String author ="";
                        String publishedDate = "Yayım Tarihi Bulunamadı";
                        String description = "Açıklama Bulunamadı";
                        int pageCount = 1000;
                        String categories = "Kategori Bulunamadı ";
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


                                bookPopular.add(new BookSearch(title , author , publishedDate , description ,categories
                                        ,thumbnail,buy,previewLink,price,pageCount,url));


//                                bookAdapter = new BookAdapterCategory(getActivity(), bookPopular, this);
//                                rvBookRecommend.setAdapter(bookAdapter);
                                showBook();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TAG" , e.toString());

                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                progressDialog.dismiss();
            }
        });
        mRequestQueue.add(request);
    }

    private void loadDataAction( List<Book> items) {
        progressDialog.show();
        if (items != null) {
            List<Book> models = items;
            Log.e("EKLEME", models.toArray().toString());
            Collections.reverse(items);
//            bookHorizontalAdapter.addAll(items);
//            bookHorizontalAdapter.notifyDataSetChanged();
            showBookHorizontal(items);
        }
        progressDialog.dismiss();
    }

    private void showBookHorizontal(List<Book> items) {
        bookHorizontalAdapter = new BookHorizontalAdapter(getActivity(), bookPlayNow, this);
        rvNowBookPlaying.setAdapter(bookHorizontalAdapter);
        bookHorizontalAdapter.addAll(items);
        bookHorizontalAdapter.notifyDataSetChanged();
    }

    private void showBook() {
        bookAdapter = new BookAdapterCategory(getActivity(), bookPopular, this);
        rvBookRecommend.setAdapter(bookAdapter);
        bookAdapter.notifyDataSetChanged();
    }


    @Override
    public void onSelected(Book modelBook) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("detailBook", modelBook);
        startActivity(intent);
    }



    @Override
    public void onSelected(BookSearch ModelBook) {

    }

    private boolean Read_network_state(Context context)
    {    boolean is_connected;
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        is_connected=info!=null&&info.isConnectedOrConnecting();
        return is_connected;
    }
}