package com.aipasa.configuracion;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.aipasa.R;
import com.aipasa.configuracion.NotificacionesActivity;
import com.google.android.material.appbar.MaterialToolbar;

public class ConfiguracionActivity extends AppCompatActivity {

    LinearLayout eliminarCuenta;
    View overlay;
    View card;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // CLICK BOTÓN ATRÁS (IMPORTANTE)
        toolbar.setNavigationOnClickListener(v -> finish());

        // =====================
        // VISTAS
        // =====================
        Switch switchModo = findViewById(R.id.switchModoOscuro);

        eliminarCuenta = findViewById(R.id.eliminar_cuenta);
        overlay = findViewById(R.id.overlayEliminarCuenta);
        card = findViewById(R.id.cardEliminarCuenta);
        email = findViewById(R.id.tvEmailSoporte);

        // ATRIBUCIONES
        findViewById(R.id.atribuciones).setOnClickListener(v -> {
            openAtribuciones();
        });
        findViewById(R.id.licencias).setOnClickListener(v -> {
            openLicencia();
        });

        findViewById(R.id.terminos_condiciones).setOnClickListener(v -> {
            openTerminosYcondiciones();
        });
        findViewById(R.id.permisos_dispositivo).setOnClickListener(v -> {
            openPermisos();
        });
        findViewById(R.id.politica_privacidad).setOnClickListener(v -> {
            openPrivacidad();
        });
        findViewById(R.id.guardados).setOnClickListener(v -> {
            openGuardados();
        });
        findViewById(R.id.notificaciones).setOnClickListener(v -> {
            openNotificaciones();
        });
        // =====================
        // MODO OSCURO
        // =====================
        SharedPreferences prefs = getSharedPreferences("settings", Context.MODE_PRIVATE);

        boolean oscuroActivo = prefs.getBoolean("modoOscuro", false);
        switchModo.setChecked(oscuroActivo);

        if (oscuroActivo) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        switchModo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            prefs.edit().putBoolean("modoOscuro", isChecked).apply();
        });

        // =====================
        // POPUP ELIMINAR CUENTA
        // =====================
        eliminarCuenta.setOnClickListener(v -> {
            overlay.setVisibility(View.VISIBLE);
            card.setVisibility(View.VISIBLE);
        });

        overlay.setOnClickListener(v -> {
            overlay.setVisibility(View.GONE);
            card.setVisibility(View.GONE);
        });

        // =====================
        // EMAIL SOPORTE
        // =====================
        email.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:petfect26@gmail.com"));
            startActivity(intent);
        });

        email.setOnLongClickListener(v -> {
            ClipboardManager clipboard =
                    (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            ClipData clip = ClipData.newPlainText("email", "petfect26@gmail.com");
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this, "Correo copiado", Toast.LENGTH_SHORT).show();
            return true;
        });

        findViewById(R.id.guardados).setOnClickListener(v -> {

            Intent intent = new Intent(
                    ConfiguracionActivity.this,
                    GuaradarMascotaActivity.class
            );

            startActivity(intent);
        });
    }


    private void openAtribuciones() {
        startActivity(new Intent(ConfiguracionActivity.this, AtribucionesActivity.class));
    }

    private void openLicencia() {
        startActivity(new Intent(ConfiguracionActivity.this, LicenciaActivity.class));
    }
    private void openTerminosYcondiciones() {
        startActivity(new Intent(ConfiguracionActivity.this, TerminosCondicionesActivity.class));
    }

    private void openPermisos() {
        startActivity(new Intent(ConfiguracionActivity.this, PermisosDispositivoActivity.class));
    }

    private void openPrivacidad() {
        startActivity(new Intent(ConfiguracionActivity.this, PoliticaPrivacidadActivity.class));
    }
    private void openGuardados() {
        startActivity(new Intent(ConfiguracionActivity.this, GuaradarMascotaActivity.class));
    }
    private void openNotificaciones() {
        startActivity(new Intent(ConfiguracionActivity.this, NotificacionesActivity.class));
    }
}