package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Planner;
import com.chilltime.planifyapi.repository.PlannerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SAPlanner {

    @Autowired
    private PlannerRepository plannerRepository;
    // Inyectar PasswordEncoder para codificar contraseñas
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public TContext register(Planner planner) {
        TContext response;
        try {
            validatePlanner(planner.getUsername(), planner.getPassword());
            Planner savedPlanner = plannerRepository.save(planner);
            response = new TContext(200, "Planner registrado correctamente", savedPlanner);
        } catch (IllegalArgumentException e) {
            response = new TContext(200, e.getMessage(), null);
        }
        return response;
    }

    @Transactional
    public TContext login(Planner planner) {
        Planner savedPlanner = plannerRepository.findByUsername(planner.getUsername());

        if (savedPlanner == null) {
            return new TContext(200, "El usuario o la contraseña no son correctos", null);
        }

        if (!passwordEncoder.matches(planner.getPassword(), savedPlanner.getPassword())) {
            return new TContext(200, "El usuario o la contraseña no son correctos", null);
        }

        return new TContext(200, "Login efectuado correctamente", savedPlanner);
    }

    private void validatePlanner(String username, String password) {
        //Validar campos vacíos y cadenas en blanco
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario ha dejado alguno de los campos vacíos");
        }

        //Validar longitud del nombre de usuario
        if (username.length() > 15) {
            throw new IllegalArgumentException("El nombre de usuario no es válido. Debe tener máximo 15 caracteres");
        }

        //Validar si el planner ya existe
        if (plannerRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
    }
}