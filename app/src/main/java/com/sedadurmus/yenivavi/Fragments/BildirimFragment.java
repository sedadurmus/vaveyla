package com.sedadurmus.yenivavi.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Adapter.BildirimAdapter;
import com.sedadurmus.yenivavi.Adapter.SectionPagerAdapter;
import com.sedadurmus.yenivavi.Model.Bildiren;
import com.sedadurmus.yenivavi.Model.Chat;
import com.sedadurmus.yenivavi.R;

import java.util.List;

public class BildirimFragment extends Fragment {

    private RecyclerView recyclerView;
    private BildirimAdapter bildirimAdapter;
    private List<Bildiren> bildirimListesi;
    private SwipeRefreshLayout refreshLayout;
    View myFragment;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view = inflater.inflate(R.layout.fragment_bildirim, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());
                adapter.addFragment(new HaberFragment(), "Bildirimler");

                int unread= 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getRecevier().equals(firebaseUser.getUid())  && !chat.isIsseen()){
                        unread++;
                    }
                }
                if (unread == 0){
                    adapter.addFragment(new ChatFragment(), "Mesajlar");
                }else {
                    adapter.addFragment(new ChatFragment(), "("+unread+") Mesajlar");
                }
                viewPager.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return view;
    }

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
    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());
//        adapter.addFragment(new HaberFragment(), "Bildirimler");
//        adapter.addFragment(new ChatFragment(), "Mesajlar");
        viewPager.setAdapter(adapter);
    }

//    // bunları bildirim olan sayfaya ekleeeeeeeeeeeeeeeeeeeeee
//    private void bildirimleriOku() {
//
//        FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference bildirimYolu = FirebaseDatabase.getInstance().getReference("Bildirimler").child(mevcutKullanici.getUid());
//
//        bildirimYolu.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                bildirimListesi.clear();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Bildiren bildirim = snapshot.getValue(Bildiren.class);
//                    bildirim.setTarih(snapshot.child("bildirimTarihi").getValue().toString());
//                    bildirimListesi.add(bildirim);
//
//                }
//
//                Collections.reverse(bildirimListesi);
//                bildirimAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    //    Refresh
//    private void bildirimleriOkuRefresh() {
//
//        FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference bildirimYolu = FirebaseDatabase.getInstance().getReference("Bildirimler").child(mevcutKullanici.getUid());
//
//        refreshLayout.setRefreshing(false);
//
//        bildirimYolu.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                bildirimListesi.clear();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren())
//                {
//                    Bildiren bildirim = snapshot.getValue(Bildiren.class);
//                    bildirim.setTarih(snapshot.child("bildirimTarihi").getValue().toString());
//                    bildirimListesi.add(bildirim);
//
//
//                }
//
//                Collections.reverse(bildirimListesi);
//                refreshLayout.setRefreshing(false);
//                bildirimAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}
