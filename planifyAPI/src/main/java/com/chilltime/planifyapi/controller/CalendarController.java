package com.chilltime.planifyapi.controller;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Calendario;
import com.chilltime.planifyapi.service.SACalendario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private SACalendario calendarioService;

    @PostMapping("/create-private")
    public ResponseEntity<TContext> crearCalendarioPrivado(@RequestBody Calendario cal) {
        //System.out.println(cal.toString())
        TContext cont = new TContext();
        try {
            cont = calendarioService.crearCalendarioPrivado(cal);
        }
        catch (Exception e) {
            cont = new TContext(500, "Error en la API " + e.getMessage(), null);
            return ResponseEntity.status(cont.getStatus_code()).body(cont);
        }


        return ResponseEntity.status(cont.getStatus_code()).body(cont);
    }

}