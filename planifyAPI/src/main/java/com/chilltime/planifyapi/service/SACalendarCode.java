package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.CalendarCode;
import com.chilltime.planifyapi.repository.CodigoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SACalendarCode {

    @Autowired
    private CodigoRepository codigoRepository;

    private static final int CODE_LENGTH = 6;
    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Transactional
    public TContext createCode(CalendarCode code) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CODE_CHARACTERS.length());
            sb.append(CODE_CHARACTERS.charAt(index));
        }

        CalendarCode calendarCode = new CalendarCode();
        calendarCode.setCodigo(sb.toString());
        codigoRepository.save(calendarCode);

        return new TContext(200, "Creado correctamente", calendarCode);
    }
}
