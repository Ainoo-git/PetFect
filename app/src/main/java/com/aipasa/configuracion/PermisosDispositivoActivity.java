package com.aipasa.configuracion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.aipasa.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.messaging.FirebaseMessaging;

public class PermisosDispositivoActivity extends AppCompatActivity {

    LinearLayout permisoCamara;
    LinearLayout permisoGaleria;
    LinearLayout permisoUbicacion;
    LinearLayout permisoNotificaciones;

    private ActivityResultLauncher<String> permisoNotificacionesLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisos_dispositivo);

        configurarToolbar();

        permisoCamara = findViewById(R.id.permisoCamara);
        permisoGaleria = findViewById(R.id.permisoGaleria);
        permisoUbicacion = findViewById(R.id.permisoUbicacion);
        permisoNotificaciones = findViewById(R.id.permisoNotificaciones);

        permisoNotificacionesLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.RequestPermission(),
                        isGranted -> {
                            if (isGranted) {
                                suscribirNotificaciones();
                            } else {
                                Toast.makeText(this,
                                        "Permiso de notificaciones denegado",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                );

        permisoCamara.setOnClickListener(v -> abrirAjustesPermisos());
        permisoGaleria.setOnClickListener(v -> abrirAjustesPermisos());
        permisoUbicacion.setOnClickListener(v -> abrirAjustesPermisos());
        permisoNotificaciones.setOnClickListener(v -> pedirPermisoNotificaciones());
    }

    // ===== TOOLBAR CORRECTA =====
    private void configurarToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void pedirPermisoNotificaciones() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                permisoNotificacionesLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                );

            } else {
                suscribirNotificaciones();

                Toast.makeText(this,
                        "Las notificaciones ya están activadas",
                        Toast.LENGTH_SHORT).show();
            }

        } else {
            suscribirNotificaciones();

            Toast.makeText(this,
                    "Notificaciones activadas",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void suscribirNotificaciones() {

        FirebaseMessaging.getInstance()
                .subscribeToTopic("allUsers")
                .addOnSuccessListener(unused ->
                        Toast.makeText(this,
                                "Recibirás avisos de nuevas mascotas",
                                Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "No se pudieron activar las notificaciones",
                                Toast.LENGTH_SHORT).show()
                );
    }

    private void abrirAjustesPermisos() {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

        Uri uri = Uri.fromParts("package", getPackageName(), null);

        intent.setData(uri);

        startActivity(intent);
    }
}