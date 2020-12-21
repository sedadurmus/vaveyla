package com.sedadurmus.yenivavi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.sedadurmus.yenivavi.BookDetailActivity;
import com.sedadurmus.yenivavi.Model.Book;
import com.sedadurmus.yenivavi.Model.DownLoadImageTask;
import com.sedadurmus.yenivavi.R;

import java.util.List;


public class BookHorizontalAdapter extends RecyclerView.Adapter<com.sedadurmus.yenivavi.Adapter.BookHorizontalAdapter.ViewHolder> {

    private List<Book> mBooks;
    private com.sedadurmus.yenivavi.Adapter.BookHorizontalAdapter.onSelectData onSelectData;
    private Context mContext;

    public interface onSelectData {
        void onSelected(Book modelBook);
    }

    public BookHorizontalAdapter(Context context, List<Book> mBooks, com.sedadurmus.yenivavi.Adapter.BookHorizontalAdapter.onSelectData xSelectData) {
        this.mContext = context;
        this.mBooks = mBooks;
        this.onSelectData = xSelectData;
    }

    @Override
    public BookHorizontalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_book_horizontal, parent, false);
        return new BookHorizontalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookHorizontalAdapter.ViewHolder holder, int position) {
        final Book data = mBooks.get(position);

        new DownLoadImageTask(holder.imgPhoto).execute(mBooks.get(position).getImg_url());
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    //Class Holder
    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhotoBook);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(mContext, BookDetailActivity.class);
                        intent.putExtra("name", mBooks.get(pos).getName());
                        intent.putExtra("img_url", mBooks.get(pos).getImg_url());
                        intent.putExtra("author", mBooks.get(pos).getAuthor());
                        intent.putExtra("description", mBooks.get(pos).getDescription());
                        intent.putExtra("translator", mBooks.get(pos).getTranslator());
                        intent.putExtra("printing_office", mBooks.get(pos).getPrinting_office());
                        intent.putExtra("published_year", mBooks.get(pos).getPublished_year());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
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