package com.aipasa.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.aipasa.firebase.MascotaAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private ListenerRegistration mascotasListener;

    private List<DocumentSnapshot> listaPerdidos = new ArrayList<>();
    private List<DocumentSnapshot> listaAdopciones = new ArrayList<>();

    private MascotaAdapter adapterPerdidos;
    private MascotaAdapter adapterAdopciones;

    private LinearLayout sectionPerdidos, sectionAdopciones, sectionVeterinarias;
    private TextView tvNadaSeleccionado, tvNombreUsuario;
    private TextView tvTituloPerdidos, tvTituloAdopciones;

    private RecyclerView recyclerPerdidos, recyclerAdopciones;

    private boolean prefPerdidos, prefAdopciones, prefVeterinarias;
    private static final String PREFS = "petfect_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = auth.getCurrentUser();

        sectionPerdidos = findViewById(R.id.sectionPerdidos);
        sectionAdopciones = findViewById(R.id.sectionAdopciones);
        sectionVeterinarias = findViewById(R.id.sectionVeterinarias);
        tvNadaSeleccionado = findViewById(R.id.tvNadaSeleccionado);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvTituloPerdidos = findViewById(R.id.tvTituloPerdidos);
        tvTituloAdopciones = findViewById(R.id.tvTituloAdopciones);
        recyclerPerdidos = findViewById(R.id.recyclerPerdidos);
        recyclerAdopciones = findViewById(R.id.recyclerAdopciones);

        configurarRecyclerViews();
        mostrarNombreUsuario();
        cargarPreferencias();
        configurarFab();
        configurarBotones();

        cargarMascotas();
    }

    private void configurarFab() {
        FloatingActionButton fabCentral = findViewById(R.id.fab_central);
        if (fabCentral != null) {
            fabCentral.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, PublicacionActivity.class);
                startActivity(intent);
            });
        }
    }

    private void configurarBotones() {
        Button btnAll = findViewById(R.id.btnAll);
        Button btnAdopciones = findViewById(R.id.btnAdopciones);
        Button btnPerdidos = findViewById(R.id.btnPerdidos);
        Button btnMapa = findViewById(R.id.btnMapa);

        btnAll.setOnClickListener(v -> mostrarAll());
        btnAdopciones.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AdopcionesActivity.class)));
        btnPerdidos.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PerdidosActivity.class)));
        btnMapa.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MapaActivity.class)));

        findViewById(R.id.imgPerfil).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Profile.class);
            startActivity(intent);
        });
    }

    private void configurarRecyclerViews() {
        recyclerPerdidos.setLayoutManager(new LinearLayoutManager(this));
        adapterPerdidos = new MascotaAdapter(listaPerdidos);
        recyclerPerdidos.setAdapter(adapterPerdidos);

        recyclerAdopciones.setLayoutManager(new LinearLayoutManager(this));
        adapterAdopciones = new MascotaAdapter(listaAdopciones);
        recyclerAdopciones.setAdapter(adapterAdopciones);
    }

    private void mostrarNombreUsuario() {
        if (currentUser != null) {
            String email = currentUser.getEmail();
            String nombre = email != null ? email.split("@")[0] : "Usuario";
            tvNombreUsuario.setText("¡Hola, " + nombre + "!");
        } else {
            tvNombreUsuario.setText("¡Hola, Invitado!");
        }
    }

    private void cargarPreferencias() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        prefPerdidos = prefs.getBoolean("pref_perdidos", true);
        prefAdopciones = prefs.getBoolean("pref_adopciones", true);
        prefVeterinarias = prefs.getBoolean("pref_veterinarias", true);
    }

    private void cargarMascotas() {
        Query query = db.collection("mascotas")
                .orderBy("fecha", Query.Direction.DESCENDING);

        mascotasListener = query.addSnapshotListener((snapshots, error) -> {
            if (error != null) {
                Toast.makeText(this, "Error al cargar", Toast.LENGTH_SHORT).show();
                return;
            }

            if (snapshots == null) return;

            listaPerdidos.clear();
            listaAdopciones.clear();

            for (DocumentSnapshot doc : snapshots.getDocuments()) {
                String estado = doc.getString("estado");
                if ("perdido".equals(estado)) {
                    listaPerdidos.add(doc);
                } else if ("adopcion".equals(estado)) {
                    listaAdopciones.add(doc);
                }
            }

            adapterPerdidos.notifyDataSetChanged();
            adapterAdopciones.notifyDataSetChanged();

            actualizarTitulos();
            actualizarVisibilidadSecciones();
        });
    }

    private void actualizarTitulos() {
        tvTituloPerdidos.setText("Mascotas Perdidas (" + listaPerdidos.size() + ")");
        tvTituloAdopciones.setText("Mascotas en Adopción (" + listaAdopciones.size() + ")");
    }

    private void actualizarVisibilidadSecciones() {
        sectionPerdidos.setVisibility(prefPerdidos && !listaPerdidos.isEmpty() ? View.VISIBLE : View.GONE);
        sectionAdopciones.setVisibility(prefAdopciones && !listaAdopciones.isEmpty() ? View.VISIBLE : View.GONE);
        sectionVeterinarias.setVisibility(prefVeterinarias ? View.VISIBLE : View.GONE);
        mostrarMensajeSiNada();
    }

    private void mostrarAll() {
        actualizarVisibilidadSecciones();
    }

    private void mostrarMensajeSiNada() {
        boolean nadaVisible = sectionPerdidos.getVisibility() != View.VISIBLE &&
                sectionAdopciones.getVisibility() != View.VISIBLE &&
                sectionVeterinarias.getVisibility() != View.VISIBLE;

        tvNadaSeleccionado.setVisibility(nadaVisible ? View.VISIBLE : View.GONE);

        if (nadaVisible) {
            if (listaPerdidos.isEmpty() && listaAdopciones.isEmpty()) {
                tvNadaSeleccionado.setText("No hay mascotas publicadas");
            } else {
                tvNadaSeleccionado.setText("No hay contenido visible según tus preferencias");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPreferencias();
        actualizarVisibilidadSecciones();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mascotasListener != null) {
            mascotasListener.remove();
        }
    }
}