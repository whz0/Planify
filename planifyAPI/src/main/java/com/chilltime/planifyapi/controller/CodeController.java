package com.chilltime.planifyapi.controller;

import com.chilltime.planifyapi.entity.Codigo;
import com.chilltime.planifyapi.service.SACodigo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/codigo")
public class CodeController {

    @Autowired
    private SACodigo codeService;

    @PostMapping("/create-code")
    public Codigo createCode() {
        // TODO: Catch exception y retornar un código HTTP que no sea 200
        Codigo codec;
        try {
            codec = codeService.createCode();
        }

        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return codec;
    }
}
