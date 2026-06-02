package com.aipasa.editar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditarPerfilActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etEmail;
    EditText etPassword;
    Button btnGuardar;

    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser user;

    String currentUsername = "";
    String currentEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        configurarToolbar();
        inicializarVistas();
        cargarDatosUsuario();
        configurarBotonGuardar();
    }

    private void configurarToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void inicializarVistas() {
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnGuardar = findViewById(R.id.btnGuardar);
    }

    private void cargarDatosUsuario() {
        if (user != null) {
            currentEmail = user.getEmail();
            etEmail.setText(currentEmail);

            db.collection("usuarios")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            currentUsername = documentSnapshot.getString("username");
                            etUsername.setText(currentUsername);
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(
                                    this,
                                    "Error cargando usuario",
                                    Toast.LENGTH_SHORT
                            ).show()
                    );
        }
    }

    private void configurarBotonGuardar() {
        btnGuardar.setOnClickListener(v -> {
            if (user == null) {
                return;
            }

            String newUsername = etUsername.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();
            String newPassword = etPassword.getText().toString().trim();

            if (!newUsername.isEmpty() && !newUsername.equals(currentUsername)) {
                Map<String, Object> map = new HashMap<>();
                map.put("username", newUsername);

                db.collection("usuarios")
                        .document(user.getUid())
                        .update(map);

                currentUsername = newUsername;
            }

            if (!newEmail.isEmpty() && !newEmail.equals(currentEmail)) {
                user.updateEmail(newEmail)
                        .addOnSuccessListener(unused ->
                                Toast.makeText(
                                        this,
                                        "Email actualizado",
                                        Toast.LENGTH_SHORT
                                ).show()
                        )
                        .addOnFailureListener(e ->
                                Toast.makeText(
                                        this,
                                        "Error email: " + e.getMessage(),
                                        Toast.LENGTH_SHORT
                                ).show()
                        );

                currentEmail = newEmail;
            }

            if (!newPassword.isEmpty()) {
                user.updatePassword(newPassword)
                        .addOnSuccessListener(unused ->
                                Toast.makeText(
                                        this,
                                        "Contraseña actualizada",
                                        Toast.LENGTH_SHORT
                                ).show()
                        )
                        .addOnFailureListener(e ->
                                Toast.makeText(
                                        this,
                                        "Error password: " + e.getMessage(),
                                        Toast.LENGTH_SHORT
                                ).show()
                        );
            }

            btnGuardar.postDelayed(() -> {
                Toast.makeText(
                        this,
                        "Perfil actualizado",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            }, 1000);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}