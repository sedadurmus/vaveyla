package com.sedadurmus.yenivavi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sedadurmus.yenivavi.DetailActivity;
import com.sedadurmus.yenivavi.Model.BookSearch;
import com.sedadurmus.yenivavi.R;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_film, parent, false);

        return new BookAdapterCategory.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookAdapterCategory.ViewHolder holder, int position) {
        final BookSearch data = items.get(position);

//        Rating = data.getVote_average();
        holder.tvTitle.setText(data.getTitle());
        holder.tvRealeseDate.setText(data.getPublishedDate());
        holder.tvDesc.setText(data.getDescription());
//        holder.tvCategory.setText(data.getCategories());

//        float newValue = (float)Rating;
//        holder.ratingBar.setNumStars(5);
//        holder.ratingBar.setStepSize((float) 0.5);
//        holder.ratingBar.setRating(newValue / 2);

//        Glide.with(mContext)
//                .load(ApiEndpoint.URLIMAGE + data.getmThumbnail())
//                .apply(new RequestOptions()
//                        .placeholder(R.drawable.ic_image)
//                        .transform(new RoundedCorners(16)))
//                .into(holder.imgPhoto);
//
//        holder.cvFilm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSelectData.onSelected(data);
//            }
//        });
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
        public RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            cvFilm = itemView.findViewById(R.id.cvFilm);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRealeseDate = itemView.findViewById(R.id.tvRealeseDate);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            ratingBar = itemView.findViewById(R.id.ratingBar);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("mTitle", items.get(pos).getTitle());
                        intent.putExtra("mThumbnail", items.get(pos).getThumbnail());
                        intent.putExtra("mDescription", items.get(pos).getDescription());
                        intent.putExtra("mPublishedDate", items.get(pos).getPublishedDate());
//                        intent.putExtra("vote_average", Double.toString(items.get(pos).getVote_average()));
//                        intent.putExtra("vote_count", Integer.toString(items.get(pos).getVote_count()));
//                        intent.putExtra("video",(items.get(pos).getVideo()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });

        }
    }

}