package com.sedadurmus.yenivavi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sedadurmus.yenivavi.Adapter.BildirimAdapter;
import com.sedadurmus.yenivavi.Fragments.DashboardFragment;
import com.sedadurmus.yenivavi.Fragments.HaberFragment;
import com.sedadurmus.yenivavi.Fragments.HomeFragment;
import com.sedadurmus.yenivavi.Fragments.ProfilPaylasimFragment;
import com.sedadurmus.yenivavi.Fragments.SearchFragment;
import com.sedadurmus.yenivavi.Model.Bildiren;

import java.util.List;

public class MainActivity extends AppCompatActivity {

//    ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    private BildirimAdapter bildirimAdapter;
    private List<Bildiren> bildirimListesi;
    public static FirebaseUser mevcutKullanici;
    public static String searchWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        uygulamada reklam için ekledik
        MobileAds.initialize(this);

        bottomNavigationView = findViewById(R.id.botom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

//        final BadgeDrawable badge_drawable = binding.botomNavigation.getOrCreateBadge(R.id.notification);
//        badge_drawable.setBackgroundColor(Color.BLUE);
//        badge_drawable.setBadgeTextColor(Color.YELLOW);
//        badge_drawable.setMaxCharacterCount(5);
//        badge_drawable.setVisible(true);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String gonderen = intent.getString("gonderenId");
            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileid", gonderen);
            editor.apply();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new HomeFragment());
            transaction.commit();
        }

////        bildirim sayısını bakmadığımız bildirim oalrak göstermeli
//        FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference bildirimYolu = FirebaseDatabase.getInstance().getReference("Bildirimler").child(mevcutKullanici.getUid());
//        bildirimYolu.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Bildiren bildirim = snapshot.getValue(Bildiren.class);
////   Bir kez göstermeli bildirimi yeni bildirim gelince tekrar göstermeli.
//                    badge_drawable.setNumber(Integer.parseInt(dataSnapshot.getChildrenCount() + ""));
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.profile:
                    SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                    editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    editor.apply();
                    selectedFragment = new ProfilPaylasimFragment();
                    break;
                case R.id.search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.dashbord:
                    selectedFragment = new DashboardFragment();
                    break;
                case R.id.notification:
//                    BadgeDrawable badge_notification = binding.botomNavigation.getBadge(R.id.notification);
//                    badge_notification.clearNumber();
//                    badge_notification.setVisible(false);
                    selectedFragment = new HaberFragment();
                    break;
            }
            if (selectedFragment!= null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        }
    };

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        binding=null;
//    }
}
