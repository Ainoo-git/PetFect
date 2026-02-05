package com.aipasa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 100;

    private View sectionPerdidos, sectionAdopciones, sectionVeterinarias;
    private TextView tvNadaSeleccionado;

    private boolean prefPerdidos, prefAdopciones, prefVeterinarias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Secciones
        sectionPerdidos = findViewById(R.id.sectionPerdidos);
        sectionAdopciones = findViewById(R.id.sectionAdopciones);
        sectionVeterinarias = findViewById(R.id.sectionVeterinarias);
        tvNadaSeleccionado = findViewById(R.id.tvNadaSeleccionado);

        // Botones superiores
        Button btnAll = findViewById(R.id.btnAll);
        Button btnAdopciones = findViewById(R.id.btnAdopciones);
        Button btnPerdidos = findViewById(R.id.btnPerdidos);
        Button btnMapa = findViewById(R.id.btnMapa);

        // Bottom bar y FAB
        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        FloatingActionButton fab = findViewById(R.id.fabPublicar);

        cargarPreferencias();
        mostrarAll();

        // Filtros
        btnAll.setOnClickListener(v -> mostrarAll());
        btnAdopciones.setOnClickListener(v -> mostrarSoloAdopciones());
        btnPerdidos.setOnClickListener(v -> mostrarSoloPerdidos());
        btnMapa.setOnClickListener(v -> mostrarSoloVeterinarias());

        // FAB → abrir cámara
        fab.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        // Menú inferior (TU menu_bottom)
        bottomAppBar.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {

                case R.id.item_profile:
                    startActivity(new Intent(this, Profile.class));
                    return true;

                case R.id.item3: // MainBab
                    startActivity(new Intent(this, MainBab.class));
                    return true;

                case R.id.item4: // MainBn
                    startActivity(new Intent(this, MainActivity.class));
                    return true;

                case R.id.item5: // Signout
                    startActivity(new Intent(this, Login.class));
                    finish();
                    return true;

                case R.id.mySetting:
                    startActivity(new Intent(this, PreferenciasActivity.class));
                    return true;
            }

            return false;
        });
    }

    // Resultado de la cámara → formulario
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bitmap image = (Bitmap) data.getExtras().get("data");

            Intent intent = new Intent(this, PublicacionActivity.class);
            intent.putExtra("imagen", image);
            startActivity(intent);
        }
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
