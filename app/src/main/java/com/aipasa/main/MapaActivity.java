package com.aipasa.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.aipasa.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference databaseRef;

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), perms -> {
                Boolean fineLocationGranted = perms.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                if (fineLocationGranted != null && fineLocationGranted) {
                    enableMyLocation();
                } else {
                    Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        databaseRef = FirebaseDatabase.getInstance().getReference("pines_mascotas");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f));
                }
            });
        }
    }

    private void showAddPinDialog(LatLng latLng) {
        final EditText input = new EditText(this);
        input.setHint("Ej: Perro perdido, Parque canino...");
        input.setPadding(48, 32, 48, 32);

        new AlertDialog.Builder(this)
                .setTitle("Nuevo pin")
                .setMessage("Escribe un título para este marcador:")
                .setView(input)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String titulo = input.getText().toString().trim();
                    if (!titulo.isEmpty()) {
                        savePinToFirebase(latLng, titulo);
                    } else {
                        Toast.makeText(this, "El título no puede estar vacío", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void savePinToFirebase(LatLng latLng, String titulo) {
        String pinId = databaseRef.push().getKey();
        if (pinId == null) return;

        Map<String, Object> pinData = new HashMap<>();
        pinData.put("lat", latLng.latitude);
        pinData.put("lng", latLng.longitude);
        pinData.put("titulo", titulo);

        databaseRef.child(pinId).setValue(pinData)
                .addOnSuccessListener(aVoid -> Toast.makeText(MapaActivity.this, "Pin guardado", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(MapaActivity.this, "Error al guardar el pin", Toast.LENGTH_SHORT).show());
    }

    private void listenForPins() {
        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                addMarkerFromSnapshot(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void addMarkerFromSnapshot(DataSnapshot snapshot) {
        Double lat = snapshot.child("lat").getValue(Double.class);
        Double lng = snapshot.child("lng").getValue(Double.class);
        String titulo = snapshot.child("titulo").getValue(String.class);

        if (lat != null && lng != null) {
            if (titulo == null) titulo = "Sin título";
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(titulo));
        }
    }
}