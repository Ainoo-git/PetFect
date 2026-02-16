package com.aipasa.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aipasa.R;

public class PublicacionActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int CAMERA_PERMISSION_CODE = 200;

    private LinearLayout layoutImagen;
    private ImageView imgMascota;
    private TextView txtAddPhoto;
    private Button btnPublicar;
    private CheckBox checkLegal;

    private Uri imageUri;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicacion);

        layoutImagen = findViewById(R.id.layoutImagen);
        imgMascota = findViewById(R.id.imgMascota);
        txtAddPhoto = findViewById(R.id.txtAddPhoto);
        btnPublicar = findViewById(R.id.btnPublicar);
        checkLegal = findViewById(R.id.checkLegal);

        imgMascota.setVisibility(View.GONE);
        btnPublicar.setEnabled(false);

        layoutImagen.setOnClickListener(v -> mostrarOpcionesImagen());

        checkLegal.setOnCheckedChangeListener((buttonView, isChecked) ->
                btnPublicar.setEnabled(isChecked)
        );
    }

    private void mostrarOpcionesImagen() {

        String[] opciones = {"Hacer foto", "Elegir de galería"};

        new AlertDialog.Builder(this)
                .setTitle("Añadir imagen")
                .setItems(opciones, (dialog, which) -> {

                    if (which == 0) {

                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_GRANTED) {

                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, REQUEST_CAMERA);

                        } else {

                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.CAMERA},
                                    CAMERA_PERMISSION_CODE);
                        }

                    } else {

                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, REQUEST_GALLERY);
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }
        }
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
