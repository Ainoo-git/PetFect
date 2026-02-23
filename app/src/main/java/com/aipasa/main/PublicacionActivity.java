package com.aipasa.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aipasa.R;
import com.aipasa.firebase.SupabaseClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.*;

public class PublicacionActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int CAMERA_PERMISSION_CODE = 200;
    private static final int STORAGE_PERMISSION_CODE = 300;

    private LinearLayout layoutImagen;
    private ImageView imgMascota;
    private Button btnPublicar;
    private CheckBox checkLegal;

    private EditText etNombre, etTelefono, etEdad, etChip, etInfoAdicional;
    private CheckBox cbPerdido, cbAdopcion, cbPerro, cbGato;

    private Uri imageUri;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicacion);

        layoutImagen = findViewById(R.id.layoutImagen);
        imgMascota = findViewById(R.id.imgMascota);
        btnPublicar = findViewById(R.id.btnPublicar);
        checkLegal = findViewById(R.id.checkLegal);

        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etEdad = findViewById(R.id.etEdad);
        etChip = findViewById(R.id.etChip);
        etInfoAdicional = findViewById(R.id.etInfoAdicional);

        cbPerdido = findViewById(R.id.cbPerdido);
        cbAdopcion = findViewById(R.id.cbAdopcion);
        cbPerro = findViewById(R.id.cbPerro);
        cbGato = findViewById(R.id.cbGato);

        imgMascota.setVisibility(View.GONE);
        btnPublicar.setEnabled(false);

        layoutImagen.setOnClickListener(v -> mostrarOpcionesImagen());

        checkLegal.setOnCheckedChangeListener((buttonView, isChecked) ->
                btnPublicar.setEnabled(isChecked));

        btnPublicar.setOnClickListener(v -> guardarMascota());
    }

    private void mostrarOpcionesImagen() {
        String[] opciones = {"Hacer foto", "Elegir de galería"};

        new AlertDialog.Builder(this)
                .setTitle("Añadir imagen")
                .setItems(opciones, (dialog, which) -> {
                    if (which == 0) verificarPermisoCamara();
                    else verificarPermisoGaleria();
                })
                .show();
    }

    private void verificarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        } else abrirCamara();
    }

    private void verificarPermisoGaleria() {
        String permiso = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ? Manifest.permission.READ_MEDIA_IMAGES
                : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permiso)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permiso},
                    STORAGE_PERMISSION_CODE);
        } else abrirGaleria();
    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void abrirGaleria() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA && data != null) {
            imageBitmap = (Bitmap) data.getExtras().get("data");
            imgMascota.setImageBitmap(imageBitmap);
        }

        if (requestCode == REQUEST_GALLERY && data != null) {
            imageUri = data.getData();
            imgMascota.setImageURI(imageUri);
        }

        layoutImagen.setVisibility(View.GONE);
        imgMascota.setVisibility(View.VISIBLE);
    }

    private void guardarMascota() {

        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String edad = etEdad.getText().toString().trim();
        String chip = etChip.getText().toString().trim();
        String infoAdicional = etInfoAdicional.getText().toString().trim();

        final String estado = cbPerdido.isChecked() ? "perdido" :
                cbAdopcion.isChecked() ? "adopcion" : null;

        final String tipo = cbPerro.isChecked() ? "perro" :
                cbGato.isChecked() ? "gato" : null;

        if (nombre.isEmpty() || estado == null || tipo == null) {
            Toast.makeText(this, "Completa los campos obligatorios",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null && imageBitmap == null) {
            Toast.makeText(this, "Selecciona una imagen",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            byte[] imageBytes;

            if (imageUri != null) {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                imageBytes = inputStream.readAllBytes();
            } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                imageBytes = baos.toByteArray();
            }

            subirImagenASupabase(imageBytes, nombre, tipo, estado,
                    telefono, edad, chip, infoAdicional);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void subirImagenASupabase(byte[] bytes,
                                      String nombre, String tipo, String estado,
                                      String telefono, String edad,
                                      String chip, String infoAdicional) {

        String fileName = UUID.randomUUID().toString() + ".jpg";

        RequestBody requestBody =
                RequestBody.create(bytes, MediaType.parse("image/jpeg"));

        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL +
                        "/storage/v1/object/" +
                        SupabaseClient.BUCKET_NAME +
                        "/" + fileName)
                .addHeader("apikey", SupabaseClient.SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SupabaseClient.SUPABASE_KEY)
                .addHeader("Content-Type", "image/jpeg")
                .put(requestBody)
                .build();

        SupabaseClient.getClient().newCall(request)
                .enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, java.io.IOException e) {
                        runOnUiThread(() ->
                                Toast.makeText(PublicacionActivity.this,
                                        "Error subiendo imagen",
                                        Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(Call call, Response response) {

                        if (response.isSuccessful()) {

                            String publicUrl =
                                    SupabaseClient.SUPABASE_URL +
                                            "/storage/v1/object/public/" +
                                            SupabaseClient.BUCKET_NAME +
                                            "/" + fileName;

                            guardarEnFirestore(publicUrl,
                                    nombre, tipo, estado,
                                    telefono, edad, chip, infoAdicional);

                        } else {
                            runOnUiThread(() ->
                                    Toast.makeText(PublicacionActivity.this,
                                            "Error Supabase: " + response.code(),
                                            Toast.LENGTH_LONG).show());
                        }
                    }
                });
    }

    private void guardarEnFirestore(String urlDescarga,
                                    String nombre, String tipo, String estado,
                                    String telefono, String edad,
                                    String chip, String infoAdicional) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = db.collection("mascotas").document().getId();

        Map<String, Object> mascota = new HashMap<>();
        mascota.put("id", id);
        mascota.put("nombre", nombre);
        mascota.put("tipo", tipo);
        mascota.put("estado", estado);
        mascota.put("telefono", telefono);
        mascota.put("edad", edad);
        mascota.put("chip", chip);
        mascota.put("infoAdicional", infoAdicional);
        mascota.put("fotoUrl", urlDescarga);
        mascota.put("fecha", System.currentTimeMillis());

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mascota.put("userId",
                    FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

        db.collection("mascotas")
                .document(id)
                .set(mascota)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this,
                            "Mascota publicada",
                            Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Error al publicar",
                                Toast.LENGTH_SHORT).show());
    }
}