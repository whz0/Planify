package com.chilltime.planifyapi;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.controller.CalendarController;
import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.service.SACalendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CalendarControllerUnitTest {

    @Mock
    private SACalendar saCalendar;

    @InjectMocks
    private CalendarController calendarController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePrivateCalendarWithValidInput() {
        Calendar calendar = new Calendar();
        calendar.setName("Calendario Test");
        calendar.setDescription("Descripción Test");
        calendar.setId_client(1L);

        TContext context = new TContext(200, "Creado correctamente", calendar);

        when(saCalendar.createPrivateCalendar(calendar)).thenReturn(context);

        ResponseEntity<TContext> response = calendarController.CreatePrivateCalendar(calendar);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Creado correctamente", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    public void testCreatePrivateCalendarWithInvalidName() {
        Calendar calendar = new Calendar();
        calendar.setName("");
        calendar.setDescription("Descripción Test");
        calendar.setId_client(1L);

        when(saCalendar.createPrivateCalendar(calendar)).thenThrow(new IllegalArgumentException("El nombre es obligatorio y no puede estar compuesto por espacios en blanco."));

        ResponseEntity<TContext> response = calendarController.CreatePrivateCalendar(calendar);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("El nombre es obligatorio y no puede estar compuesto por espacios en blanco.", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    public void testCreatePrivateCalendarWithLongDescription() {
        Calendar calendar = new Calendar();
        calendar.setName("Calendario Test");
        calendar.setDescription("Descripción".repeat(30)); // Descripción demasiado larga
        calendar.setId_client(1L);

        when(saCalendar.createPrivateCalendar(calendar)).thenThrow(new IllegalArgumentException("La descripción debe tener un máximo de 255 caracteres."));

        ResponseEntity<TContext> response = calendarController.CreatePrivateCalendar(calendar);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("La descripción debe tener un máximo de 255 caracteres.", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    public void testCreatePrivateCalendarWithDuplicateName() {
        Calendar calendar = new Calendar();
        calendar.setName("Calendario Test");
        calendar.setDescription("Descripción Test");
        calendar.setId_client(1L);

        when(saCalendar.createPrivateCalendar(calendar)).thenThrow(new IllegalArgumentException("El nombre ya está en uso por otro calendario privado suyo. Por favor, elija otro nombre."));

        ResponseEntity<TContext> response = calendarController.CreatePrivateCalendar(calendar);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("El nombre ya está en uso por otro calendario privado suyo. Por favor, elija otro nombre.", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    public void testCreatePrivateCalendarWithNonAlphanumericName() {
        Calendar calendar = new Calendar();
        calendar.setName("Calendario@123");
        calendar.setDescription("Descripción Test");
        calendar.setId_client(1L);

        when(saCalendar.createPrivateCalendar(calendar)).thenThrow(new IllegalArgumentException("El nombre debe contener solo caracteres alfanuméricos y espacios."));

        ResponseEntity<TContext> response = calendarController.CreatePrivateCalendar(calendar);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("El nombre debe contener solo caracteres alfanuméricos y espacios.", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    public void testCreatePrivateCalendarWithNonExistentClient() {
        Calendar calendar = new Calendar();
        calendar.setName("Calendario Test");
        calendar.setDescription("Descripción Test");
        calendar.setId_client(1L);

        when(saCalendar.createPrivateCalendar(calendar)).thenThrow(new RuntimeException("Usuario no encontrado"));

        ResponseEntity<TContext> response = calendarController.CreatePrivateCalendar(calendar);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Usuario no encontrado", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}