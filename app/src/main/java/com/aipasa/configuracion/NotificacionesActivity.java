package com.aipasa.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.aipasa.add.NotificacionesAdapter;
import com.aipasa.firebase.NotificacionModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class NotificacionesActivity extends AppCompatActivity {

    private RecyclerView recyclerNotificaciones;

    private NotificacionesAdapter adapter;
    private List<NotificacionModel> listaNotificaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        recyclerNotificaciones = findViewById(R.id.recyclerNotificaciones);

        listaNotificaciones = new ArrayList<>();

        adapter = new NotificacionesAdapter(this, listaNotificaciones);

        recyclerNotificaciones.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerNotificaciones.setAdapter(adapter);

        cargarNotificaciones();
    }

    private void cargarNotificaciones() {

        FirebaseFirestore.getInstance()
                .collection("notificaciones")
                .orderBy("fecha", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {

                    if (error != null || value == null) return;

                    listaNotificaciones.clear();

                    for (DocumentSnapshot doc : value.getDocuments()) {

                        NotificacionModel notif =
                                doc.toObject(NotificacionModel.class);

                        if (notif != null) {
                            notif.setId(doc.getId());
                            listaNotificaciones.add(notif);
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }
}