package com.chilltime.planifyfront.utils;

import com.calendarfx.model.Calendar;
import com.chilltime.planifyfront.model.transfer.TCalendario;
import com.chilltime.planifyfront.model.transfer.TEvento;

public class CalendarUtils {
    public static TCalendario toTCalendario(Calendar<TEvento> calendar) {
        TCalendario calendario = new TCalendario();
        calendario.setNombre(calendar.getName());
        calendario.setActivo(true);
        calendario.setTipo("Privado");
        return calendario;
    }

    public static Calendar<TEvento> toCalendar(TCalendario calendar) {
        return new Calendar<>(calendar.getNombre());
    }
}
