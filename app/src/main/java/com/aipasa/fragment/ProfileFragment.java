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
import com.aipasa.configuracion.ConfiguracionActivity;
import com.aipasa.editar.EditarPerfilActivity;
import com.aipasa.firebase.MascotaProfileAdapter;
import com.aipasa.firebase.SupabaseClient;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
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
    private static final int PICK_IMAGE_REQUEST = 1001;
    private ImageView profileImage;

    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private ActivityResultLauncher<String> galeriaLauncher;

    private Uri imageUri;
    private Bitmap imageBitmap;

    private ListenerRegistration usuarioListener;

    // RECYCLER
    private RecyclerView rvMascotas;
    private MascotaProfileAdapter adapter;
    private List<DocumentSnapshot> listaMascotas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        MaterialToolbar toolbar = view.findViewById(R.id.topAppBar);

        toolbar.setNavigationOnClickListener(v -> {
            ((BottomNavigationView)
                    requireActivity().findViewById(R.id.bottom_nav))
                    .setSelectedItemId(R.id.home);
        });

        tvNombre = view.findViewById(R.id.nombre2);
        profileImage = view.findViewById(R.id.profile_image);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        escucharCambiosUsuario();

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

        profileImage.setOnClickListener(v -> mostrarOpcionesImagen());

        Button btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(v -> mostrarConfirmacionCerrarSesion());

        Button btnConfiguracion = view.findViewById(R.id.btnConfiguracion);
        Button btnEditPerfil = view.findViewById(R.id.btnEditarPerfil);

        btnConfiguracion.setOnClickListener(v -> openConfig());
        btnEditPerfil.setOnClickListener(v -> openEditPerfil());

        rvMascotas = view.findViewById(R.id.rvMascotas);
        rvMascotas.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvMascotas.setNestedScrollingEnabled(false);

        adapter = new MascotaProfileAdapter(requireContext(), listaMascotas);
        rvMascotas.setAdapter(adapter);

        cargarMisMascotas();

        return view;
    }

    private void escucharCambiosUsuario() {
        if (currentUser == null) {
            tvNombre.setText("Usuario");
            profileImage.setImageResource(R.drawable.baseline_person_24);
            return;
        }

        usuarioListener = db.collection("usuarios")
                .document(currentUser.getUid())
                .addSnapshotListener((doc, error) -> {
                    if (!isAdded()) {
                        return;
                    }

                    if (error != null || doc == null || !doc.exists()) {
                        tvNombre.setText("Usuario");
                        profileImage.setImageResource(R.drawable.baseline_person_24);
                        return;
                    }

                    String username = doc.getString("username");

                    if (username != null && !username.trim().isEmpty()) {
                        tvNombre.setText(username);
                    } else {
                        tvNombre.setText("Usuario");
                    }

                    String fotoPerfil = doc.getString("fotoPerfil");

                    if (fotoPerfil != null && !fotoPerfil.trim().isEmpty()) {
                        Glide.with(requireContext())
                                .load(fotoPerfil)
                                .circleCrop()
                                .placeholder(R.drawable.baseline_person_24)
                                .error(R.drawable.baseline_person_24)
                                .into(profileImage);
                    } else {
                        profileImage.setImageResource(R.drawable.baseline_person_24);
                    }
                });
    }

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
                    if (which == 0) {
                        verificarPermisoCamara();
                    } else {
                        abrirGaleria();
                    }
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
    public void onActivityResult(
            int requestCode,
            int resultCode,
            @Nullable Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == 101 && data != null && data.getExtras() != null) {
            imageBitmap = (Bitmap) data.getExtras().get("data");

            profileImage.setImageBitmap(imageBitmap);

            procesarYSubirImagenPerfil(null, imageBitmap);
        }
    }

    private void procesarYSubirImagenPerfil(Uri imageUri, Bitmap imageBitmap) {
        try {
            byte[] imageBytes;

            if (imageUri != null) {
                InputStream inputStream =
                        requireContext().getContentResolver().openInputStream(imageUri);

                if (inputStream == null) {
                    Toast.makeText(
                            requireContext(),
                            "No se pudo leer la imagen",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                int nRead;
                byte[] data = new byte[16384];

                while ((nRead = inputStream.read(data)) != -1) {
                    buffer.write(data, 0, nRead);
                }

                imageBytes = buffer.toByteArray();

                inputStream.close();

            } else {
                if (imageBitmap == null) {
                    Toast.makeText(
                            requireContext(),
                            "No se pudo preparar la imagen",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                imageBitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        80,
                        baos
                );

                imageBytes = baos.toByteArray();
            }

            subirImagenPerfilASupabase(imageBytes);

        } catch (Exception e) {
            e.printStackTrace();

            if (isAdded()) {
                Toast.makeText(
                        requireContext(),
                        "Error preparando imagen",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    private void subirImagenPerfilASupabase(byte[] bytes) {
        if (currentUser == null) {
            Toast.makeText(
                    requireContext(),
                    "No hay usuario iniciado",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String fileName =
                "perfiles/" +
                        currentUser.getUid() +
                        "_" +
                        System.currentTimeMillis() +
                        ".jpg";

        RequestBody requestBody =
                RequestBody.create(
                        bytes,
                        MediaType.parse("image/jpeg")
                );

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

        SupabaseClient.getClient()
                .newCall(request)
                .enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, java.io.IOException e) {
                        if (getActivity() == null) {
                            return;
                        }

                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(
                                        requireContext(),
                                        "Error subiendo foto de perfil",
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

                            Map<String, Object> data = new HashMap<>();
                            data.put("fotoPerfil", publicUrl);

                            db.collection("usuarios")
                                    .document(currentUser.getUid())
                                    .set(data, SetOptions.merge())
                                    .addOnSuccessListener(unused -> {
                                        if (getActivity() == null) {
                                            return;
                                        }

                                        requireActivity().runOnUiThread(() ->
                                                Toast.makeText(
                                                        requireContext(),
                                                        "Foto de perfil actualizada",
                                                        Toast.LENGTH_SHORT
                                                ).show()
                                        );
                                    })
                                    .addOnFailureListener(e -> {
                                        if (getActivity() == null) {
                                            return;
                                        }

                                        requireActivity().runOnUiThread(() ->
                                                Toast.makeText(
                                                        requireContext(),
                                                        "Error guardando foto en Firebase",
                                                        Toast.LENGTH_SHORT
                                                ).show()
                                        );
                                    });

                        } else {
                            if (getActivity() == null) {
                                return;
                            }

                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(
                                            requireContext(),
                                            "Error Supabase: " + response.code(),
                                            Toast.LENGTH_SHORT
                                    ).show()
                            );
                        }

                        response.close();
                    }
                });
    }

    private void mostrarConfirmacionCerrarSesion() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.cerrar_sesion))
                .setMessage(getString(R.string.confirmar_cerrar_sesion))
                .setPositiveButton(getString(R.string.si_cerrar_sesion), (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();

                    Intent intent = new Intent(requireContext(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .setNegativeButton(getString(R.string.cancelar), null)
                .show();
    }

    private void openConfig() {
        startActivity(new Intent(requireContext(), ConfiguracionActivity.class));
    }

    private void openEditPerfil() {
        startActivity(new Intent(requireContext(), EditarPerfilActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (usuarioListener != null) {
            usuarioListener.remove();
            usuarioListener = null;
        }
    }
}