package com.aipasa.configuracion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aipasa.R;

public class AtribucionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atribuciones);

        // TOOLBAR
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // flecha atrás
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // flecha atrás
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView linkFlaticon = findViewById(R.id.linkFlaticon);
        TextView linkChatGPT = findViewById(R.id.linkChatGPT);

        linkFlaticon.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.flaticon.es/"));
            startActivity(intent);
        });

        linkChatGPT.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://chatgpt.com/"));
            startActivity(intent);
        });
    }
}