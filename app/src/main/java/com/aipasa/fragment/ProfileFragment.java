package com.aipasa.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aipasa.R;
import com.aipasa.auth.Login;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflamos el layout de profile
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        // Toolbar back
        MaterialToolbar toolbar = view.findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> {
            // Si quieres, podrías reemplazar con algo que vuelva al HomeFragment
            requireActivity().onBackPressed();
        });

        // Nombre en el perfil
        TextView tvNombre = view.findViewById(R.id.nombre2);
        SharedPreferences prefs = requireContext().getSharedPreferences("petfect_prefs", getContext().MODE_PRIVATE);
        String username = prefs.getString("username", "Nombre");
        tvNombre.setText(username);

        // Botones del layout
//        view.findViewById(R.id.btnLogout).setOnClickListener(v -> openLogin(view));
//        view.findViewById(R.id.btnMapa).setOnClickListener(v -> openMapa(view));

        return view;
    }

    private void openLogin(View view) {
        // Cierra sesión en Firebase
        FirebaseAuth.getInstance().signOut();

        SharedPreferences prefs = requireContext().getSharedPreferences("petfect_prefs", getContext().MODE_PRIVATE);
        prefs.edit().clear().apply();

        // Abre la Activity de Login
        startActivity(new Intent(requireContext(), Login.class));
        requireActivity().finish(); // para que no pueda volver con atrás
    }

    private void openMapa(View view) {
        // Si ya tienes MapFragment en MainBab, podrías reemplazar el fragment en vez de abrir Activity
        startActivity(new Intent(requireContext(), com.aipasa.fragment.MapFragment.class));
        // Nota: si quieres navegación interna, esto se puede reemplazar con getParentFragmentManager().replace(...)
    }
}