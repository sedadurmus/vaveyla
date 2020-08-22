package com.sedadurmus.yenivavi.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Model.Book;
import com.sedadurmus.yenivavi.Model.DownLoadImageTask;
import com.sedadurmus.yenivavi.Model.Movie;
import com.sedadurmus.yenivavi.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KitapAdapter extends RecyclerView.Adapter<KitapAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Book> mBooks;

    private FirebaseUser mevcutFirebaseUser;

    public KitapAdapter(Context mContext) {
        this.mContext = mContext;
        this.mBooks = new ArrayList<Book>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Book book =mBooks.get(position);
        new DownLoadImageTask(holder.bookImg).execute(mBooks.get(position).getImg_url());
        holder.bookTitle.setText(mBooks.get(position).getName());

//        favoriEklendi(book, holder.favori);

//        holder.favori.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mBooks.clear();
//                Log.e("FAVORİ",holder.favori.getTag().toString() );
//                if (holder.favori.getTag().equals("ekle")) {
//
//                    favoriEkle(book);
//                } else if (holder.favori.getTag().equals("eklendi")){
//                    FirebaseDatabase.getInstance().getReference("Favoriler").child(mevcutFirebaseUser.getUid())
//                            .child(book.getId()).removeValue();
//
//
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView bookTitle;
        ImageView bookImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImg = itemView.findViewById(R.id.book_resim);
            bookTitle = itemView.findViewById(R.id.book_title);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int pos = getAdapterPosition();
//                    if (pos != RecyclerView.NO_POSITION){
//                        Intent intent = new Intent(mContext, DetailActivity.class);
//                        intent.putExtra("title", mMovies.get(pos).getTitle());
//                        intent.putExtra("poster_path", mMovies.get(pos).getPosterPath());
//                        intent.putExtra("overview", mMovies.get(pos).getOverview());
//                        intent.putExtra("release_date", mMovies.get(pos).getRelease_date());
//                        intent.putExtra("vote_average", Double.toString(mMovies.get(pos).getVote_average()));
//                        intent.putExtra("vote_count", Integer.toString(mMovies.get(pos).getVote_count()));
//                        intent.putExtra("video",(mMovies.get(pos).getVideo()));
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mContext.startActivity(intent);
//                    }
//                }
//            });


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
                if (dataSnapshot.child(mevcutKullanici.getUid()).child(movie.getId()).exists()) {
                    imageView.setImageResource(R.drawable.ic_check_circle);
                    imageView.setTag("eklendi");

                } else {
                    imageView.setImageResource(R.drawable.ic_add_circle);
                    imageView.setTag("ekle");
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
        hashMap.put("title", movie.getTitle());
        hashMap.put("poster_path", movie.getPosterPath());
        favoriFire.child(id).setValue(hashMap);

    }



    public void addAll(List<Book> videoModelList) {
        Log.e("MOVIE","ADDALL");

        for (Book videoModel : videoModelList) {

                add(videoModel);
                //Log.e("NEWS",videoModel.Title);
        }

    }

    public void add(Book videoModel) {
        mBooks.add(videoModel);
        //notifyItemInserted(Items.size() - 1);
        notifyDataSetChanged();
    }
}
