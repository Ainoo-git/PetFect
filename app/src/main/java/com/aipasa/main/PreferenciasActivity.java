package com.aipasa.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.MainBab;
import com.aipasa.R;

public class PreferenciasActivity extends AppCompatActivity {

    private CheckBox cbPerdidos;
    private CheckBox cbAdopciones;
    private CheckBox cbVeterinarias;

    private static final String PREFS = "petfect_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        cbPerdidos = findViewById(R.id.cbPerdidos);
        cbAdopciones = findViewById(R.id.cbAdopciones);
        cbVeterinarias = findViewById(R.id.cbVeterinarias);

        Button btnGuardar = findViewById(R.id.btnGuardarPreferencias);

        cargarPreferencias();

        if (btnGuardar != null) {
            btnGuardar.setOnClickListener(v -> {
                guardarPreferencias();

                Toast.makeText(
                        PreferenciasActivity.this,
                        "Preferencias guardadas",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent = new Intent(PreferenciasActivity.this, MainBab.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                finish();
            });
        }
    }

    private void cargarPreferencias() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        if (cbPerdidos != null) {
            cbPerdidos.setChecked(prefs.getBoolean("pref_perdidos", true));
        }

        if (cbAdopciones != null) {
            cbAdopciones.setChecked(prefs.getBoolean("pref_adopciones", true));
        }

        if (cbVeterinarias != null) {
            cbVeterinarias.setChecked(prefs.getBoolean("pref_veterinarias", true));
        }
    }

    private void guardarPreferencias() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        boolean perdidos = cbPerdidos != null && cbPerdidos.isChecked();
        boolean adopciones = cbAdopciones != null && cbAdopciones.isChecked();
        boolean veterinarias = cbVeterinarias != null && cbVeterinarias.isChecked();

        prefs.edit()
                .putBoolean("pref_perdidos", perdidos)
                .putBoolean("pref_adopciones", adopciones)
                .putBoolean("pref_veterinarias", veterinarias)
                .putBoolean("preferencias_configuradas", true)
                .apply();
    }
}