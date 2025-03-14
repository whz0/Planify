package com.chilltime.planifyapi;

import com.chilltime.planifyapi.controller.EventController;
import com.chilltime.planifyapi.entity.Evento;
import com.chilltime.planifyapi.service.SAEvento;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventControllerUnitTest {

    @Mock
    private SAEvento eventoService;

    @InjectMocks
    private EventController eventController;

    @Test
    public void testCreateEvent() {
        Evento evento = new Evento();
        evento.setNombre("Evento Test");
        evento.setFecha(LocalDate.now().plusDays(1));
        evento.setHora(LocalTime.now().plusHours(1));
        evento.setUbicacion("Ubicacion Test");

        when(eventoService.crearEvento(any(Evento.class))).thenReturn(evento);

        Evento result = eventController.createEvent(evento);

        assertNotNull(result);
        assertEquals("Evento Test", result.getNombre());
    }

    @Test
    public void testCreateEventInvalid() {
        Evento evento = new Evento();
        evento.setNombre(""); // Nombre vacío

        when(eventoService.crearEvento(any(Evento.class))).thenThrow(new IllegalArgumentException("Rellene todos los campos vacíos"));

        assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(evento);
        });
    }
    @Test
    public void testCreateEventWithPastDate() {
        Evento evento = new Evento();
        evento.setNombre("Evento Test");
        evento.setFecha(LocalDate.now().minusDays(1));
        evento.setHora(LocalTime.now().plusHours(1));
        evento.setUbicacion("Ubicacion Test");

        when(eventoService.crearEvento(any(Evento.class))).thenThrow(new IllegalArgumentException("La fecha no puede ser anterior a la fecha actual"));

        assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(evento);
        });
    }
    @Test
    public void testCreateEventWithLongName() {
        Evento evento = new Evento();
        evento.setNombre("Evento Test con un nombre muy largo que no debería ser permitido");
        evento.setFecha(LocalDate.now().plusDays(1));
        evento.setHora(LocalTime.now().plusHours(1));
        evento.setUbicacion("Ubicacion Test");

        when(eventoService.crearEvento(any(Evento.class))).thenThrow(new IllegalArgumentException("El campo nombre no puede tener menos de 20 caracteres"));

        assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(evento);
        });
    }
    @Test
    public void testCreateEventWithNoASCIIName() {
        Evento evento = new Evento();
        evento.setNombre("España");
        evento.setFecha(LocalDate.now().plusDays(1));
        evento.setHora(LocalTime.now().plusHours(1));
        evento.setUbicacion("Ubicacion Test");

        when(eventoService.crearEvento(any(Evento.class))).thenThrow(new IllegalArgumentException("Los campos nombre y ubicación deben ser caracteres ASCII"));

        assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(evento);
        });
    }
    @Test
    public void testCreateEventWithNoASCIILocation() {
        Evento evento = new Evento();
        evento.setNombre("Evento Test");
        evento.setFecha(LocalDate.now().plusDays(1));
        evento.setHora(LocalTime.now().plusHours(1));
        evento.setUbicacion("España");

        when(eventoService.crearEvento(any(Evento.class))).thenThrow(new IllegalArgumentException("Los campos nombre y ubicación deben ser caracteres ASCII"));

        assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(evento);
        });
    }
    @Test
    public void testCreateEventWithEmptyFields() {
        Evento evento = new Evento();
        evento.setNombre("Evento Test");
        evento.setFecha(LocalDate.now().plusDays(1));
        evento.setHora(LocalTime.now().plusHours(1));
        evento.setUbicacion("");

        when(eventoService.crearEvento(any(Evento.class))).thenThrow(new IllegalArgumentException("Rellene todos los campos vacíos"));

        assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(evento);
        });
    }
}
