package com.chilltime.planifyapi.controller;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.CalendarCode;
import com.chilltime.planifyapi.service.SACalendarCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/codigo")
public class CodeController {

    @Autowired
    private SACalendarCode codeService;

    @PostMapping("/create-code")
    public ResponseEntity<TContext> createCode(@RequestBody CalendarCode code) {
        // TODO: Catch exception y retornar un código HTTP que no sea 200
        TContext codec = new TContext();
        try {
            codec = codeService.createCode(code);
        }

        catch (IllegalArgumentException e) {
            codec = new TContext(200, e.getMessage(), null);
        }

        return ResponseEntity.status(codec.getStatus_code()).body(codec);
    }
}
