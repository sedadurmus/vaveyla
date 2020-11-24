package com.sedadurmus.yenivavi.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sedadurmus.yenivavi.Adapter.SectionPagerAdapter;
import com.sedadurmus.yenivavi.R;

public class SearchFragment extends Fragment {

//    EditText arama_bar;
    ImageView arama;
    View myFragment;
    ViewPager viewPager;
    TabLayout tabLayout;
    SearchView searchView ;
    TextView kesfetTxt;
    Context context;

//    APİ - tmdb den aldığım apiiiiiiiii keeey
//     b7ee738bdfe5a91a0cec31c619d58968

//    örnek api şeysi - api_key= buraya kendi key'ini yazman lazııııııım
//    https://api.themoviedb.org/3/movie/550?api_key=b7ee738bdfe5a91a0cec31c619d58968

//    ------------------------------------------

//    https://image.tmdb.org/t/p/

//    https://api.themoviedb.org/3/movie/popular?api_key=b7ee738bdfe5a91a0cec31c619d58968
//    https://api.themoviedb.org/3/movie/157336?api_key=b7ee738bdfe5a91a0cec31c619d58968&append_to_response=videos,images
//    https://api.themoviedb.org/3/movie/550/images?api_key=b7ee738bdfe5a91a0cec31c619d58968&language=en-US&include_image_language=en,null

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_moviebook, container, false);
        viewPager = myFragment.findViewById(R.id.viewPager);
        tabLayout = myFragment.findViewById(R.id.tabLayout);
        arama=myFragment.findViewById(R.id.aramaya_git);
        searchView = myFragment.findViewById(R.id.searchView);
        kesfetTxt= myFragment.findViewById(R.id.kesfet);

        arama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AramaBarFragment()).commit();

            }
        });

        return myFragment;
    }
    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentMovie(),  "Film");
        adapter.addFragment(new KitapFragment(), "Kitap");
        viewPager.setAdapter(adapter);
    }


}
