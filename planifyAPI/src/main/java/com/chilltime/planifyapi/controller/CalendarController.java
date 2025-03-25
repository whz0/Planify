package com.chilltime.planifyapi.controller;

import com.chilltime.planifyapi.entity.Calendario;
import com.chilltime.planifyapi.service.SACalendario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private SACalendario calendarioService;

    @PostMapping("/create-private")
    public Calendario crearCalendarioPrivado(@RequestBody Calendario cal) {
        System.out.println(cal.toString());
        try {
            cal = calendarioService.crearCalendarioPrivado(cal);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return cal;
    }

}