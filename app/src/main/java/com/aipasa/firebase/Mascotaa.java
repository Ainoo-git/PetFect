package com.aipasa.firebase;

public class Mascotaa {

    private String nombre;
    private String tipo;
    private String estado;
    private String telefono;
    private String infoAdicional;
    private String fotoUrl;
    private long fecha;

    // Constructor vacío obligatorio para Firestore
    public Mascotaa() {
    }

    // Constructor completo
    public Mascotaa(String nombre, String tipo, String estado,
                    String telefono, String infoAdicional,
                    String fotoUrl, long fecha) {

        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.telefono = telefono;
        this.infoAdicional = infoAdicional;
        this.fotoUrl = fotoUrl;
        this.fecha = fecha;
    }

    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getEstado() { return estado; }
    public String getTelefono() { return telefono; }
    public String getInfoAdicional() { return infoAdicional; }
    public String getFotoUrl() { return fotoUrl; }
    public long getFecha() { return fecha; }
}
