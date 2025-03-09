package com.planifyAPI.planifyAPI.service;

import com.planifyAPI.planifyAPI.entity.Evento;

import com.planifyAPI.planifyAPI.repository.EventoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SAEvento {

    private final EventoRepository eventoRepository;

    @Autowired
    public SAEvento(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @Transactional
    public Evento crearEvento(Evento evento){

        return eventoRepository.save(evento);
    }

    //public int eliminarEvento();

    //public List<Evento> listarEventos();
}
