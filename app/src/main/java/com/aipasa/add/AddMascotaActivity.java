package com.aipasa.add;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aipasa.R;
import com.aipasa.data.Mascota;
import com.aipasa.data.MascotaDatabase;

public class AddMascotaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mascota);

        CheckBox cbPerro = findViewById(R.id.cbPerro);
        CheckBox cbGato = findViewById(R.id.cbGato);
        CheckBox cbAdopcion = findViewById(R.id.cbAdopcion);
        CheckBox cbPerdido = findViewById(R.id.cbPerdido);

        EditText etChip = findViewById(R.id.etChip);
        EditText etInfo = findViewById(R.id.etInfo);

        Button btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(v -> {
            Mascota mascota = new Mascota();

            mascota.tipo = cbPerro.isChecked() ? "perro" : "gato";
            mascota.estado = cbAdopcion.isChecked() ? "adopcion" : "perdido";
            mascota.chip = etChip.getText().toString();
            mascota.infoAdicional = etInfo.getText().toString();
            mascota.fecha = System.currentTimeMillis();
            mascota.fotoUri = "";

            MascotaDatabase.getInstance(this)
                    .mascotaDao()
                    .insertar(mascota);

            Toast.makeText(this, "Mascota guardada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
