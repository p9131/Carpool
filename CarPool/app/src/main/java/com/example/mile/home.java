package com.example.mile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Switch;

import com.example.mile.Cart.cart;
import com.example.mile.Route.Routes;
import com.example.mile.User.User;
import com.example.mile.history.history;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class home extends AppCompatActivity {
    private void switchfrag(Fragment fragment) {
        FragmentManager ak = getSupportFragmentManager();
        FragmentTransaction ft = ak.beginTransaction();
        ft.replace(R.id.fragmentContainerView3, fragment);
        ft.commit();
    }

    public void start() {
        Intent intent = getIntent();
        if (intent.hasExtra("user")) {
            User user = (User) intent.getSerializableExtra("user");
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            profile fragProfile2 = new profile();
            fragProfile2.setArguments(bundle);
            FragmentManager ak = getSupportFragmentManager();
            FragmentTransaction ft = ak.beginTransaction();
            ft.replace(R.id.fragmentContainerView3, fragProfile2);
            ft.commit();
        }
    }

        @SuppressLint("NonConstantResourceId")
        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            BottomNavigationView bottomNavigationView = findViewById(R.id.navigator);
            bottomNavigationView.setSelectedItemId(R.id.homeitem);
            Routes fragRoutes;
            cart fragCart;
            history fragHistory;
            profile fragProfile;
            if (FirebaseHelp.isNetworkConnected(getApplicationContext())) {
                fragRoutes = new Routes();
                switchfrag(fragRoutes);
                fragCart = new cart();
                fragHistory = new history();
                fragProfile = new profile();

                bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                                                                   @Override
                                                                   public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                                                       if (FirebaseHelp.isNetworkConnected(getApplicationContext())) {
                                                                           if (item.getItemId() == R.id.homeitem) {
                                                                               switchfrag(fragRoutes);
                                                                               overridePendingTransition(R.anim.slidein, R.anim.slideout);
                                                                               return true;
                                                                           } else if (item.getItemId() == R.id.cartitem) {
                                                                               switchfrag(fragCart);
                                                                               overridePendingTransition(R.anim.slidein, R.anim.slideout);
                                                                               return true;
                                                                           } else if (item.getItemId() == R.id.historyitem) {
                                                                               switchfrag(fragHistory);
                                                                               overridePendingTransition(R.anim.slidein, R.anim.slideout);
                                                                               return true;
                                                                           } else if (item.getItemId() == R.id.profileitem) {
                                                                               switchfrag(fragProfile);
                                                                               overridePendingTransition(R.anim.slidein, R.anim.slideout);
                                                                               return true;
                                                                           }
                                                                           return false;
                                                                       }
                                                                   else {
//                                                                       start();
                                                                   }
                                                                       return true;
                                                                   }
                                                               }
                );
            }
            else {
                start();
            }


        }

    }