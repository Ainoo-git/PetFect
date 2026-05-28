package com.aipasa.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
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

    public MapFragment() {
        // Constructor vacío obligatorio
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_map,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(
                        requireContext()
                );

        db = FirebaseFirestore.getInstance();

        requestPermissionLauncher =
                registerForActivityResult(
                        new ActivityResultContracts
                                .RequestMultiplePermissions(),

                        perms -> {

                            Boolean fineLocationGranted =
                                    perms.getOrDefault(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            false
                                    );

                            if (fineLocationGranted != null &&
                                    fineLocationGranted) {

                                enableMyLocation();

                            } else {

                                Toast.makeText(
                                        requireContext(),
                                        "Permiso de ubicación denegado",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        });

        SupportMapFragment mapFragment =
                new SupportMapFragment();

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.map_container, mapFragment)
                .commit();

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        checkAndRequestLocationPermission();

        cargarMascotas();
    }

    private void checkAndRequestLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)

                == PackageManager.PERMISSION_GRANTED) {

            enableMyLocation();

        } else {

            requestPermissionLauncher.launch(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    });
        }
    }

    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)

                == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);

            fusedLocationClient
                    .getLastLocation()
                    .addOnSuccessListener(
                            requireActivity(),

                            location -> {

                                if (location != null) {

                                    LatLng userLatLng =
                                            new LatLng(
                                                    location.getLatitude(),
                                                    location.getLongitude()
                                            );

                                    mMap.animateCamera(
                                            CameraUpdateFactory
                                                    .newLatLngZoom(
                                                            userLatLng,
                                                            15f
                                                    )
                                    );
                                }
                            });
        }
    }

    private void cargarMascotas() {

        db.collection("mascotas")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot document :
                            queryDocumentSnapshots) {

                        Double lat =
                                document.getDouble("latitud");

                        Double lng =
                                document.getDouble("longitud");

                        String nombre =
                                document.getString("nombre");

                        String idMascota =
                                document.getString("id");

                        if (lat != null &&
                                lng != null &&
                                lat != 0 &&
                                lng != 0) {

                            LatLng posicion =
                                    new LatLng(lat, lng);

                            Marker marker =
                                    mMap.addMarker(
                                            new MarkerOptions()
                                                    .position(posicion)
                                                    .title(nombre)
                                    );

                            if (marker != null) {

                                marker.setTag(idMascota);

                            }
                        }
                    }

                    activarClicksMarkers();
                })

                .addOnFailureListener(e -> {

                    Toast.makeText(
                            requireContext(),
                            "Error Firestore",
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
    private void activarClicksMarkers() {

        mMap.setOnMarkerClickListener(marker -> {

            String idMascota =
                    (String) marker.getTag();

            if (idMascota != null) {

                TarjetaFragment fragment =
                        TarjetaFragment.newInstance(idMascota);

                fragment.show(
                        requireActivity()
                                .getSupportFragmentManager(),
                        "tarjeta"
                );
            }

            return true;
        });
    }
}