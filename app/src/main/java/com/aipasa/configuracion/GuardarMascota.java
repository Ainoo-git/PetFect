package com.aipasa.configuracion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.aipasa.firebase.MascotaAdapterGuardados;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GuardarMascota extends Fragment {

    private RecyclerView recyclerGuardados;

    private MascotaAdapterGuardados adapter;

    private FirebaseFirestore db;

    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_guardar_mascota,
                container,
                false
        );

        recyclerGuardados = view.findViewById(R.id.recyclerGuardados);

        recyclerGuardados.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        adapter = new MascotaAdapterGuardados(
                new ArrayList<>()
        );

        recyclerGuardados.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();

        cargarGuardados();

        return view;
    }

    private void cargarGuardados() {

        String uid = auth.getCurrentUser().getUid();

        db.collection("usuarios")
                .document(uid)
                .collection("guardados")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    adapter.actualizarLista(
                            queryDocumentSnapshots.getDocuments()
                    );
                });
    }
}