package com.aipasa.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.*;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.*;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.*;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aipasa.R;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference databaseRef;

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), perms -> {
                Boolean fineLocationGranted = perms.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                if (fineLocationGranted != null && fineLocationGranted) {
                    enableMyLocation();
                } else {
                    Toast.makeText(requireContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        databaseRef = FirebaseDatabase.getInstance().getReference("pines_mascotas");

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        checkAndRequestLocationPermission();
        mMap.setOnMapLongClickListener(this::showAddPinDialog);
        listenForPins();
    }

    private void checkAndRequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
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
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);

            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng user = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user, 15f));
                }
            });
        }
    }

    private void showAddPinDialog(LatLng latLng) {
        EditText input = new EditText(requireContext());

        new AlertDialog.Builder(requireContext())
                .setTitle("Nuevo pin")
                .setView(input)
                .setPositiveButton("Guardar", (d, w) -> {
                    String titulo = input.getText().toString().trim();
                    if (!titulo.isEmpty()) {
                        savePinToFirebase(latLng, titulo);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void savePinToFirebase(LatLng latLng, String titulo) {
        String id = databaseRef.push().getKey();
        if (id == null) return;

        Map<String, Object> data = new HashMap<>();
        data.put("lat", latLng.latitude);
        data.put("lng", latLng.longitude);
        data.put("titulo", titulo);

        databaseRef.child(id).setValue(data);
    }

    private void listenForPins() {
        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String s) {
                addMarkerFromSnapshot(snapshot);
            }
            public void onChildChanged(@NonNull DataSnapshot snapshot, String s) {}
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            public void onChildMoved(@NonNull DataSnapshot snapshot, String s) {}
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void addMarkerFromSnapshot(DataSnapshot snapshot) {
        Double lat = snapshot.child("lat").getValue(Double.class);
        Double lng = snapshot.child("lng").getValue(Double.class);
        String titulo = snapshot.child("titulo").getValue(String.class);

        if (lat != null && lng != null && mMap != null) {
            if (titulo == null) titulo = "Sin título";
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title(titulo));
        }
    }
}