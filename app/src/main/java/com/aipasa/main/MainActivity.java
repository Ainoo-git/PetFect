package com.aipasa.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.aipasa.auth.Login;
import com.aipasa.auth.SignUp;

public class MainActivity extends AppCompatActivity {

    private View sectionPerdidos, sectionAdopciones, sectionVeterinarias;
    private TextView tvNadaSeleccionado;

    private boolean prefPerdidos, prefAdopciones, prefVeterinarias;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bab);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Secciones
        sectionPerdidos = findViewById(R.id.sectionPerdidos);
        sectionAdopciones = findViewById(R.id.sectionAdopciones);
        sectionVeterinarias = findViewById(R.id.sectionVeterinarias);
        tvNadaSeleccionado = findViewById(R.id.tvNadaSeleccionado);

        // Botones action bar
        Button btnAll = findViewById(R.id.btnAll);
        Button btnAdopciones = findViewById(R.id.btnAdopciones);
        Button btnPerdidos = findViewById(R.id.btnPerdidos);
        Button btnMapa = findViewById(R.id.btnMapa);

        cargarPreferencias();

        // Estado inicial → ALL
        mostrarAll();

        btnAll.setOnClickListener(v -> mostrarAll());
        btnAdopciones.setOnClickListener(v -> mostrarSoloAdopciones());
        btnPerdidos.setOnClickListener(v -> mostrarSoloPerdidos());
        btnMapa.setOnClickListener(v -> mostrarSoloVeterinarias());
    }
    public void OpenProfile(View view) {
        Intent i = new Intent(MainActivity.this, Profile.class);
        startActivity(i);
    }
    private void cargarPreferencias() {
        SharedPreferences prefs = getSharedPreferences("petfect_prefs", MODE_PRIVATE);
        prefPerdidos = prefs.getBoolean("pref_perdidos", false);
        prefAdopciones = prefs.getBoolean("pref_adopciones", false);
        prefVeterinarias = prefs.getBoolean("pref_veterinarias", false);
    }

    private void mostrarAll() {
        sectionPerdidos.setVisibility(prefPerdidos ? View.VISIBLE : View.GONE);
        sectionAdopciones.setVisibility(prefAdopciones ? View.VISIBLE : View.GONE);
        sectionVeterinarias.setVisibility(prefVeterinarias ? View.VISIBLE : View.GONE);

        mostrarMensajeSiNada();
    }

    private void mostrarSoloPerdidos() {
        sectionPerdidos.setVisibility(prefPerdidos ? View.VISIBLE : View.GONE);
        sectionAdopciones.setVisibility(View.GONE);
        sectionVeterinarias.setVisibility(View.GONE);

        mostrarMensajeSiNada();
    }

    private void mostrarSoloAdopciones() {
        sectionPerdidos.setVisibility(View.GONE);
        sectionAdopciones.setVisibility(prefAdopciones ? View.VISIBLE : View.GONE);
        sectionVeterinarias.setVisibility(View.GONE);

        mostrarMensajeSiNada();
    }

    private void mostrarSoloVeterinarias() {
        sectionPerdidos.setVisibility(View.GONE);
        sectionAdopciones.setVisibility(View.GONE);
        sectionVeterinarias.setVisibility(prefVeterinarias ? View.VISIBLE : View.GONE);

        mostrarMensajeSiNada();
    }

    private void mostrarMensajeSiNada() {
        boolean nadaVisible =
                sectionPerdidos.getVisibility() == View.GONE &&
                        sectionAdopciones.getVisibility() == View.GONE &&
                        sectionVeterinarias.getVisibility() == View.GONE;

        tvNadaSeleccionado.setVisibility(nadaVisible ? View.VISIBLE : View.GONE);
    }
}
