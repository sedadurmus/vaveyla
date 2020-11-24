package com.sedadurmus.yenivavi.Fragments;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;
import com.sedadurmus.yenivavi.Adapter.MovieAdapterCategory;
import com.sedadurmus.yenivavi.Adapter.MovieHorizontalAdapter;
import com.sedadurmus.yenivavi.Api.ApiEndpoint;
import com.sedadurmus.yenivavi.Model.Movie;
import com.sedadurmus.yenivavi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//import com.azhar.moviedb.R;
//import com.azhar.moviedb.activities.DetailMovieActivity;
//import com.azhar.moviedb.adapter.MovieAdapter;
//import com.azhar.moviedb.adapter.MovieHorizontalAdapter;
//import com.azhar.moviedb.model.ModelMovie;
//import com.azhar.moviedb.networking.ApiEndpoint;

public class FragmentMovie extends Fragment implements MovieHorizontalAdapter.onSelectData, MovieAdapterCategory.onSelectData {

    private RecyclerView rvNowPlaying, rvFilmRecommend;
    private MovieHorizontalAdapter movieHorizontalAdapter;
    private MovieAdapterCategory movieAdapter;
    private ProgressDialog progressDialog;
    private SearchView searchFilm;
    private List<Movie> moviePlayNow = new ArrayList<>();
    private List<Movie> moviePopular = new ArrayList<>();

    public FragmentMovie() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_film, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data");

        searchFilm = rootView.findViewById(R.id.searchFilm);
        searchFilm.setQueryHint(getString(R.string.search_film));
        searchFilm.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setSearchMovie(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals(""))
                    getMovie();
                return false;
            }
        });

        int searchPlateId = searchFilm.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchFilm.findViewById(searchPlateId);
        if (searchPlate != null) {
            searchPlate.setBackgroundColor(Color.TRANSPARENT);
        }

        rvNowPlaying = rootView.findViewById(R.id.rvNowPlaying);
        rvNowPlaying.setHasFixedSize(true);
        rvNowPlaying.setLayoutManager(new CardSliderLayoutManager(getActivity()));
        new CardSnapHelper().attachToRecyclerView(rvNowPlaying);

        rvFilmRecommend = rootView.findViewById(R.id.rvFilmRecommend);
        rvFilmRecommend.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFilmRecommend.setHasFixedSize(true);

        getMovieHorizontal();
        getMovie();

        return rootView;
    }

    private void setSearchMovie(String query) {
        progressDialog.show();
        AndroidNetworking.get(ApiEndpoint.BASEURL + ApiEndpoint.SEARCH_MOVIE
                + ApiEndpoint.APIKEY + ApiEndpoint.LANGUAGE + ApiEndpoint.QUERY + query)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            moviePopular = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Movie dataApi = new Movie();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("release_date");

                                dataApi.setId(jsonObject.getString("id"));
                                dataApi.setTitle(jsonObject.getString("title"));
                                dataApi.setVote_average(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setRelease_date(formatter.format(dateFormat.parse(datePost)));
                                dataApi.setPoster_path(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getDouble("popularity"));
                                moviePopular.add(dataApi);
                                showMovie();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getMovieHorizontal() {
        progressDialog.show();
        AndroidNetworking.get(ApiEndpoint.BASEURL + ApiEndpoint.MOVIE_PLAYNOW + ApiEndpoint.APIKEY + ApiEndpoint.LANGUAGE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Movie dataApi = new Movie();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("release_date");

                                dataApi.setId(jsonObject.getString("id"));
                                dataApi.setTitle(jsonObject.getString("title"));
                                dataApi.setVote_average(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setRelease_date(formatter.format(dateFormat.parse(datePost)));
                                dataApi.setPoster_path(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getDouble("popularity"));
                                moviePlayNow.add(dataApi);
                                showMovieHorizontal();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getMovie() {
        progressDialog.show();
        AndroidNetworking.get(ApiEndpoint.BASEURL + ApiEndpoint.MOVIE_POPULAR + ApiEndpoint.APIKEY + ApiEndpoint.LANGUAGE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            moviePopular = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Movie dataApi = new Movie();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("release_date");

                                dataApi.setId(jsonObject.getString("id"));
                                dataApi.setTitle(jsonObject.getString("title"));
                                dataApi.setVote_average(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setRelease_date(formatter.format(dateFormat.parse(datePost)));
                                dataApi.setPoster_path(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getDouble("popularity"));
                                moviePopular.add(dataApi);
                                showMovie();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showMovieHorizontal() {
        movieHorizontalAdapter = new MovieHorizontalAdapter(getActivity(), moviePlayNow, this);
        rvNowPlaying.setAdapter(movieHorizontalAdapter);
        movieHorizontalAdapter.notifyDataSetChanged();
    }

    private void showMovie() {
        movieAdapter = new MovieAdapterCategory(getActivity(), moviePopular, this);
        rvFilmRecommend.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
    }



    @Override
    public void onSelected(Movie modelMovie) {
//        Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
//        intent.putExtra("detailMovie", modelMovie);
//        startActivity(intent);
    }
}