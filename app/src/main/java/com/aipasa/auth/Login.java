package com.aipasa.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.main.MainActivity;
import com.aipasa.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private TextInputEditText etUser;
    private TextInputEditText etPass;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);

        mAuth = FirebaseAuth.getInstance();

        // Si ya está logueado, entrar directamente
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void SignLogin(View view) {

        String email = etUser.getText().toString().trim();
        String password = etPass.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Toast.makeText(this,
                                "Inicio de sesión correcto",
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(this, MainActivity.class));
                        finish();

                    } else {

                        Toast.makeText(this,
                                "Credenciales incorrectas",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
