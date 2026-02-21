package com.aipasa.repository;

import com.aipasa.firebase.Mascota;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentReference;

public class MascotaRepository {

    private FirebaseFirestore db;

    public MascotaRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void obtenerMascotas(String estado, OnSuccessListener<QuerySnapshot> success) {
        db.collection("mascotas")
                .whereEqualTo("estado", estado)
                .get()
                .addOnSuccessListener(success);
    }

    public void guardarMascota(Mascota mascota, OnSuccessListener<Void> success) {
        DocumentReference docRef = db.collection("mascotas").document();
        mascota.setId(docRef.getId());
        docRef.set(mascota)
                .addOnSuccessListener(success);
    }
}