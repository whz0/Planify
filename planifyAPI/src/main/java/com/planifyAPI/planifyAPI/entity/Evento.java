package com.planifyAPI.planifyAPI.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Evento {
    private String nombre;
    private LocalDate fecha;
    private String hora;
    private String ubicacion;
    @Id
    private Long id_evento;

}
