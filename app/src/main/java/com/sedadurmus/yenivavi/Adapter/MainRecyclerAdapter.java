package com.sedadurmus.yenivavi.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sedadurmus.yenivavi.Model.AllBookCategory;
import com.sedadurmus.yenivavi.Model.Book;
import com.sedadurmus.yenivavi.R;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {

    private Context context;
    private List<AllBookCategory> allBookCategories;

    public MainRecyclerAdapter(Context context, List<AllBookCategory> allBookCategories) {
        this.context = context;
        this.allBookCategories = allBookCategories;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_row_item, parent, false);
        return new MainRecyclerAdapter.MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
         holder.categoryTitle.setText(allBookCategories.get(position).getName());

        BookAdapter bookAdapter = new BookAdapter(holder.itemRecycler.getContext(), allBookCategories.get(position).getBooks());
        holder.itemRecycler.setLayoutManager(new LinearLayoutManager(holder.itemRecycler.getContext(), RecyclerView.HORIZONTAL, false));
        holder.itemRecycler.setAdapter(bookAdapter);
//         holder.books = allBookCategories.get(position).getBooks();
      //  setCatItemRecycler(holder.itemRecycler, allBookCategories.get(position).getBooks());
    }

    @Override
    public int getItemCount() {
        return allBookCategories.toArray().length;
    }

    public static final class MainViewHolder extends RecyclerView.ViewHolder{

        TextView categoryTitle;
        RecyclerView itemRecycler;

//        List<Book> books;
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryTitle =itemView.findViewById(R.id.cat_title);
//            books = itemView.findViewById(R.id.book_recycler);

            itemRecycler = itemView.findViewById(R.id.book_recycler);
        }
    }

    private void setCatItemRecycler(RecyclerView recyclerView, ArrayList<Book> bookList){
        BookAdapter bookAdapter = new BookAdapter(recyclerView.getContext(), bookList);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(bookAdapter);
    }




    public void addAll(List<AllBookCategory> videoModelList) {
        Log.e("BookCategory","ADDALL");

        for (AllBookCategory videoModel : videoModelList) {

            add(videoModel);
            //Log.e("NEWS",videoModel.Title);
        }

    }

    public void add(AllBookCategory videoModel) {
        allBookCategories.add(videoModel);
        //notifyItemInserted(Items.size() - 1);
        notifyDataSetChanged();
    }
}
