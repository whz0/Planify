package com.planifyAPI.planifyAPI.service;

import com.planifyAPI.planifyAPI.entity.Evento;

import com.planifyAPI.planifyAPI.repository.EventoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SAEvento {

    @Autowired
    private final EventoRepository eventoRepository;

    public SAEvento(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @Transactional
    public Evento crearEvento(Evento evento){

        if(evento.getFecha() == null || evento.getHora() == null || evento.getNombre() == null || evento.getUbicacion() == null){
            throw new IllegalArgumentException("Rellene todos los campos vacíos");
        }

        if(evento.getFecha().isBefore(new Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()) ||
                (evento.getFecha().isEqual(new Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()) &&
                        evento.getHora().isBefore(new Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime()))){
            throw new IllegalArgumentException("La fecha no puede ser anterior a la fecha actual");
        }

        if((!esASCII(evento.getNombre()) && evento.getNombre().length() <= 20) || !esASCII(evento.getUbicacion())){
            throw new IllegalArgumentException("Los campos nombre y ubicación deben ser caracteres ASCII");
        }

        return eventoRepository.save(evento);
    }

    private boolean esASCII(String cadena){
        for (char c : cadena.toCharArray()) {
            if (c > 127) {
                return false;
            }
        }
        return true;
    }

    //public int eliminarEvento();

    //public List<Evento> listarEventos();
}
