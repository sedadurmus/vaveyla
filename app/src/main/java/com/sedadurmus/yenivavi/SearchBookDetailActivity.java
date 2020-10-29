package com.sedadurmus.yenivavi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class SearchBookDetailActivity extends AppCompatActivity {

    private ImageView post_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book_detail);



        //hide the default actionBar
//        getSupportActionBar().hide();

        //Receive
        Bundle extras = getIntent().getExtras();
        String title =""
                , authors ="", description="" , categories ="", publishDate=""
                ,info ="", buy ="",preview ="" ,thumbnail ="";
        if(extras != null){
            title = extras.getString("book_title");
            authors = extras.getString("book_author");
            description = extras.getString("book_desc");
            categories = extras.getString("book_categories");
            publishDate = extras.getString("book_publish_date");
            info = extras.getString("book_info");
            buy = extras.getString("book_buy");
            preview = extras.getString("book_preview");
            thumbnail = extras.getString("book_thumbnail");
        }

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        TextView tvTitle = findViewById(R.id.aa_book_name);
        TextView tvAuthors = findViewById(R.id.aa_author);
        TextView tvDesc = findViewById(R.id.aa_description);
        TextView tvCatag = findViewById(R.id.aa_categorie);
        TextView tvPublishDate = findViewById(R.id.aa_publish_date);
        TextView tvInfo = findViewById(R.id.aa_info);
        TextView tvPreview = findViewById(R.id.aa_preview);

        post_search =findViewById(R.id.search_book_gonderi);

        ImageView ivThumbnail = findViewById(R.id.aa_thumbnail);

        tvTitle.setText(title);
        tvAuthors.setText(authors);
        tvDesc.setText(description);
        tvCatag.setText(categories);
        tvPublishDate.setText(publishDate);

        final String finalInfo = info;
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(finalInfo));
                startActivity(i);
            }
        });


        final String finalPreview = preview;
        tvPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(finalPreview));
                startActivity(i);

            }
        });

        collapsingToolbarLayout.setTitle(title);

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

        Glide.with(this).load(thumbnail).apply(requestOptions).into(ivThumbnail);


        //        gönderide paylaştırabilmek için
//        final String finalThumbnail = thumbnail;
        final String finalThumbnail = thumbnail;
        post_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Post_Book_SearchActivity.class);
                intent.putExtra("mThumbnail", finalThumbnail);
                intent.putExtra("mTitle",   getIntent().getExtras().getString("book_title"));
                intent.putExtra("mAuthors",   getIntent().getExtras().getString("book_author"));
                startActivity(intent);
            }
        });
    }
}