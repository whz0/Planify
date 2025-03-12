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
        // TODO: Catch exception y retornar un código HTTP que no sea 200
        return eventoService.crearEvento(evento);
    }
}
