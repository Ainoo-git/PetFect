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

    private TextInputEditText etUser;
    private TextInputEditText etPass;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUser = findViewById(R.id.etUsername);
        etPass = findViewById(R.id.etPassword);

        mAuth = FirebaseAuth.getInstance();
    }

    // ESTE método lo llama el botón desde el XML
    public void CreateAccount(View view) {

        String email = etUser.getText().toString().trim();
        String password = etPass.getText().toString().trim();

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

                        Toast.makeText(SignUp.this,
                                "Cuenta creada correctamente",
                                Toast.LENGTH_SHORT).show();

                        mAuth.signOut();

                        startActivity(new Intent(SignUp.this, Login.class));
                        finish();

                    } else {

                        Toast.makeText(SignUp.this,
                                "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
