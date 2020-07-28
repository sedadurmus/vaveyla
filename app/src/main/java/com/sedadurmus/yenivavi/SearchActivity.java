package com.sedadurmus.yenivavi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sedadurmus.yenivavi.Adapter.SearchAdapter;

public class SearchActivity extends AppCompatActivity {

    EditText Ara;

    ViewPager viewPager;
    TabLayout tabLayout;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Toolbar toolbar =findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Ara= findViewById(R.id.edit_kullanici_arama_bar);

        viewPager =findViewById(R.id.viewPager);
        tabLayout =findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Kişi"));
        tabLayout.addTab(tabLayout.newTab().setText("Film"));
        tabLayout.addTab(tabLayout.newTab().setText("Kitap"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final SearchAdapter adapter = new SearchAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        Ara.setHint("Kullanıcı Ara");

                        return ;
                    case 1:
                        Ara.setHint("Film Ara");

                        return ;
                    case 2:
                        Ara.setHint("Kitap Ara");
                        return ;
                    default:
                        return ;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


}