package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.CalendarCode;
import com.chilltime.planifyapi.repository.CalendarCodeRepository;
import com.chilltime.planifyapi.repository.CalendarRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class SACalendarCode {

    @Autowired
    private CalendarCodeRepository calendarCodeRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    private static final int CODE_LENGTH = 6;
    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Transactional
    public TContext createCode(Long calendarId) {
        Optional<Calendar> calendarOpt = calendarRepository.findById(calendarId);
        if(calendarOpt.isEmpty()) {
            return new TContext(404, "Calendario no encontrado", null);
        }

        Random random = new Random();
        Calendar calendar = calendarOpt.get();

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
        calendarCode.setCalendar(calendar);
        calendarCodeRepository.save(calendarCode);

        return new TContext(200, "Código creado correctamente", calendarCode.getCode());
    }

    @Transactional
    public boolean validateCode(String code) {
        if(code == null || code.isEmpty()) {
            return false;
        }
        Optional<CalendarCode> optionalCode = calendarCodeRepository.findByCode(code);
        if(optionalCode.isPresent()) {
            CalendarCode calendarCode = optionalCode.get();
            return !calendarCode.isUsed();
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

    @Transactional
    public Calendar getCalendarByCode(String code) {
        Optional<CalendarCode> calendarCodeOpt = calendarCodeRepository.findByCode(code);

        if (calendarCodeOpt.isEmpty()) {
            return null;
        }

        return calendarCodeOpt.get().getCalendar();
    }
}