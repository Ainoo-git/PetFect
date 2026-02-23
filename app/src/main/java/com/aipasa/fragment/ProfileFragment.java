package com.aipasa.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aipasa.R;
import com.aipasa.auth.Login;
import com.aipasa.main.MapaActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private TextView tvNombre;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflamos el layout
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Toolbar con flecha atrás
        MaterialToolbar toolbar = view.findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        // Nombre en el perfil
        tvNombre = view.findViewById(R.id.nombre2);
        SharedPreferences prefs = requireContext().getSharedPreferences("petfect_prefs", getContext().MODE_PRIVATE);
        String username = prefs.getString("username", "Nombre");
        tvNombre.setText(username);

        // Botón Cerrar sesión
        Button btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::openLogin);

        // Botón Configuración
        Button btnConfiguracion = view.findViewById(R.id.btnConfiguracion);
        btnConfiguracion.setOnClickListener(this::openMapa);

        // Botón Editar perfil (si quieres agregar funcionalidad más adelante)
        Button btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil);
        btnEditarPerfil.setOnClickListener(v -> {
            // Aquí puedes abrir un Activity o Fragment de edición de perfil
        });

        return view;
    }

    private void openLogin(View view) {
        // Cierra sesión en Firebase
        FirebaseAuth.getInstance().signOut();

        // Limpia SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("petfect_prefs", getContext().MODE_PRIVATE);
        prefs.edit().clear().apply();

        // Abre la Activity de Login y cierra la actual
        Intent intent = new Intent(requireContext(), Login.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void openMapa(View view) {
        // Abre el Activity de mapa
        Intent intent = new Intent(requireContext(), MapaActivity.class);
        startActivity(intent);
    }
}