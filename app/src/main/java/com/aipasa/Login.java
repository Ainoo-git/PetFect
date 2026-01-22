package com.aipasa;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    private TextInputEditText etUser;
    private TextInputEditText etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
    }

    // Lo llama el botón LOGIN (android:onClick="SignLogin")
    public void SignLogin(View view) {
        String username = etUser.getText() != null ? etUser.getText().toString().trim() : "";
        String password = etPass.getText() != null ? etPass.getText().toString().trim() : "";

        if (username.isEmpty()) {
            Toast.makeText(this, "Introduce un nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Introduce una contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardamos el usuario para usarlo en Profile
        SharedPreferences prefs = getSharedPreferences("petfect_prefs", MODE_PRIVATE);
        prefs.edit()
                .putString("username", username)
                .apply();

        startActivity(new Intent(this, MainActivity.class));
        finish();

        // Ir a la pantalla principal
        Intent i = new Intent(Login.this, MainActivity.class);
        startActivity(i);
    }

    // Lo llama el botón SIGN UP (android:onClick="OpenSignup")
    public void OpenSignup(View view) {
        Intent i = new Intent(Login.this, SignUp.class);
        startActivity(i);
    }
}
