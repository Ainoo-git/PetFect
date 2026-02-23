package com.aipasa.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.aipasa.R;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class ActivityTarjeta extends AppCompatActivity {

    private ImageView imgAccion;
    private TextView txtTitulo, txtFecha, txtDescripcion;
    private Button btnVerMas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjeta);

        imgAccion = findViewById(R.id.imgAccion);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtFecha = findViewById(R.id.txtFecha);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        btnVerMas = findViewById(R.id.btnVerMas);

        // Obtener ID de la publicación
        String articuloId = getIntent().getStringExtra("ARTICULO_ID");
        if (articuloId == null || articuloId.isEmpty()) {
            Toast.makeText(this, "No se recibió la publicación", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarMascota(articuloId);
    }

    private void cargarMascota(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("mascotas").document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        Toast.makeText(this, "La publicación no existe", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    DocumentSnapshot doc = documentSnapshot;

                    // Cargar datos en la UI
                    txtTitulo.setText(doc.getString("nombre"));
                    txtFecha.setText(doc.getString("tipo") + " • " + doc.getString("estado"));

                    String descripcion = "Edad: " + doc.getString("edad") + "\n"
                            + "Chip: " + doc.getString("chip") + "\n"
                            + "Teléfono: " + doc.getString("telefono") + "\n"
                            + "Info adicional: " + doc.getString("infoAdicional");
                    txtDescripcion.setText(descripcion);

                    // Imagen con Glide + placeholder
                    String fotoUrl = doc.getString("fotoUrl");
                    if (fotoUrl != null && !fotoUrl.isEmpty()) {
                        Glide.with(this)
                                .load(fotoUrl)
                                .placeholder(R.drawable.logologin)
                                .into(imgAccion);
                    } else {
                        imgAccion.setImageResource(R.drawable.logologin);
                    }

                    // Botón de contacto
                    btnVerMas.setOnClickListener(v -> {
                        String telefono = doc.getString("telefono");
                        if (telefono != null && !telefono.isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + telefono));
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "No hay número de contacto disponible", Toast.LENGTH_SHORT).show();
                        }
                    });

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al cargar la publicación", Toast.LENGTH_SHORT).show());
    }
}