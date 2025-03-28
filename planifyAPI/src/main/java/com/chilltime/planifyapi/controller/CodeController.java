package com.chilltime.planifyapi.controller;

import com.chilltime.planifyapi.entity.CalendarCode;
import com.chilltime.planifyapi.service.SACalendarCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/codigo")
public class CodeController {

    @Autowired
    private SACalendarCode codeService;

    @PostMapping("/create-code")
    public CalendarCode createCode() {
        // TODO: Catch exception y retornar un código HTTP que no sea 200
        CalendarCode codec;
        try {
            codec = codeService.createCode();
        }

        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return codec;
    }
}
