package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.Event;

import com.chilltime.planifyapi.repository.CalendarRepository;
import com.chilltime.planifyapi.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

@Service
public class SAEvent {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Transactional
    public Event createEvent(Event event, Long calendarId) {

        if(event.getDate() == null || event.getTime() == null || event.getName() == null || event.getLocation() == null){
            throw new IllegalArgumentException("Rellene todos los campos vacíos");
        }

        if (event.getDate().isBefore(LocalDate.now()) ||
                (event.getDate().isEqual(LocalDate.now()) && event.getTime().isBefore(LocalTime.now()))) {
            throw new IllegalArgumentException("La fecha no puede ser anterior a la fecha actual");
        }

        if (event.getName().length() > 20) {
            throw new IllegalArgumentException("El campo nombre no puede tener más de 20 caracteres");
        }

        if(!isASCII(event.getName()) || !isASCII(event.getLocation())){
            throw new IllegalArgumentException("Los campos nombre y ubicación deben ser caracteres ASCII");
        }

        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new IllegalArgumentException("Calendario no encontrado"));

        Event savedEvent = eventRepository.save(event);
        // Initialize collections if null
        if (savedEvent.getCalendars() == null) {
            savedEvent.setCalendars(new java.util.HashSet<>());
        }
        if (calendar.getEvents() == null) {
            calendar.setEvents(new java.util.HashSet<>());
        }

        // Associate the event with the calendar
        calendar.getEvents().add(savedEvent);

        calendarRepository.save(calendar);


        return savedEvent;
    }

    private boolean isASCII(String chain){
        for (char c : chain.toCharArray()) {
            if (c > 127) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    public TContext getEventsByCalendarId(Long idCalendar) {
        Calendar calendar = calendarRepository.findById(idCalendar).orElseThrow(()->new RuntimeException());
        return new TContext(200, "Eventos obtenidos correctamente", eventRepository.findByCalendars(calendar));
    }

    //public int eliminarEvento();

    //public List<Evento> listarEventos();
}
