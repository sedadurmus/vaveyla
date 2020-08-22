package com.sedadurmus.yenivavi.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sedadurmus.yenivavi.Adapter.KullaniciAdapter;
import com.sedadurmus.yenivavi.Adapter.SectionPagerAdapter;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.R;
import com.sedadurmus.yenivavi.SearchActivity;

import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private KullaniciAdapter kullaniciAdapter;
    private List<Kullanici> mKullaniciler;


    private SwipeRefreshLayout refreshLayout;
//    EditText arama_bar;
    ImageView arama;
    View myFragment;
    ViewPager viewPager;
    TabLayout tabLayout;


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

        arama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                getContext().startActivity(intent);
            }
        });

//        recyclerView =myFragment.findViewById(R.id.recyler_view_Arama);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        refreshLayout =myFragment.findViewById(R.id.refresh);
//        arama_bar =myFragment.findViewById(R.id.edit_arama_bar);
//        mKullaniciler = new ArrayList<>();
//        kullaniciAdapter =new KullaniciAdapter(getContext(), mKullaniciler);
//        recyclerView.setAdapter(kullaniciAdapter);

//        kullanicileriOku();
//        loadMovies();
//        arama_bar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                kullaniciAra(s.toString().toLowerCase());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mKullaniciler.clear();
//                kullanicileriOkuRefresh();
//            }
//        });

        return myFragment;
    }
    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MovieFragment(), "Film");
        adapter.addFragment(new KitapFragment(), "Kitap");
        viewPager.setAdapter(adapter);
    }

//    private void kullaniciAra (String s)
//    {
//        Query sorgu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").orderByChild("kullaniciadi")
//                .startAt(s)
//                .endAt(s+"\uf8ff");
//
//        sorgu.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mKullaniciler.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren())
//                {
//                    Kullanici kullanici = snapshot.getValue(Kullanici.class);
//                    mKullaniciler.add(kullanici);
//                }
//                kullaniciAdapter.notifyDataSetChanged();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//    }
//
//    private void kullanicileriOku ()
//    {
//        DatabaseReference kullanicilerYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar");
//
//        kullanicilerYolu.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (arama_bar.getText().toString().equals(""))
//                {
//                    mKullaniciler.clear();
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
//                    {
//                        Kullanici kullanici =snapshot.getValue(Kullanici.class);
//                        mKullaniciler.add(kullanici);
//                    }
//                    kullaniciAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

//    private void kullanicileriOkuRefresh ()
//    {
//        DatabaseReference kullanicilerYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar");
//
//        refreshLayout.setRefreshing(false);
//
//        kullanicilerYolu.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (arama_bar.getText().toString().equals(""))
//                {
//                    mKullaniciler.clear();
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
//                    {
//                        Kullanici kullanici =snapshot.getValue(Kullanici.class);
//                        mKullaniciler.add(kullanici);
//                    }
//                    kullaniciAdapter.notifyDataSetChanged();
//                    refreshLayout.setRefreshing(false);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}
