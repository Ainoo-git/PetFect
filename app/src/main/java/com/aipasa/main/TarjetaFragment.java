package com.aipasa.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.BitmapDescriptor;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
    private TextView txtTitulo;
    private TextView txtFecha;
    private TextView txtEdad;
    private TextView txtChip;
    private TextView txtTelefono;
    private TextView txtInfoAdicional;
    private TextView txtMostrarMasInfo;

    private Button btnVerMas;
    private ImageButton btnGuardar;
    private MaterialToolbar topAppBar;
    private MapView mapMiniContacto;

    private boolean infoExpandida = false;

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
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_tarjeta, container, false);

        imgAccion = view.findViewById(R.id.imgAccion);
        txtTitulo = view.findViewById(R.id.txtTitulo);
        txtFecha = view.findViewById(R.id.txtFecha);
        txtEdad = view.findViewById(R.id.txtEdad);
        txtChip = view.findViewById(R.id.txtChip);
        txtTelefono = view.findViewById(R.id.txtTelefono);
        txtInfoAdicional = view.findViewById(R.id.txtInfoAdicional);
        txtMostrarMasInfo = view.findViewById(R.id.txtMostrarMasInfo);

        mapMiniContacto = view.findViewById(R.id.mapMiniContacto);

        if (mapMiniContacto != null) {
            mapMiniContacto.onCreate(savedInstanceState);
        }

        topAppBar = view.findViewById(R.id.topAppBar);

        if (topAppBar != null) {
            topAppBar.setNavigationOnClickListener(v -> dismiss());
        }

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
                    if (!isAdded()) {
                        return;
                    }

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

                    Double latitud = documentSnapshot.getDouble("latitud");
                    Double longitud = documentSnapshot.getDouble("longitud");

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
                        if (!tipoEstado.isEmpty()) {
                            tipoEstado += " • ";
                        }

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

                    String textoInfo = getString(
                            R.string.info_adicional_label,
                            info != null && !info.isEmpty()
                                    ? info
                                    : getString(R.string.sin_informacion)
                    );

                    txtInfoAdicional.setText(textoInfo);
                    configurarMostrarMasInfo();

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

                    configurarMiniMapa(
                            nombre,
                            estado,
                            latitud,
                            longitud
                    );

                    if (btnGuardar != null) {
                        btnGuardar.bringToFront();

                        btnGuardar.setOnClickListener(v ->
                                Toast.makeText(
                                        getContext(),
                                        getString(R.string.guardado),
                                        Toast.LENGTH_SHORT
                                ).show()
                        );
                    }

                    if (btnVerMas != null) {
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
                    }
                })
                .addOnFailureListener(e -> {
                    if (!isAdded()) {
                        return;
                    }

                    Toast.makeText(
                            getContext(),
                            getString(R.string.error_cargar_publicacion),
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }

    private void configurarMostrarMasInfo() {
        if (txtInfoAdicional == null || txtMostrarMasInfo == null) {
            return;
        }

        infoExpandida = false;

        txtInfoAdicional.setMaxLines(Integer.MAX_VALUE);
        txtInfoAdicional.setEllipsize(null);

        txtMostrarMasInfo.setText(getString(R.string.mostrar_mas));
        txtMostrarMasInfo.setVisibility(View.GONE);

        txtInfoAdicional.postDelayed(() -> {
            if (txtInfoAdicional == null || txtMostrarMasInfo == null) {
                return;
            }

            int lineasTotales = txtInfoAdicional.getLineCount();

            if (lineasTotales > 2) {
                txtInfoAdicional.setMaxLines(2);
                txtInfoAdicional.setEllipsize(TextUtils.TruncateAt.END);
                txtMostrarMasInfo.setVisibility(View.VISIBLE);
                txtMostrarMasInfo.setText(getString(R.string.mostrar_mas));
            } else {
                txtMostrarMasInfo.setVisibility(View.GONE);
            }
        }, 150);

        txtMostrarMasInfo.setOnClickListener(v -> {
            if (txtInfoAdicional == null || txtMostrarMasInfo == null) {
                return;
            }

            if (infoExpandida) {
                txtInfoAdicional.setMaxLines(2);
                txtInfoAdicional.setEllipsize(TextUtils.TruncateAt.END);
                txtMostrarMasInfo.setText(getString(R.string.mostrar_mas));
                infoExpandida = false;
            } else {
                txtInfoAdicional.setMaxLines(Integer.MAX_VALUE);
                txtInfoAdicional.setEllipsize(null);
                txtMostrarMasInfo.setText(getString(R.string.mostrar_menos));
                infoExpandida = true;
            }
        });
    }

    private void configurarMiniMapa(
            String nombre,
            String estado,
            Double latitud,
            Double longitud
    ) {
        if (mapMiniContacto == null || latitud == null || longitud == null) {
            return;
        }

        mapMiniContacto.getMapAsync(googleMap -> {
            LatLng ubicacionMascota = new LatLng(latitud, longitud);

            googleMap.clear();

            googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(ubicacionMascota, 15f)
            );

            int iconoPuntero;

            if ("perdido".equalsIgnoreCase(estado)) {
                iconoPuntero = R.drawable.puntero_perdido;
            } else if ("adopcion".equalsIgnoreCase(estado)
                    || "adopción".equalsIgnoreCase(estado)) {
                iconoPuntero = R.drawable.puntero_adopcion;
            } else {
                iconoPuntero = R.drawable.puntero_perdido;
            }

            googleMap.addMarker(
                    new MarkerOptions()
                            .position(ubicacionMascota)
                            .title(nombre != null ? nombre : getString(R.string.sin_nombre))
                            .icon(crearIconoPequeno(iconoPuntero))
            );

            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setScrollGesturesEnabled(false);
            googleMap.getUiSettings().setZoomGesturesEnabled(false);
            googleMap.getUiSettings().setRotateGesturesEnabled(false);
            googleMap.getUiSettings().setTiltGesturesEnabled(false);
        });
    }

    private BitmapDescriptor crearIconoPequeno(int drawableId) {
        Bitmap bitmapOriginal = BitmapFactory.decodeResource(getResources(), drawableId);

        if (bitmapOriginal == null) {
            return BitmapDescriptorFactory.defaultMarker();
        }

        Bitmap bitmapPequeno = Bitmap.createScaledBitmap(bitmapOriginal, 80, 80, false);
        return BitmapDescriptorFactory.fromBitmap(bitmapPequeno);
    }

    private String traducirTipo(String tipo) {
        if (tipo == null) {
            return "";
        }

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
        if (estado == null) {
            return "";
        }

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

    @Override
    public void onResume() {
        super.onResume();

        if (mapMiniContacto != null) {
            mapMiniContacto.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mapMiniContacto != null) {
            mapMiniContacto.onPause();
        }

        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mapMiniContacto != null) {
            mapMiniContacto.onDestroy();
            mapMiniContacto = null;
        }

        super.onDestroyView();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        if (mapMiniContacto != null) {
            mapMiniContacto.onLowMemory();
        }
    }
}