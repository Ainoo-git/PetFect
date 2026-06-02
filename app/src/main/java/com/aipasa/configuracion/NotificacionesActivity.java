package com.aipasa.configuracion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.aipasa.firebase.NotificacionesAdapter;
import com.aipasa.firebase.NotificacionModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class NotificacionesActivity
        extends AppCompatActivity {

    private RecyclerView recyclerNotificaciones;

    private NotificacionesAdapter adapter;

    private List<NotificacionModel> listaNotificaciones;

    private FirebaseFirestore db;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_notificaciones
        );

        //========nuevo========
        configurarToolbar();
        //=====================

        recyclerNotificaciones =
                findViewById(
                        R.id.recyclerNotificaciones
                );

        listaNotificaciones =
                new ArrayList<>();

        adapter =
                new NotificacionesAdapter(
                        this,
                        listaNotificaciones
                );

        recyclerNotificaciones.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerNotificaciones.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();

        cargarNotificaciones();
    }
    //===================nuevo==================
    private void configurarToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());
    }
    //===========================================
    private void cargarNotificaciones() {

        FirebaseUser usuario =
                auth.getCurrentUser();

        if (usuario == null) return;

        String uid = usuario.getUid();

        db.collection("notificaciones")
                .whereEqualTo(
                        "idUsuario",
                        uid
                )
                .orderBy(
                        "fecha",
                        Query.Direction.DESCENDING
                )
                .addSnapshotListener(
                        (value, error) -> {

                            if (error != null
                                    || value == null) {
                                return;
                            }

                            listaNotificaciones.clear();

                            for (DocumentSnapshot doc
                                    : value.getDocuments()) {

                                NotificacionModel notif =
                                        doc.toObject(
                                                NotificacionModel.class
                                        );

                                if (notif != null) {

                                    notif.setId(
                                            doc.getId()
                                    );

                                    listaNotificaciones.add(
                                            notif
                                    );
                                }
                            }

                            adapter.notifyDataSetChanged();
                        });
    }
}