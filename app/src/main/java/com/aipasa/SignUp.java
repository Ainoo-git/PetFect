package com.aipasa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class SignUp extends AppCompatActivity {

    private TextInputEditText etUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUser = findViewById(R.id.etUser);
    }


    public void CreateAccount(View view) {
        String username = (etUser.getText() != null) ? etUser.getText().toString().trim() : "";

        if (username.isEmpty()) {
            Toast.makeText(this, "Introduce un nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("petfect_prefs", MODE_PRIVATE);
        prefs.edit()
                .putString("username", username)
                .apply();

        startActivity(new Intent(this, PreferenciasActivity.class));
        finish();

    }
}
