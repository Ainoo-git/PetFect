package com.aipasa.configuracion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.aipasa.firebase.Notificacion;
import com.aipasa.firebase.NotificacionesAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class NotificacionActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private NotificacionesAdapter adapter;
    private List<Notificacion> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        recycler = findViewById(R.id.recyclerNotificaciones);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        lista = new ArrayList<>();

        adapter = new NotificacionesAdapter(this, lista);

        recycler.setAdapter(adapter);

        cargarNotificaciones();
    }

    private void cargarNotificaciones() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("notificaciones")
                .orderBy("fecha", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {

                    if (value == null) return;

                    lista.clear();

                    for (var doc : value.getDocuments()) {

                        Notificacion n = doc.toObject(Notificacion.class);

                        if (n != null) {
                            lista.add(n);
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }
}