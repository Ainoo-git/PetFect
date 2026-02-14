package com.aipasa.repository;

import android.content.Context;

import com.aipasa.data_Room.Mascota;
import com.aipasa.data_Room.MascotaDAO;
import com.aipasa.data_Room.MascotaDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MascotaRepository {

    private FirebaseFirestore firestore;
    private MascotaDAO mascotaDAO;
    private Executor executor = Executors.newSingleThreadExecutor();

    public MascotaRepository(Context context) {
        firestore = FirebaseFirestore.getInstance();
        MascotaDatabase db = MascotaDatabase.getInstance(context);
        mascotaDAO = db.mascotaDAO();
    }

    public void sincronizarMascotas(Callback<List<Mascota>> callback) {

        firestore.collection("mascotas")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    List<Mascota> lista = new ArrayList<>();

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Mascota mascota = doc.toObject(Mascota.class);
                        lista.add(mascota);
                    }

                    executor.execute(() -> {
                        mascotaDAO.deleteAll();
                        mascotaDAO.insertAll(lista);
                    });

                    callback.onResult(lista);
                })
                .addOnFailureListener(e -> {

                    executor.execute(() -> {
                        List<Mascota> local = mascotaDAO.getAll();
                        callback.onResult(local);
                    });
                });
    }

    public interface Callback<T> {
        void onResult(T data);
    }
}
