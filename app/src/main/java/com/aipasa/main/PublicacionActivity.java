package com.aipasa.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class PublicacionActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;

    private LinearLayout layoutImagen;
    private ImageView imgMascota;
    private TextView txtAddPhoto;
    private Button btnPublicar;
    private CheckBox checkLegal; // añadirlo al xml

    private CheckBox cbPerro, cbGato, cbOtro;
    private CheckBox cbPerdido, cbAdopcion;

    private EditText etNombre, etTelefono, etInfoAdicional;

    private Uri imageUri;
    private Bitmap imageBitmap;

    private FirebaseFirestore db; // guarda imagenes
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicacion);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        layoutImagen = findViewById(R.id.layoutImagen);
        imgMascota = findViewById(R.id.imgMascota);
        txtAddPhoto = findViewById(R.id.txtAddPhoto);
        btnPublicar = findViewById(R.id.btnPublicar);
        checkLegal = findViewById(R.id.checkLegal);

        cbPerro = findViewById(R.id.cbPerro);
        cbGato = findViewById(R.id.cbGato);
        cbOtro = findViewById(R.id.cbOtro);

        cbPerdido = findViewById(R.id.cbPerdido);
        cbAdopcion = findViewById(R.id.cbAdopcion);

        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etInfoAdicional = findViewById(R.id.etInfoAdicional);

        imgMascota.setVisibility(View.GONE);
        btnPublicar.setEnabled(false);

        layoutImagen.setOnClickListener(v -> mostrarOpcionesImagen());

        checkLegal.setOnCheckedChangeListener((buttonView, isChecked) ->
                btnPublicar.setEnabled(isChecked)
        );

        btnPublicar.setOnClickListener(v -> subirPublicacion());
    }

    private void mostrarOpcionesImagen() {

        String[] opciones = {"Hacer foto", "Elegir de galería"};

        new AlertDialog.Builder(this)
                .setTitle("Añadir imagen")
                .setItems(opciones, (dialog, which) -> {
                    if (which == 0) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, REQUEST_CAMERA);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, REQUEST_GALLERY);
                    }
                })
                .show();
    }

    private void subirPublicacion() {

        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String info = etInfoAdicional.getText().toString().trim();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Introduce el nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cbPerro.isChecked() && !cbGato.isChecked() && !cbOtro.isChecked()) {
            Toast.makeText(this, "Selecciona un tipo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cbPerdido.isChecked() && !cbAdopcion.isChecked()) {
            Toast.makeText(this, "Selecciona un estado", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null && imageBitmap == null) {
            Toast.makeText(this, "Añade una imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        String tipo = cbPerro.isChecked() ? "Perro" :
                cbGato.isChecked() ? "Gato" : "Otro";

        String estado = cbPerdido.isChecked() ? "Perdido" : "En adopción";

        String fileName = "imagenes/" + System.currentTimeMillis() + ".jpg";
        StorageReference fileRef = storageRef.child(fileName);

        if (imageUri != null) {

            fileRef.putFile(imageUri)
                    .continueWithTask(task -> fileRef.getDownloadUrl())
                    .addOnSuccessListener(uri ->
                            guardarEnFirestore(nombre, tipo, estado, telefono, info, uri.toString())
                    );

        } else {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] data = baos.toByteArray();

            fileRef.putBytes(data)
                    .continueWithTask(task -> fileRef.getDownloadUrl())
                    .addOnSuccessListener(uri ->
                            guardarEnFirestore(nombre, tipo, estado, telefono, info, uri.toString())
                    );
        }
    }

    private void guardarEnFirestore(String nombre, String tipo, String estado,
                                    String telefono, String info, String fotoUrl) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
    // lo guarda en el firebase los datos del animal que se ha publicado

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid();

        DocumentReference docRef = db.collection("mascotas").document();

        Map<String, Object> mascota = new HashMap<>();
        mascota.put("id", docRef.getId());
        mascota.put("nombre", nombre);
        mascota.put("tipo", tipo);
        mascota.put("estado", estado);
        mascota.put("telefono", telefono);
        mascota.put("infoAdicional", info);
        mascota.put("fotoUrl", fotoUrl);
        mascota.put("fecha", System.currentTimeMillis());
        mascota.put("userId", userId);

        docRef.set(mascota)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Publicación guardada", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {

            if (requestCode == REQUEST_CAMERA && data.getExtras() != null) {
                imageBitmap = (Bitmap) data.getExtras().get("data");
                imgMascota.setImageBitmap(imageBitmap);
            }

            if (requestCode == REQUEST_GALLERY) {
                imageUri = data.getData();
                imgMascota.setImageURI(imageUri);
            }

            layoutImagen.setVisibility(View.GONE);
            txtAddPhoto.setVisibility(View.GONE);
            imgMascota.setVisibility(View.VISIBLE);
        }
    }
}
