package com.aipasa.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.aipasa.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PublicacionFragment extends Fragment {

    private LinearLayout layoutImagen;
    private ImageView imgMascota;

    private TextInputEditText etNombre, etTelefono, etEdad, etChip, etInfoAdicional, etOtroTipo;

    private CheckBox cbPerdido, cbAdopcion, cbPerro, cbGato, cbOtro, checkLegal;

    private MaterialButton btnPublicar;

    private Uri imageUri;
    private Bitmap imageBitmap;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_publicacion, container, false);

        initViews(view);
        setupListeners();

        return view;
    }

    private void initViews(View view) {

        layoutImagen = view.findViewById(R.id.layoutImagen);
        imgMascota = view.findViewById(R.id.imgMascota);

        etNombre = view.findViewById(R.id.etNombre);
        etTelefono = view.findViewById(R.id.etTelefono);
        etEdad = view.findViewById(R.id.etEdad);
        etChip = view.findViewById(R.id.etChip);
        etInfoAdicional = view.findViewById(R.id.etInfoAdicional);
        etOtroTipo = view.findViewById(R.id.etOtroTipo);

        cbPerdido = view.findViewById(R.id.cbPerdido);
        cbAdopcion = view.findViewById(R.id.cbAdopcion);
        cbPerro = view.findViewById(R.id.cbPerro);
        cbGato = view.findViewById(R.id.cbGato);
        cbOtro = view.findViewById(R.id.cbOtro);
        checkLegal = view.findViewById(R.id.checkLegal);

        btnPublicar = view.findViewById(R.id.btnPublicar);

        imgMascota.setVisibility(View.GONE);
        btnPublicar.setEnabled(false);
    }

    private void setupListeners() {

        layoutImagen.setOnClickListener(v -> mostrarOpcionesImagen());

        checkLegal.setOnCheckedChangeListener((buttonView, isChecked) ->
                btnPublicar.setEnabled(isChecked));

        cbOtro.setOnCheckedChangeListener((buttonView, isChecked) ->
                etOtroTipo.setVisibility(isChecked ? View.VISIBLE : View.GONE));

        btnPublicar.setOnClickListener(v -> guardarMascota());
    }

    private void mostrarOpcionesImagen() {

        String[] opciones = {"Hacer foto", "Elegir de galería"};

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
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

    private void guardarMascota() {

        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String edad = etEdad.getText().toString().trim();
        String chip = etChip.getText().toString().trim();
        String infoAdicional = etInfoAdicional.getText().toString().trim();

        String estado = null;
        if (cbPerdido.isChecked()) estado = "perdido";
        if (cbAdopcion.isChecked()) estado = "adopcion";

        String tipo = null;
        if (cbPerro.isChecked()) tipo = "perro";
        if (cbGato.isChecked()) tipo = "gato";
        if (cbOtro.isChecked()) tipo = etOtroTipo.getText().toString().trim();

        if (nombre.isEmpty() || estado == null || tipo == null || tipo.isEmpty()) {
            Toast.makeText(requireContext(),
                    "Completa los campos obligatorios",
                    Toast.LENGTH_SHORT).show();
            return;
        }

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
        mascota.put("fotoUrl", "");
        mascota.put("fecha", System.currentTimeMillis());

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mascota.put("userId",
                    FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

        db.collection("mascotas")
                .document(id)
                .set(mascota)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(requireContext(),
                            "Mascota publicada",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(),
                                "Error al publicar",
                                Toast.LENGTH_SHORT).show());
    }
}