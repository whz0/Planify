package com.chilltime.planifyapi;

import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.CalendarCode;
import com.chilltime.planifyapi.entity.Client;
import com.chilltime.planifyapi.repository.CalendarRepository;
import com.chilltime.planifyapi.repository.ClientRepository;
import com.chilltime.planifyapi.repository.CodigoRepository;
import com.chilltime.planifyapi.service.SACalendarCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SACalendarCodeUnitTest {

    /*private class StubClient extends Client {
        public StubClient() {
            super();
        }
    }

    private class StubCalendar extends Calendar {
        public StubCalendar() {
            super();
            this.setName("StubCalendar");
            this.setDescription("Stub Description");
            this.setId_client(1L);
            this.setId(1L);
        }
    }*/

    @Mock
    private CodigoRepository codeRepository;

    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private SACalendarCode saCode;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCalendarCodeWithValidInput() {

        Client client = new Client();
        client.setId(1L);
        client.setUsername("testUser");
        client.setPassword("testPassword");

        when(clientRepository.findById(client.getId())).thenReturn(java.util.Optional.of(client));

        Calendar calendar = new Calendar();
        calendar.setId(1L);
        calendar.setClient(client);
        calendar.setType("private");
        when(calendarRepository.findById(calendar.getId())).thenReturn(java.util.Optional.of(calendar));

        CalendarCode code = new CalendarCode();
        code.setCalendar(calendar);
        code.setCode("ABC123");
        code.setUsed(false);
        code.setId(1L);
        when(codeRepository.save(code)).thenReturn(code);

        TContext result = saCode.createCode(code);

        assertNotNull(result);
        assertEquals(200, result.getStatus_code());
        assertEquals("Se ha registrado el codigo de union", result.getMessage());
        assertNotNull(result.getData());
    }

    @Test
    public void testCreateCalendarCodeWithNoCalendarInput() {

        Calendar calendar = new Calendar();

        CalendarCode code = new CalendarCode();
        code.setCalendar(calendar);

        TContext result = saCode.createCode(code);

        assertNotNull(result);
        assertEquals(404, result.getStatus_code());
        assertEquals("Calendario no encontrado", result.getMessage());
        assertNull(result.getData());
    }

    /*
    @Test
    public void testCreateCalendarCodeWithClientThatIsNotTheOwner() {

        Client client = new Client();
        Client client2 = new Client();
        client.setId(1L);
        client2.setId(2L);

        when(clientRepository.findById(client.getId())).thenReturn(java.util.Optional.of(client));
        when(clientRepository.findById(client2.getId())).thenReturn(java.util.Optional.of(client2));

        Calendar calendar = new Calendar();
        calendar.setType("publico");
        when(calendarRepository.findById(calendar.getId())).thenReturn(java.util.Optional.of(calendar));
        calendar.setClient(client);
        calendar.setClient(client2);

        CalendarCode code = new CalendarCode();
        code.setCalendar(calendar);

        TContext result = saCode.createCode(code);

        assertNotNull(result);
        assertEquals(404, result.getStatus_code());
        assertEquals("Calendario no encontrado", result.getMessage());
        assertNull(result.getData());
    }
    */

    @Test
    public void testCreateCalendarCodeWithNullInput() {

        String code = null;

        TContext result = saCode.validateCode(code);

        assertNotNull(result);
        assertEquals(400, result.getStatus_code());
        assertEquals("Código inválido", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    public void testCreateCalendarCodeWithEmptyInput() {

        String code = new String();

        TContext result = saCode.validateCode(code);

        assertNotNull(result);
        assertEquals(400, result.getStatus_code());
        assertEquals("Código inválido", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    public void testCreateCalendarCodeWithNotFoundInput() {

        String code = new String(" ");

        TContext result = saCode.validateCode(code);

        assertNotNull(result);
        assertEquals(404, result.getStatus_code());
        assertEquals("Código no encontrado", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    public void testCreateCalendarCodeWithUsedCode() {

        CalendarCode code = new CalendarCode();
        code.setCode("ABC123");
        code.setUsed(true);
        code.setId(1L);

        String codeIn = new String("ABC123");

        when(codeRepository.findByCode(codeIn)).thenReturn(Optional.of(code));

        TContext result = saCode.validateCode(codeIn);

        assertNotNull(result);
        assertEquals(409, result.getStatus_code());
        assertEquals("El código ya ha sido usado", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    public void testCreateCalendarCodeWithValidCode() {

        CalendarCode code = new CalendarCode();
        code.setCode("ABC123");
        code.setUsed(false);
        code.setId(1L);

        String codeIn = new String("ABC123");

        when(codeRepository.findByCode(codeIn)).thenReturn(Optional.of(code));
        when(codeRepository.save(code)).thenReturn(code);

        TContext result = saCode.validateCode(codeIn);

        assertNotNull(result);
        assertEquals(200, result.getStatus_code());
        assertEquals("Código validado correctamente", result.getMessage());
        assertNotNull(result.getData());
    }
}
