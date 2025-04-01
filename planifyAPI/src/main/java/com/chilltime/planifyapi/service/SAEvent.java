package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.Client;
import com.chilltime.planifyapi.entity.Event;

import com.chilltime.planifyapi.repository.CalendarRepository;
import com.chilltime.planifyapi.repository.ClientRepository;
import com.chilltime.planifyapi.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class SAEvent {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public Event createEvent(Event event){

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

        return eventRepository.save(event);
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

    @Transactional
    public TContext confirmAssistance(Long clientId, Long eventId) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);

        if (clientOpt.isEmpty()) {
            return new TContext(404, "Usuario no encontrado", null);
        }

        Optional<Event> eventOpt = eventRepository.findById(eventId);

        if (eventOpt.isEmpty()) {
            return new TContext(404, "Evento no encontrado", null);
        }

        Client client = clientOpt.get();
        Event event = eventOpt.get();

        event.getParticipants().add(client);

        return new TContext(200, "Asistencia confirmada con éxito", null);
    }

    //public int eliminarEvento();

    //public List<Evento> listarEventos();
}
