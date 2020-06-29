package com.sedadurmus.yenivavi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Model.Kullanici;
import java.util.HashMap;
import static com.sedadurmus.yenivavi.LoginActivity.kullanici;

public class ReklamActivity extends AppCompatActivity implements RewardedVideoAdListener {

    String profilId;
    FirebaseUser mevcutKullanici;
    RewardedVideoAd rewardedVideoAd;
    TextView textView;
    Button button;
    int value=0;
    int clickcount=0;

    CardView cardView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewarded);

        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        button = findViewById(R.id.videoButton);
//        cardView = findViewById(R.id.card_view);
////        cardView.setBackgroundResource(R.drawable.vaveyla_yazi);
        textView = findViewById(R.id.txt_puan_ads);
        textView.setText("Şuanki puanın: " + kullanici.getProfilPuan());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rewardedVideoAd.isLoaded()){
                    rewardedVideoAd.show();
                }
            }
        });

        MobileAds.initialize(this,
                "ca-app-pub-5244554971825167~3413590380" );
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);
        loadAds();
    }
//    ca-app-pub-5244554971825167~3413590380

    private void loadAds() {
        rewardedVideoAd.loadAd("ca-app-pub-5244554971825167/8826533318", new AdRequest.Builder().build());
    }

//ca-app-pub-5244554971825167/8826533318
//ca-app-pub-3940256099942544/5224354917

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

    boolean isOkey = true;
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
    protected void onResume() {
        rewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        rewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        rewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
//
// ca-app-pub-5244554971825167/8826533318
