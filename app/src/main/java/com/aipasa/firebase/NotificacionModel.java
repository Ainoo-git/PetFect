package com.aipasa.firebase;

import com.google.firebase.Timestamp;

import java.util.List;

public class NotificacionModel {

    private String id;
    private String idUsuario;
    private String idMascota;
    private String nombreMascota;
    private String tipo;
    private String estado;
    private String titulo;
    private String mensaje;
    private String imagenUrl;
    private Timestamp fecha;
    private boolean leido;
    private List<String> eliminadaPor;

    public NotificacionModel() {
    }

    public NotificacionModel(
            String idUsuario,
            String idMascota,
            String nombreMascota,
            String tipo,
            String estado,
            String titulo,
            String mensaje,
            String imagenUrl,
            Timestamp fecha,
            boolean leido,
            List<String> eliminadaPor
    ) {
        this.idUsuario = idUsuario;
        this.idMascota = idMascota;
        this.nombreMascota = nombreMascota;
        this.tipo = tipo;
        this.estado = estado;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.imagenUrl = imagenUrl;
        this.fecha = fecha;
        this.leido = leido;
        this.eliminadaPor = eliminadaPor;
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

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(String idMascota) {
        this.idMascota = idMascota;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public boolean isLeido() {
        return leido;
    }

    public boolean isLeida() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public void setLeida(boolean leida) {
        this.leido = leida;
    }

    public List<String> getEliminadaPor() {
        return eliminadaPor;
    }

    public void setEliminadaPor(List<String> eliminadaPor) {
        this.eliminadaPor = eliminadaPor;
    }
}