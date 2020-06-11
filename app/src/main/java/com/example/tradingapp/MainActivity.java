package com.example.tradingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Context mainActivity = MainActivity.this;
    private final String TAG = "DAYS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Fragment home = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =

            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment selected = null;

                    switch(menuItem.getItemId()) {

                        case R.id.action_home:
                            selected = new HomeFragment();
                            break;

                        case R.id.action_sma_ema:
                            selected = new MovingAveragesFragment();
                            break;

                        case R.id.action_stock_bot:
                            selected = new StockBotFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected).commit();

                    return true;
                }
            };
}
