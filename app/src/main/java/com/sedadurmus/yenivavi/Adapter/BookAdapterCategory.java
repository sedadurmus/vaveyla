package com.sedadurmus.yenivavi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sedadurmus.yenivavi.Model.BookSearch;
import com.sedadurmus.yenivavi.R;
import com.sedadurmus.yenivavi.SearchBookDetailActivity;

import java.util.List;

public class BookAdapterCategory extends RecyclerView.Adapter<BookAdapterCategory.ViewHolder> {

    private List<BookSearch> items;
    private BookAdapterCategory.onSelectData onSelectData;
    private Context mContext;
    private double Rating;
    private RequestOptions options;


    public interface onSelectData {
        void onSelected(BookSearch ModelBook);
    }

    public BookAdapterCategory(Context context, List<BookSearch> items, BookAdapterCategory.onSelectData xSelectData) {
        this.mContext = context;
        this.items = items;
        this.onSelectData = xSelectData;

        options = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

//    @Override
//    public MovieAdapterCategory.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_film, parent, false);
//        return new MovieAdapterCategory(view);
//    }


    @NonNull
    @Override
    public BookAdapterCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_book, parent, false);

        return new BookAdapterCategory.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookAdapterCategory.ViewHolder holder, int position) {
        final BookSearch data = items.get(position);

        holder.tvTitle.setText(data.getTitle());
        holder.tvRealeseDate.setText(data.getAuthors());
//        holder.tvPrice.setText(book.getPrice());
        holder.tvDesc.setText(data.getDescription());

        //load image from internet and set it into imageView using Glide
        Glide.with(mContext).load(data.getThumbnail()).apply(options).into(holder.imgPhoto);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //Class Holder
    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cvFilm;
        public ImageView imgPhoto;
        public TextView tvTitle;
        public TextView tvRealeseDate;
        public TextView tvDesc;

        RelativeLayout container;


        public ViewHolder(View itemView) {
            super(itemView);
            cvFilm = itemView.findViewById(R.id.cvFilm);
            imgPhoto = itemView.findViewById(R.id.thumbnail);
            tvTitle = itemView.findViewById(R.id.title);
            tvRealeseDate = itemView.findViewById(R.id.author);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            container = itemView.findViewById(R.id.container);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext , SearchBookDetailActivity.class);
                    int pos = getAdapterPosition();
                    i.putExtra("book_title" ,items.get(pos).getTitle());
                    i.putExtra("book_author" ,items.get(pos).getAuthors());
                    i.putExtra("book_desc" ,items.get(pos).getDescription());
                    i.putExtra("book_categories" ,items.get(pos).getCategories());
                    i.putExtra("book_publish_date" ,items.get(pos).getPublishedDate());
                    i.putExtra("book_info" ,items.get(pos).getmUrl());
                    i.putExtra("book_buy" ,items.get(pos).getBuy());
                    i.putExtra("book_preview" ,items.get(pos).getPerview());
                    i.putExtra("book_thumbnail" ,items.get(pos).getThumbnail());

                    mContext.startActivity(i);
                }
            });
        }
    }

}