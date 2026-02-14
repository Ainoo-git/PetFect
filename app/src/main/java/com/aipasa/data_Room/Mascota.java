package com.aipasa.data_Room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "mascotas")
public class Mascota {

    @PrimaryKey
    @NonNull
    private String id;

    private String nombre;
    private String tipo;
    private String estado;
    private String telefono;
    private String infoAdicional;
    private String fotoUrl;
    private long fecha;
    private String userId;

    public Mascota() {
    }

    public Mascota(String id, String nombre, String tipo, String estado,
                   String telefono, String infoAdicional,
                   String fotoUrl, long fecha, String userId) {

        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.telefono = telefono;
        this.infoAdicional = infoAdicional;
        this.fotoUrl = fotoUrl;
        this.fecha = fecha;
        this.userId = userId;
    }

    // Getters y setters
    @NonNull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }
}

