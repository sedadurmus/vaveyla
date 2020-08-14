package com.sedadurmus.yenivavi.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sedadurmus.yenivavi.Fragments.DukkanFragment;
import com.sedadurmus.yenivavi.Fragments.GorevFragment;
import com.sedadurmus.yenivavi.Fragments.MovieSearchFragment;

public class SearchAdapter extends FragmentPagerAdapter {
    public SearchAdapter(@NonNull Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    private Context myContext;
    int totalTabs;

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MovieSearchFragment movieSearchFragment = new MovieSearchFragment();


                return movieSearchFragment;
            case 1:
                DukkanFragment dukkanFragment = new DukkanFragment();
                return dukkanFragment;
            case 2:
                GorevFragment gorevFragment = new GorevFragment();
                return gorevFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}