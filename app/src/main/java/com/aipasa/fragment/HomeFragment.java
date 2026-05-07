package com.aipasa.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

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

    private ImageView imgPerfil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = auth.getCurrentUser();

        // Views
        sectionPerdidos = view.findViewById(R.id.sectionPerdidos);
        sectionAdopciones = view.findViewById(R.id.sectionAdopciones);
        sectionVeterinarias = view.findViewById(R.id.sectionVeterinarias);
        tvNadaSeleccionado = view.findViewById(R.id.tvNadaSeleccionado);
        tvNombreUsuario = view.findViewById(R.id.tvNombreUsuario);
        tvTituloPerdidos = view.findViewById(R.id.tvTituloPerdidos);
        tvTituloAdopciones = view.findViewById(R.id.tvTituloAdopciones);
        recyclerPerdidos = view.findViewById(R.id.recyclerPerdidos);
        recyclerAdopciones = view.findViewById(R.id.recyclerAdopciones);

        imgPerfil = view.findViewById(R.id.imgPerfil);

        configurarRecyclerViews();
        mostrarNombreUsuario();
        cargarPreferencias();
        configurarFab(view);
        configurarBotonesFiltrado(view);
        cargarFotoPerfil();

        cargarMascotas();

        return view;
    }

    // FAB sigue abriendo PublicacionActivity
    private void configurarFab(View view) {
        FloatingActionButton fabCentral = view.findViewById(R.id.fab_central);
        if (fabCentral != null) {
            fabCentral.setOnClickListener(v -> {
                // abrir actividad de publicación
                startActivity(new android.content.Intent(requireContext(), com.aipasa.main.PublicacionActivity.class));
            });
        }
    }

    private void cargarFotoPerfil() {

        if (currentUser == null) return;

        db.collection("usuarios")
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(doc -> {

                    String url = doc.getString("fotoPerfil");

                    if (url != null && getContext() != null) {

                        Glide.with(requireContext())
                                .load(url)
                                .circleCrop()
                                .into(imgPerfil);
                    }
                });
    }
    // Botones filtrarán secciones dentro del HomeFragment
    private void configurarBotonesFiltrado(View view) {
        Button btnAll = view.findViewById(R.id.btnAll);
        Button btnAdopciones = view.findViewById(R.id.btnAdopciones);
        Button btnPerdidos = view.findViewById(R.id.btnPerdidos);
        Button btnMapa = view.findViewById(R.id.btnMapa);

        btnAll.setOnClickListener(v -> mostrarSecciones(true, true, prefVeterinarias));
        btnAdopciones.setOnClickListener(v -> mostrarSecciones(false, true, false));
        btnPerdidos.setOnClickListener(v -> mostrarSecciones(true, false, false));
        btnMapa.setOnClickListener(v -> {

            Toast.makeText(requireContext(), "Abre sección Mapa", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.imgPerfil).setOnClickListener(v -> {
            if (getActivity() != null) {
                ((com.google.android.material.bottomnavigation.BottomNavigationView)
                        getActivity().findViewById(R.id.bottom_nav))
                        .setSelectedItemId(R.id.profile);
            }
        });
    }

    // Control de visibilidad de secciones según filtrado
    private void mostrarSecciones(boolean mostrarPerdidos, boolean mostrarAdopciones, boolean mostrarVeterinarias) {
        sectionPerdidos.setVisibility(mostrarPerdidos && !listaPerdidos.isEmpty() ? View.VISIBLE : View.GONE);
        sectionAdopciones.setVisibility(mostrarAdopciones && !listaAdopciones.isEmpty() ? View.VISIBLE : View.GONE);
        sectionVeterinarias.setVisibility(mostrarVeterinarias ? View.VISIBLE : View.GONE);
        mostrarMensajeSiNada();
    }

    private void configurarRecyclerViews() {
        recyclerPerdidos.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapterPerdidos = new MascotaAdapter(listaPerdidos);
        recyclerPerdidos.setAdapter(adapterPerdidos);

        recyclerAdopciones.setLayoutManager(new LinearLayoutManager(requireContext()));
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
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS, requireContext().MODE_PRIVATE);
        prefPerdidos = prefs.getBoolean("pref_perdidos", true);
        prefAdopciones = prefs.getBoolean("pref_adopciones", true);
        prefVeterinarias = prefs.getBoolean("pref_veterinarias", true);
    }

    private void cargarMascotas() {
        Query query = db.collection("mascotas")
                .orderBy("fecha", Query.Direction.DESCENDING);

        mascotasListener = query.addSnapshotListener((snapshots, error) -> {
            if (error != null) {
                Toast.makeText(requireContext(), "Error al cargar", Toast.LENGTH_SHORT).show();
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
            mostrarSecciones(prefPerdidos, prefAdopciones, prefVeterinarias);
        });
    }

    private void actualizarTitulos() {
        tvTituloPerdidos.setText("Mascotas Perdidas (" + listaPerdidos.size() + ")");
        tvTituloAdopciones.setText("Mascotas en Adopción (" + listaAdopciones.size() + ")");
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
    public void onResume() {
        super.onResume();
        cargarPreferencias();
        mostrarSecciones(prefPerdidos, prefAdopciones, prefVeterinarias);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mascotasListener != null) {
            mascotasListener.remove();
        }
    }
}