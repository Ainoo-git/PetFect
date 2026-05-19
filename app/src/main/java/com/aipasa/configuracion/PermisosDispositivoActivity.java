package com.aipasa.configuracion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.google.android.material.appbar.MaterialToolbar;

public class PermisosDispositivoActivity extends AppCompatActivity {

    LinearLayout permisoCamara;
    LinearLayout permisoGaleria;
    LinearLayout permisoUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisos_dispositivo);

        // TOOLBAR
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        // VISTAS
        permisoCamara = findViewById(R.id.permisoCamara);
        permisoGaleria = findViewById(R.id.permisoGaleria);
        permisoUbicacion = findViewById(R.id.permisoUbicacion);

        // CLICK -> AJUSTES APP
        permisoCamara.setOnClickListener(v -> abrirAjustesPermisos());

        permisoGaleria.setOnClickListener(v -> abrirAjustesPermisos());

        permisoUbicacion.setOnClickListener(v -> abrirAjustesPermisos());
    }

    private void abrirAjustesPermisos() {

        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        );

        Uri uri = Uri.fromParts(
                "package",
                getPackageName(),
                null
        );

        intent.setData(uri);

        startActivity(intent);
    }
}