package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.Client;
import com.chilltime.planifyapi.repository.CalendarRepository;
import com.chilltime.planifyapi.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SACalendar {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public TContext createPrivateCalendar(Calendar calendar) {
        Client client = clientRepository.findById(calendar.getId_client()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        calendar.setClient(client);

        validateCalendar(calendar.getName(), calendar.getDescription(), calendar.getClient());

        return new TContext(200, "Creado correctamente", calendarRepository.save(calendar));

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


}
