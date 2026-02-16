package com.aipasa.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // IMPORTANTE: usar los IDs reales del XML
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        mAuth = FirebaseAuth.getInstance();
    }

    // Coincide con android:onClick="crearCuenta"
    public void crearCuenta(View view) {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this,
                    "La contraseña debe tener mínimo 6 caracteres",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(this,
                                "Cuenta creada correctamente",
                                Toast.LENGTH_SHORT).show();

                        mAuth.signOut();

                        startActivity(new Intent(this, Login.class));
                        finish();

                    } else {

                        Toast.makeText(this,
                                "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void openLogin(View view) {
        startActivity(new Intent(this, Login.class));
        finish();
    }
}
