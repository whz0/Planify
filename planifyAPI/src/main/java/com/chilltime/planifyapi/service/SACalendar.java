package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.CalendarCode;
import com.chilltime.planifyapi.entity.Client;
import com.chilltime.planifyapi.repository.CalendarRepository;
import com.chilltime.planifyapi.repository.ClientRepository;
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
    private ClientRepository clientRepository;

    @Autowired
    private SACalendarCode calendarCodeService;

    @Transactional
    public TContext createPrivateCalendar(Calendar calendar) {
        Client client = clientRepository.findById(calendar.getId_client()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        calendar.setClient(client);

        validateCalendar(calendar.getName(), calendar.getDescription(), calendar.getClient());
        calendar.setActive(true);
        calendar.setType("PRIVATE");

        Calendar savedCalendar = calendarRepository.save(calendar);

        return new TContext(200, "Creado correctamente", savedCalendar);

    }

    @Transactional
    public TContext getCalendarsByUserId(Long userId) {
        Client client = clientRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Calendar> calendars = calendarRepository.findByClient(client);

        if (calendars.isEmpty()) {
            return new TContext(404, "No se encontraron calendarios para este usuario", null);
        }

        return new TContext(200, "Calendarios obtenidos correctamente", calendars);
    }

    private void validateCalendar(String name, String description, Client client) {
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

        if (calendarRepository.existsByNameAndClient(name, client)) {
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

    @Transactional
    public TContext joinCalendar(Long userId, String code) {
        if (!calendarCodeService.validateCode(code)) {
            return new TContext(404, "Código no válido o ya utilizado", null);
        }

        Client client = findClientById(userId);
        CalendarCode calendarCode = useCalendarCode(code);

        if (calendarCode == null) {
            return new TContext(404, "Código no encontrado", null);
        }

        client.getCalendars().add(calendarCode.getCalendar());

        return new TContext(200, "Se ha unido al usuario al calendario", calendarCode.getCalendar());
    }

    private Client findClientById(Long userId) {
        return clientRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private CalendarCode useCalendarCode(String code) {
        return calendarCodeService.useCode(code);
    }
}
