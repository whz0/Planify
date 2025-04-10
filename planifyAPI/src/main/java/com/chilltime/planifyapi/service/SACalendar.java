package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.Planner;
import com.chilltime.planifyapi.repository.CalendarRepository;
import com.chilltime.planifyapi.repository.PlannerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SACalendar {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private PlannerRepository plannerRepository;

    @Transactional
    public TContext createPrivateCalendar(Calendar calendar) {
        Planner planner = plannerRepository.findById(calendar.getId_planner()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        calendar.setPlanner(planner);

        System.out.println("planner ID: " + calendar.getId_planner());
        System.out.println("Calendarios en BD: " + calendarRepository.count());


        validateCalendar(calendar.getName(), calendar.getDescription(), calendar.getPlanner());
        calendar.setActive(true);
        calendar.setType("PRIVATE");
        return new TContext(200, "Creado correctamente", calendarRepository.save(calendar));

    }

    @Transactional
    public TContext getCalendarsByUserId(Long userId) {
        Planner planner = plannerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Calendar> calendars = calendarRepository.findByPlanner(planner);

        if (calendars.isEmpty()) {
            return new TContext(404, "No se encontraron calendarios para este usuario", null);
        }

        return new TContext(200, "Calendarios obtenidos correctamente", calendars);
    }

    private void validateCalendar(String name, String description, Planner planner) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio y no puede estar compuesto por espacios en blanco.");
        }

        if (!name.matches("[a-zA-Z0-9 ]+")) {
            throw new IllegalArgumentException("El nombre debe contener solo caracteres alfanuméricos y espacios.");
        }

        if(name.length() > 20){
            throw new IllegalArgumentException("El campo nombre no puede tener más de 20 caracteres");
        }
        if (description != null && description.length() > 255) {
            throw new IllegalArgumentException("La descripción debe tener un máximo de 255 caracteres.");
        }

        if (calendarRepository.existsByNameAndPlanner(name, planner)) {
            throw new IllegalArgumentException("El nombre ya está en uso por otro calendario privado suyo. Por favor, elija otro nombre.");
        }
    }

    @Transactional
    public TContext getCalendarById(Long calendarId) {
        Optional<Calendar> calendar = calendarRepository.findById(calendarId);

        if (calendar.isEmpty()) {
            return new TContext(404, "Calendario no encontrado", null);
        }

        return new TContext(200, "Calendario obtenido correctamente", calendar.get());
    }

}
