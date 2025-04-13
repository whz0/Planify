package com.chilltime.planifyapi.controller;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Planner;
import com.chilltime.planifyapi.service.SAPlanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/planner", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlannerController {

    @Autowired
    private SAPlanner plannerService;

    @PostMapping("/login-planner")
    public ResponseEntity<TContext> loginPlanner(@RequestBody Planner planner) {

        TContext reponse = new TContext();

        try{
            reponse = plannerService.login(planner);
        }
        catch(Exception e){
            reponse = new TContext(200,  e.getMessage(), null);
        }
        return ResponseEntity.status(reponse.getStatus_code()).body(reponse);
    }
}
