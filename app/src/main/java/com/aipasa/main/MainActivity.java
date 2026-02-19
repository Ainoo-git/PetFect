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
import com.aipasa.add.AddMascotaActivity;
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

    // Firebase
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private ListenerRegistration mascotasListener;

    // Datos
    private List<DocumentSnapshot> listaPerdidos = new ArrayList<>();
    private List<DocumentSnapshot> listaAdopciones = new ArrayList<>();

    // Adaptadores
    private MascotaAdapter adapterPerdidos;
    private MascotaAdapter adapterAdopciones;

    // Secciones
    private LinearLayout sectionPerdidos, sectionAdopciones, sectionVeterinarias;
    private TextView tvNadaSeleccionado, tvNombreUsuario;
    private TextView tvTituloPerdidos, tvTituloAdopciones;

    // RecyclerViews
    private RecyclerView recyclerPerdidos, recyclerAdopciones;

    // Preferencias
    private boolean prefPerdidos, prefAdopciones, prefVeterinarias;
    private static final String PREFS = "petfect_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = auth.getCurrentUser();

        // Referencias del layout
        sectionPerdidos = findViewById(R.id.sectionPerdidos);
        sectionAdopciones = findViewById(R.id.sectionAdopciones);
        sectionVeterinarias = findViewById(R.id.sectionVeterinarias);
        tvNadaSeleccionado = findViewById(R.id.tvNadaSeleccionado);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvTituloPerdidos = findViewById(R.id.tvTituloPerdidos);
        tvTituloAdopciones = findViewById(R.id.tvTituloAdopciones);
        recyclerPerdidos = findViewById(R.id.recyclerPerdidos);
        recyclerAdopciones = findViewById(R.id.recyclerAdopciones);

        // Configurar RecyclerViews
        configurarRecyclerViews();

        // Mostrar nombre del usuario
        mostrarNombreUsuario();

        // Cargar preferencias
        cargarPreferencias();

        // Configurar FAB
        FloatingActionButton fabCentral = findViewById(R.id.fab_central);
        if (fabCentral != null) {
            fabCentral.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AddMascotaActivity.class);
                startActivity(intent);
            });
        }

        // Botones de navegación
        Button btnAll = findViewById(R.id.btnAll);
        Button btnAdopciones = findViewById(R.id.btnAdopciones);
        Button btnPerdidos = findViewById(R.id.btnPerdidos);
        Button btnMapa = findViewById(R.id.btnMapa);

        btnAll.setOnClickListener(v -> mostrarAll());
        btnAdopciones.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AdopcionesActivity.class)));
        btnPerdidos.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PerdidosActivity.class)));
        btnMapa.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MapaActivity.class)));

        // Click en imagen de perfil (puede abrir preferencias o perfil)
        findViewById(R.id.imgPerfil).setOnClickListener(v -> {
            // Si quieres abrir preferencias:
            Intent intent = new Intent(MainActivity.this, PreferenciasActivity.class);
            startActivity(intent);

            // O si quieres abrir perfil:
            // OpenProfile(v);
        });

        // Cargar mascotas
        cargarMascotas();
    }

    private void configurarRecyclerViews() {
        if (recyclerPerdidos != null) {
            recyclerPerdidos.setLayoutManager(new LinearLayoutManager(this));
            adapterPerdidos = new MascotaAdapter(listaPerdidos);
            recyclerPerdidos.setAdapter(adapterPerdidos);
        }

        if (recyclerAdopciones != null) {
            recyclerAdopciones.setLayoutManager(new LinearLayoutManager(this));
            adapterAdopciones = new MascotaAdapter(listaAdopciones);
            recyclerAdopciones.setAdapter(adapterAdopciones);
        }
    }

    private void mostrarNombreUsuario() {
        if (tvNombreUsuario != null) {
            if (currentUser != null) {
                String email = currentUser.getEmail();
                String nombre = email != null ? email.split("@")[0] : "Usuario";
                tvNombreUsuario.setText("¡Hola, " + nombre + "!");
            } else {
                tvNombreUsuario.setText("¡Hola, Invitado!");
            }
        }
    }

    private void cargarPreferencias() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        prefPerdidos = prefs.getBoolean("pref_perdidos", true); // true por defecto
        prefAdopciones = prefs.getBoolean("pref_adopciones", true);
        prefVeterinarias = prefs.getBoolean("pref_veterinarias", true);
    }

    private void cargarMascotas() {
        Query query = db.collection("mascotas")
                .orderBy("fecha", Query.Direction.DESCENDING);

        mascotasListener = query.addSnapshotListener((snapshots, error) -> {
            if (error != null) {
                Toast.makeText(this, "Error al cargar: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            listaPerdidos.clear();
            listaAdopciones.clear();

            if (snapshots != null) {
                for (DocumentSnapshot doc : snapshots.getDocuments()) {
                    String estado = doc.getString("estado");
                    if ("perdido".equals(estado)) {
                        listaPerdidos.add(doc);
                    } else if ("adopcion".equals(estado)) {
                        listaAdopciones.add(doc);
                    }
                }
            }

            if (adapterPerdidos != null) adapterPerdidos.notifyDataSetChanged();
            if (adapterAdopciones != null) adapterAdopciones.notifyDataSetChanged();

            actualizarTitulos();
            actualizarVisibilidadSecciones();
        });
    }

    private void actualizarTitulos() {
        if (tvTituloPerdidos != null) {
            tvTituloPerdidos.setText("Mascotas Perdidas (" + listaPerdidos.size() + ")");
        }
        if (tvTituloAdopciones != null) {
            tvTituloAdopciones.setText("Mascotas en Adopción (" + listaAdopciones.size() + ")");
        }
    }

    private void actualizarVisibilidadSecciones() {
        if (sectionPerdidos != null) {
            boolean hayPerdidos = !listaPerdidos.isEmpty();
            sectionPerdidos.setVisibility(prefPerdidos && hayPerdidos ? View.VISIBLE : View.GONE);
        }

        if (sectionAdopciones != null) {
            boolean hayAdopciones = !listaAdopciones.isEmpty();
            sectionAdopciones.setVisibility(prefAdopciones && hayAdopciones ? View.VISIBLE : View.GONE);
        }

        if (sectionVeterinarias != null) {
            sectionVeterinarias.setVisibility(prefVeterinarias ? View.VISIBLE : View.GONE);
        }

        mostrarMensajeSiNada();
    }

    private void mostrarAll() {
        actualizarVisibilidadSecciones();
    }

    private void mostrarMensajeSiNada() {
        boolean nadaVisible = true;

        if (sectionPerdidos != null && sectionPerdidos.getVisibility() == View.VISIBLE) nadaVisible = false;
        if (sectionAdopciones != null && sectionAdopciones.getVisibility() == View.VISIBLE) nadaVisible = false;
        if (sectionVeterinarias != null && sectionVeterinarias.getVisibility() == View.VISIBLE) nadaVisible = false;

        if (tvNadaSeleccionado != null) {
            tvNadaSeleccionado.setVisibility(nadaVisible ? View.VISIBLE : View.GONE);
            if (nadaVisible) {
                if (listaPerdidos.isEmpty() && listaAdopciones.isEmpty()) {
                    tvNadaSeleccionado.setText("No hay mascotas publicadas");
                } else {
                    tvNadaSeleccionado.setText("No hay contenido visible según tus preferencias");
                }
            }
        }
    }

    // Métodos de navegación
    public void OpenProfile(View view) {
        if (currentUser != null) {
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
        }
    }

    public void openPerdidos(View view) {
        startActivity(new Intent(this, PerdidosActivity.class));
    }

    public void openAdopcion(View view) {
        startActivity(new Intent(this, AdopcionesActivity.class));
    }

    public void openMapa(View view) {
        startActivity(new Intent(this, MapaActivity.class));
    }

    // Método para abrir preferencias (puedes llamarlo desde un botón)
    public void openPreferencias(View view) {
        Intent intent = new Intent(this, PreferenciasActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar preferencias cuando volvemos de PreferenciasActivity
        cargarPreferencias();
        actualizarVisibilidadSecciones();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth != null) {
            currentUser = auth.getCurrentUser();
            mostrarNombreUsuario();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mascotasListener != null) {
            mascotasListener.remove();
        }
    }
}