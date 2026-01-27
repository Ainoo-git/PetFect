package com.aipasa.main;

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

        // Referencias a los CheckBox
        cbPerdidos = findViewById(R.id.cbPerdidos);
        cbAdopciones = findViewById(R.id.cbAdopciones);
        cbVeterinarias = findViewById(R.id.cbVeterinarias);
        cbNotificaciones = findViewById(R.id.cbNotificaciones);

        Button btnGuardar = findViewById(R.id.btnGuardarPreferencias);


        cargarPreferencias();

        // Guardar al pulsar el botón
        btnGuardar.setOnClickListener(v -> {
            guardarPreferencias();
            Toast.makeText(this, "Preferencias guardadas", Toast.LENGTH_SHORT).show();
            finish(); // vuelve a MainActivity
        });
    }

    private void cargarPreferencias() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        cbPerdidos.setChecked(prefs.getBoolean("pref_perdidos", false));
        cbAdopciones.setChecked(prefs.getBoolean("pref_adopciones", false));
        cbVeterinarias.setChecked(prefs.getBoolean("pref_veterinarias", false));
        cbNotificaciones.setChecked(prefs.getBoolean("pref_notificaciones", false));
    }

    private void guardarPreferencias() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        prefs.edit()
                .putBoolean("pref_perdidos", cbPerdidos.isChecked())
                .putBoolean("pref_adopciones", cbAdopciones.isChecked())
                .putBoolean("pref_veterinarias", cbVeterinarias.isChecked())
                .putBoolean("pref_notificaciones", cbNotificaciones.isChecked())
                .apply();
    }
}
