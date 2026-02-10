package com.aipasa.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.main.MainActivity;
import com.aipasa.R;
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

        // Comprobar si el usuario ya inició sesión
        SharedPreferences prefs = getSharedPreferences("petfect_prefs", MODE_PRIVATE);
        boolean logueado = prefs.getBoolean("usuarioLogueado", false);

        if (logueado) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    // Botón LOGIN
    public void SignLogin(View view) {

        String username = etUser.getText() != null
                ? etUser.getText().toString().trim()
                : "";

        String password = etPass.getText() != null
                ? etPass.getText().toString().trim()
                : "";

        //Campos vacíos
        if (username.isEmpty()) {
            Toast.makeText(this,
                    "Introduce un nombre de usuario",
                    Toast.LENGTH_SHORT).show();
            etUser.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this,
                    "Introduce una contraseña",
                    Toast.LENGTH_SHORT).show();
            etPass.requestFocus();
            return;
        }

        // Comprobar si el usuario existe
        SharedPreferences prefs = getSharedPreferences("petfect_prefs", MODE_PRIVATE);

        String userGuardado = prefs.getString("registered_user", null);
        String passGuardada = prefs.getString("registered_pass", null);

        if (userGuardado == null) {
            // No hay ningún usuario registrado
            Toast.makeText(this,
                    "El usuario no existe",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!username.equals(userGuardado)) {
            // Usuario incorrecto
            Toast.makeText(this,
                    "El usuario no existe",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(passGuardada)) {
            // Contraseña incorrecta
            Toast.makeText(this,
                    "Contraseña incorrecta",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        //Login correcto → guardar sesión
        prefs.edit()
                .putBoolean("usuarioLogueado", true)
                .putString("username", username)
                .apply();

        Toast.makeText(this,
                "Inicio de sesión correcto",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Botón SIGN UP
    public void OpenSignup(View view) {
        Intent i = new Intent(Login.this, SignUp.class);
        startActivity(i);
    }
}
