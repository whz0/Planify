package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.entity.Calendario;
import com.chilltime.planifyapi.entity.Usuario;
import com.chilltime.planifyapi.repository.CalendarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SACalendario {

    @Autowired
    private final CalendarioRepository calendarioRepository;

    public SACalendario(CalendarioRepository calendarioRepository) {
        this.calendarioRepository = calendarioRepository;
    }

    @Transactional
    public Calendario crearCalendarioPrivado(String nombre, String descripcion, Usuario usuario) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del calendario no puede estar vacío");
        }

        Calendario calendario = new Calendario();
        calendario.setNombre(nombre);
        calendario.setDescripcion(descripcion);
        calendario.setActivo(true);
        calendario.setTipo("PRIVADO");
        calendario.setUsuario(usuario);

        return calendarioRepository.save(calendario);
    }
}
