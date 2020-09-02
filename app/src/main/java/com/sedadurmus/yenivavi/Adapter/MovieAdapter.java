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

//        favoriEklendi(movie, holder.favori);
//
//        holder.favori.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMovies.clear();
//                Log.e("FAVORİ",holder.favori.getTag().toString() );
//                if (holder.favori.getTag().equals("ekle")) {
//
//                    favoriEkle(movie);
//                } else if (holder.favori.getTag().equals("eklendi")){
//                    FirebaseDatabase.getInstance().getReference("Favoriler")
//                            .child("FilmFavorisi")
//                            .child(mevcutFirebaseUser.getUid())
//                            .child(movie.getId()).removeValue();
//
//
//                }
//            }
//        });
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


//    //favori ekleme için yaptıklarım
//
//    private void favoriEklendi(final Movie movie,  final ImageView imageView) {
//        final FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
//
//        DatabaseReference begeniVeriTabaniYolu = FirebaseDatabase.getInstance().getReference()
//                .child("Favoriler").child("FilmFavorisi");
//        begeniVeriTabaniYolu.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                assert mevcutKullanici != null;
//                if (dataSnapshot.child(mevcutKullanici.getUid()).child(movie.getId()).exists()) {
//                    imageView.setImageResource(R.drawable.ic_check_circle);
//                    imageView.setTag("eklendi");
//
//                } else {
//                    imageView.setImageResource(R.drawable.ic_add_circle);
//                    imageView.setTag("ekle");
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//    }
//
//    public void favoriEkle (final Movie movie){
//        final FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
//        assert mevcutKullanici != null;
//        DatabaseReference favoriFire = FirebaseDatabase.getInstance().getReference("Favoriler").child("FilmFavorisi")
//                .child(mevcutKullanici.getUid());
//
//        String id = movie.getId();
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("title", movie.getTitle());
//        hashMap.put("poster_path", movie.getPosterPath());
//        favoriFire.child(id).setValue(hashMap);
//
//    }



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
