package com.aipasa.firebase;

public class Mascota {

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

    // Getters y Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getEstado() { return estado; }
    public String getTelefono() { return telefono; }
    public String getInfoAdicional() { return infoAdicional; }
    public String getFotoUrl() { return fotoUrl; }
    public long getFecha() { return fecha; }
    public String getUserId() { return userId; }
}
