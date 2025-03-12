package com.planifyAPI.planifyAPI.controller;

import com.planifyAPI.planifyAPI.entity.Evento;
import com.planifyAPI.planifyAPI.service.SAEvento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private SAEvento eventoService;

    @PostMapping("/create-event")
    public Evento createEvent(@RequestBody Evento evento) {
        //evento.setId(null); // Por si me llega un ID desde el fron (no deberia)
        return eventoService.crearEvento(evento);
    }
}
