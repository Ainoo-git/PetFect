package com.aipasa.configuracion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.aipasa.R;

public class ConfiguracionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_configuracion); // 👈 CLAVE

        Switch switchModo = findViewById(R.id.switchModoOscuro);

        SharedPreferences prefs = getSharedPreferences("settings", Context.MODE_PRIVATE);

        // 🔹 leer modo guardado (BOOLEANO)
        boolean oscuroActivo = prefs.getBoolean("modoOscuro", false);

        switchModo.setChecked(oscuroActivo);

        // 🔹 aplicar modo al iniciar
        if (oscuroActivo) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // 🔹 listener del switch
        switchModo.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            prefs.edit().putBoolean("modoOscuro", isChecked).apply();
        });
    }
}