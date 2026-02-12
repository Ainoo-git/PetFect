package com.aipasa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PublicacionActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;

    private LinearLayout layoutImagen;
    private ImageView imgMascota;
    private TextView txtAddPhoto;
    private Button btnPublicar;
    private CheckBox checkLegal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicacion);

        // Referencias
        layoutImagen = findViewById(R.id.layoutImagen);
        imgMascota = findViewById(R.id.imgMascota);
        txtAddPhoto = findViewById(R.id.txtAddPhoto);
        btnPublicar = findViewById(R.id.btnPublicar);
        checkLegal = findViewById(R.id.checkLegal);

        // Estado inicial
        imgMascota.setVisibility(View.GONE);
        btnPublicar.setEnabled(false);

        // Pulsar recuadro de imagen
        layoutImagen.setOnClickListener(v -> {
            String[] opciones = {"Hacer foto", "Elegir de galería"};

            new AlertDialog.Builder(this)
                    .setTitle("Añadir imagen")
                    .setItems(opciones, (dialog, which) -> {
                        if (which == 0) {
                            Intent cameraIntent =
                                    new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, REQUEST_CAMERA);
                        } else {
                            Intent galleryIntent =
                                    new Intent(Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, REQUEST_GALLERY);
                        }
                    })
                    .show();
        });

        // Checkbox legal → habilita publicar
        checkLegal.setOnCheckedChangeListener((buttonView, isChecked) ->
                btnPublicar.setEnabled(isChecked)
        );

        // Publicar
        btnPublicar.setOnClickListener(v -> {
            // Aquí más adelante irá Firebase
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {

            if (requestCode == REQUEST_CAMERA) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                imgMascota.setImageBitmap(image);
            }

            if (requestCode == REQUEST_GALLERY) {
                Uri imageUri = data.getData();
                imgMascota.setImageURI(imageUri);
            }

            // Mostrar imagen y ocultar recuadro
            layoutImagen.setVisibility(View.GONE);
            txtAddPhoto.setVisibility(View.GONE);
            imgMascota.setVisibility(View.VISIBLE);
        }
    }
}
