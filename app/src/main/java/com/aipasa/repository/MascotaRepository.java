package com.aipasa.repository;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MascotaRepository {

    private FirebaseFirestore db;

    public MascotaRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void obtenerMascotas(String estado,
                                OnSuccessListener<QuerySnapshot> success) {

        db.collection("mascotas")
                .whereEqualTo("estado", estado)
                .get()
                .addOnSuccessListener(success);
    }
}
