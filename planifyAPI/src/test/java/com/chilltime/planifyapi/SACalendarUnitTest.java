package com.chilltime.planifyapi;

import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.Planner;
import com.chilltime.planifyapi.repository.CalendarRepository;
import com.chilltime.planifyapi.repository.PlannerRepository;
import com.chilltime.planifyapi.service.SACalendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class SACalendarUnitTest {
    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private PlannerRepository plannerRepository;

    @InjectMocks
    private SACalendar saCalendar;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePrivateCalendarWithValidInput() {
        Calendar calendar = new Calendar();
        calendar.setName("Calendario Test");
        calendar.setDescription("Descripción Test");
        calendar.setId_planner(1L);

        Planner planner = new Planner();
        planner.setId(1L);

        when(plannerRepository.findById(1L)).thenReturn(java.util.Optional.of(planner));
        when(calendarRepository.save(calendar)).thenReturn(calendar);

        TContext result = saCalendar.createPrivateCalendar(calendar);

        assertNotNull(result);
        assertEquals(200, result.getStatus_code());
        assertEquals("Creado correctamente", result.getMessage());
        assertNotNull(result.getData());
    }

    @Test
    public void testCreatePrivateCalendarWithInvalidName() {
        Calendar calendar = new Calendar();
        calendar.setName("");
        calendar.setDescription("Descripción Test");
        calendar.setId_planner(1L);

        Planner planner = new Planner();
        planner.setId(1L);

        when(plannerRepository.findById(1L)).thenReturn(java.util.Optional.of(planner));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saCalendar.createPrivateCalendar(calendar);
        });

        assertEquals("El nombre es obligatorio y no puede estar compuesto por espacios en blanco.", exception.getMessage());
    }
    @Test
    public void testCreatePrivateCalendarWithLongDescription() {
        Calendar calendar = new Calendar();
        calendar.setName("Calendario Test");
        calendar.setDescription("Descripción".repeat(30)); // Descripción demasiado larga
        calendar.setId_planner(1L);

        Planner planner = new Planner();
        planner.setId(1L);

        when(plannerRepository.findById(1L)).thenReturn(java.util.Optional.of(planner));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saCalendar.createPrivateCalendar(calendar);
        });

        assertEquals("La descripción debe tener un máximo de 255 caracteres.", exception.getMessage());
    }

    @Test
    public void testCreatePrivateCalendarWithDuplicateName() {
        Calendar calendar = new Calendar();
        calendar.setName("Calendario Test");
        calendar.setDescription("Descripción Test");
        calendar.setId_planner(1L);

        Planner planner = new Planner();
        planner.setId(1L);

        when(plannerRepository.findById(1L)).thenReturn(java.util.Optional.of(planner));
        when(calendarRepository.existsByNameAndPlanner("Calendario Test", planner)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saCalendar.createPrivateCalendar(calendar);
        });

        assertEquals("El nombre ya está en uso por otro calendario privado suyo. Por favor, elija otro nombre.", exception.getMessage());
    }

    @Test
    public void testCreatePrivateCalendarWithNonAlphanumericName() {
        Calendar calendar = new Calendar();
        calendar.setName("Calendario@123");
        calendar.setDescription("Descripción Test");
        calendar.setId_planner(1L);

        Planner planner = new Planner();
        planner.setId(1L);

        when(plannerRepository.findById(1L)).thenReturn(java.util.Optional.of(planner));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saCalendar.createPrivateCalendar(calendar);
        });

        assertEquals("El nombre debe contener solo caracteres alfanuméricos y espacios.", exception.getMessage());
    }

    @Test
    public void testCreatePrivateCalendarWithNonExistentPlanner() {
        Calendar calendar = new Calendar();
        calendar.setName("Calendario Test");
        calendar.setDescription("Descripción Test");
        calendar.setId_planner(1L);

        when(plannerRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            saCalendar.createPrivateCalendar(calendar);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
    }



}
