package com.aipasa.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;

public class PreferenciasActivity extends AppCompatActivity {

    private CheckBox cbPerdidos, cbAdopciones, cbVeterinarias, cbNotificaciones;

    private static final String PREFS = "petfect_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        cbPerdidos = findViewById(R.id.cbPerdidos);
        cbAdopciones = findViewById(R.id.cbAdopciones);
        cbVeterinarias = findViewById(R.id.cbVeterinarias);
        cbNotificaciones = findViewById(R.id.cbNotificaciones);

        Button btnGuardar = findViewById(R.id.btnGuardarPreferencias);

        cargarPreferencias();

        btnGuardar.setOnClickListener(v -> {
            guardarPreferencias();
            Toast.makeText(this, "Preferencias guardadas", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PreferenciasActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void cargarPreferencias() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        cbPerdidos.setChecked(prefs.getBoolean("pref_perdidos", true));
        cbAdopciones.setChecked(prefs.getBoolean("pref_adopciones", true));
        cbVeterinarias.setChecked(prefs.getBoolean("pref_veterinarias", true));
        cbNotificaciones.setChecked(prefs.getBoolean("pref_notificaciones", true));
    }

    private void guardarPreferencias() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        prefs.edit()
                .putBoolean("pref_perdidos", cbPerdidos.isChecked())
                .putBoolean("pref_adopciones", cbAdopciones.isChecked())
                .putBoolean("pref_veterinarias", cbVeterinarias.isChecked())
                .putBoolean("pref_notificaciones", cbNotificaciones.isChecked())
                .putBoolean("preferencias_configuradas", true)
                .apply();
    }
}