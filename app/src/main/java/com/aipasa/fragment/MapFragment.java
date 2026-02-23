package com.aipasa.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aipasa.R;

public class MapFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflamos el layout del fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Configurar EdgeToEdge como estaba en la activity
//        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.mapaFragmentRoot),
//                (v, insets) -> {
//                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//                    return insets;
//                });

        return view;
    }
}