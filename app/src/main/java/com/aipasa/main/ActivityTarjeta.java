package com.aipasa.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

                    //  DATOS BÁSICOS

                    String nombre = doc.getString("nombre");
                    String tipo = doc.getString("tipo");
                    String estado = doc.getString("estado");
                    String edad = doc.getString("edad");
                    String chip = doc.getString("chip");
                    String telefono = doc.getString("telefono");
                    String info = doc.getString("infoAdicional");
                    String fotoUrl = doc.getString("fotoUrl");

                    txtTitulo.setText(nombre != null ? nombre : "Sin nombre");

                    // Tipo y estado formateados
                    String tipoEstado = "";
                    if (tipo != null && !tipo.isEmpty()) {
                        tipoEstado += tipo.substring(0,1).toUpperCase() + tipo.substring(1);
                    }
                    if (estado != null && !estado.isEmpty()) {
                        if (!tipoEstado.isEmpty()) tipoEstado += " • ";
                        tipoEstado += estado.substring(0,1).toUpperCase() + estado.substring(1);
                    }
                    txtFecha.setText(tipoEstado);

                    //  DESCRIPCIÓN

                    String descripcion =
                            "Edad: " + (edad != null ? edad : "No especificada") + "\n" +
                                    "Chip: " + (chip != null ? chip : "No especificado") + "\n" +
                                    "Teléfono: " + (telefono != null ? telefono : "No disponible") + "\n" +
                                    "Info adicional: " + (info != null ? info : "Sin información");

                    txtDescripcion.setText(descripcion);

                    //  IMAGEN
                    if (fotoUrl != null && !fotoUrl.isEmpty()) {
                        Glide.with(this)
                                .load(fotoUrl)
                                .placeholder(R.drawable.logologin)
                                .error(R.drawable.logologin)
                                .centerCrop()
                                .into(imgAccion);
                    } else {
                        imgAccion.setImageResource(R.drawable.logologin);
                    }

                    // BOTÓN CONTACTO

                    btnVerMas.setOnClickListener(v -> {
                        if (telefono != null && !telefono.isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + telefono));
                            startActivity(intent);
                        } else {
                            Toast.makeText(this,
                                    "No hay número de contacto disponible",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Error al cargar la publicación",
                                Toast.LENGTH_SHORT).show());
    }
}