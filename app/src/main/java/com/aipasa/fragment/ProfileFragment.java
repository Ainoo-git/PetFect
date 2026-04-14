package com.aipasa.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aipasa.R;
import com.aipasa.auth.Login;
import com.aipasa.configuracion.ConfiguracionFragment;
import com.aipasa.main.MapaActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    private TextView tvNombre;
    private static final int PICK_IMAGE_REQUEST = 1001;
    private ImageView profileImage;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private StorageReference storageRef;

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
        storageRef = FirebaseStorage.getInstance().getReference("profile_images");

        // 🔹 Mostrar nombre
        mostrarNombreUsuario();

        // Imagen de perfil
        profileImage = view.findViewById(R.id.profile_image);
        cargarImagenPerfil();
        profileImage.setOnClickListener(v -> abrirGaleria());

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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == Activity.RESULT_OK &&
                data != null &&
                data.getData() != null) {

            Uri imageUri = data.getData();
            subirImagenFirebase(imageUri);
        }
    }

    // Subir imagen a Firebase Storage y guardar URL en Firestore
    private void subirImagenFirebase(Uri imageUri) {

        if (currentUser == null) return;

        StorageReference fileRef = storageRef.child(currentUser.getUid() + ".jpg");

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot ->
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {

                            db.collection("usuarios")
                                    .document(currentUser.getUid())
                                    .update("fotoPerfil", uri.toString())
                                    .addOnSuccessListener(aVoid -> {

                                        Glide.with(requireContext())
                                                .load(uri)
                                                .centerCrop()
                                                .placeholder(R.drawable.baseline_person_24)
                                                .into(profileImage);

                                        Toast.makeText(requireContext(), "Foto actualizada", Toast.LENGTH_SHORT).show();
                                    });
                        }))
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), "Error subiendo imagen", Toast.LENGTH_SHORT).show());
    }
    // Cargar imagen de perfil si ya existe
    private void cargarImagenPerfil() {

        if (currentUser == null) return;

        db.collection("usuarios")
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(doc -> {

                    if (doc.exists()) {
                        String url = doc.getString("fotoPerfil");

                        if (url != null) {
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
        startActivity(new Intent(requireContext(), ConfiguracionFragment.class));
    }
}