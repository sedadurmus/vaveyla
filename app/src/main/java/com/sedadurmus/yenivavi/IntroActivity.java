package com.sedadurmus.yenivavi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sedadurmus.yenivavi.Adapter.IntroViewPagerAdapter;
import com.sedadurmus.yenivavi.Model.ScreenItem;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    public TabLayout tabLayout;
    int position = 0;
    Button btnGetStarted;
    Animation btnAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        when this activity  is about to be launch we need to check  if its opened before or not
        if(restorePrefData())
        {
            Intent mainActivity = new Intent(getApplicationContext(),RegisterActivity.class);
            startActivity(mainActivity);
            finish();
        }
        setContentView(R.layout.activity_intro);
//
////        HİDE TO ACTİON BAR
//        getSupportActionBar().hide();
//        ini views
        btnGetStarted = findViewById(R.id.btn_getstarted);
        tabLayout = findViewById(R.id.tabindicator);
        btnAnim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);

//        fill list screen
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Hoş geldin!", "Aramıza katıldığın için çok mutluyuz! Aramıza katılmanın şerefine 20 puanın olduğunu hatırlatmak istedik. Hadi şimdi Vaveyla hakkında birkaç bilgi edin!", R.drawable.hg_grup));
        mList.add(new ScreenItem("Görev", "Görevlere katılarak ve paylaşımlarda bulunarak puan toplayabilirsin. Merak etme görevler çok eğlenceli!", R.drawable.gorev_grup));
        mList.add(new ScreenItem("Dükkan", "Topladığın puanlar ile Vaveyla da çeşitli etkinlikler için oluşturduğumuz dükkandan gönlünce alışveriş yapabilirsin!", R.drawable.dukkan_grup));
//        mList.add(new ScreenItem("Bağış Yap", "Puanlarınla bağış yapabileceğini söylemiş miydik?", R.drawable.vaveyla_rock));
        mList.add(new ScreenItem("Akış", "Görevlere katıl, puan topla, puanlarınla harca ama sakın paylaşımlardan puan aldığını unutma ve \n akış için paylaş!", R.drawable.akis_grup));

//        setup viewpager
        screenPager = findViewById(R.id.screen_view_pager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);
//        setup tablaout
        tabLayout.setupWithViewPager(screenPager);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size()-1)
                {
                    loadLastScreen();
                }
                else
                {
                    btnGetStarted.setVisibility(View.INVISIBLE);
//                    btnGec.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);

                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        get started button click listener
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                kayıt ol ekranına gidiyooooooooooorrrrrrrr
                Intent mainActivity =new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(mainActivity);
//               also we need to save a boolean value to storage so next time when the user run the app
//                we could know that he is already checked the intro screen activity
//                i'm going to use shared preferences to that process
                savePrefsdata();
                finish();

            }
        });

    }

    private boolean restorePrefData() {
        SharedPreferences  pref =getApplicationContext().getSharedPreferences("myPrefs",    MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore =pref.getBoolean("isIntroOpened",false);
        return isIntroActivityOpenedBefore;

    }

    private void savePrefsdata() {

        SharedPreferences  pref =getApplicationContext().getSharedPreferences("myPrefs",    MODE_PRIVATE);
        SharedPreferences.Editor editor =pref.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.commit();

    }

    private void loadLastScreen() {

//        btnGec.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);

//        TODO: ADD an animation the getstarted button
//        setup animation
        btnGetStarted.setAnimation(btnAnim);

    }

}