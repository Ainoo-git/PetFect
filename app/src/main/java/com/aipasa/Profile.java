package com.aipasa; // Cambia esto si tu paquete es distinto

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Toolbar back
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Nombre en el perfil
        TextView tvNombre = findViewById(R.id.nombre2);

        SharedPreferences prefs = getSharedPreferences("petfect_prefs", MODE_PRIVATE);
        String username = prefs.getString("username", "Nombre");

        tvNombre.setText(username);
    }
}
