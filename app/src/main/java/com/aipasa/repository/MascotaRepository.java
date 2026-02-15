package com.aipasa.repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MascotaRepository {

    private FirebaseFirestore db;

    public MascotaRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void guardarMascota(Map<String, Object> mascota,
                               OnSuccessListener<DocumentReference> success,
                               OnFailureListener failure) {

        db.collection("mascotas")
                .add(mascota)
                .addOnSuccessListener(success)
                .addOnFailureListener(failure);
    }
}
