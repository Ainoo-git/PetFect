package com.aipasa.main;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.aipasa.firebase.MascotaAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdopcionesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MascotaAdapter adapter;
    private List<DocumentSnapshot> lista = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopciones);

        recyclerView = findViewById(R.id.recyclerMascotas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MascotaAdapter(lista);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        cargarAdopciones();
    }

    private void cargarAdopciones() {
        db.collection("mascotas")
                .whereEqualTo("estado", "adopcion")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    lista.clear();
                    lista.addAll(queryDocumentSnapshots.getDocuments());
                    adapter.notifyDataSetChanged();

                    if (lista.isEmpty()) {
                        Toast.makeText(this, "No hay mascotas en adopción", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al cargar", Toast.LENGTH_SHORT).show());
    }
}