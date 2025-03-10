package com.planify.planifyfront.model.transfer;

import java.time.LocalDate;
import java.time.LocalTime;

public class TEvento {

    private int id;

    private LocalDate fecha;

    private String ubicacion;

    private LocalTime hora;

    private String nombre;

    public TEvento(int id, LocalDate fecha, String ubicacion, LocalTime hora, String nombre) {
        this.id = id;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.hora = hora;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Event: " + nombre + " at " + hora;
    }

}
