package com.aipasa.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    // Secciones de la pantalla principal
    private View sectionPerdidos, sectionAdopciones, sectionVeterinarias;
    private TextView tvNadaSeleccionado;

    // Preferencias del usuario
    private boolean prefPerdidos, prefAdopciones, prefVeterinarias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias a las secciones
        sectionPerdidos = findViewById(R.id.sectionPerdidos);
        sectionAdopciones = findViewById(R.id.sectionAdopciones);
        sectionVeterinarias = findViewById(R.id.sectionVeterinarias);
        tvNadaSeleccionado = findViewById(R.id.tvNadaSeleccionado);

        // Botones de la barra superior
        Button btnAll = findViewById(R.id.btnAll);
        Button btnAdopciones = findViewById(R.id.btnAdopciones);
        Button btnPerdidos = findViewById(R.id.btnPerdidos);


        // Cargar preferencias guardadas
        cargarPreferencias();

        // Estado inicial
        mostrarAll();

        // Botón ALL muestra tod según preferencia
        btnAll.setOnClickListener(v -> mostrarAll());

        // Botón ADOPCIONES → abre nueva activity
        btnAdopciones.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdopcionesActivity.class);
            startActivity(intent);
        });

        // Botón PERDIDOS → abre nueva activity
        btnPerdidos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PerdidosActivity.class);
            startActivity(intent);
        });

        // Botón MAPA / VETERINARIAS
        //btnMapa.setOnClickListener(v -> mostrarSoloVeterinarias());
    }
    FloatingActionButton fabCentral = findViewById(R.id.fab_central);

    fabCentral.setOnClickListener(v -> {
        Intent intent = new Intent(MainActivity.this, PublicacionActivity.class);
        startActivity(intent);
    });


    // Abrir perfil del usuario
    public void OpenProfile(View view) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    // Cargar preferencias del usuario
    private void cargarPreferencias() {
        SharedPreferences prefs = getSharedPreferences("petfect_prefs", MODE_PRIVATE);
        prefPerdidos = prefs.getBoolean("pref_perdidos", true);
        prefAdopciones = prefs.getBoolean("pref_adopciones", true);
        prefVeterinarias = prefs.getBoolean("pref_veterinarias", true);
    }

    // Mostrar todas las secciones permitidas
    private void mostrarAll() {
        sectionPerdidos.setVisibility(prefPerdidos ? View.VISIBLE : View.GONE);
        sectionAdopciones.setVisibility(prefAdopciones ? View.VISIBLE : View.GONE);
        sectionVeterinarias.setVisibility(prefVeterinarias ? View.VISIBLE : View.GONE);
        mostrarMensajeSiNada();
    }

    // Mostrar solo veterinarias (mapa)
    private void mostrarSoloVeterinarias() {
        sectionPerdidos.setVisibility(View.GONE);
        sectionAdopciones.setVisibility(View.GONE);
        sectionVeterinarias.setVisibility(prefVeterinarias ? View.VISIBLE : View.GONE);
        mostrarMensajeSiNada();
    }

    // Mostrar mensaje si no hay contenido visible
    private void mostrarMensajeSiNada() {
        boolean nadaVisible =
                sectionPerdidos.getVisibility() == View.GONE &&
                        sectionAdopciones.getVisibility() == View.GONE &&
                        sectionVeterinarias.getVisibility() == View.GONE;

        tvNadaSeleccionado.setVisibility(nadaVisible ? View.VISIBLE : View.GONE);
    }

}
