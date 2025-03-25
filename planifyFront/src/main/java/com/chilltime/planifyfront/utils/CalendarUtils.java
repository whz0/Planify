package com.chilltime.planifyfront.utils;

import com.calendarfx.model.Calendar;
import com.chilltime.planifyfront.model.transfer.TCalendario;

public class CalendarUtils {
    public static TCalendario toTCalendario(Calendar calendar) {
        TCalendario calendario = new TCalendario();
        calendario.setNombre(calendar.getName());
        calendario.setDescripcion((String) calendar.getUserObject());
        calendario.setActivo(true);
        calendario.setTipo("Privado");
        return calendario;
    }

    public static Calendar toCalendar(TCalendario calendar) {
        Calendar calendario = new Calendar(calendar.getNombre());
        calendario.setUserObject(calendar.getDescripcion());
        return calendario;
    }
}
