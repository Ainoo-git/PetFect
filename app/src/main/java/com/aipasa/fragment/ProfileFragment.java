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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.aipasa.auth.Login;
import com.aipasa.firebase.MascotaAdapter; // ✅ USAMOS EL MISMO QUE HOME
import com.aipasa.firebase.SupabaseClient;
import com.aipasa.main.MapaActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.*;

public class ProfileFragment extends Fragment {

    private TextView tvNombre;
    private ImageView profileImage;

    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private ActivityResultLauncher<String> galeriaLauncher;

    private Uri imageUri;
    private Bitmap imageBitmap;

    //  RECYCLER
    private RecyclerView rvMascotas;
    private MascotaAdapter adapter;
    private List<DocumentSnapshot> listaMascotas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Toolbar
        MaterialToolbar toolbar = view.findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        // Nombre usuario
        tvNombre = view.findViewById(R.id.nombre2);
        SharedPreferences prefs = requireContext().getSharedPreferences("petfect_prefs", getContext().MODE_PRIVATE);
        String username = prefs.getString("username", "Nombre");
        tvNombre.setText(username);

        // Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // Imagen perfil
        profileImage = view.findViewById(R.id.profile_image);

        galeriaLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageUri = uri;
                        imageBitmap = null;
                        profileImage.setImageURI(uri);
                        procesarYSubirImagenPerfil(imageUri, null);
                    }
                }
        );

        cargarImagenPerfil();
        profileImage.setOnClickListener(v -> mostrarOpcionesImagen());

        // Botones
        Button btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this::openLogin);

        Button btnConfiguracion = view.findViewById(R.id.btnConfiguracion);
        btnConfiguracion.setOnClickListener(v -> openMapa());

        // RECYCLER
        rvMascotas = view.findViewById(R.id.rvMascotas);

        rvMascotas.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvMascotas.setNestedScrollingEnabled(false);

        adapter = new MascotaAdapter(listaMascotas);
        rvMascotas.setAdapter(adapter);

        cargarMisMascotas();

        return view;
    }

    // SOLO MASCOTAS DEL USUARIO
    private void cargarMisMascotas() {

        if (currentUser == null) return;

        db.collection("mascotas")
                .whereEqualTo("userId", currentUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    listaMascotas.clear();

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        listaMascotas.add(doc);
                    }

                    adapter.notifyDataSetChanged();
                });
    }


    private void mostrarOpcionesImagen() {
        String[] opciones = {"Hacer foto", "Elegir de galería"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Cambiar foto de perfil")
                .setItems(opciones, (dialog, which) -> {
                    if (which == 0) verificarPermisoCamara();
                    else abrirGaleria();
                })
                .show();
    }

    private void verificarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            abrirCamara();
        }
    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);
    }

    private void abrirGaleria() {
        galeriaLauncher.launch("image/*");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == 101 && data != null) {
            imageBitmap = (Bitmap) data.getExtras().get("data");
            profileImage.setImageBitmap(imageBitmap);
            procesarYSubirImagenPerfil(null, imageBitmap);
        }
    }

    private void procesarYSubirImagenPerfil(Uri imageUri, Bitmap imageBitmap) {
        try {
            byte[] imageBytes;

            if (imageUri != null) {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                int nRead;
                byte[] data = new byte[16384];

                while ((nRead = inputStream.read(data)) != -1) {
                    buffer.write(data, 0, nRead);
                }

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

    private void subirImagenPerfilASupabase(byte[] bytes) {

        String fileName = "perfiles/" + currentUser.getUid() + ".jpg";

        RequestBody requestBody = RequestBody.create(bytes, MediaType.parse("image/jpeg"));

        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL + "/storage/v1/object/" +
                        SupabaseClient.BUCKET_NAME + "/" + fileName)
                .addHeader("apikey", SupabaseClient.SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SupabaseClient.SUPABASE_KEY)
                .put(requestBody)
                .build();

        SupabaseClient.getClient().newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, java.io.IOException e) {}

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {

                    String publicUrl = SupabaseClient.SUPABASE_URL +
                            "/storage/v1/object/public/" +
                            SupabaseClient.BUCKET_NAME + "/" + fileName;

                    Map<String, Object> data = new HashMap<>();
                    data.put("fotoPerfil", publicUrl);

                    db.collection("usuarios")
                            .document(currentUser.getUid())
                            .set(data, SetOptions.merge());

                    requireActivity().runOnUiThread(() ->
                            Glide.with(requireContext()).load(publicUrl).into(profileImage));
                }
            }
        });
    }

    private void cargarImagenPerfil() {
        if (currentUser == null) return;

        db.collection("usuarios")
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    String url = doc.getString("fotoPerfil");
                    if (url != null) {
                        Glide.with(requireContext()).load(url).into(profileImage);
                    }
                });
    }



    private void openLogin(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(requireContext(), Login.class));
        requireActivity().finish();
    }

    private void openMapa() {
        startActivity(new Intent(requireContext(), MapaActivity.class));
    }
}