package com.aipasa.firebase;

public class Notificacion {

    private String id;
    private String titulo;
    private String mensaje;
    private String imagenUrl;
    private long fecha;
    private String mascotaId;

    public Notificacion() {}

    public Notificacion(String id, String titulo, String mensaje,
                        String imagenUrl, long fecha, String mascotaId) {
        this.id = id;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.imagenUrl = imagenUrl;
        this.fecha = fecha;
        this.mascotaId = mascotaId;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getMensaje() { return mensaje; }
    public String getImagenUrl() { return imagenUrl; }
    public long getFecha() { return fecha; }
    public String getMascotaId() { return mascotaId; }

    public void setId(String id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public void setFecha(long fecha) { this.fecha = fecha; }
    public void setMascotaId(String mascotaId) { this.mascotaId = mascotaId; }
}