package com.aipasa.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aipasa.R;
import com.aipasa.firebase.SupabaseClient;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublicacionActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    private static final int CAMERA_PERMISSION_CODE = 200;

    private ActivityResultLauncher<String> galeriaLauncher;

    private LinearLayout layoutImagen;
    private ImageView imgMascota;
    private Button btnPublicar;
    private CheckBox checkLegal;

    private EditText etNombre, etTelefono, etEdad, etChip, etInfoAdicional;

    private RadioButton cbPerdido, cbAdopcion, cbPerro, cbGato, cbOtro;

    private Uri imageUri;
    private Bitmap imageBitmap;

    private String modo;
    private String idMascota;
    private String fotoUrlActual;

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

        cbPerdido = findViewById(R.id.rbPerdido);
        cbAdopcion = findViewById(R.id.rbAdopcion);
        cbPerro = findViewById(R.id.rbPerro);
        cbGato = findViewById(R.id.rbGato);
        cbOtro = findViewById(R.id.rbOtro);

        imgMascota.setVisibility(View.GONE);
        btnPublicar.setEnabled(false);

        modo = getIntent().getStringExtra("modo");
        idMascota = getIntent().getStringExtra("idMascota");

        if ("editar".equals(modo)) {
            btnPublicar.setText("Guardar cambios");
        }

        layoutImagen.setOnClickListener(v -> mostrarOpcionesImagen());
        imgMascota.setOnClickListener(v -> mostrarOpcionesImagen());

        checkLegal.setOnCheckedChangeListener((buttonView, isChecked) ->
                btnPublicar.setEnabled(isChecked));

        galeriaLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageUri = uri;
                        imageBitmap = null;

                        imgMascota.setImageURI(uri);
                        layoutImagen.setVisibility(View.GONE);
                        imgMascota.setVisibility(View.VISIBLE);
                    }
                }
        );

        btnPublicar.setOnClickListener(v -> guardarMascota());

        if ("editar".equals(modo) && idMascota != null) {
            cargarDatosMascota(idMascota);
        }
    }

    private void mostrarOpcionesImagen() {
        String[] opciones = {"Hacer foto", "Elegir de galería"};

        new AlertDialog.Builder(this)
                .setTitle("Añadir imagen")
                .setItems(opciones, (dialog, which) -> {
                    if (which == 0) {
                        verificarPermisoCamara();
                    } else {
                        abrirGaleria();
                    }
                })
                .show();
    }

    private void verificarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE
            );

        } else {
            abrirCamara();
        }
    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void abrirGaleria() {
        galeriaLauncher.launch("image/*");
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA && data != null && data.getExtras() != null) {
            imageBitmap = (Bitmap) data.getExtras().get("data");
            imageUri = null;

            imgMascota.setImageBitmap(imageBitmap);
            layoutImagen.setVisibility(View.GONE);
            imgMascota.setVisibility(View.VISIBLE);
        }
    }

    private void guardarMascota() {

        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String edad = etEdad.getText().toString().trim();
        String chip = etChip.getText().toString().trim();
        String infoAdicional = etInfoAdicional.getText().toString().trim();

        String estado = cbPerdido.isChecked() ? "perdido" :
                cbAdopcion.isChecked() ? "adopcion" : null;

        String tipo = cbPerro.isChecked() ? "perro" :
                cbGato.isChecked() ? "gato" :
                        cbOtro.isChecked() ? "otro" : null;

        if (nombre.isEmpty() || estado == null || tipo == null) {
            Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null && imageBitmap == null) {

            if ("editar".equals(modo) && fotoUrlActual != null) {
                guardarEnFirestore(
                        fotoUrlActual,
                        nombre,
                        tipo,
                        estado,
                        telefono,
                        edad,
                        chip,
                        infoAdicional
                );
            } else {
                Toast.makeText(this, "Selecciona una imagen", Toast.LENGTH_SHORT).show();
            }

            return;
        }

        try {
            byte[] imageBytes;

            if (imageUri != null) {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                int nRead;
                byte[] data = new byte[16384];

                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }

                buffer.flush();
                imageBytes = buffer.toByteArray();

                inputStream.close();

            } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                imageBytes = baos.toByteArray();
            }

            subirImagenASupabase(
                    imageBytes,
                    nombre,
                    tipo,
                    estado,
                    telefono,
                    edad,
                    chip,
                    infoAdicional
            );

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error preparando imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void subirImagenASupabase(byte[] bytes,
                                      String nombre,
                                      String tipo,
                                      String estado,
                                      String telefono,
                                      String edad,
                                      String chip,
                                      String infoAdicional) {

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
                                Toast.makeText(
                                        PublicacionActivity.this,
                                        "Error subiendo imagen",
                                        Toast.LENGTH_SHORT
                                ).show()
                        );
                    }

                    @Override
                    public void onResponse(Call call, Response response) {

                        if (response.isSuccessful()) {

                            String publicUrl =
                                    SupabaseClient.SUPABASE_URL +
                                            "/storage/v1/object/public/" +
                                            SupabaseClient.BUCKET_NAME +
                                            "/" + fileName;

                            guardarEnFirestore(
                                    publicUrl,
                                    nombre,
                                    tipo,
                                    estado,
                                    telefono,
                                    edad,
                                    chip,
                                    infoAdicional
                            );

                        } else {
                            runOnUiThread(() ->
                                    Toast.makeText(
                                            PublicacionActivity.this,
                                            "Error Supabase: " + response.code(),
                                            Toast.LENGTH_LONG
                                    ).show()
                            );
                        }

                        response.close();
                    }
                });
    }

    private void guardarEnFirestore(String urlDescarga,
                                    String nombre,
                                    String tipo,
                                    String estado,
                                    String telefono,
                                    String edad,
                                    String chip,
                                    String infoAdicional) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String id;

        if ("editar".equals(modo) && idMascota != null) {
            id = idMascota;
        } else {
            id = db.collection("mascotas").document().getId();
        }

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
            mascota.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

        db.collection("mascotas")
                .document(id)
                .set(mascota)
                .addOnSuccessListener(unused -> {
                    if ("editar".equals(modo)) {
                        Toast.makeText(this, "Mascota actualizada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Mascota publicada", Toast.LENGTH_SHORT).show();
                    }

                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(
                                this,
                                "Error al guardar",
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }

    private void cargarDatosMascota(String id) {

        FirebaseFirestore.getInstance()
                .collection("mascotas")
                .document(id)
                .get()
                .addOnSuccessListener(doc -> {

                    if (!doc.exists()) return;

                    etNombre.setText(doc.getString("nombre"));
                    etTelefono.setText(doc.getString("telefono"));
                    etEdad.setText(doc.getString("edad"));
                    etChip.setText(doc.getString("chip"));
                    etInfoAdicional.setText(doc.getString("infoAdicional"));

                    String estado = doc.getString("estado");

                    if ("perdido".equals(estado)) {
                        cbPerdido.setChecked(true);
                    }

                    if ("adopcion".equals(estado)) {
                        cbAdopcion.setChecked(true);
                    }

                    String tipo = doc.getString("tipo");

                    if ("perro".equals(tipo)) {
                        cbPerro.setChecked(true);
                    }

                    if ("gato".equals(tipo)) {
                        cbGato.setChecked(true);
                    }

                    if ("otro".equals(tipo)) {
                        cbOtro.setChecked(true);
                    }

                    fotoUrlActual = doc.getString("fotoUrl");

                    if (fotoUrlActual != null && !fotoUrlActual.isEmpty()) {
                        Glide.with(this).load(fotoUrlActual).into(imgMascota);
                        layoutImagen.setVisibility(View.GONE);
                        imgMascota.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(
                                this,
                                "Error cargando datos",
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }
}