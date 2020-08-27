package com.sedadurmus.yenivavi.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Book book =mBooks.get(position);
        new DownLoadImageTask(holder.bookImg).execute(mBooks.get(position).getImg_url());
//        holder.bookTitle.setText(mBooks.get(position).getName());
//        holder.bookAuthor.setText(mBooks.get(position).getAuthor());


        kitapFavoriEklendi(book, holder.favori);

        holder.favori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBooks.clear();
                Log.e("FAVORİ",holder.favori.getTag().toString() );
                if (holder.favori.getTag().equals("ekle")) {

                    kitapFavoriEkle(book);
                } else if (holder.favori.getTag().equals("eklendi")){
                    FirebaseDatabase.getInstance().getReference("Favoriler")
                            .child("KitapFavorisi")
                            .child(mevcutFirebaseUser.getUid())
                            .child(mBooks.toString()).removeValue();


                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


//        TextView bookTitle, bookAuthor;
        ImageView bookImg, favori;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImg = itemView.findViewById(R.id.book_resim);
            favori= itemView.findViewById(R.id.kitapfavoriEkle);
//            bookTitle = itemView.findViewById(R.id.book_title);
//            bookAuthor = itemView.findViewById(R.id.book_author);


        }
    }


    //favori ekleme için yaptıklarım

    private void kitapFavoriEklendi(final Book book,  final ImageView imageView) {
        final FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference begeniVeriTabaniYolu = FirebaseDatabase.getInstance().getReference()
                .child("Favoriler").child("KitapFavorisi");
        begeniVeriTabaniYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                assert mevcutKullanici != null;
                if (dataSnapshot.child(mevcutKullanici.getUid()).child(book.getName()).exists()) {
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

    public void kitapFavoriEkle (final Book book){
        final FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        assert mevcutKullanici != null;
        DatabaseReference favoriFire = FirebaseDatabase.getInstance().getReference("Favoriler")
                .child("KitapFavorisi")
                .child(mevcutKullanici.getUid());

        String id = book.getName();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("title", book.getName());
        hashMap.put("author", book.getAuthor());
        hashMap.put("poster_path", book.getImg_url());
        favoriFire.child(id).setValue(hashMap);

    }



    public void addAll(List<Book> videoModelList) {
        Log.e("BOOK","ADDALL");

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
