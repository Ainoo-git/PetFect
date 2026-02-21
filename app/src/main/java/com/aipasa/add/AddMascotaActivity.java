package com.aipasa.add;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public classAddMascotaActivity extends AppCompatActivity {

    private EditText etNombre, etTelefono, etEdad, etChip, etInfoAdicional, etOtroTipo;
    private CheckBox cbPerdido, cbAdopcion;
    private CheckBox cbPerro, cbGato, cbOtro;
    private CheckBox checkLegal;
    private Button btnPublicar;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicacion);

        // Referencias
        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etEdad = findViewById(R.id.etEdad);
        etChip = findViewById(R.id.etChip);
        etInfoAdicional = findViewById(R.id.etInfoAdicional);
        etOtroTipo = findViewById(R.id.etOtroTipo);

        cbPerdido = findViewById(R.id.cbPerdido);
        cbAdopcion = findViewById(R.id.cbAdopcion);

        cbPerro = findViewById(R.id.cbPerro);
        cbGato = findViewById(R.id.cbGato);
        cbOtro = findViewById(R.id.cbOtro);

        checkLegal = findViewById(R.id.checkLegal);
        btnPublicar = findViewById(R.id.btnPublicar);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnPublicar.setOnClickListener(v -> publicarMascota());
    }

    private void publicarMascota() {

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkLegal.isChecked()) {
            Toast.makeText(this, "Debes aceptar la condición legal", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid();

        // Estado
        String estado = "";
        if (cbPerdido.isChecked()) estado = "perdido";
        else if (cbAdopcion.isChecked()) estado = "adopcion";

        // Tipo
        String tipo = "";
        if (cbPerro.isChecked()) tipo = "perro";
        else if (cbGato.isChecked()) tipo = "gato";
        else if (cbOtro.isChecked()) tipo = etOtroTipo.getText().toString();

        Map<String, Object> mascota = new HashMap<>();
        mascota.put("nombre", etNombre.getText().toString());
        mascota.put("telefono", etTelefono.getText().toString());
        mascota.put("edad", etEdad.getText().toString());
        mascota.put("chip", etChip.getText().toString());
        mascota.put("infoAdicional", etInfoAdicional.getText().toString());
        mascota.put("estado", estado);
        mascota.put("tipo", tipo);
        mascota.put("fecha", System.currentTimeMillis());
        mascota.put("userId", userId);

        db.collection("mascotas")
                .add(mascota)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Mascota publicada", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al publicar", Toast.LENGTH_SHORT).show();
                });
    }
}
