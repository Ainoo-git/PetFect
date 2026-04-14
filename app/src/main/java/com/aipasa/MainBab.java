package com.aipasa;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.aipasa.fragment.HomeFragment;

import com.aipasa.fragment.MapFragment;
import com.aipasa.fragment.ProfileFragment;
import com.aipasa.fragment.SearchFragment;
import com.aipasa.main.PublicacionActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainBab extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainbab);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        FloatingActionButton fab = findViewById(R.id.fab_central);

        // Cargar fragment inicial
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_container, new HomeFragment())
                    .commit();
        }

        // Navegación inferior
        bottomNav.setOnItemSelectedListener(item -> {

            Fragment selectedFragment = null;

            int id = item.getItemId();

            if (id == R.id.home) {
                selectedFragment = new HomeFragment();

            } else if (id == R.id.map) {
                selectedFragment = new MapFragment();

            } else if (id == R.id.search) {
                selectedFragment = new SearchFragment();

            } else if (id == R.id.profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_container, selectedFragment)
                        .commit();
            }

            return true;
        });

        // FAB central
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainBab.this, PublicacionActivity.class);
            startActivity(intent);
        });
    }
}