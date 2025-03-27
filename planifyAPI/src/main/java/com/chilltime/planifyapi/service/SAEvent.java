package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.entity.Event;

import com.chilltime.planifyapi.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SAEvent {

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public Event createEvent(Event event){

        if(event.getDate() == null || event.getTime() == null || event.getName() == null || event.getLocation() == null){
            throw new IllegalArgumentException("Rellene todos los campos vacíos");
        }

        if(event.getDate().isBefore(new Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()) ||
                (event.getDate().isEqual(new Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()) &&
                        event.getTime().isBefore(new Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime()))){
            throw new IllegalArgumentException("La fecha no puede ser anterior a la fecha actual");
        }

        if(event.getName().length() >= 20){
            throw new IllegalArgumentException("El campo nombre no puede tener menos de 20 caracteres");
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

    //public int eliminarEvento();

    //public List<Evento> listarEventos();
}
