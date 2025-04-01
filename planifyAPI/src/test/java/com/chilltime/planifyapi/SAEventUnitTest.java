package com.chilltime.planifyapi;

import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.Event;
import com.chilltime.planifyapi.repository.EventRepository;
import com.chilltime.planifyapi.repository.CalendarRepository;
import com.chilltime.planifyapi.service.SAEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SAEventUnitTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private CalendarRepository calendarRepository;

    @InjectMocks
    private SAEvent saevent;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateEventWithValidInput() {
        // Create test event
        Event event = new Event();
        event.setName("evento Test");
        event.setDate(LocalDate.now().plusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("Ubicacion Test");

        // Setup calendar
        Long calendarId = 1L;
        Calendar calendar = new Calendar();
        calendar.setId(calendarId);

        // Setup mock behavior
        when(calendarRepository.findById(calendarId)).thenReturn(Optional.of(calendar));
        when(eventRepository.save(event)).thenReturn(event);

        // Save mutable state for verification
        Event savedEvent = new Event();
        savedEvent.setName(event.getName());
        savedEvent.setDate(event.getDate());
        savedEvent.setTime(event.getTime());
        savedEvent.setLocation(event.getLocation());
        savedEvent.setCalendars(new HashSet<>());
        calendar.setEvents(new HashSet<>());

        when(eventRepository.save(event)).thenReturn(savedEvent);

        // Call service method
        Event result = saevent.createEvent(event, calendarId);

        // Verify results
        assertNotNull(result);
        assertEquals("evento Test", result.getName());
        verify(calendarRepository).findById(calendarId);
        verify(eventRepository).save(event);
        verify(calendarRepository).save(calendar);
    }

    @Test
    public void testCreateEventWithEmptyFields() {
        // Create test event with empty fields
        Event event = new Event();
        Long calendarId = 1L;

        // Test exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saevent.createEvent(event, calendarId);
        });

        assertEquals("Rellene todos los campos vacíos", exception.getMessage());

        // Verify no repository calls were made
        verify(eventRepository, never()).save(any(Event.class));
        verify(calendarRepository, never()).save(any(Calendar.class));
    }

    @Test
    public void testCreateEventWithPastDate() {
        // Create test event with past date
        Event event = new Event();
        event.setName("evento Test");
        event.setDate(LocalDate.now().minusDays(1));
        event.setTime(LocalTime.now());
        event.setLocation("Ubicacion Test");
        Long calendarId = 1L;

        // Test exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saevent.createEvent(event, calendarId);
        });

        assertEquals("La fecha no puede ser anterior a la fecha actual", exception.getMessage());

        // Verify no repository calls were made
        verify(eventRepository, never()).save(any(Event.class));
        verify(calendarRepository, never()).save(any(Calendar.class));
    }

    @Test
    public void testCreateEventWithNoASCIICharacter() {
        // Create test event with non-ASCII characters
        Event event = new Event();
        event.setName("evento Test");
        event.setDate(LocalDate.now().plusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("Ubicación con ñ");
        Long calendarId = 1L;

        // Test exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saevent.createEvent(event, calendarId);
        });

        assertEquals("Los campos nombre y ubicación deben ser caracteres ASCII", exception.getMessage());

        // Verify no repository calls were made
        verify(eventRepository, never()).save(any(Event.class));
        verify(calendarRepository, never()).save(any(Calendar.class));
    }

    @Test
    public void testCreateEventWithTooLongName() {
        // Create test event with name longer than 20 characters
        Event event = new Event();
        event.setName("This name is definitely longer than twenty characters");
        event.setDate(LocalDate.now().plusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("Ubicacion Test");
        Long calendarId = 1L;

        // Test exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saevent.createEvent(event, calendarId);
        });

        assertEquals("El campo nombre no puede tener más de 20 caracteres", exception.getMessage());

        // Verify no repository calls were made
        verify(eventRepository, never()).save(any(Event.class));
        verify(calendarRepository, never()).save(any(Calendar.class));
    }

    @Test
    public void testCreateEventWithNonExistentCalendar() {
        // Create test event
        Event event = new Event();
        event.setName("evento Test");
        event.setDate(LocalDate.now().plusDays(1));
        event.setTime(LocalTime.now().plusHours(1));
        event.setLocation("Ubicacion Test");
        Long calendarId = 999L;

        // Setup calendar repository to return empty
        when(calendarRepository.findById(calendarId)).thenReturn(Optional.empty());

        // Test exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saevent.createEvent(event, calendarId);
        });

        assertEquals("Calendario no encontrado", exception.getMessage());

        // Verify repository calls
        verify(calendarRepository).findById(calendarId);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    public void testGetEventsByCalendarId() {
        Long calendarId = 1L;
        Calendar calendar = new Calendar();
        calendar.setId(calendarId);

        List<Event> events = new ArrayList<>();
        events.add(new Event());

        when(calendarRepository.findById(calendarId)).thenReturn(Optional.of(calendar));
        when(eventRepository.findByCalendars(calendar)).thenReturn(events);

        TContext result = saevent.getEventsByCalendarId(calendarId);

        assertNotNull(result);
        assertEquals(200, result.getStatus_code());
        assertEquals("Eventos obtenidos correctamente", result.getMessage());
        assertEquals(events, result.getData());
    }

    @Test
    public void testGetEventsByNonExistentCalendarId() {
        Long calendarId = 999L;

        when(calendarRepository.findById(calendarId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            saevent.getEventsByCalendarId(calendarId);
        });

        verify(calendarRepository).findById(calendarId);
        verify(eventRepository, never()).findByCalendars(any(Calendar.class));
    }
}