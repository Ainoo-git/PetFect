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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class TarjetaFragment extends DialogFragment {

    private ImageView imgAccion;
    private TextView txtTitulo, txtFecha, txtEdad, txtChip, txtTelefono, txtInfoAdicional;
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

        txtEdad = view.findViewById(R.id.txtEdad);
        txtChip = view.findViewById(R.id.txtChip);
        txtTelefono = view.findViewById(R.id.txtTelefono);
        txtInfoAdicional = view.findViewById(R.id.txtInfoAdicional);

        topAppBar = view.findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(v -> dismiss());

        btnVerMas = view.findViewById(R.id.btnVerMas);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        String articuloId = null;

        if (getArguments() != null) {
            articuloId = getArguments().getString(ARG_ARTICULO_ID);
        }

        if (articuloId == null || articuloId.isEmpty()) {
            Toast.makeText(
                    getContext(),
                    getString(R.string.no_se_recibio_publicacion),
                    Toast.LENGTH_SHORT
            ).show();
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
                        Toast.makeText(
                                getContext(),
                                getString(R.string.publicacion_no_existe),
                                Toast.LENGTH_SHORT
                        ).show();
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

                    txtTitulo.setText(
                            nombre != null && !nombre.isEmpty()
                                    ? nombre
                                    : getString(R.string.sin_nombre)
                    );

                    String tipoTraducido = traducirTipo(tipo);
                    String estadoTraducido = traducirEstado(estado);

                    String tipoEstado = "";

                    if (!tipoTraducido.isEmpty()) {
                        tipoEstado += tipoTraducido;
                    }

                    if (!estadoTraducido.isEmpty()) {
                        if (!tipoEstado.isEmpty()) tipoEstado += " • ";
                        tipoEstado += estadoTraducido;
                    }

                    txtFecha.setText(tipoEstado);

                    txtEdad.setText(
                            getString(
                                    R.string.edad_label,
                                    edad != null && !edad.isEmpty()
                                            ? edad
                                            : getString(R.string.no_especificada)
                            )
                    );

                    txtChip.setText(
                            getString(
                                    R.string.chip_label,
                                    chip != null && !chip.isEmpty()
                                            ? chip
                                            : getString(R.string.no_especificado)
                            )
                    );

                    txtTelefono.setText(
                            getString(
                                    R.string.telefono_label,
                                    telefono != null && !telefono.isEmpty()
                                            ? telefono
                                            : getString(R.string.no_disponible)
                            )
                    );

                    txtInfoAdicional.setText(
                            getString(
                                    R.string.info_adicional_label,
                                    info != null && !info.isEmpty()
                                            ? info
                                            : getString(R.string.sin_informacion)
                            )
                    );

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
                            Toast.makeText(
                                    getContext(),
                                    getString(R.string.guardado),
                                    Toast.LENGTH_SHORT
                            ).show()
                    );

                    btnVerMas.setOnClickListener(v -> {
                        if (telefono != null && !telefono.isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + telefono));
                            startActivity(intent);
                        } else {
                            Toast.makeText(
                                    getContext(),
                                    getString(R.string.no_hay_numero_contacto),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });

                })
                .addOnFailureListener(e ->
                        Toast.makeText(
                                getContext(),
                                getString(R.string.error_cargar_publicacion),
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }

    private String traducirTipo(String tipo) {
        if (tipo == null) return "";

        switch (tipo.toLowerCase(Locale.ROOT)) {
            case "perro":
                return getString(R.string.tipo_perro);
            case "gato":
                return getString(R.string.tipo_gato);
            case "otro":
                return getString(R.string.tipo_otro);
            default:
                return tipo;
        }
    }

    private String traducirEstado(String estado) {
        if (estado == null) return "";

        switch (estado.toLowerCase(Locale.ROOT)) {
            case "perdido":
                return getString(R.string.estado_perdido);
            case "adopcion":
            case "adopción":
                return getString(R.string.estado_adopcion);
            case "encontrado":
                return getString(R.string.estado_encontrado);
            default:
                return estado;
        }
    }
}