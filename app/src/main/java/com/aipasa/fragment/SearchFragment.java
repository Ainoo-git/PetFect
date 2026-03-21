package com.aipasa.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.aipasa.firebase.MascotaAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerPerdidos, recyclerAdopciones;
    private MascotaAdapter adapterPerdidos, adapterAdopciones;
    private List<DocumentSnapshot> listaPerdidos = new ArrayList<>();
    private List<DocumentSnapshot> listaAdopciones = new ArrayList<>();
    private FirebaseFirestore db;
    private ListenerRegistration mascotasListener;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        db = FirebaseFirestore.getInstance();

        recyclerPerdidos = view.findViewById(R.id.recyclerPerdidosSearch);
        recyclerAdopciones = view.findViewById(R.id.recyclerAdopcionesSearch);
        searchView = view.findViewById(R.id.searchView);

        configurarRecyclerViews();
        cargarMascotas();
        configurarBusqueda();

        return view;
    }

    private void configurarRecyclerViews() {
        recyclerPerdidos.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapterPerdidos = new MascotaAdapter(listaPerdidos);
        recyclerPerdidos.setAdapter(adapterPerdidos);

        recyclerAdopciones.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapterAdopciones = new MascotaAdapter(listaAdopciones);
        recyclerAdopciones.setAdapter(adapterAdopciones);
    }

    private void cargarMascotas() {
        Query query = db.collection("mascotas").orderBy("fecha", Query.Direction.DESCENDING);
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
        });
    }

    private void configurarBusqueda() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarListas(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarListas(newText);
                return true;
            }
        });
    }

    private void filtrarListas(String texto) {
        String textoLower = texto.toLowerCase();

        List<DocumentSnapshot> filtradosPerdidos = new ArrayList<>();
        for (DocumentSnapshot doc : listaPerdidos) {
            String nombre = doc.getString("nombre");
            String tipo = doc.getString("tipo");
            if ((nombre != null && nombre.toLowerCase().contains(textoLower)) ||
                    (tipo != null && tipo.toLowerCase().contains(textoLower))) {
                filtradosPerdidos.add(doc);
            }
        }
        adapterPerdidos.actualizarLista(filtradosPerdidos);

        List<DocumentSnapshot> filtradosAdopciones = new ArrayList<>();
        for (DocumentSnapshot doc : listaAdopciones) {
            String nombre = doc.getString("nombre");
            String tipo = doc.getString("tipo");
            if ((nombre != null && nombre.toLowerCase().contains(textoLower)) ||
                    (tipo != null && tipo.toLowerCase().contains(textoLower))) {
                filtradosAdopciones.add(doc);
            }
        }
        adapterAdopciones.actualizarLista(filtradosAdopciones);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mascotasListener != null) {
            mascotasListener.remove();
        }
    }
}