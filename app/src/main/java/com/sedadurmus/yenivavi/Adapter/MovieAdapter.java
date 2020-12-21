package com.sedadurmus.yenivavi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sedadurmus.yenivavi.DetailActivity;
import com.sedadurmus.yenivavi.Model.DownLoadImageTask;
import com.sedadurmus.yenivavi.Model.Movie;
import com.sedadurmus.yenivavi.R;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Movie> mMovies;

    private FirebaseUser mevcutFirebaseUser;

    public MovieAdapter(Context mContext) {
        this.mContext = mContext;
        this.mMovies = new ArrayList<Movie>();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Movie movie =mMovies.get(position);
        mevcutFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        new DownLoadImageTask(holder.filmGorsel).execute( "https://image.tmdb.org/t/p/w500/" +  mMovies.get(position).getPosterPath());

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView filmGorsel, favori;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filmGorsel= itemView.findViewById(R.id.film_img);
//            favori= itemView.findViewById(R.id.favoriEkle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("title", mMovies.get(pos).getTitle());
                        intent.putExtra("poster_path", mMovies.get(pos).getPosterPath());
                        intent.putExtra("overview", mMovies.get(pos).getOverview());
                        intent.putExtra("release_date", mMovies.get(pos).getRelease_date());
                        intent.putExtra("vote_average", Double.toString(mMovies.get(pos).getVote_average()));
                        intent.putExtra("vote_count", Integer.toString(mMovies.get(pos).getVote_count()));
                        intent.putExtra("video",(mMovies.get(pos).getVideo()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });


        }
    }


    public void addAll(List<Movie> videoModelList) {
        Log.e("MOVIE","ADDALL");

        for (Movie videoModel : videoModelList) {

                add(videoModel);
                //Log.e("NEWS",videoModel.Title);
        }

    }

    public void add(Movie videoModel) {
        mMovies.add(videoModel);
        //notifyItemInserted(Items.size() - 1);
        notifyDataSetChanged();
    }
}
