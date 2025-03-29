package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.entity.CalendarCode;
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

    private static final int CODE_LENGTH = 6;
    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Transactional
    public CalendarCode createCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CODE_CHARACTERS.length());
            sb.append(CODE_CHARACTERS.charAt(index));
        }

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
