package com.sedadurmus.yenivavi.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Adapter.MissionAdapter;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.Model.yapilabilirGorev;
import com.sedadurmus.yenivavi.R;
import com.sedadurmus.yenivavi.ReklamActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GorevFragment extends Fragment implements RewardedVideoAdListener {

    private FirebaseUser mevcutKullanici;
    private RewardedVideoAd rewardedVideoAd;
    TextView textView;
    Button button;

    private RecyclerView profileListView;
    private List<yapilabilirGorev> missionList = new ArrayList<yapilabilirGorev>();
    Context context;
    private FloatingActionButton fabAds;
    ImageView geri;
    //    TextView gorev_paylasim;
    private MissionAdapter missionAdapter;
    int clickcount=0;

    private SwipeRefreshLayout refreshLayout;

    public static void showDialog(yapilabilirGorev gorev) {
        seciliGorev = gorev;
        if (seciliGorev == null) return;
//        dialog.setMessage(seciliGorev.getGorevBasligi() + " isimli göreve başlamak istiyor musunuz?");
//        dialog.show();
    }
    @SuppressLint("StaticFieldLeak")
    private static Context GetContext;
    public static yapilabilirGorev seciliGorev;
    private static AlertDialog.Builder dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_gorev, container, false);
        fabAds = view.findViewById(R.id.fabAds);
        LoadMissions();
        missionAdapter = new MissionAdapter(getContext(), missionList);

        GetContext = getContext();
        profileListView = view.findViewById(R.id.gorev_list);
        profileListView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        profileListView.setLayoutManager(linearLayoutManager);
        profileListView.setAdapter(missionAdapter);
        refreshLayout = view.findViewById(R.id.refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                missionList.clear();
                LoadMissionsRefresh();
            }
        });

        fabAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), ReklamActivity.class));
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater factory = LayoutInflater.from(v.getContext());
                final View view = factory.inflate(R.layout.alert_dialog, null);
                builder.setView(view);
                builder.setCancelable(false);
                ((TextView)view.findViewById(R.id.textTitle)).setText(R.string.alertReklamTitle);
                ((TextView)view.findViewById(R.id.textMessage)).setText(R.string.alertMessageReklam);
                ((Button)view.findViewById(R.id.buttonNo)).setText(R.string.alertButtonNo);
                ((Button)view.findViewById(R.id.buttonYes)).setText(R.string.alertButtonYes);
                ((ImageView)view.findViewById(R.id.imageicon)).setImageResource(R.drawable.ic_info);
                final AlertDialog alertDialog1 = builder.create();
                view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        evet dediği zaman reklam aktivitiye göndermek yerine direkt açılsın olmaz mı?
                        startActivity(new Intent(getContext(), ReklamActivity.class));
//                        if(rewardedVideoAd.isLoaded()){
//                            rewardedVideoAd.show();
//                        }
                    }
                });
                view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "Reklam izlersen ikimizde kazanacağız :)", Toast.LENGTH_SHORT).show();
                        alertDialog1.cancel();
                    }
                });
                if (alertDialog1.getWindow() != null){
                    alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog1.show();
            }
        });

        MobileAds.initialize(getContext(),
                "ca-app-pub-5244554971825167~3413590380" );
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        rewardedVideoAd.setRewardedVideoAdListener(this);
        loadAds();
        return view;
    }

    void LoadMissions() {
        final DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Gorevler");
        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                missionList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    yapilabilirGorev gorev = new yapilabilirGorev();
                    gorev.setGorevBaslangic(((String) snapshot.child("gorevBaslangic").getValue()));
                    gorev.setGorevBasligi(((String) snapshot.child("gorevBasligi").getValue()));
                    gorev.setGorevBitis(((String) snapshot.child("gorevBitis").getValue()));
                    gorev.setGorevDurumu(((boolean) snapshot.child("gorevDurumu").getValue()));
                    gorev.setGorevHakkinda(((String) snapshot.child("gorevHakkinda").getValue()));
                    gorev.setGorevId(((String) snapshot.child("gorevId").getValue()));
                    gorev.setGorevPuani(((Long) snapshot.child("gorevPuani").getValue()));
                    gorev.setGorevResmi(((String) snapshot.child("gorevResmi").getValue()));
                    if (gorev.compareDate() && gorev.isGorevDurumu()) {
                        missionList.add(gorev);
                    }
                }
                missionAdapter = new MissionAdapter(getContext(), missionList);
                profileListView.setAdapter(missionAdapter);
                missionAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    //REFRESHvadfghhsfh
    private void LoadMissionsRefresh() {
        final DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Gorevler");
        refreshLayout.setRefreshing(false);
        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                missionList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    yapilabilirGorev gorev = new yapilabilirGorev();
                    gorev.setGorevBaslangic(((String) snapshot.child("gorevBaslangic").getValue()));
                    gorev.setGorevBasligi(((String) snapshot.child("gorevBasligi").getValue()));
                    gorev.setGorevBitis(((String) snapshot.child("gorevBitis").getValue()));
                    gorev.setGorevDurumu(((boolean) snapshot.child("gorevDurumu").getValue()));
                    gorev.setGorevHakkinda(((String) snapshot.child("gorevHakkinda").getValue()));
                    gorev.setGorevId(((String) snapshot.child("gorevId").getValue()));
                    gorev.setGorevPuani(((Long) snapshot.child("gorevPuani").getValue()));
                    gorev.setGorevResmi(((String) snapshot.child("gorevResmi").getValue()));
                    if (gorev.compareDate() && gorev.isGorevDurumu()) {
                        missionList.add(gorev);
                    }
                }
                missionAdapter = new MissionAdapter(getContext(), missionList);
                profileListView.setAdapter(missionAdapter);
                missionAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void loadAds() {
        rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }
    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadAds();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onRewarded(RewardItem rewardItem) {
        profilpuanartis();

    }
    private boolean isOkey = true;
    private void profilpuanartis() {
        isOkey=true;
        final DatabaseReference adsYol = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(mevcutKullanici.getUid());
        adsYol.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!isOkey) return;
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                HashMap<String, Object> kullaniciGuncelleHashMap = new HashMap<>();
                assert kullanici != null;
                kullaniciGuncelleHashMap.put("profilPuan", kullanici.getProfilPuan() + 5);
                adsYol.updateChildren(kullaniciGuncelleHashMap);
                startActivity(new Intent(getContext(), ProfilPaylasimFragment.class));
                isOkey=false;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
    @Override
    public void onResume() {
        rewardedVideoAd.resume(getContext());
        super.onResume();
    }

    @Override
    public void onPause() {
        rewardedVideoAd.pause(getContext());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        rewardedVideoAd.destroy(getContext());
        super.onDestroy();
    }


}
