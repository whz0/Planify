package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Planner;
import com.chilltime.planifyapi.repository.PlannerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SAPlanner {

    @Autowired
    private PlannerRepository plannerRepository;

    @Transactional
    public TContext register(Planner planner) {
        validatePlanner(planner.getUsername(), planner.getPassword());
        Planner savedPlanner = plannerRepository.save(planner);
        return new TContext(200, "Planner registrado correctamente", savedPlanner);
    }

    @Transactional
    public TContext login(Planner planner) {
        Planner savedPlanner = plannerRepository.findByUsername(planner.getUsername());

        if (planner.getPassword().equals(savedPlanner.getPassword())) {
            return new TContext(403, "El usuario o la contraseña no son correctos", null);
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