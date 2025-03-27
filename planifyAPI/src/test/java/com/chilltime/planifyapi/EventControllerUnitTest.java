package com.chilltime.planifyapi;

import com.chilltime.planifyapi.controller.EventController;
import com.chilltime.planifyapi.entity.Event;
import com.chilltime.planifyapi.service.SAEvent;
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
    private SAEvent eventService;

    @InjectMocks
    private EventController eventController;

    @Test
    public void testCreateEvent() {
        Event event = new Event();
        event.setName("evento Test");
        event.setDate(LocalDate.now().plusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("Ubicacion Test");

        when(eventService.createEvent(any(Event.class))).thenReturn(event);

        Event result = eventController.createEvent(event);

        assertNotNull(result);
        assertEquals("evento Test", result.getName());
    }

    @Test
    public void testCreateEventInvalid() {
        Event event = new Event();
        event.setName(""); // Nombre vacío

        when(eventService.createEvent(any(Event.class))).thenThrow(new IllegalArgumentException("Rellene todos los campos vacíos"));

        assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(event);
        });
    }
    @Test
    public void testCreateEventWithPastDate() {
        Event event = new Event();
        event.setName("evento Test");
        event.setDate(LocalDate.now().minusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("Ubicacion Test");

        when(eventService.createEvent(any(Event.class))).thenThrow(new IllegalArgumentException("La fecha no puede ser anterior a la fecha actual"));

        assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(event);
        });
    }
    @Test
    public void testCreateEventWithLongName() {
        Event event = new Event();
        event.setName("evento Test con un nombre muy largo que no debería ser permitido");
        event.setDate(LocalDate.now().plusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("Ubicacion Test");

        when(eventService.createEvent(any(Event.class))).thenThrow(new IllegalArgumentException("El campo nombre no puede tener menos de 20 caracteres"));

        assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(event);
        });
    }
    @Test
    public void testCreateEventWithNoASCIIName() {
        Event event = new Event();
        event.setName("España");
        event.setDate(LocalDate.now().plusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("Ubicacion Test");

        when(eventService.createEvent(any(Event.class))).thenThrow(new IllegalArgumentException("Los campos nombre y ubicación deben ser caracteres ASCII"));

        assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(event);
        });
    }
    @Test
    public void testCreateEventWithNoASCIILocation() {
        Event event = new Event();
        event.setName("evento Test");
        event.setDate(LocalDate.now().plusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("España");

        when(eventService.createEvent(any(Event.class))).thenThrow(new IllegalArgumentException("Los campos nombre y ubicación deben ser caracteres ASCII"));

        assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(event);
        });
    }
    @Test
    public void testCreateEventWithEmptyFields() {
        Event event = new Event();
        event.setName("evento Test");
        event.setDate(LocalDate.now().plusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("");

        when(eventService.createEvent(any(Event.class))).thenThrow(new IllegalArgumentException("Rellene todos los campos vacíos"));

        assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(event);
        });
    }
}
