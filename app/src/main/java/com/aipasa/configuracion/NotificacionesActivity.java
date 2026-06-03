package com.aipasa.configuracion;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.aipasa.firebase.NotificacionModel;
import com.aipasa.firebase.NotificacionesAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class NotificacionesActivity extends AppCompatActivity {

    private RecyclerView recyclerNotificaciones;
    private NotificacionesAdapter adapter;
    private List<NotificacionModel> listaNotificaciones;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private final ColorDrawable fondoRojo = new ColorDrawable(
            Color.rgb(220, 53, 69)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        configurarToolbar();
        inicializarFirebase();
        inicializarRecycler();
        configurarEliminarArrastrando();
        cargarNotificaciones();
    }

    private void configurarToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void inicializarFirebase() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    private void inicializarRecycler() {
        recyclerNotificaciones = findViewById(R.id.recyclerNotificaciones);

        listaNotificaciones = new ArrayList<>();

        adapter = new NotificacionesAdapter(
                this,
                listaNotificaciones
        );

        recyclerNotificaciones.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerNotificaciones.setAdapter(adapter);
    }

    private void cargarNotificaciones() {
        FirebaseUser usuarioActual = auth.getCurrentUser();

        if (usuarioActual == null) {
            Toast.makeText(
                    this,
                    "Usuario no encontrado",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String uidActual = usuarioActual.getUid();

        db.collection("notificaciones")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(
                                this,
                                "Error cargando notificaciones: " + error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                        return;
                    }

                    if (value == null) {
                        return;
                    }

                    listaNotificaciones.clear();

                    for (DocumentSnapshot doc : value.getDocuments()) {
                        String idUsuarioPublicador = doc.getString("idUsuario");

                        if (idUsuarioPublicador != null && idUsuarioPublicador.equals(uidActual)) {
                            continue;
                        }

                        List<String> eliminadaPor = (List<String>) doc.get("eliminadaPor");

                        if (eliminadaPor != null && eliminadaPor.contains(uidActual)) {
                            continue;
                        }

                        NotificacionModel notificacion =
                                doc.toObject(NotificacionModel.class);

                        if (notificacion != null) {
                            notificacion.setId(doc.getId());
                            listaNotificaciones.add(notificacion);
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }

    private void configurarEliminarArrastrando() {
        ItemTouchHelper.SimpleCallback callback =
                new ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                ) {
                    @Override
                    public boolean onMove(
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            @NonNull RecyclerView.ViewHolder target
                    ) {
                        return false;
                    }

                    @Override
                    public void onSwiped(
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            int direction
                    ) {
                        int posicion = viewHolder.getAdapterPosition();

                        if (posicion == RecyclerView.NO_POSITION) {
                            return;
                        }

                        NotificacionModel notificacion =
                                listaNotificaciones.get(posicion);

                        ocultarNotificacionParaMi(notificacion, posicion);
                    }

                    @Override
                    public void onChildDraw(
                            @NonNull Canvas canvas,
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX,
                            float dY,
                            int actionState,
                            boolean isCurrentlyActive
                    ) {
                        View itemView = viewHolder.itemView;

                        if (dX > 0) {
                            fondoRojo.setBounds(
                                    itemView.getLeft(),
                                    itemView.getTop(),
                                    itemView.getLeft() + Math.round(dX),
                                    itemView.getBottom()
                            );
                        } else if (dX < 0) {
                            fondoRojo.setBounds(
                                    itemView.getRight() + Math.round(dX),
                                    itemView.getTop(),
                                    itemView.getRight(),
                                    itemView.getBottom()
                            );
                        } else {
                            fondoRojo.setBounds(0, 0, 0, 0);
                        }

                        fondoRojo.draw(canvas);

                        super.onChildDraw(
                                canvas,
                                recyclerView,
                                viewHolder,
                                dX,
                                dY,
                                actionState,
                                isCurrentlyActive
                        );
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerNotificaciones);
    }

    private void ocultarNotificacionParaMi(
            NotificacionModel notificacion,
            int posicion
    ) {
        FirebaseUser usuarioActual = auth.getCurrentUser();

        if (usuarioActual == null) {
            adapter.notifyItemChanged(posicion);
            return;
        }

        if (notificacion.getId() == null || notificacion.getId().isEmpty()) {
            adapter.notifyItemChanged(posicion);
            return;
        }

        String uidActual = usuarioActual.getUid();

        db.collection("notificaciones")
                .document(notificacion.getId())
                .update("eliminadaPor", FieldValue.arrayUnion(uidActual))
                .addOnSuccessListener(unused -> {
                    if (posicion >= 0 && posicion < listaNotificaciones.size()) {
                        listaNotificaciones.remove(posicion);
                        adapter.notifyItemRemoved(posicion);
                        adapter.notifyItemRangeChanged(posicion, listaNotificaciones.size());
                    }

                    Toast.makeText(
                            this,
                            "Notificación eliminada",
                            Toast.LENGTH_SHORT
                    ).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(
                            this,
                            "No se pudo eliminar la notificación",
                            Toast.LENGTH_SHORT
                    ).show();

                    adapter.notifyItemChanged(posicion);
                });
    }
}