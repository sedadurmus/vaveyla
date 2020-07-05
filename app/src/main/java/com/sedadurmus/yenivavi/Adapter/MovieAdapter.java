package com.sedadurmus.yenivavi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sedadurmus.yenivavi.DetailActivity;
import com.sedadurmus.yenivavi.Model.DownLoadImageTask;
import com.sedadurmus.yenivavi.Model.Movie;
import com.sedadurmus.yenivavi.R;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Movie> mMovies;

    public MovieAdapter(Context mContext) {
        this.mContext = mContext;
        this.mMovies = new ArrayList<Movie>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movie movie =mMovies.get(position);
//        Glide.with(mContext).load(movie.getPosterPath()).into(holder.filmGorsel);
        new DownLoadImageTask(holder.filmGorsel).execute( "https://image.tmdb.org/t/p/w500/" +  mMovies.get(position).getPosterPath());
//        String sDate1 = mMovies.get(position).getRelease_date();
//        SimpleDateFormat  formatter  =new SimpleDateFormat("yyyy-MM-dd");
//        holder.filmAdi.setText(mMovies.get(position).getTitle());
//        holder.filmHakkinda.setText(mMovies.get(position).getOverview());


    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView filmAdi, filmBegeni, filmHakkinda, txtTarih;
        ImageView filmGorsel;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filmGorsel= itemView.findViewById(R.id.film_img);

//            txtTarih= itemView.findViewById(R.id.txt_tarih);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Movie clickedDataItem = mMovies.get(pos);
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
//                        Toast.makeText(view.getContext(), clickedDataItem.getTitle() + "  Tıkladınız" , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void addAll(List<Movie> videoModelList) {
        Log.e("NEWS","ADDALL");
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
