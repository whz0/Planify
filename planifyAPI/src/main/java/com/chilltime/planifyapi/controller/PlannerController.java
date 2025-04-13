package com.chilltime.planifyapi.controller;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Planner;
import com.chilltime.planifyapi.service.SAPlanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/planner")
public class PlannerController {

    @Autowired
    private SAPlanner plannerService;

    @PostMapping("/register")
    public ResponseEntity<TContext> registerPlanner(@RequestBody Planner planner) {
        try {
            TContext response = plannerService.register(planner);
            return ResponseEntity.status(response.getStatus_code()).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new TContext(200, e.getMessage(), null));
        }
    }
}