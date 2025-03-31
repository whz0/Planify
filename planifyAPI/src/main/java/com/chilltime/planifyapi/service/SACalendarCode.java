package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.CalendarCode;
import com.chilltime.planifyapi.repository.CalendarRepository;
import com.chilltime.planifyapi.repository.ClientRepository;
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
    private ClientRepository clientRepository;

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
        boolean esPropietario = calendar.getClient().getId().equals(code.getCalendar().getClient().getId());

        // Si el calendario es público y el usuario no tiene permisos, bloquear
        if (!"private".equalsIgnoreCase(calendar.getType()) && !esPropietario) {
            return new TContext(403, "No puedes generar un código para un calendario público sin permisos", null);
        }
        Random random = new Random();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CODE_CHARACTERS.length());
            sb.append(CODE_CHARACTERS.charAt(index));
        }
        code.setCode(sb.toString());

        CalendarCode calendarCode = new CalendarCode();
        calendarCode.setCodigo(sb.toString());
        calendarCode.setUsado(false);
        codigoRepository.save(calendarCode);

        return calendarCode;
    }

    @Transactional
    public boolean validateCode(String code) {
        if(code == null || code.isEmpty()) {
            return false;
        }
        Optional<CalendarCode> optionalCode= codigoRepository.findByCodigo(code);
        if(optionalCode.isPresent()) {
            CalendarCode calendarCode = optionalCode.get();
            if(!calendarCode.isUsado()) {
                calendarCode.setUsado(true);
                codigoRepository.save(calendarCode);
                return true;
            }
        }
        return false;
    }

}
