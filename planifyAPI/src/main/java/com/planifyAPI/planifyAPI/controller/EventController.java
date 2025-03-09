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

    @GetMapping("/message")
    public String message() {
        return "";
    }

    @PostMapping("/create-event")
    public String createEvent(@RequestBody Evento evento) {
        return eventoService.crearEvento(evento);
    }
}
