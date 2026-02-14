package com.aipasa.repository;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mascotas")
public class Mascota_Hibrido {

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

    // Constructor vacío obligatorio para Firebase
    public Mascota_Hibrido() {}

    public Mascota_Hibrido(String id, String nombre, String tipo, String estado,
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


    @NonNull public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getEstado() { return estado; }
    public String getTelefono() { return telefono; }
    public String getInfoAdicional() { return infoAdicional; }
    public String getFotoUrl() { return fotoUrl; }
    public long getFecha() { return fecha; }
    public String getUserId() { return userId; }

    // SETTERS Room
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setInfoAdicional(String infoAdicional) { this.infoAdicional = infoAdicional; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public void setFecha(long fecha) { this.fecha = fecha; }
    public void setUserId(String userId) { this.userId = userId; }
}
