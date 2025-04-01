package com.chilltime.planifyapi.controller;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Event;
import com.chilltime.planifyapi.service.SAEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/event", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventController {

    @Autowired
    private SAEvent eventService;

    @PostMapping("/create-event")
    public ResponseEntity<TContext> createEvent(@RequestBody Event event) {
        TContext cont;
        try {
            cont = eventService.createEvent(event);
        } catch (IllegalArgumentException e) {
            cont = new TContext(200, e.getMessage(), null);
        }

        return ResponseEntity.status(cont.getStatus_code()).body(cont);
    }
}

