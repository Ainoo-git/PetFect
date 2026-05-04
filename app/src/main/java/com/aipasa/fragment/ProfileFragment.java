package com.aipasa.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher; // 🔥 NUEVO
import androidx.activity.result.contract.ActivityResultContracts; // 🔥 NUEVO
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aipasa.R;
import com.aipasa.auth.Login;
import com.aipasa.configuracion.ConfiguracionActivity;
import com.aipasa.firebase.SupabaseClient; // 🔥 NUEVO
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions; // 🔥 NUEVO

import java.io.ByteArrayOutputStream; // 🔥 NUEVO
import java.io.InputStream; // 🔥 NUEVO
import java.util.HashMap; // 🔥 NUEVO
import java.util.Map; // 🔥 NUEVO

import okhttp3.*; // 🔥 NUEVO

public class ProfileFragment extends Fragment {

    private TextView tvNombre;
    private static final int PICK_IMAGE_REQUEST = 1001;
    private ImageView profileImage;

    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private ActivityResultLauncher<String> galeriaLauncher;

    private Uri imageUri;
    private Bitmap imageBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflamos el layout
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Toolbar con flecha atrás
        MaterialToolbar toolbar = view.findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());


        // Inicializar vistas
//        tvNombre = view.findViewById(R.id.nombre2);
//        profileImage = view.findViewById(R.id.profile_image);

        // Nombre en el perfil
        tvNombre = view.findViewById(R.id.nombre2);
        SharedPreferences prefs = requireContext().getSharedPreferences("petfect_prefs", getContext().MODE_PRIVATE);
        String username = prefs.getString("username", "Nombre");
        tvNombre.setText(username);

        // Inicializar Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // 🔹 Mostrar nombre
        mostrarNombreUsuario();

        // Imagen de perfil
        profileImage = view.findViewById(R.id.profile_image);

        // launcher galería
        galeriaLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageUri = uri;
                        imageBitmap = null;

                        profileImage.setImageURI(uri);

                        // subir imagen
                        procesarYSubirImagenPerfil(imageUri, null);
                    }
                }
        );

        // Cargar imagen de perfil si ya existe
        cargarImagenPerfil();

        // Click para cambiar imagen
        profileImage.setOnClickListener(v -> mostrarOpcionesImagen());

        // Botón Cerrar sesión
        Button btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::openLogin);

        // Botón Configuración
        Button btnConfiguracion = view.findViewById(R.id.btnConfiguracion);
        btnConfiguracion.setOnClickListener(v -> openConfig());

        // Botón Editar perfil
        Button btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil);
        btnEditarPerfil.setOnClickListener(v -> {
            // Aquí puedes abrir un Activity o Fragment de edición de perfil más adelante
        });

        return view;
    }

    // Abre galería o cámara
    private void mostrarOpcionesImagen() {
        String[] opciones = {"Hacer foto", "Elegir de galería"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Cambiar foto de perfil")
                .setItems(opciones, (dialog, which) -> {
                    if (which == 0) {
                        verificarPermisoCamara();
                    } else {
                        abrirGaleria();
                    }
                })
                .show();
    }

    // Permiso cámara
    private void verificarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            abrirCamara();
        }
    }

    // Abrir cámara
    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);
    }

    // Abrir galería
    // Mostrar nombre usuario
    private void mostrarNombreUsuario() {

        if (currentUser != null) {
            String email = currentUser.getEmail();
            String nombre = (email != null) ? email.split("@")[0] : "Usuario";

            tvNombre.setText(nombre);
        } else {
            tvNombre.setText("Invitado");
        }

        // (Opcional) Si luego queremos usar SharedPreferences en vez de Firebase:
        /*
        SharedPreferences prefs = requireContext().getSharedPreferences("petfect_prefs", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "Usuario");
        tvNombre.setText(username);
        */
    }

    // Abrir galería
    private void abrirGaleria() {
        galeriaLauncher.launch("image/*");
    }

    // Resultado cámara
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == 101 && data != null) {
            imageBitmap = (Bitmap) data.getExtras().get("data");
            imageUri = null;

            profileImage.setImageBitmap(imageBitmap);

            // subir imagen
            procesarYSubirImagenPerfil(null, imageBitmap);
        }
    }

    // igual que PublicacionActivity(Se ha creado un bucket de usuario para meter la url de la imagen )
    private void procesarYSubirImagenPerfil(Uri imageUri, Bitmap imageBitmap) {
        try {
            byte[] imageBytes;

            if (imageUri != null) {

                InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
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

            subirImagenPerfilASupabase(imageBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // subir a Supabase
    private void subirImagenPerfilASupabase(byte[] bytes) {

        String fileName = "perfiles/" + currentUser.getUid() + ".jpg";

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
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(requireContext(),
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

                            // guardar URL en Firestore
                            Map<String, Object> data = new HashMap<>();
                            data.put("fotoPerfil", publicUrl);

                            db.collection("usuarios")
                                    .document(currentUser.getUid())
                                    .set(data, SetOptions.merge());

                            // Mostrar imagen
                            requireActivity().runOnUiThread(() ->
                                    Glide.with(requireContext())
                                            .load(publicUrl)
                                            .into(profileImage));
                        }
                    }
                });
    }

    // Cargar imagen de perfil si ya existe
    private void cargarImagenPerfil() {
        if (currentUser == null) return;

        db.collection("usuarios")
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String url = documentSnapshot.getString("fotoPerfil");
                        if (url != null && !url.isEmpty()) {
                            Glide.with(requireContext())
                                    .load(url)
                                    .centerCrop()
                                    .placeholder(R.drawable.baseline_person_24)
                                    .into(profileImage);
                        }
                    }
                });
    }

    // Cerrar sesión
    private void openLogin(View view) {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences prefs = requireContext().getSharedPreferences("petfect_prefs", getContext().MODE_PRIVATE);
        prefs.edit().clear().apply();

        Intent intent = new Intent(requireContext(), Login.class);
        startActivity(intent);
        requireActivity().finish();
    }

    // Ir al mapa
    private void openConfig() {
        startActivity(new Intent(requireContext(), ConfiguracionActivity.class));
    }
}