package com.aipasa.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.pm.PackageManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aipasa.R;
import com.aipasa.main.AdopcionesActivity;
import com.aipasa.main.MapaActivity;
import com.aipasa.main.PerdidosActivity;
import com.aipasa.main.Profile;
import com.aipasa.main.PublicacionActivity;

public class HomeFragment extends Fragment {

    // Secciones
    private View sectionPerdidos, sectionAdopciones, sectionVeterinarias;
    private TextView tvNadaSeleccionado;

    // Preferencias
    private boolean prefPerdidos, prefAdopciones, prefVeterinarias;

    // Cámara
    private static final int REQUEST_CAMERA = 1;
    private static final int CAMERA_PERMISSION_CODE = 100;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Referencias
        sectionPerdidos = view.findViewById(R.id.sectionPerdidos);
        sectionAdopciones = view.findViewById(R.id.sectionAdopciones);
        sectionVeterinarias = view.findViewById(R.id.sectionVeterinarias);
        tvNadaSeleccionado = view.findViewById(R.id.tvNadaSeleccionado);

        Button btnAll = view.findViewById(R.id.btnAll);
        Button btnAdopciones = view.findViewById(R.id.btnAdopciones);
        Button btnPerdidos = view.findViewById(R.id.btnPerdidos);
        Button btnMapa = view.findViewById(R.id.btnMapa);

        View imgPerfil = view.findViewById(R.id.imgPerfil);

        // 🔹 Clicks
        btnAll.setOnClickListener(v -> mostrarAll());

        btnAdopciones.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AdopcionesActivity.class));
        });

        btnPerdidos.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PerdidosActivity.class));
        });

        btnMapa.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MapaActivity.class));
        });

        imgPerfil.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), Profile.class));
        });

        // Preferencias
        cargarPreferencias();

        // Estado inicial
        mostrarAll();
    }

    // -------------------------
    // CÁMARA
    // -------------------------

    private void abrirCamara() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA &&
                resultCode == getActivity().RESULT_OK &&
                data != null) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            Intent intent = new Intent(getActivity(), PublicacionActivity.class);
            intent.putExtra("fotoDesdeCamara", photo);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                abrirCamara();

            } else {
                Toast.makeText(getActivity(),
                        "Permiso de cámara denegado",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // -------------------------
    // PREFERENCIAS
    // -------------------------

    private void cargarPreferencias() {
        SharedPreferences prefs = getActivity()
                .getSharedPreferences("petfect_prefs", getActivity().MODE_PRIVATE);

        prefPerdidos = prefs.getBoolean("pref_perdidos", true);
        prefAdopciones = prefs.getBoolean("pref_adopciones", true);
        prefVeterinarias = prefs.getBoolean("pref_veterinarias", true);
    }

    // -------------------------
    // UI
    // -------------------------

    private void mostrarAll() {
        sectionPerdidos.setVisibility(prefPerdidos ? View.VISIBLE : View.GONE);
        sectionAdopciones.setVisibility(prefAdopciones ? View.VISIBLE : View.GONE);
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