package com.aipasa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private View sectionPerdidos, sectionAdopciones, sectionVeterinarias;
    private TextView tvNadaSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sectionPerdidos = findViewById(R.id.sectionPerdidos);
        sectionAdopciones = findViewById(R.id.sectionAdopciones);
        sectionVeterinarias = findViewById(R.id.sectionVeterinarias);
        tvNadaSeleccionado = findViewById(R.id.tvNadaSeleccionado);

        Button btnOpenPreferencias = findViewById(R.id.btnOpenPreferencias);
        btnOpenPreferencias.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, PreferenciasActivity.class))
        );

        aplicarPreferencias();
    }

    @Override
    protected void onResume() {
        super.onResume();
        aplicarPreferencias(); // se actualiza al volver de Preferencias
    }

    private void aplicarPreferencias() {
        SharedPreferences prefs = getSharedPreferences("petfect_prefs", MODE_PRIVATE);

        boolean verPerdidos = prefs.getBoolean("pref_perdidos", false);
        boolean verAdopciones = prefs.getBoolean("pref_adopciones", false);
        boolean verVeterinarias = prefs.getBoolean("pref_veterinarias", false);

        sectionPerdidos.setVisibility(verPerdidos ? View.VISIBLE : View.GONE);
        sectionAdopciones.setVisibility(verAdopciones ? View.VISIBLE : View.GONE);
        sectionVeterinarias.setVisibility(verVeterinarias ? View.VISIBLE : View.GONE);

        // Si no ha marcado nada, mostramos un mensaje
        boolean nada = !verPerdidos && !verAdopciones && !verVeterinarias;
        tvNadaSeleccionado.setVisibility(nada ? View.VISIBLE : View.GONE);

        // Si mostramos el mensaje, ocultamos las secciones
        if (nada) {
            sectionPerdidos.setVisibility(View.GONE);
            sectionAdopciones.setVisibility(View.GONE);
            sectionVeterinarias.setVisibility(View.GONE);
        }
    }
}
