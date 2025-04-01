package com.chilltime.planifyapi;

import com.chilltime.planifyapi.controller.CodeController;
import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.CalendarCode;
import com.chilltime.planifyapi.service.SACalendarCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class CodeControllerUnitTest {

    @Mock
    private SACalendarCode saCode;

    @InjectMocks
    private CodeController codeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCodeWithValidInput() {

        CalendarCode code = new CalendarCode();

        TContext context = new TContext();
        context.setStatus_code(200);
        context.setMessage("Se ha registrado el codigo de union");
        context.setData(code);

        when(saCode.createCode(code)).thenReturn(context);

        ResponseEntity<TContext> response = codeController.createCode(code);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Se ha registrado el codigo de union", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    public void testCreateCodeWithEmptyCalendarInput() {

        CalendarCode code = new CalendarCode();
        code.setCalendar(new Calendar());

        TContext context = new TContext();
        context.setStatus_code(404);
        context.setMessage("Calendario no encontrado");
        context.setData(code);

        when(saCode.createCode(code)).thenReturn(context);

        ResponseEntity<TContext> response = codeController.createCode(code);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Calendario no encontrado", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

}
