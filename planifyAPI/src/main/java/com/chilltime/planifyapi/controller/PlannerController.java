package com.chilltime.planifyapi.controller;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Planner;
import com.chilltime.planifyapi.service.SAPlanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/planner", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlannerController {

    @Autowired
    private SAPlanner plannerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login-planner")
    public ResponseEntity<TContext> loginPlanner(@RequestBody Planner planner) {
        TContext response;

        try {
            response = plannerService.login(planner);
            return ResponseEntity.status(response.getStatus_code()).body(response);
        } catch (Exception e) {
            response = new TContext(200, e.getMessage(), null);
            return ResponseEntity.status(response.getStatus_code()).body(response);
        }
    }
    @PostMapping("/register")
    public ResponseEntity<TContext> registerPlanner(@RequestBody Planner planner) {
        try {
            if (planner.getUsername() == null || planner.getUsername().trim().isEmpty() ||
                    planner.getPassword() == null || planner.getPassword().trim().isEmpty()) {
                return ResponseEntity.status(200).body(
                        new TContext(200, "El usuario ha dejado alguno de los campos vacíos", null));
            }

            planner.setPassword(passwordEncoder.encode(planner.getPassword()));

            if (planner.getRole() == null || planner.getRole().isEmpty()) {
                planner.setRole("ROLE_USER");
            }

            TContext response = plannerService.register(planner);
            return ResponseEntity.status(response.getStatus_code()).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(200).body(new TContext(200, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(200).body(new TContext(200, e.getMessage(), null));
        }
    }
}
