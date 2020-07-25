package com.sedadurmus.yenivavi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.DetailActivity;
import com.sedadurmus.yenivavi.Model.DownLoadImageTask;
import com.sedadurmus.yenivavi.Model.Movie;
import com.sedadurmus.yenivavi.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Movie> mMovies;

    private FirebaseUser mevcutFirebaseUser;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Movie movie =mMovies.get(position);
        mevcutFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        new DownLoadImageTask(holder.filmGorsel).execute( "https://image.tmdb.org/t/p/w500/" +  mMovies.get(position).getPosterPath());
//        holder.favori.setTag("ekle");
        favoriEklendi(movie, holder.favori);
        holder.favori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("FAVORİ",holder.favori.getTag().toString() );
                if (holder.favori.getTag().equals("ekle")) {

                    favoriEkle(movie);
                } else if (holder.favori.getTag().equals("eklendi")){
                    FirebaseDatabase.getInstance().getReference("Favoriler").child(mevcutFirebaseUser.getUid())
                            .child(movie.getId()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(mContext, "favoriden çıkarıldı.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView filmAdi, filmBegeni, filmHakkinda, txtTarih;
        ImageView filmGorsel, favori;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filmGorsel= itemView.findViewById(R.id.film_img);
            favori= itemView.findViewById(R.id.favoriEkle);

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


    //favori ekleme için yaptıklarım

    private void favoriEklendi(final Movie movie,  final ImageView imageView) {
        final FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference begeniVeriTabaniYolu = FirebaseDatabase.getInstance().getReference()
                .child("Favoriler");
        begeniVeriTabaniYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                assert mevcutKullanici != null;
                if (dataSnapshot.child(mevcutKullanici.getUid()).child(movie.getId()).exists()) { //firebaste bu film favorilerde var mı
                    imageView.setImageResource(R.drawable.ic_check_circle);
                    imageView.setTag("eklendi");
                    Log.e("favori", "eklendiye girdi");

                } else {
                    imageView.setImageResource(R.drawable.ic_add_circle);
                    imageView.setTag("ekle");

//                    favoriEkle(movie);
                    Log.e("favori", "ekleye girdi");

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void favoriEkle (final Movie movie){
        final FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        assert mevcutKullanici != null;
        DatabaseReference favoriFire = FirebaseDatabase.getInstance().getReference("Favoriler")
                .child(mevcutKullanici.getUid());

        String id = movie.getId();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", movie.getTitle());
        hashMap.put("img_url", movie.getPosterPath());
        favoriFire.child(id).setValue(hashMap);

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
