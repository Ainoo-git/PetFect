package com.aipasa.configuracion;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.aipasa.R;
import com.google.android.material.appbar.MaterialToolbar;

public class ConfiguracionActivity extends AppCompatActivity {

    private LinearLayout eliminarCuenta;
    private View overlay;
    private View card;
    private TextView email;

    private static final String PREFS_SETTINGS = "settings";
    private static final String PREF_MODO_OSCURO = "modoOscuro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        SharedPreferences prefs = getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE);

        configurarToolbar();
        inicializarVistas();
        configurarClicks();
        configurarModoOscuro(prefs);
        configurarEliminarCuenta();
    }

    private void configurarToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void inicializarVistas() {
        eliminarCuenta = findViewById(R.id.eliminar_cuenta);
        overlay = findViewById(R.id.overlayEliminarCuenta);
        card = findViewById(R.id.cardEliminarCuenta);
        email = findViewById(R.id.tvEmailSoporte);
    }

    private void configurarClicks() {
        findViewById(R.id.atribuciones).setOnClickListener(v -> openAtribuciones());
        findViewById(R.id.licencias).setOnClickListener(v -> openLicencia());
        findViewById(R.id.terminos_condiciones).setOnClickListener(v -> openTerminosYcondiciones());
        findViewById(R.id.permisos_dispositivo).setOnClickListener(v -> openPermisos());
        findViewById(R.id.politica_privacidad).setOnClickListener(v -> openPrivacidad());
        findViewById(R.id.guardados).setOnClickListener(v -> openGuardados());
        findViewById(R.id.notificaciones).setOnClickListener(v -> openNotificaciones());
        findViewById(R.id.idioma).setOnClickListener(v -> openIdioma());
    }

    private void configurarModoOscuro(SharedPreferences prefs) {
        Switch switchModo = findViewById(R.id.switchModoOscuro);

        boolean oscuroActivo = prefs.getBoolean(PREF_MODO_OSCURO, false);
        switchModo.setChecked(oscuroActivo);

        switchModo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit()
                    .putBoolean(PREF_MODO_OSCURO, isChecked)
                    .apply();

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    private void configurarEliminarCuenta() {
        eliminarCuenta.setOnClickListener(v -> {
            overlay.setVisibility(View.VISIBLE);
            card.setVisibility(View.VISIBLE);
        });

        overlay.setOnClickListener(v -> {
            overlay.setVisibility(View.GONE);
            card.setVisibility(View.GONE);
        });

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

            Toast.makeText(
                    this,
                    getString(R.string.correo_copiado),
                    Toast.LENGTH_SHORT
            ).show();

            return true;
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

    private void openIdioma() {
        startActivity(new Intent(ConfiguracionActivity.this, IdiomaActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}