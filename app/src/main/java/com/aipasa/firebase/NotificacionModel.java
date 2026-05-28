package com.aipasa.firebase;

import com.google.firebase.Timestamp;

public class NotificacionModel {

    private String id;

    private String idUsuario;

    private String idMascota;

    private String nombreMascota;

    private String tipo;

    private String imagenUrl;

    private Timestamp fecha;

    private boolean leido;

    public NotificacionModel() {
    }

    public NotificacionModel(
            String idUsuario,
            String idMascota,
            String nombreMascota,
            String tipo,
            String imagenUrl,
            Timestamp fecha,
            boolean leido
    ) {

        this.idUsuario = idUsuario;
        this.idMascota = idMascota;
        this.nombreMascota = nombreMascota;
        this.tipo = tipo;
        this.imagenUrl = imagenUrl;
        this.fecha = fecha;
        this.leido = leido;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getIdMascota() {
        return idMascota;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public String getTipo() {
        return tipo;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }
}