package com.chilltime.planifyapi.controller;

import com.chilltime.planifyapi.entity.Event;
import com.chilltime.planifyapi.service.SAEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private SAEvent eventoService;

    @PostMapping("/create-event")
    public Event createEvent(@RequestBody Event evento) {
        // TODO: Catch exception y retornar un código HTTP que no sea 200
        Event eventc;
        try {
            eventc = eventoService.createEvent(evento);
        }

        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return eventc;
    }
}
