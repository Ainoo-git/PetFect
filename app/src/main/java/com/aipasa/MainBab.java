package com.aipasa;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.aipasa.main.MainActivity;
import com.aipasa.main.MapaActivity;
import com.aipasa.main.Profile;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.aipasa.fragment.HomeFragment;
import com.aipasa.fragment.MapFragment;
import com.aipasa.fragment.SearchFragment;
import com.aipasa.fragment.ProfileFragment;

public class MainBab extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainbab);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.home) {
                    selectedFragment = new HomeFragment();
                } else if (item.getItemId() == R.id.map) {
                    selectedFragment = new MapFragment();
                } else if (item.getItemId() == R.id.search) {
                    selectedFragment = new SearchFragment();
                } else if (item.getItemId() == R.id.profile) {
                    selectedFragment = new ProfileFragment();
                }

                if (savedInstanceState == null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout, new HomeFragment())
                            .commit();
                    bottomNav.setSelectedItemId(R.id.home);
                }

                return true;
            }
        });

        // FAB
        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v ->
                Toast.makeText(this, "FAB Clicked", Toast.LENGTH_SHORT).show()
        );


    }


    // BottomSheet
    private void showBottomSheetDialog() {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.activity_mainbab, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
    }
}
//        TextView option1 = view.findViewById(R.id.option1);
//        TextView option2 = view.findViewById(R.id.option2);
//        TextView option3 = view.findViewById(R.id.option3);
//
//        option1.setOnClickListener(v -> {
//            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        });
//
//        option2.setOnClickListener(v -> {
//            Toast.makeText(this, "About clicked", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        });
//
//        option3.setOnClickListener(v -> {
//            Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        });

// BottomAppBar → si quieres usar la hamburguesa y menú
//        BottomAppBar bottomAppBar = findViewById(R.id.bottomNavigationView);
//        bottomAppBar.setNavigationOnClickListener(v -> showBottomSheetDialog());
//        bottomAppBar.setOnMenuItemClickListener(item -> {
//            if (item.getItemId() == R.id.heart) {
//                Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//            return false;
//        });