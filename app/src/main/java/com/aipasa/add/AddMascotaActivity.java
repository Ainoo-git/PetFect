package com.aipasa.add;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddMascotaActivity extends AppCompatActivity {

    private LinearLayout layoutImagen;
    private ImageView imgMascota;

    private TextInputEditText etNombre, etTelefono, etEdad, etChip, etInfoAdicional, etOtroTipo;
    private CheckBox cbPerdido, cbAdopcion, cbPerro, cbGato, cbOtro, checkLegal;
    private MaterialButton btnPublicar;

    private Uri imageUri;
    private Bitmap imageBitmap;

    // Firebase
    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    imageBitmap = (Bitmap) result.getData().getExtras().get("data");
                    imgMascota.setImageBitmap(imageBitmap);
                    imgMascota.setVisibility(View.VISIBLE);
                    layoutImagen.setVisibility(View.GONE);
                }
            });

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    imgMascota.setImageURI(imageUri);
                    imgMascota.setVisibility(View.VISIBLE);
                    layoutImagen.setVisibility(View.GONE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicacion);

        // Inicializar Firebase
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        initViews();
        setupListeners();
    }

    private void initViews() {
        layoutImagen = findViewById(R.id.layoutImagen);
        imgMascota = findViewById(R.id.imgMascota);
        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etEdad = findViewById(R.id.etEdad);
        etChip = findViewById(R.id.etChip);
        etInfoAdicional = findViewById(R.id.etInfoAdicional);
        etOtroTipo = findViewById(R.id.etOtroTipo);
        cbPerdido = findViewById(R.id.cbPerdido);
        cbAdopcion = findViewById(R.id.cbAdopcion);
        cbPerro = findViewById(R.id.cbPerro);
        cbGato = findViewById(R.id.cbGato);
        cbOtro = findViewById(R.id.cbOtro);
        checkLegal = findViewById(R.id.checkLegal);
        btnPublicar = findViewById(R.id.btnPublicar);

        imgMascota.setVisibility(View.GONE);
        btnPublicar.setEnabled(false);
    }

    private void setupListeners() {
        layoutImagen.setOnClickListener(v -> mostrarOpcionesImagen());

        checkLegal.setOnCheckedChangeListener((buttonView, isChecked) ->
                btnPublicar.setEnabled(isChecked));

        cbOtro.setOnCheckedChangeListener((buttonView, isChecked) ->
                etOtroTipo.setVisibility(isChecked ? View.VISIBLE : View.GONE));

        btnPublicar.setOnClickListener(v -> {
            if (imageBitmap == null && imageUri == null) {
                guardarMascota("");
            } else {
                subirImagenYGuardar();
            }
        });
    }

    private void mostrarOpcionesImagen() {
        String[] opciones = {"Hacer foto", "Elegir de galería"};

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Añadir imagen")
                .setItems(opciones, (dialog, which) -> {
                    if (which == 0) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraLauncher.launch(cameraIntent);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        galleryLauncher.launch(galleryIntent);
                    }
                })
                .show();
    }

    private void subirImagenYGuardar() {
        btnPublicar.setEnabled(false);
        btnPublicar.setText("Subiendo imagen...");

        String nombreImagen = "mascotas/" + UUID.randomUUID() + ".jpg";
        StorageReference storageRef = storage.getReference().child(nombreImagen);

        if (imageBitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] data = baos.toByteArray();

            storageRef.putBytes(data)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            guardarMascota(uri.toString());
                        }).addOnFailureListener(e -> guardarMascota(""));
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al subir imagen", Toast.LENGTH_SHORT).show();
                        guardarMascota("");
                    });
        } else if (imageUri != null) {
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            guardarMascota(uri.toString());
                        }).addOnFailureListener(e -> guardarMascota(""));
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al subir imagen", Toast.LENGTH_SHORT).show();
                        guardarMascota("");
                    });
        }
    }

    private void guardarMascota(String fotoUrl) {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String edad = etEdad.getText().toString().trim();
        String chip = etChip.getText().toString().trim();
        String infoAdicional = etInfoAdicional.getText().toString().trim();

        String estado = "";
        if (cbPerdido.isChecked()) estado = "perdido";
        else if (cbAdopcion.isChecked()) estado = "adopcion";

        String tipo = "";
        if (cbPerro.isChecked()) tipo = "perro";
        else if (cbGato.isChecked()) tipo = "gato";
        else if (cbOtro.isChecked()) tipo = etOtroTipo.getText().toString().trim();

        if (nombre.isEmpty() || estado.isEmpty() || tipo.isEmpty()) {
            Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            btnPublicar.setEnabled(true);
            btnPublicar.setText("Publicar");
            return;
        }

        btnPublicar.setText("Publicando...");


        String id = db.collection("mascotas").document().getId();

        Map<String, Object> mascota = new HashMap<>();
        mascota.put("id", id);              // ← IMPORTANTE: guardar el ID
        mascota.put("nombre", nombre);
        mascota.put("estado", estado);
        mascota.put("tipo", tipo);
        mascota.put("telefono", telefono);
        mascota.put("edad", edad);
        mascota.put("chip", chip);
        mascota.put("infoAdicional", infoAdicional);
        mascota.put("fotoUrl", fotoUrl);
        mascota.put("fecha", System.currentTimeMillis());
        mascota.put("userId", auth.getCurrentUser().getUid());

        db.collection("mascotas")
                .document(id)
                .set(mascota)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Mascota publicada", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al publicar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    btnPublicar.setEnabled(true);
                    btnPublicar.setText("Publicar");
                });
    }
}