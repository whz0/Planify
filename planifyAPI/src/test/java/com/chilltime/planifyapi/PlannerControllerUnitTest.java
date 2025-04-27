package com.chilltime.planifyapi;

import com.chilltime.planifyapi.controller.PlannerController;
import com.chilltime.planifyapi.entity.Planner;
import com.chilltime.planifyapi.service.SAPlanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PlannerControllerUnitTest {

    @Mock
    private SAPlanner saPlanner;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PlannerController plannerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Mock el comportamiento del PasswordEncoder para que devuelva una cadena hasheada simulada
        when(passwordEncoder.encode(any(String.class))).thenReturn("hashedPassword");
    }

    @Test
    public void testRegisterPlannerWithValidInput() {
        Planner planner = new Planner();
        planner.setUsername("testuser");
        planner.setPassword("password123");
        planner.setRole("USER");

        Planner savedPlanner = new Planner();
        savedPlanner.setUsername("testuser");
        savedPlanner.setPassword("hashedPassword");
        savedPlanner.setRole("USER");

        TContext context = new TContext(200, "Planner registrado correctamente", savedPlanner);

        when(saPlanner.register(any(Planner.class))).thenReturn(context);

        ResponseEntity<TContext> response = plannerController.registerPlanner(planner);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Planner registrado correctamente", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());

        // Verificar que se llamó a passwordEncoder.encode() con la contraseña original
        verify(passwordEncoder).encode("password123");
    }

    @Test
    public void testRegisterPlannerWithEmptyUsername() {
        Planner planner = new Planner();
        planner.setUsername("");
        planner.setPassword("password123");
        planner.setRole("USER");

        when(saPlanner.register(any(Planner.class))).thenThrow(new IllegalArgumentException("El usuario ha dejado alguno de los campos vacíos"));

        ResponseEntity<TContext> response = plannerController.registerPlanner(planner);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("El usuario ha dejado alguno de los campos vacíos", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    public void testRegisterPlannerWithEmptyPassword() {
        Planner planner = new Planner();
        planner.setUsername("testuser");
        planner.setPassword("");
        planner.setRole("USER");

        when(saPlanner.register(any(Planner.class))).thenThrow(new IllegalArgumentException("El usuario ha dejado alguno de los campos vacíos"));

        ResponseEntity<TContext> response = plannerController.registerPlanner(planner);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("El usuario ha dejado alguno de los campos vacíos", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    public void testRegisterPlannerWithNullUsername() {
        Planner planner = new Planner();
        planner.setUsername(null);
        planner.setPassword("password123");
        planner.setRole("USER");

        when(saPlanner.register(any(Planner.class))).thenThrow(new IllegalArgumentException("El usuario ha dejado alguno de los campos vacíos"));

        ResponseEntity<TContext> response = plannerController.registerPlanner(planner);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("El usuario ha dejado alguno de los campos vacíos", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    public void testRegisterPlannerWithNullPassword() {
        Planner planner = new Planner();
        planner.setUsername("testuser");
        planner.setPassword(null);
        planner.setRole("USER");

        when(saPlanner.register(any(Planner.class))).thenThrow(new IllegalArgumentException("El usuario ha dejado alguno de los campos vacíos"));

        ResponseEntity<TContext> response = plannerController.registerPlanner(planner);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("El usuario ha dejado alguno de los campos vacíos", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    public void testRegisterPlannerWithUsernameTooLong() {
        Planner planner = new Planner();
        planner.setUsername("usuariodemasiaaadolargo"); // Más de 15 caracteres
        planner.setPassword("password123");
        planner.setRole("USER");

        when(saPlanner.register(any(Planner.class))).thenThrow(new IllegalArgumentException("El nombre de usuario no es válido. Debe tener máximo 15 caracteres"));

        ResponseEntity<TContext> response = plannerController.registerPlanner(planner);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("El nombre de usuario no es válido. Debe tener máximo 15 caracteres", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    public void testRegisterPlannerWithExistingUsername() {
        Planner planner = new Planner();
        planner.setUsername("existinguser");
        planner.setPassword("password123");
        planner.setRole("USER");

        when(saPlanner.register(any(Planner.class))).thenThrow(new IllegalArgumentException("El nombre de usuario ya existe"));

        ResponseEntity<TContext> response = plannerController.registerPlanner(planner);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("El nombre de usuario ya existe", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    public void testRegisterPlannerWithInvalidPasswordLength() {
        // Aunque este requisito está comentado en el código, podemos agregar la prueba
        // como referencia para cuando se active

        Planner planner = new Planner();
        planner.setUsername("testuser");
        planner.setPassword("short"); // Menos de 8 caracteres
        planner.setRole("USER");

        when(saPlanner.register(any(Planner.class))).thenThrow(new IllegalArgumentException("La contraseña debe de tener entre 8 y 15 caracteres de longitud"));

        ResponseEntity<TContext> response = plannerController.registerPlanner(planner);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("La contraseña debe de tener entre 8 y 15 caracteres de longitud", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    public void testRegisterPlannerWithUnexpectedException() {
        Planner planner = new Planner();
        planner.setUsername("testuser");
        planner.setPassword("password123");
        planner.setRole("USER");

        when(saPlanner.register(any(Planner.class))).thenThrow(new RuntimeException("Error inesperado en el servidor"));

        ResponseEntity<TContext> response = plannerController.registerPlanner(planner);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Error inesperado en el servidor", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}