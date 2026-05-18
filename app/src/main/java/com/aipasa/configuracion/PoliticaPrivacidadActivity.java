package com.aipasa.configuracion;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

import com.aipasa.R;

public class PoliticaPrivacidadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_privacidad);
//
//        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
//
//        toolbar.setNavigationOnClickListener(v -> finish());

        findViewById(R.id.topAppBar).setOnClickListener(v -> {
            openConfig();
        });
    }


    private void openConfig() {
        startActivity(new Intent(PoliticaPrivacidadActivity.this, ConfiguracionActivity.class));
    }
}