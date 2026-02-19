package com.aipasa.add;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddMascotaActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;

    private LinearLayout layoutImagen;
    private ImageView imgMascota;
    private TextView txtAddPhoto;
    private Button btnPublicar;
    private CheckBox checkLegal;

    private EditText etNombre, etTelefono, etEdad, etChip, etInfoAdicional, etOtroTipo;
    private CheckBox cbPerdido, cbAdopcion, cbPerro, cbGato, cbOtro;

    private Uri imageUri;
    private Bitmap imageBitmap;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicacion); // Tu layout de publicación

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Referencias
        layoutImagen = findViewById(R.id.layoutImagen);
        imgMascota = findViewById(R.id.imgMascota);
        txtAddPhoto = findViewById(R.id.txtAddPhoto);
        btnPublicar = findViewById(R.id.btnPublicar);
        checkLegal = findViewById(R.id.checkLegal);

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

        // Configuración inicial
        imgMascota.setVisibility(View.GONE);
        btnPublicar.setEnabled(false);

        // Listeners
        layoutImagen.setOnClickListener(v -> mostrarOpcionesImagen());

        checkLegal.setOnCheckedChangeListener((buttonView, isChecked) ->
                btnPublicar.setEnabled(isChecked));

        cbOtro.setOnCheckedChangeListener((buttonView, isChecked) ->
                etOtroTipo.setVisibility(isChecked ? View.VISIBLE : View.GONE));

        btnPublicar.setOnClickListener(v -> publicarMascota());
    }

    private void publicarMascota() {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String edad = etEdad.getText().toString().trim();
        String chip = etChip.getText().toString().trim();
        String infoAdicional = etInfoAdicional.getText().toString().trim();

        // Validar estado
        String estado = "";
        if (cbPerdido.isChecked()) estado = "perdido";
        else if (cbAdopcion.isChecked()) estado = "adopcion";

        // Validar tipo
        String tipo = "";
        if (cbPerro.isChecked()) tipo = "perro";
        else if (cbGato.isChecked()) tipo = "gato";
        else if (cbOtro.isChecked()) tipo = etOtroTipo.getText().toString().trim();

        if (nombre.isEmpty() || estado.isEmpty() || tipo.isEmpty()) {
            Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid();

        Map<String, Object> mascota = new HashMap<>();
        mascota.put("nombre", nombre);
        mascota.put("tipo", tipo);
        mascota.put("estado", estado);
        mascota.put("telefono", telefono);
        mascota.put("edad", edad);
        mascota.put("chip", chip);
        mascota.put("infoAdicional", infoAdicional);
        mascota.put("fotoUrl", ""); // Por ahora vacío
        mascota.put("fecha", System.currentTimeMillis());
        mascota.put("userId", userId);

        db.collection("mascotas")
                .add(mascota)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Mascota publicada", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al publicar", Toast.LENGTH_SHORT).show());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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