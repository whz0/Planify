package com.chilltime.planifyapi.controller;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.service.SACalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private SACalendar calendarService;

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

}