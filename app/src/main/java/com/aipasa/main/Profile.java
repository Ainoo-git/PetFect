package com.aipasa.main; // Cambia esto si tu paquete es distinto

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.aipasa.auth.Login;
import com.aipasa.creditos.Creditos;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        // Va a creditos
        Button btnConfiguracion = findViewById(R.id.btnConfiguracion);

        btnConfiguracion.setOnClickListener(v ->
                startActivity(new Intent(this, Creditos.class))
        );

        // Toolbar back
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Nombre en el perfil
        TextView tvNombre = findViewById(R.id.nombre2);

        SharedPreferences prefs = getSharedPreferences("petfect_prefs", MODE_PRIVATE);
        String username = prefs.getString("username", "Nombre");

        tvNombre.setText(username);

    }
    public void openLogin2(View view) {
        // Cierra sesión en Firebase
        FirebaseAuth.getInstance().signOut();


        SharedPreferences prefs = getSharedPreferences("petfect_prefs", MODE_PRIVATE);
        prefs.edit().clear().apply();

        // Abre la Activity de Login
        startActivity(new Intent(this, Login.class));
        finish(); // para que el usuario no pueda volver con el botón atrás
    }
//    public void openMapa(View view) {
//        Intent intent = new Intent(this, MapaActivity.class);
//        startActivity(intent);
//    }
}
