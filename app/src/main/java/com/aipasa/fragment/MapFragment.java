package com.aipasa.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aipasa.R;
import com.aipasa.main.TarjetaFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseFirestore db;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    public MapFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Registrar SIEMPRE en onCreate, antes de que el fragment se inicie
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                perms -> {
                    Boolean granted = perms.getOrDefault(
                            Manifest.permission.ACCESS_FINE_LOCATION, false);
                    if (granted != null && granted) {
                        enableMyLocation();
                    } else if (isAdded()) {
                        Toast.makeText(requireContext(),
                                "Permiso de ubicación denegado",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        db = FirebaseFirestore.getInstance();

        // Reutilizar el SupportMapFragment si ya existe para no recrearlo cada vez
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_container);

        if (mapFragment == null) {
            mapFragment = new SupportMapFragment();
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_container, mapFragment)
                    .commit();
        }

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        checkAndRequestLocationPermission();
        cargarMascotas();
    }

    private void checkAndRequestLocationPermission() {
        if (!isAdded()) return;

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation();
        } else {
            requestPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void enableMyLocation() {
        if (mMap == null || !isAdded()) return;

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null && mMap != null && isAdded()) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(location.getLatitude(), location.getLongitude()),
                                    15f));
                        }
                    });
        }
    }

    private void cargarMascotas() {
        db.collection("mascotas")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!isAdded() || mMap == null) return;

                    mMap.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Double lat = document.getDouble("latitud");
                        Double lng = document.getDouble("longitud");
                        String nombre = document.getString("nombre");
                        String estado = document.getString("estado");
                        String idMascota = document.getString("id");

                        if (idMascota == null || idMascota.isEmpty())
                            idMascota = document.getId();
                        if (nombre == null || nombre.isEmpty())
                            nombre = "Mascota";
                        if (estado == null || estado.isEmpty())
                            estado = "";

                        if (lat != null && lng != null && lat != 0 && lng != 0) {
                            int icono = obtenerIconoPorEstado(estado);
                            Marker marker = mMap.addMarker(
                                    new MarkerOptions()
                                            .position(new LatLng(lat, lng))
                                            .title(nombre)
                                            .snippet(estado)
                                            .icon(crearIconoPequeno(icono))
                            );
                            if (marker != null) marker.setTag(idMascota);
                        }
                    }

                    activarClicksMarkers();
                })
                .addOnFailureListener(e -> {
                    if (!isAdded()) return;
                    Toast.makeText(requireContext(), "Error al cargar el mapa",
                            Toast.LENGTH_SHORT).show();
                });
    }

    private int obtenerIconoPorEstado(String estado) {
        if ("perdido".equalsIgnoreCase(estado)) return R.drawable.puntero_perdido;
        if ("adopcion".equalsIgnoreCase(estado) || "adopción".equalsIgnoreCase(estado))
            return R.drawable.puntero_adopcion;
        return 0;
    }

    private BitmapDescriptor crearIconoPequeno(int drawableId) {
        if (drawableId == 0) return BitmapDescriptorFactory.defaultMarker();

        Bitmap imagen = BitmapFactory.decodeResource(getResources(), drawableId);
        if (imagen == null) return BitmapDescriptorFactory.defaultMarker();

        return BitmapDescriptorFactory.fromBitmap(
                Bitmap.createScaledBitmap(imagen, 170, 170, false));
    }

    private void activarClicksMarkers() {
        if (mMap == null) return;

        mMap.setOnMarkerClickListener(marker -> {
            if (!isAdded()) return true;

            String idMascota = (String) marker.getTag();
            if (idMascota != null) {
                TarjetaFragment.newInstance(idMascota)
                        .show(requireActivity().getSupportFragmentManager(), "tarjeta");
            }
            return true;
        });
    }
}
