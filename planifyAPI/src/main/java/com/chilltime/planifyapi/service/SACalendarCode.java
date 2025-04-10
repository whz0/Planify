package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.CalendarCode;
import com.chilltime.planifyapi.repository.CalendarRepository;
import com.chilltime.planifyapi.repository.PlannerRepository;
import com.chilltime.planifyapi.repository.CodigoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class SACalendarCode {

    @Autowired
    private CodigoRepository codigoRepository;
    @Autowired
    private CalendarRepository calendarRepository;
    @Autowired
    private PlannerRepository plannerRepository;

    private static final int CODE_LENGTH = 6;
    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Transactional
    public TContext createCode(CalendarCode code) {

        // Verificar si el usuario tiene permiso
        Optional<Calendar> calendarOpt = calendarRepository.findById(code.getCalendar().getId());

        if (calendarOpt.isEmpty()) {
            return new TContext(404, "Calendario no encontrado", null);
        }

        Calendar calendar = calendarOpt.get();
        boolean esPropietario = calendar.getPlanner().getId().equals(code.getCalendar().getPlanner().getId());

        // Si el calendario es público y el usuario no tiene permisos, bloquear
        if (!"private".equalsIgnoreCase(calendar.getType()) && !esPropietario) {
            return new TContext(403, "No puedes generar un código para un calendario público sin permisos", null);
        }
        // Llamada a la función que genera el código
        String newCode = generateUniqueCode();

        // Asignar valores y guardar
        CalendarCode calendarCode = new CalendarCode();
        calendarCode.setCode(newCode);
        calendarCode.setUsed(false);
        calendarCode.setCalendar(calendar);
        codigoRepository.save(calendarCode);

        return new TContext(200,"Se ha registrado el codigo de union",calendarCode);
    }

    @Transactional
    public TContext validateCode(String code) {
        if (code == null || code.isEmpty()) {
            return new TContext(400, "Código inválido", null);
        }

        Optional<CalendarCode> optionalCode = codigoRepository.findByCode(code);

        if (optionalCode.isEmpty()) {
            return new TContext(404, "Código no encontrado", null);
        }

        CalendarCode calendarCode = optionalCode.get();
        if (calendarCode.isUsed()) {
            return new TContext(409, "El código ya ha sido usado", null);
        }

        calendarCode.setUsed(true);
        codigoRepository.save(calendarCode);

        return new TContext(200, "Código validado correctamente", calendarCode);
    }

    private String generateUniqueCode() {
        Random random = new Random();
        String newCode;
        do {
            StringBuilder sb = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                int index = random.nextInt(CODE_CHARACTERS.length());
                sb.append(CODE_CHARACTERS.charAt(index));
            }
            newCode = sb.toString();
        } while (codigoRepository.findByCode(newCode).isPresent());
        return newCode;
    }

}
