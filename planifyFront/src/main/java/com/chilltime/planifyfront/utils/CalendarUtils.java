package com.chilltime.planifyfront.utils;

import com.calendarfx.model.Calendar;
import com.chilltime.planifyfront.model.transfer.TCalendar;
import com.chilltime.planifyfront.model.transfer.TEvent;

public class CalendarUtils {
    public static TCalendar toTCalendar(Calendar<TEvent> calendarIn) {
        TCalendar calendar = new TCalendar();
        calendar.setName(calendarIn.getName());
        calendar.setActive(true);
        calendar.setType("Privado");
        return calendar;
    }

    public static Calendar<TEvent> toCalendar(TCalendar calendar) {
        return new Calendar<>(calendar.getName());
    }
}
