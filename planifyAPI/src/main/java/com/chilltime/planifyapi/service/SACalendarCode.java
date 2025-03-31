package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.entity.CalendarCode;
import com.chilltime.planifyapi.repository.CalendarCodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class SACalendarCode {

    @Autowired
    private CalendarCodeRepository calendarCodeRepository;

    private static final int CODE_LENGTH = 6;
    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Transactional
    public CalendarCode createCode() {
        Random random = new Random();

        String code;
        do {
            StringBuilder sb = new StringBuilder(CODE_LENGTH);

            for (int i = 0; i < CODE_LENGTH; i++) {
                int index = random.nextInt(CODE_CHARACTERS.length());
                sb.append(CODE_CHARACTERS.charAt(index));
            }

            code = sb.toString();
        } while (calendarCodeRepository.findByCodeAndUsedFalse(code).isPresent());

        CalendarCode calendarCode = new CalendarCode();
        calendarCode.setCode(code);
        calendarCode.setUsed(false);
        calendarCodeRepository.save(calendarCode);

        return calendarCode;
    }

    @Transactional
    public boolean validateCode(String code) {
        if(code == null || code.isEmpty()) {
            return false;
        }
        Optional<CalendarCode> optionalCode = calendarCodeRepository.findByCode(code);
        if(optionalCode.isPresent()) {
            CalendarCode calendarCode = optionalCode.get();
            if(!calendarCode.isUsed()) {
                calendarCodeRepository.save(calendarCode);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public CalendarCode useCode(String code) {
        Optional<CalendarCode> calendarCodeOpt = calendarCodeRepository.findByCodeAndUsedFalse(code);

        if (calendarCodeOpt.isEmpty()) {
            return null;
        }

        CalendarCode calendarCode = calendarCodeOpt.get();
        calendarCode.setUsed(true);
        calendarCodeRepository.save(calendarCode);

        return calendarCode;
    }
}
