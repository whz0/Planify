package com.chilltime.planifyapi;

import com.chilltime.planifyapi.controller.EventController;
import com.chilltime.planifyapi.entity.Event;
import com.chilltime.planifyapi.service.SAEvent;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventControllerUnitTest {

    @Mock
    private SAEvent eventService;

    @InjectMocks
    private EventController eventController;

    @Test
    public void testCreateEvent() {
        // Create test event
        Event event = new Event();
        event.setName("evento Test");
        event.setDate(LocalDate.now().plusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("Ubicacion Test");

        // Set up calendar ID
        Long calendarId = 1L;

        // Create payload map
        Map<String, Object> payload = new HashMap<>();
        payload.put("event", event);
        payload.put("calendarId", calendarId);

        // Mock service behavior with calendar ID parameter
        when(eventService.createEvent(any(Event.class), eq(calendarId))).thenReturn(event);

        // Call controller method
        ResponseEntity<Event> response = eventController.createEvent(payload);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("evento Test", response.getBody().getName());

        // Verify service was called with correct parameters
        verify(eventService).createEvent(any(Event.class), eq(calendarId));
    }

    @Test
    public void testCreateEventInvalid() {
        // Create test event with invalid data
        Event event = new Event();
        event.setName(""); // Nombre vacío

        // Set up calendar ID
        Long calendarId = 1L;

        // Create payload map
        Map<String, Object> payload = new HashMap<>();
        payload.put("event", event);
        payload.put("calendarId", calendarId);

        // Mock service throwing exception
        when(eventService.createEvent(any(Event.class), eq(calendarId)))
                .thenThrow(new IllegalArgumentException("Rellene todos los campos vacíos"));

        // Test exception handling
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(payload);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void testCreateEventWithPastDate() {
        // Create test event with past date
        Event event = new Event();
        event.setName("evento Test");
        event.setDate(LocalDate.now().minusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("Ubicacion Test");

        // Set up calendar ID
        Long calendarId = 1L;

        // Create payload map
        Map<String, Object> payload = new HashMap<>();
        payload.put("event", event);
        payload.put("calendarId", calendarId);

        // Mock service throwing exception
        when(eventService.createEvent(any(Event.class), eq(calendarId)))
                .thenThrow(new IllegalArgumentException("La fecha no puede ser anterior a la fecha actual"));

        // Test exception handling
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(payload);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void testCreateEventWithNoASCIIName() {
        // Create test event with non-ASCII name
        Event event = new Event();
        event.setName("España");
        event.setDate(LocalDate.now().plusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("Ubicacion Test");

        // Set up calendar ID
        Long calendarId = 1L;

        // Create payload map
        Map<String, Object> payload = new HashMap<>();
        payload.put("event", event);
        payload.put("calendarId", calendarId);

        // Mock service throwing exception
        when(eventService.createEvent(any(Event.class), eq(calendarId)))
                .thenThrow(new IllegalArgumentException("Los campos nombre y ubicación deben ser caracteres ASCII"));

        // Test exception handling
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(payload);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void testCreateEventWithNoASCIILocation() {
        // Create test event with non-ASCII location
        Event event = new Event();
        event.setName("evento Test");
        event.setDate(LocalDate.now().plusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("España");

        // Set up calendar ID
        Long calendarId = 1L;

        // Create payload map
        Map<String, Object> payload = new HashMap<>();
        payload.put("event", event);
        payload.put("calendarId", calendarId);

        // Mock service throwing exception
        when(eventService.createEvent(any(Event.class), eq(calendarId)))
                .thenThrow(new IllegalArgumentException("Los campos nombre y ubicación deben ser caracteres ASCII"));

        // Test exception handling
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(payload);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void testCreateEventWithEmptyFields() {
        // Create test event with empty location
        Event event = new Event();
        event.setName("evento Test");
        event.setDate(LocalDate.now().plusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("");

        // Set up calendar ID
        Long calendarId = 1L;

        // Create payload map
        Map<String, Object> payload = new HashMap<>();
        payload.put("event", event);
        payload.put("calendarId", calendarId);

        // Mock service throwing exception
        when(eventService.createEvent(any(Event.class), eq(calendarId)))
                .thenThrow(new IllegalArgumentException("Rellene todos los campos vacíos"));

        // Test exception handling
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(payload);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void testCreateEventWithInvalidCalendarId() {
        // Create test event
        Event event = new Event();
        event.setName("evento Test");
        event.setDate(LocalDate.now().plusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("Ubicacion Test");

        // Set up invalid calendar ID
        Long calendarId = 999L;

        // Create payload map
        Map<String, Object> payload = new HashMap<>();
        payload.put("event", event);
        payload.put("calendarId", calendarId);

        // Mock service throwing exception
        when(eventService.createEvent(any(Event.class), eq(calendarId)))
                .thenThrow(new IllegalArgumentException("Calendario no encontrado"));

        // Test exception handling
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventController.createEvent(payload);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}