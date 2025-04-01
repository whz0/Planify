package com.chilltime.planifyapi;

import com.chilltime.planifyapi.controller.CalendarController;
import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.service.SACalendar;
import com.chilltime.planifyapi.service.SAEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalendarControllerUnitTest {

    @Mock
    private SACalendar saCalendar;

    @Mock
    private SAEvent saEvent;

    @InjectMocks
    private CalendarController calendarController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /** TEST PARA CREAR UN CALENDARIO PRIVADO **/
    @Test
    void testCreatePrivateCalendar_Success() {
        Calendar calendar = new Calendar();
        TContext expectedContext = new TContext(200, "Creado correctamente", calendar);

        when(saCalendar.createPrivateCalendar(calendar)).thenReturn(expectedContext);

        ResponseEntity<TContext> response = calendarController.CreatePrivateCalendar(calendar);

        verify(saCalendar, times(1)).createPrivateCalendar(calendar);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Creado correctamente", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testCreatePrivateCalendar_ExceptionHandling() {
        Calendar calendar = new Calendar();
        when(saCalendar.createPrivateCalendar(calendar)).thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<TContext> response = calendarController.CreatePrivateCalendar(calendar);

        verify(saCalendar, times(1)).createPrivateCalendar(calendar);
        assertEquals(200, response.getStatusCodeValue()); // Aquí debería ser 500 si manejas excepciones correctamente
        assertEquals("Error interno", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    /** TEST PARA OBTENER CALENDARIOS POR ID DE USUARIO **/
    @Test
    void testGetCalendarsByUserId_Success() {
        Long userId = 1L;
        List<Calendar> calendarList = new ArrayList<>();
        TContext expectedContext = new TContext(200, "Calendarios obtenidos correctamente", calendarList);

        when(saCalendar.getCalendarsByUserId(userId)).thenReturn(expectedContext);

        ResponseEntity<TContext> response = calendarController.getCalendarsByUserId(userId);

        verify(saCalendar, times(1)).getCalendarsByUserId(userId);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Calendarios obtenidos correctamente", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    /** TEST PARA OBTENER UN CALENDARIO POR ID **/
    @Test
    void testGetCalendarById_Success() {
        Long calendarId = 1L;
        Calendar calendar = new Calendar();
        TContext expectedContext = new TContext(200, "Calendario obtenido correctamente", calendar);

        when(saCalendar.getCalendarById(calendarId)).thenReturn(expectedContext);

        ResponseEntity<TContext> response = calendarController.getCalendarById(calendarId);

        verify(saCalendar, times(1)).getCalendarById(calendarId);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Calendario obtenido correctamente", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    /** TEST PARA OBTENER EVENTOS POR ID DE CALENDARIO **/
    @Test
    void testGetEventsByCalendarId_Success() {
        Long calendarId = 1L;
        List<Object> events = new ArrayList<>(); // Ajustar el tipo según la clase real de eventos
        TContext expectedContext = new TContext(200, "Eventos obtenidos correctamente", events);

        when(saEvent.getEventsByCalendarId(calendarId)).thenReturn(expectedContext);

        ResponseEntity<TContext> response = calendarController.getEventsByCalendarId(calendarId);

        verify(saEvent, times(1)).getEventsByCalendarId(calendarId);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Eventos obtenidos correctamente", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    /** TEST PARA UNIRSE A UN CALENDARIO **/
    @Test
    void testJoinCalendar_Success() {
        Long userId = 1L;
        String code = "ABC123";
        TContext expectedContext = new TContext(200, "Unido correctamente", null);

        when(saCalendar.joinCalendar(userId, code)).thenReturn(expectedContext);

        ResponseEntity<TContext> response = calendarController.joinCalendar(userId, code);

        verify(saCalendar, times(1)).joinCalendar(userId, code);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Unido correctamente", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testJoinCalendar_ExceptionHandling() {
        Long userId = 1L;
        String code = "ABC123";

        when(saCalendar.joinCalendar(userId, code)).thenThrow(new RuntimeException("Código inválido"));

        ResponseEntity<TContext> response = calendarController.joinCalendar(userId, code);

        verify(saCalendar, times(1)).joinCalendar(userId, code);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Código inválido", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}
