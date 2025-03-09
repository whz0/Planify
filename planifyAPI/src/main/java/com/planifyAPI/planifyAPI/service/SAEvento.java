package com.planifyAPI.planifyAPI.service;

import com.planifyAPI.planifyAPI.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SAEventoImp {

    private final EventoRepository eventoRepository;

    @Autowired
    public SAEventoImp(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }


}
