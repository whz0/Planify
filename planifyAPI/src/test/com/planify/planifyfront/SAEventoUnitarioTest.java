package com.planify.planifyfront;

import com.planifyAPI.planifyAPI.entity.Evento;
import com.planifyAPI.planifyAPI.repository.EventoRepository;
import com.planifyAPI.planifyAPI.service.SAEvento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SAEventoUnitarioTest {

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private SAEvento saEvento;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearEventoConDatosValidos() {
        Evento evento = new Evento();
        evento.setNombre("Evento Test");
        evento.setFecha(LocalDate.now().plusDays(1));
        evento.setHora(LocalTime.now().plusHours(1));
        evento.setUbicacion("Ubicacion Test");

        when(eventoRepository.save(evento)).thenReturn(evento);

        Evento result = saEvento.crearEvento(evento);

        assertNotNull(result);
        assertEquals("Evento Test", result.getNombre());
    }

    @Test
    public void testCrearEventoConCamposVacios() {
        Evento evento = new Evento();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saEvento.crearEvento(evento);
        });

        assertEquals("Rellene todos los campos vacíos", exception.getMessage());
    }

    @Test
    public void testCrearEventoConFechaPasada() {
        Evento evento = new Evento();
        evento.setNombre("Evento Test");
        evento.setFecha(LocalDate.now().minusDays(1));
        evento.setHora(LocalTime.now());
        evento.setUbicacion("Ubicacion Test");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saEvento.crearEvento(evento);
        });

        assertEquals("La fecha no puede ser anterior a la fecha actual", exception.getMessage());
    }

    @Test
    public void testCrearEventoConNombreLargo() {
        Evento evento = new Evento();
        evento.setNombre("Nombre de evento muy largo que excede el límite");
        evento.setFecha(LocalDate.now().plusDays(1));
        evento.setHora(LocalTime.now().plusHours(1));
        evento.setUbicacion("Ubicacion Test");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saEvento.crearEvento(evento);
        });

        assertEquals("El campo nombre no puede tener menos de 20 caracteres", exception.getMessage());
    }

    @Test
    public void testCrearEventoConCaracteresNoASCII() {
        Evento evento = new Evento();
        evento.setNombre("Evento Test");
        evento.setFecha(LocalDate.now().plusDays(1));
        evento.setHora(LocalTime.now().plusHours(1));
        evento.setUbicacion("Ubicación con ñ");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saEvento.crearEvento(evento);
        });

        assertEquals("Los campos nombre y ubicación deben ser caracteres ASCII", exception.getMessage());
    }
}
