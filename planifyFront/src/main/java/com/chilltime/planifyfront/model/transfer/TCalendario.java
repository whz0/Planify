package com.chilltime.planifyfront.model.transfer;

public class TCalendario {
    private Long id;
    private String nombre;
    private String descripcion;
    private boolean activo;
    private String tipo;
    private Long id_usuario;

    public TCalendario() {
    }

    public TCalendario(Long id, String nombre, String descripcion, boolean activo, String tipo, Long id_usuario) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
        this.tipo = tipo;
        this.id_usuario = id_usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String toString() {
        return "TCalendario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", activo=" + activo +
                ", tipo='" + tipo + '\'' +
                ", id_usuario=" + id_usuario +
                '}';
    }
}
