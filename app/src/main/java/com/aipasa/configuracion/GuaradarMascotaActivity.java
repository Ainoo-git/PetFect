package com.aipasa.configuracion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.aipasa.firebase.MascotaAdapterGuardados;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GuaradarMascotaActivity extends AppCompatActivity {

    private RecyclerView recyclerGuardados;
    private MascotaAdapterGuardados adapter;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_guardar_mascota);

        configurarToolbar();

        recyclerGuardados = findViewById(R.id.recyclerGuardados);

        recyclerGuardados.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new MascotaAdapterGuardados(
                new ArrayList<>()
        );

        recyclerGuardados.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        cargarGuardados();
    }

    private void configurarToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());
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