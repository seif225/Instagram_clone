package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.instagramclone.Fragments.HomeFragment;
import com.example.instagramclone.Fragments.NotificationFragment;
import com.example.instagramclone.Fragments.ProfileFragment;
import com.example.instagramclone.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeMain extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        initializeFields();

        Bundle intent = getIntent().getExtras();

        if (intent != null) {
            String publisher = intent.getString("publisherid");

            SharedPreferences.Editor editor =
                    getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileid", publisher);
            editor.apply();


            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_container, new ProfileFragment()).commit();


        } else getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, new HomeFragment()).commit();

        findViewById(R.id.signOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                sendUserToMain();
            }
        });
    }

    private void initializeFields() {

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

    }

    private void sendUserToMain() {

        startActivity(new Intent(HomeMain.this, LoginActivity.class));
        finish();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;

                case R.id.nav_search:
                    selectedFragment = new SearchFragment();

                    break;

                case R.id.nav_add:
                    selectedFragment = null;
                    startActivity(new Intent(HomeMain.this, PostActivity.class));

                    break;

                case R.id.nav_fav:
                    selectedFragment = new NotificationFragment();
                    break;

                case R.id.nav_profile:
                    SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    editor.apply();
                    selectedFragment = new ProfileFragment();

                    break;


            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }


            return true;
        }
    };

}
