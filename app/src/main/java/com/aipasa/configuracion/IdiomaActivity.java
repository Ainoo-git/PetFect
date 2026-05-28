package com.aipasa.configuracion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import com.aipasa.R;
import com.google.android.material.appbar.MaterialToolbar;

public class IdiomaActivity extends AppCompatActivity {

    private static final String PREFS_SETTINGS = "settings";
    private static final String PREF_IDIOMA = "idioma";

    private RadioGroup radioGroupIdioma;
    private RadioButton radioEspanol;
    private RadioButton radioIngles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idioma);

        configurarToolbar();
        inicializarVistas();
        cargarIdiomaActual();
        configurarCambioIdioma();
    }

    private void configurarToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.topAppBarIdioma);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void inicializarVistas() {
        radioGroupIdioma = findViewById(R.id.radioGroupIdioma);
        radioEspanol = findViewById(R.id.radioEspanol);
        radioIngles = findViewById(R.id.radioIngles);
    }

    private void cargarIdiomaActual() {
        SharedPreferences prefs = getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE);
        String idiomaActual = prefs.getString(PREF_IDIOMA, "es");

        if ("en".equals(idiomaActual)) {
            radioIngles.setChecked(true);
        } else {
            radioEspanol.setChecked(true);
        }
    }

    private void configurarCambioIdioma() {
        radioGroupIdioma.setOnCheckedChangeListener((group, checkedId) -> {
            String codigoIdioma;

            if (checkedId == R.id.radioIngles) {
                codigoIdioma = "en";
            } else {
                codigoIdioma = "es";
            }

            SharedPreferences prefs = getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE);

            prefs.edit()
                    .putString(PREF_IDIOMA, codigoIdioma)
                    .apply();

            LocaleListCompat appLocale =
                    LocaleListCompat.forLanguageTags(codigoIdioma);

            AppCompatDelegate.setApplicationLocales(appLocale);

            recreate();
        });
    }
}