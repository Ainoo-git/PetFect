package com.aipasa.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.aipasa.auth.Login;
import com.aipasa.creditos.Creditos;
import com.aipasa.firebase.MascotaAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    private RecyclerView rvMascotas;
    private MascotaAdapter adapter;
    private List<DocumentSnapshot> listaMascotas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        // Botón configuración
        Button btnConfiguracion = findViewById(R.id.btnConfiguracion);
        btnConfiguracion.setOnClickListener(v ->
                startActivity(new Intent(this, Creditos.class))
        );

        // Toolbar back
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Nombre usuario
        TextView tvNombre = findViewById(R.id.nombre2);
        SharedPreferences prefs = getSharedPreferences("petfect_prefs", MODE_PRIVATE);
        String username = prefs.getString("username", "Nombre");
        tvNombre.setText(username);

        // RECYCLER VIEW
        rvMascotas = findViewById(R.id.rvMascotas);
        rvMascotas.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MascotaAdapter(listaMascotas);
        rvMascotas.setAdapter(adapter);

        // Cargar mascotas del usuario
        cargarMisMascotas();
    }

    // SOLO MASCOTAS DEL USUARIO
    private void cargarMisMascotas() {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("mascotas")
                .whereEqualTo("userId", uid)
                .orderBy("fecha", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, error) -> {

                    if (error != null) {
                        android.widget.Toast.makeText(this, "Error cargando", android.widget.Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (snapshots == null) return;

                    listaMascotas.clear();

                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        listaMascotas.add(doc);
                    }

                    adapter.notifyDataSetChanged();
                });
    }
    // Cerrar sesión
    public void openLogin2(android.view.View view) {
        FirebaseAuth.getInstance().signOut();

        SharedPreferences prefs = getSharedPreferences("petfect_prefs", MODE_PRIVATE);
        prefs.edit().clear().apply();

        startActivity(new Intent(this, Login.class));
        finish();
    }

    public void openMapa(android.view.View view) {
        Intent intent = new Intent(this, MapaActivity.class);
        startActivity(intent);
    }
}