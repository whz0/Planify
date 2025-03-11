package com.planify.planifyfront.model.transfer;

import java.time.LocalDate;
import java.time.LocalTime;

public class TEvento {

    public int id;

    public String nombre;

    public LocalDate fecha;

    public LocalTime hora;

    public String ubicacion;

    public TEvento(int id, String nombre, LocalDate fecha, LocalTime hora, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
        this.ubicacion = ubicacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public String toString() {
        return "Event: " + nombre + " at " + hora;
    }

}
