package com.chilltime.planifyapi;

import com.chilltime.planifyapi.entity.Event;
import com.chilltime.planifyapi.repository.EventRepository;
import com.chilltime.planifyapi.service.SAEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SAEventUnitTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private SAEvent saevent;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateEventWithValidInput() {
        Event event = new Event();
        event.setName("evento Test");
        event.setDate(LocalDate.now().plusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("Ubicacion Test");

        when(eventRepository.save(event)).thenReturn(event);

        Event result = saevent.createEvent(event);

        assertNotNull(result);
        assertEquals("evento Test", result.getName());
    }

    @Test
    public void testCreateEventWithEmptyFields() {
        Event event = new Event();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saevent.createEvent(event);
        });

        assertEquals("Rellene todos los campos vacíos", exception.getMessage());
    }

    @Test
    public void testCreateEventWithPastDate() {
        Event event = new Event();
        event.setName("evento Test");
        event.setDate(LocalDate.now().minusDays(1));
        event.setTime(LocalTime.now());
        event.setLocation("Ubicacion Test");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saevent.createEvent(event);
        });

        assertEquals("La fecha no puede ser anterior a la fecha actual", exception.getMessage());
    }

    @Test
    public void testCreateEventWithNoASCIICharacter() {
        Event event = new Event();
        event.setName("evento Test");
        event.setDate(LocalDate.now().plusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("Ubicación con ñ");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saevent.createEvent(event);
        });

        assertEquals("Los campos nombre y ubicación deben ser caracteres ASCII", exception.getMessage());
    }
}
