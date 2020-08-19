package com.sedadurmus.yenivavi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.sedadurmus.yenivavi.Model.Book;
import com.sedadurmus.yenivavi.Model.DownLoadImageTask;
import com.sedadurmus.yenivavi.R;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private Context mContext;
    private List<Book> bookList;
    private FirebaseUser mevcutFirebaseUser;

    public BookAdapter(Context mContext, List<Book> bookList) {
        this.mContext = mContext;
        this.bookList = bookList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);


        return new BookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        new DownLoadImageTask(holder.bookImg).execute(bookList.get(position).getImg_url());
        holder.bookTitle.setText(bookList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
    private void setCatItemRecycler(RecyclerView recyclerView, ArrayList<Book> bookList){
        BookAdapter bookAdapter = new BookAdapter(recyclerView.getContext(), bookList);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(bookAdapter);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitle;
        ImageView bookImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookImg = itemView.findViewById(R.id.book_resim);
            bookTitle = itemView.findViewById(R.id.book_title);
        }
    }
}
