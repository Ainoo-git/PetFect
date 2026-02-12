package com.aipasa.firebase;

public class Mascotaa {

    private String id;
    private String nombre;
    private String tipo;
    private String estado;
    private String telefono;
    private String infoAdicional;
    private long fecha;

    public Mascotaa() {

    }

    public Mascotaa(String nombre, String tipo, String estado,
                   String telefono, String infoAdicional, long fecha) {

        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.telefono = telefono;
        this.infoAdicional = infoAdicional;
        this.fecha = fecha;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getEstado() { return estado; }
    public String getTelefono() { return telefono; }
    public String getInfoAdicional() { return infoAdicional; }
    public long getFecha() { return fecha; }

    public void setId(String id) { this.id = id; }
}
