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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.aipasa.R;
import com.aipasa.creditos.Creditos;
import com.google.android.material.appbar.MaterialToolbar;

public class ConfiguracionActivity extends AppCompatActivity {

    private LinearLayout eliminarCuenta;
    private View overlay;
    private View card;
    private TextView email;
    private TextView tvDescripcionModo;

    private static final String PREFS_SETTINGS = "settings";
    private static final String PREF_MODO_OSCURO = "modoOscuro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        aplicarModoGuardadoAntesDeCargar();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        configurarToolbar();
        inicializarVistas();
        configurarModoOscuro();
        configurarClicks();
        configurarEliminarCuenta();
    }

    private void aplicarModoGuardadoAntesDeCargar() {
        SharedPreferences prefs = getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE);
        boolean modoOscuro = prefs.getBoolean(PREF_MODO_OSCURO, false);

        if (modoOscuro) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void configurarToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        if (toolbar == null) {
            return;
        }

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void inicializarVistas() {
        eliminarCuenta = findViewById(R.id.eliminar_cuenta);
        overlay = findViewById(R.id.overlayEliminarCuenta);
        card = findViewById(R.id.cardEliminarCuenta);
        email = findViewById(R.id.tvEmailSoporte);
        tvDescripcionModo = findViewById(R.id.tvDescripcionModo);
    }

    private void configurarModoOscuro() {
        Switch switchModo = findViewById(R.id.switchModoOscuro);

        if (switchModo == null) {
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE);
        boolean modoOscuro = prefs.getBoolean(PREF_MODO_OSCURO, false);

        actualizarTextoModo(modoOscuro);

        switchModo.setOnCheckedChangeListener(null);
        switchModo.setChecked(modoOscuro);

        switchModo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences prefs = getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE);
                boolean valorActual = prefs.getBoolean(PREF_MODO_OSCURO, false);

                if (valorActual == isChecked) {
                    return;
                }

                prefs.edit()
                        .putBoolean(PREF_MODO_OSCURO, isChecked)
                        .apply();

                actualizarTextoModo(isChecked);

                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }

    private void actualizarTextoModo(boolean modoOscuro) {
        if (tvDescripcionModo == null) {
            return;
        }

        if (modoOscuro) {
            tvDescripcionModo.setText(getString(R.string.modo_vista_descripcion_oscuro));
        } else {
            tvDescripcionModo.setText(getString(R.string.modo_vista_descripcion_claro));
        }
    }

    private void configurarClicks() {
        View guardados = findViewById(R.id.guardados);
        View notificaciones = findViewById(R.id.notificaciones);
        View idioma = findViewById(R.id.idioma);
        View permisos = findViewById(R.id.permisos_dispositivo);
        View privacidad = findViewById(R.id.politica_privacidad);
        View terminos = findViewById(R.id.terminos_condiciones);
        View licencias = findViewById(R.id.licencias);
        View atribuciones = findViewById(R.id.atribuciones);
        View creditos = findViewById(R.id.creditos);

        if (guardados != null) {
            guardados.setOnClickListener(v -> abrirActivitySeguro(GuaradarMascotaActivity.class));
        }

        if (notificaciones != null) {
            notificaciones.setOnClickListener(v -> abrirActivitySeguro(NotificacionesActivity.class));
        }

        if (idioma != null) {
            idioma.setOnClickListener(v -> abrirActivitySeguro(IdiomaActivity.class));
        }

        if (permisos != null) {
            permisos.setOnClickListener(v -> abrirActivitySeguro(PermisosDispositivoActivity.class));
        }

        if (privacidad != null) {
            privacidad.setOnClickListener(v -> abrirActivitySeguro(PoliticaPrivacidadActivity.class));
        }

        if (terminos != null) {
            terminos.setOnClickListener(v -> abrirActivitySeguro(TerminosCondicionesActivity.class));
        }

        if (licencias != null) {
            licencias.setOnClickListener(v -> abrirActivitySeguro(LicenciaActivity.class));
        }

        if (atribuciones != null) {
            atribuciones.setOnClickListener(v -> abrirActivitySeguro(AtribucionesActivity.class));
        }

        if (creditos != null) {
            creditos.setOnClickListener(v -> abrirActivitySeguro(Creditos.class));
        }
    }

    private void abrirActivitySeguro(Class<?> activityClass) {
        try {
            Intent intent = new Intent(ConfiguracionActivity.this, activityClass);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "No se pudo abrir esta pantalla",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void configurarEliminarCuenta() {
        if (eliminarCuenta == null || overlay == null || card == null || email == null) {
            return;
        }

        eliminarCuenta.setOnClickListener(v -> {
            overlay.setVisibility(View.VISIBLE);
            card.setVisibility(View.VISIBLE);
        });

        overlay.setOnClickListener(v -> {
            overlay.setVisibility(View.GONE);
            card.setVisibility(View.GONE);
        });

        email.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:petfect26@gmail.com"));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(
                        this,
                        "No se pudo abrir el correo",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        email.setOnLongClickListener(v -> {
            ClipboardManager clipboard =
                    (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText("email", "petfect26@gmail.com");
                clipboard.setPrimaryClip(clip);

                Toast.makeText(
                        this,
                        getString(R.string.correo_copiado),
                        Toast.LENGTH_SHORT
                ).show();
            }

            return true;
        });
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