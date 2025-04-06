package com.chilltime.planifyapi.controller;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.service.SACalendar;
import com.chilltime.planifyapi.service.SAEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private SACalendar calendarService;

    @Autowired
    private SAEvent eventService;

    @PostMapping("/create-private")
    public ResponseEntity<TContext> CreatePrivateCalendar(@RequestBody Calendar cal) {
        //System.out.println(cal.toString())
        TContext cont = new TContext();
        try {
            cont = calendarService.createPrivateCalendar(cal);
        }
        catch (Exception e) {
            cont = new TContext(400, e.getMessage(), null);
        }

        return ResponseEntity.status(cont.getStatus_code()).body(cont);
    }

    // Obtener calendarios por usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<TContext> getCalendarsByUserId(@PathVariable Long userId) {
        TContext cont = new TContext();
        try {
            cont = calendarService.getCalendarsByUserId(userId);
        }
        catch (Exception e) {
            cont = new TContext(200, e.getMessage(), null);
        }

        return ResponseEntity.status(cont.getStatus_code()).body(cont);
    }

    // Obtener un calendario específico por ID
    @GetMapping("/{calendarId}")
    public ResponseEntity<TContext> getCalendarById(@PathVariable Long calendarId) {
        TContext cont = new TContext();
        try {
            cont = calendarService.getCalendarById(calendarId);
        }
        catch (Exception e) {
            cont = new TContext(200, e.getMessage(), null);
        }

        return ResponseEntity.status(cont.getStatus_code()).body(cont);
    }

    // Obtener eventos de un calendario específico
    @GetMapping("/{calendarId}/events")
    public ResponseEntity<TContext> getEventsByCalendarId(@PathVariable Long calendarId) {
        TContext cont = new TContext();
        try {
            cont = eventService.getEventsByCalendarId(calendarId);
        }
        catch (Exception e) {
            cont = new TContext(200, e.getMessage(), null);
        }

        return ResponseEntity.status(cont.getStatus_code()).body(cont);
    }

}