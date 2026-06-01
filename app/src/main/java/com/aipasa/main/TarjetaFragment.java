package com.aipasa.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.aipasa.R;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TarjetaFragment extends DialogFragment {

    private ImageView imgAccion;
    private TextView txtTitulo, txtFecha, txtDescripcion;
    private Button btnVerMas;
    private ImageButton btnGuardar;
    private MaterialToolbar topAppBar;

    private static final String ARG_ARTICULO_ID = "ARTICULO_ID";

    public static TarjetaFragment newInstance(String articuloId) {
        TarjetaFragment fragment = new TarjetaFragment();

        Bundle args = new Bundle();
        args.putString(ARG_ARTICULO_ID, articuloId);

        fragment.setArguments(args);

        return fragment;
    }

    // 🔥 ESTO ES CLAVE: estilo fullscreen
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tarjeta, container, false);

        imgAccion = view.findViewById(R.id.imgAccion);
        txtTitulo = view.findViewById(R.id.txtTitulo);
        txtFecha = view.findViewById(R.id.txtFecha);
        txtDescripcion = view.findViewById(R.id.txtDescripcion);

        topAppBar = view.findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(v -> {
            dismiss(); // cierra el dialog correctamente
        });

        btnVerMas = view.findViewById(R.id.btnVerMas);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        String articuloId = null;

        if (getArguments() != null) {
            articuloId = getArguments().getString(ARG_ARTICULO_ID);
        }

        if (articuloId == null || articuloId.isEmpty()) {
            Toast.makeText(getContext(), "No se recibió la publicación", Toast.LENGTH_SHORT).show();
            return view;
        }

        cargarMascota(articuloId);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
        }
    }

    private void cargarMascota(String id) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("mascotas")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (!documentSnapshot.exists()) {
                        Toast.makeText(getContext(), "La publicación no existe", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String nombre = documentSnapshot.getString("nombre");
                    String tipo = documentSnapshot.getString("tipo");
                    String estado = documentSnapshot.getString("estado");
                    String edad = documentSnapshot.getString("edad");
                    String chip = documentSnapshot.getString("chip");
                    String telefono = documentSnapshot.getString("telefono");
                    String info = documentSnapshot.getString("infoAdicional");
                    String fotoUrl = documentSnapshot.getString("fotoUrl");

                    txtTitulo.setText(nombre != null ? nombre : "Sin nombre");

                    String tipoEstado = "";

                    if (tipo != null && !tipo.isEmpty()) {
                        tipoEstado += tipo.substring(0, 1).toUpperCase() + tipo.substring(1);
                    }

                    if (estado != null && !estado.isEmpty()) {
                        if (!tipoEstado.isEmpty()) tipoEstado += " • ";
                        tipoEstado += estado.substring(0, 1).toUpperCase() + estado.substring(1);
                    }

                    txtFecha.setText(tipoEstado);

                    String descripcion =
                            "Edad: " + (edad != null ? edad : "No especificada") +
                                    "\n\nChip: " + (chip != null ? chip : "No especificado") +
                                    "\n\nTeléfono: " + (telefono != null ? telefono : "No disponible") +
                                    "\n\nInfo adicional: " + (info != null ? info : "Sin información");

                    txtDescripcion.setText(descripcion);

                    if (fotoUrl != null && !fotoUrl.isEmpty()) {
                        Glide.with(this)
                                .load(fotoUrl)
                                .placeholder(R.drawable.logologin)
                                .error(R.drawable.logologin)
                                .centerCrop()
                                .into(imgAccion);
                    } else {
                        imgAccion.setImageResource(R.drawable.logologin);
                    }

                    btnGuardar.bringToFront();

                    btnGuardar.setOnClickListener(v ->
                            Toast.makeText(getContext(), "Guardado", Toast.LENGTH_SHORT).show()
                    );

                    btnVerMas.setOnClickListener(v -> {
                        if (telefono != null && !telefono.isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + telefono));
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(),
                                    "No hay número de contacto disponible",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(),
                                "Error al cargar la publicación",
                                Toast.LENGTH_SHORT).show()
                );
    }
}