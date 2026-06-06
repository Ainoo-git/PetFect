package com.aipasa.creditos;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.google.android.material.appbar.MaterialToolbar;

public class Creditos extends AppCompatActivity {

    private MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);

        topAppBar = findViewById(R.id.topAppBar);

        if (topAppBar != null) {
            topAppBar.setNavigationOnClickListener(v -> finish());
        }
    }
}