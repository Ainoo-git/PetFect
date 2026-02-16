package com.aipasa.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    // Secciones de la pantalla principal
    private View sectionPerdidos, sectionAdopciones, sectionVeterinarias;
    private TextView tvNadaSeleccionado;

    // Preferencias del usuario
    private boolean prefPerdidos, prefAdopciones, prefVeterinarias;

    // Código cámara
    private static final int REQUEST_CAMERA = 1;
    private static final int CAMERA_PERMISSION_CODE = 100;

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

        // FAB central
        FloatingActionButton fabCentral = findViewById(R.id.fab_central);

        // Ahora abre cámara con permiso correcto
        fabCentral.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PublicacionActivity.class);
            startActivity(intent);
        });


        // Cargar preferencias guardadas
        cargarPreferencias();

        // Estado inicial
        mostrarAll();

        btnAll.setOnClickListener(v -> mostrarAll());

        btnAdopciones.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdopcionesActivity.class);
            startActivity(intent);
        });

        btnPerdidos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PerdidosActivity.class);
            startActivity(intent);
        });
    }

    // Método abrir cámara
    private void abrirCamara() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    // Recibir imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data != null) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            Intent intent = new Intent(MainActivity.this, PublicacionActivity.class);
            intent.putExtra("fotoDesdeCamara", photo);
            startActivity(intent);
        }
    }

    // Resultado permiso
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                abrirCamara();

            } else {

                Toast.makeText(this,
                        "Permiso de cámara denegado",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

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

    // Mostrar solo veterinarias
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
