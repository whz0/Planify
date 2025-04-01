package com.chilltime.planifyapi.controller;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.CalendarCode;
import com.chilltime.planifyapi.service.SACalendar;
import com.chilltime.planifyapi.service.SACalendarCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/codigo")
public class CodeController {

    @Autowired
    private SACalendarCode codeService;

    @Autowired
    private SACalendar calendarService;

    @PostMapping("/create/{calendarId}")
    public ResponseEntity<TContext> createCodeForCalendar(@PathVariable Long calendarId) {
        try {
            TContext context = codeService.createCode(calendarId);
            return ResponseEntity.status(context.getStatus_code()).body(context);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new TContext(400, e.getMessage(), null));
        }
    }

    @GetMapping("/validate/{code}")
    public ResponseEntity<TContext> validateCode(@PathVariable String code) {
        boolean isValid = codeService.validateCode(code);
        if (isValid) {
            return ResponseEntity.ok(new TContext(200, "Código válido", true));
        } else {
            return ResponseEntity.ok(new TContext(200, "Código inválido o ya utilizado", false));
        }
    }
}