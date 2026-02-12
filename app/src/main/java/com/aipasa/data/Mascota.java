package com.aipasa.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mascotas")
public class Mascota {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String tipo;
    public String estado;
    public String fotoUri;
    public String chip;          // opcional
    public String infoAdicional; // opcional
    public long fecha;
}
