package com.aipasa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aipasa.fragment.HomeFragment;
import com.aipasa.fragment.MapFragment;
import com.aipasa.fragment.ProfileFragment;
import com.aipasa.fragment.SearchFragment;
import com.aipasa.main.PublicacionActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainBab extends AppCompatActivity {

    private ActivityResultLauncher<String> permisoNotificacionesLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainbab);

        permisoNotificacionesLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.RequestPermission(),
                        isGranted -> {
                            suscribirNotificaciones();
                        }
                );

        pedirPermisoNotificaciones();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        FloatingActionButton fab = findViewById(R.id.fab_central);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_container, new HomeFragment())
                    .commit();
        }

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

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainBab.this, PublicacionActivity.class);
            startActivity(intent);
        });
    }

    private void pedirPermisoNotificaciones() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                permisoNotificacionesLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                );

            } else {
                suscribirNotificaciones();
            }

        } else {
            suscribirNotificaciones();
        }
    }

    private void suscribirNotificaciones() {

        FirebaseMessaging.getInstance()
                .subscribeToTopic("allUsers")
                .addOnSuccessListener(unused ->
                        System.out.println("Suscrito a allUsers"))
                .addOnFailureListener(e ->
                        System.out.println("Error al suscribirse: " + e.getMessage()));
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}