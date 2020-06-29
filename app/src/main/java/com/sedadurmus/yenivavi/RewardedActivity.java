package com.sedadurmus.yenivavi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Model.Kullanici;

import java.util.HashMap;

public class RewardedActivity extends AppCompatActivity  {

    private RewardedAd gameOverRewardedAd;
    private RewardedAd extraCoinsRewardedAd;
    FirebaseUser mevcutKullanici;
    TextView puan;
    Button videoButton;
//    RewardedVideoAd rewardedAd;
    RewardedAd rewardedAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewarded);

        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        videoButton = findViewById(R.id.videoButton);
        puan = findViewById(R.id.txt_puan_ads);

        rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");


        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedAd.isLoaded()) {
                    final Activity activityContext = RewardedActivity.this;
                    RewardedAdCallback adCallback = new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.
                            Toast.makeText(activityContext, "Video yüklendi", Toast.LENGTH_SHORT).show();

                        }
                        @Override
                        public void onRewardedAdClosed() {
                            // Ad closed.
//                            profilPuanArtis();
                            Toast.makeText(activityContext, "Video kapatıldı", Toast.LENGTH_SHORT).show();
//                            videoButton.setVisibility(View.INVISIBLE);
//                            finish();

                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem reward) {
                            // User earned reward.
//                            profilPuanArtis();
                         
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {
                            // Ad failed to display.
                        }
                    };
                    rewardedAd.show(activityContext, adCallback);
                } else {
                    Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                }
            }
        });

        gameOverRewardedAd = createAndLoadRewardedAd(
                "ca-app-pub-3940256099942544/5224354917");
        extraCoinsRewardedAd = createAndLoadRewardedAd(
                "ca-app-pub-3940256099942544/5224354917");

    }

    public void profilPuanArtis(){
       final DatabaseReference adsYol = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(mevcutKullanici.getUid());
        adsYol.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                HashMap<String, Object> kullaniciGuncelleHashMap = new HashMap<>();
                assert kullanici != null;
                kullaniciGuncelleHashMap.put("profilPuan", kullanici.getProfilPuan() + 15);
                adsYol.updateChildren(kullaniciGuncelleHashMap);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    public void onRewardedAdClosed() {
        this.rewardedAd = createAndLoadRewardedAd();

    }

    public RewardedAd createAndLoadRewardedAd(String adUnitId) {
        RewardedAd rewardedAd = new RewardedAd(this, adUnitId);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

}
