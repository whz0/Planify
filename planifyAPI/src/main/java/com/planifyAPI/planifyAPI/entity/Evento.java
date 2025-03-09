package com.planifyAPI.planifyAPI.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Evento {
    private String nombre;
    private String fecha;
    private String hora;
    private String ubicacion;
    @Id
    private Long id_evento;

}
