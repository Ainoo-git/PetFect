package com.aipasa.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aipasa.R;
import com.aipasa.firebase.SupabaseClient;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.Timestamp;
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

    private static final int CAMERA_PERMISSION_CODE = 200;
    private static final int STORAGE_PERMISSION_CODE = 300;
    private static final int LOCATION_PERMISSION_CODE = 400;

    private ActivityResultLauncher<String> galeriaLauncher;
    private ActivityResultLauncher<Void> camaraLauncher;

    private LinearLayout layoutImagen;
    private ImageView imgMascota;
    private Button btnPublicar;
    private CheckBox checkLegal;

    private EditText etNombre;
    private EditText etTelefono;
    private EditText etEdad;
    private EditText etChip;
    private EditText etInfoAdicional;

    private RadioButton cbPerdido;
    private RadioButton cbAdopcion;
    private RadioButton cbPerro;
    private RadioButton cbGato;
    private RadioButton cbOtro;

    private Uri imageUri;
    private Bitmap imageBitmap;

    private String modo;
    private String idMascota;
    private String fotoUrlActual = null;
    private boolean usuarioCambioImagen = false;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicacion);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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

        layoutImagen.setOnClickListener(v -> mostrarOpcionesImagen());
        imgMascota.setOnClickListener(v -> mostrarOpcionesImagen());

        checkLegal.setOnCheckedChangeListener((buttonView, isChecked) ->
                btnPublicar.setEnabled(isChecked)
        );

        galeriaLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        usuarioCambioImagen = true;
                        imageUri = uri;
                        imageBitmap = null;

                        Glide.with(this).clear(imgMascota);
                        imgMascota.setImageDrawable(null);
                        imgMascota.setImageURI(uri);

                        layoutImagen.setVisibility(View.GONE);
                        imgMascota.setVisibility(View.VISIBLE);
                    }
                }
        );

        camaraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                bitmap -> {
                    if (bitmap != null) {
                        usuarioCambioImagen = true;
                        imageBitmap = bitmap;
                        imageUri = null;

                        Glide.with(this).clear(imgMascota);
                        imgMascota.setImageDrawable(null);
                        imgMascota.setImageBitmap(bitmap);

                        layoutImagen.setVisibility(View.GONE);
                        imgMascota.setVisibility(View.VISIBLE);
                    }
                }
        );

        btnPublicar.setOnClickListener(v -> obtenerUbicacionYGuardar());

        modo = getIntent().getStringExtra("modo");
        idMascota = getIntent().getStringExtra("idMascota");

        if ("editar".equals(modo) && idMascota != null) {
            btnPublicar.setText("Guardar cambios");
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
                        verificarPermisoGaleria();
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

    private void verificarPermisoGaleria() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        STORAGE_PERMISSION_CODE
                );

            } else {
                abrirGaleria();
            }

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_CODE
                );

            } else {
                abrirGaleria();
            }
        }
    }

    private void abrirCamara() {
        camaraLauncher.launch(null);
    }

    private void abrirGaleria() {
        galeriaLauncher.launch("image/*");
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                abrirCamara();

            } else {
                Toast.makeText(
                        this,
                        "Permiso de cámara denegado",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }

        if (requestCode == STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                abrirGaleria();

            } else {
                Toast.makeText(
                        this,
                        "Permiso de galería denegado",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }

        if (requestCode == LOCATION_PERMISSION_CODE) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                obtenerUbicacionYGuardar();

            } else {
                Toast.makeText(
                        this,
                        "Permiso de ubicación denegado",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    private void obtenerUbicacionYGuardar() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_CODE
            );

            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {

                    if (location != null) {

                        double latitud = location.getLatitude();
                        double longitud = location.getLongitude();

                        guardarMascotaConUbicacion(latitud, longitud);

                    } else {

                        Toast.makeText(
                                this,
                                "No se pudo obtener ubicación",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private void guardarMascotaConUbicacion(double latitud, double longitud) {

        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String edad = etEdad.getText().toString().trim();
        String chip = etChip.getText().toString().trim();
        String infoAdicional = etInfoAdicional.getText().toString().trim();

        final String estado = cbPerdido.isChecked() ? "perdido" :
                cbAdopcion.isChecked() ? "adopcion" : null;

        final String tipo = cbPerro.isChecked() ? "perro" :
                cbGato.isChecked() ? "gato" :
                        cbOtro.isChecked() ? "otro" : null;

        if (nombre.isEmpty() || estado == null || tipo == null) {
            Toast.makeText(
                    this,
                    "Completa los campos obligatorios",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (imageUri == null && imageBitmap == null && !"editar".equals(modo)) {
            Toast.makeText(
                    this,
                    "Selecciona una imagen",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if ("editar".equals(modo)
                && imageUri == null
                && imageBitmap == null
                && fotoUrlActual != null) {

            guardarEnFirestore(
                    fotoUrlActual,
                    nombre,
                    tipo,
                    estado,
                    telefono,
                    edad,
                    chip,
                    infoAdicional,
                    latitud,
                    longitud
            );

            return;
        }

        try {

            byte[] imageBytes;

            if (imageUri != null) {

                InputStream inputStream = getContentResolver().openInputStream(imageUri);

                if (inputStream == null) {
                    Toast.makeText(
                            this,
                            "No se pudo leer la imagen",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                int nRead;
                byte[] data = new byte[16384];

                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }

                buffer.flush();
                imageBytes = buffer.toByteArray();

                inputStream.close();

            } else if (imageBitmap != null) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                imageBitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        80,
                        baos
                );

                imageBytes = baos.toByteArray();

            } else {

                Toast.makeText(
                        this,
                        "Selecciona una imagen",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            subirImagenASupabase(
                    imageBytes,
                    nombre,
                    tipo,
                    estado,
                    telefono,
                    edad,
                    chip,
                    infoAdicional,
                    latitud,
                    longitud
            );

        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(
                    this,
                    "Error procesando la imagen",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void subirImagenASupabase(
            byte[] bytes,
            String nombre,
            String tipo,
            String estado,
            String telefono,
            String edad,
            String chip,
            String infoAdicional,
            double latitud,
            double longitud
    ) {

        String fileName = UUID.randomUUID().toString() + ".jpg";

        RequestBody requestBody = RequestBody.create(
                bytes,
                MediaType.parse("image/jpeg")
        );

        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL
                        + "/storage/v1/object/"
                        + SupabaseClient.BUCKET_NAME
                        + "/"
                        + fileName)
                .addHeader("apikey", SupabaseClient.SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SupabaseClient.SUPABASE_KEY)
                .addHeader("Content-Type", "image/jpeg")
                .put(requestBody)
                .build();

        SupabaseClient.getClient()
                .newCall(request)
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

                            String publicUrl = SupabaseClient.SUPABASE_URL
                                    + "/storage/v1/object/public/"
                                    + SupabaseClient.BUCKET_NAME
                                    + "/"
                                    + fileName;

                            guardarEnFirestore(
                                    publicUrl,
                                    nombre,
                                    tipo,
                                    estado,
                                    telefono,
                                    edad,
                                    chip,
                                    infoAdicional,
                                    latitud,
                                    longitud
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

    private void guardarEnFirestore(
            String urlDescarga,
            String nombre,
            String tipo,
            String estado,
            String telefono,
            String edad,
            String chip,
            String infoAdicional,
            double latitud,
            double longitud
    ) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String id;

        if ("editar".equals(modo) && idMascota != null) {
            id = idMascota;
        } else {
            id = db.collection("mascotas")
                    .document()
                    .getId();
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
        mascota.put("latitud", latitud);
        mascota.put("longitud", longitud);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mascota.put(
                    "userId",
                    FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getUid()
            );
        }

        db.collection("mascotas")
                .document(id)
                .set(mascota)
                .addOnSuccessListener(unused -> {

                    if (!"editar".equals(modo)) {
                        crearNotificacion(
                                db,
                                id,
                                nombre,
                                estado,
                                urlDescarga
                        );
                    }

                    if ("editar".equals(modo)) {

                        Toast.makeText(
                                this,
                                "Mascota actualizada",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        Toast.makeText(
                                this,
                                "Mascota publicada",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(
                                this,
                                "Error al publicar",
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }

    private void crearNotificacion(
            FirebaseFirestore db,
            String idMascota,
            String nombre,
            String estado,
            String urlDescarga
    ) {

        String uidActual = null;

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            uidActual = FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getUid();
        }

        Map<String, Object> notificacion = new HashMap<>();

        notificacion.put("idUsuario", uidActual);
        notificacion.put("idMascota", idMascota);
        notificacion.put("nombreMascota", nombre);
        notificacion.put("tipo", estado);
        notificacion.put("imagenUrl", urlDescarga);
        notificacion.put("fecha", Timestamp.now());
        notificacion.put("leido", false);

        db.collection("notificaciones")
                .add(notificacion);
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
                    } else if ("gato".equals(tipo)) {
                        cbGato.setChecked(true);
                    } else if ("otro".equals(tipo)) {
                        cbOtro.setChecked(true);
                    }

                    fotoUrlActual = doc.getString("fotoUrl");

                    if (fotoUrlActual != null && !fotoUrlActual.isEmpty()) {

                        layoutImagen.setVisibility(View.GONE);
                        imgMascota.setVisibility(View.VISIBLE);

                        if (!usuarioCambioImagen) {
                            Glide.with(this)
                                    .load(fotoUrlActual)
                                    .into(imgMascota);
                        }
                    }
                });
    }
}