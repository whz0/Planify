package com.chilltime.planifyapi;

import com.chilltime.planifyapi.entity.Planner;
import com.chilltime.planifyapi.repository.PlannerRepository;
import com.chilltime.planifyapi.service.SAPlanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SAPlannerUnitTest {

    @Mock
    private PlannerRepository plannerRepository;

    @InjectMocks
    private SAPlanner saPlanner;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterWithValidPlanner() {
        // Arrange
        Planner planner = new Planner();
        planner.setUsername("testuser");
        planner.setPassword("password123");
        planner.setRole("USER");

        when(plannerRepository.findByUsername("testuser")).thenReturn(null);
        when(plannerRepository.save(any(Planner.class))).thenReturn(planner);

        // Act
        TContext result = saPlanner.register(planner);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatus_code());
        assertEquals("Planner registrado correctamente", result.getMessage());
        assertNotNull(result.getData());

        // Verify
        verify(plannerRepository, times(1)).findByUsername("testuser");
        verify(plannerRepository, times(1)).save(planner);
    }

    @Test
    public void testRegisterWithNullUsername() {
        // Arrange
        Planner planner = new Planner();
        planner.setUsername(null);
        planner.setPassword("password123");
        planner.setRole("USER");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saPlanner.register(planner);
        });

        assertEquals("El usuario ha dejado alguno de los campos vacíos", exception.getMessage());

        // Verify
        verify(plannerRepository, never()).findByUsername(any());
        verify(plannerRepository, never()).save(any());
    }

    @Test
    public void testRegisterWithEmptyUsername() {
        // Arrange
        Planner planner = new Planner();
        planner.setUsername("");
        planner.setPassword("password123");
        planner.setRole("USER");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saPlanner.register(planner);
        });

        assertEquals("El usuario ha dejado alguno de los campos vacíos", exception.getMessage());

        // Verify
        verify(plannerRepository, never()).findByUsername(any());
        verify(plannerRepository, never()).save(any());
    }

    @Test
    public void testRegisterWithBlankUsername() {
        // Arrange
        Planner planner = new Planner();
        planner.setUsername("   ");
        planner.setPassword("password123");
        planner.setRole("USER");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saPlanner.register(planner);
        });

        assertEquals("El usuario ha dejado alguno de los campos vacíos", exception.getMessage());

        // Verify
        verify(plannerRepository, never()).findByUsername(any());
        verify(plannerRepository, never()).save(any());
    }

    @Test
    public void testRegisterWithNullPassword() {
        // Arrange
        Planner planner = new Planner();
        planner.setUsername("testuser");
        planner.setPassword(null);
        planner.setRole("USER");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saPlanner.register(planner);
        });

        assertEquals("El usuario ha dejado alguno de los campos vacíos", exception.getMessage());

        // Verify
        verify(plannerRepository, never()).findByUsername(any());
        verify(plannerRepository, never()).save(any());
    }

    @Test
    public void testRegisterWithEmptyPassword() {
        // Arrange
        Planner planner = new Planner();
        planner.setUsername("testuser");
        planner.setPassword("");
        planner.setRole("USER");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saPlanner.register(planner);
        });

        assertEquals("El usuario ha dejado alguno de los campos vacíos", exception.getMessage());

        // Verify
        verify(plannerRepository, never()).findByUsername(any());
        verify(plannerRepository, never()).save(any());
    }

    @Test
    public void testRegisterWithBlankPassword() {
        // Arrange
        Planner planner = new Planner();
        planner.setUsername("testuser");
        planner.setPassword("   ");
        planner.setRole("USER");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saPlanner.register(planner);
        });

        assertEquals("El usuario ha dejado alguno de los campos vacíos", exception.getMessage());

        // Verify
        verify(plannerRepository, never()).findByUsername(any());
        verify(plannerRepository, never()).save(any());
    }

    @Test
    public void testRegisterWithUsernameTooLong() {
        // Arrange
        Planner planner = new Planner();
        planner.setUsername("usuariodemasiaaadolargo"); // Más de 15 caracteres
        planner.setPassword("password123");
        planner.setRole("USER");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saPlanner.register(planner);
        });

        assertEquals("El nombre de usuario no es válido. Debe tener máximo 15 caracteres", exception.getMessage());

        // Verify
        verify(plannerRepository, never()).findByUsername(any());
        verify(plannerRepository, never()).save(any());
    }

    @Test
    public void testRegisterWithExistingUsername() {
        // Arrange
        Planner existingPlanner = new Planner();
        existingPlanner.setId(1L);
        existingPlanner.setUsername("testuser");
        existingPlanner.setPassword("oldpassword");
        existingPlanner.setRole("USER");

        Planner newPlanner = new Planner();
        newPlanner.setUsername("testuser");
        newPlanner.setPassword("password123");
        newPlanner.setRole("USER");

        when(plannerRepository.findByUsername("testuser")).thenReturn(existingPlanner);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saPlanner.register(newPlanner);
        });

        assertEquals("El nombre de usuario ya existe", exception.getMessage());

        // Verify
        verify(plannerRepository, times(1)).findByUsername("testuser");
        verify(plannerRepository, never()).save(any());
    }

    @Test
    public void testRegisterWithShortPassword() {
        // Aunque este requisito está comentado en el código, podemos agregar la prueba
        // como referencia para cuando se active

        // Arrange
        Planner planner = new Planner();
        planner.setUsername("testuser");
        planner.setPassword("short"); // Menos de 8 caracteres
        planner.setRole("USER");

        // Descomentamos temporalmente la validación para la prueba
        /*
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saPlanner.register(planner);
        });

        assertEquals("La contraseña debe de tener entre 8 y 15 caracteres de longitud", exception.getMessage());

        verify(plannerRepository, never()).findByUsername(any());
        verify(plannerRepository, never()).save(any());
        */

        // Como está comentado en el código original, verificamos que pasa la validación
        when(plannerRepository.findByUsername("testuser")).thenReturn(null);
        when(plannerRepository.save(any(Planner.class))).thenReturn(planner);

        TContext result = saPlanner.register(planner);

        assertNotNull(result);
        assertEquals(200, result.getStatus_code());
    }

    @Test
    public void testRegisterWithLongPassword() {
        // Aunque este requisito está comentado en el código, podemos agregar la prueba
        // como referencia para cuando se active

        // Arrange
        Planner planner = new Planner();
        planner.setUsername("testuser");
        planner.setPassword("thispasswordistoolongforthevalidation"); // Más de 15 caracteres
        planner.setRole("USER");

        // Descomentamos temporalmente la validación para la prueba
        /*
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            saPlanner.register(planner);
        });

        assertEquals("La contraseña debe de tener entre 8 y 15 caracteres de longitud", exception.getMessage());

        verify(plannerRepository, never()).findByUsername(any());
        verify(plannerRepository, never()).save(any());
        */

        // Como está comentado en el código original, verificamos que pasa la validación
        when(plannerRepository.findByUsername("testuser")).thenReturn(null);
        when(plannerRepository.save(any(Planner.class))).thenReturn(planner);

        TContext result = saPlanner.register(planner);

        assertNotNull(result);
        assertEquals(200, result.getStatus_code());
    }

    @Test
    public void testRegisterWithRepositoryException() {
        // Arrange
        Planner planner = new Planner();
        planner.setUsername("testuser");
        planner.setPassword("password123");
        planner.setRole("USER");

        when(plannerRepository.findByUsername("testuser")).thenReturn(null);
        when(plannerRepository.save(any(Planner.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            saPlanner.register(planner);
        });

        assertEquals("Database error", exception.getMessage());

        // Verify
        verify(plannerRepository, times(1)).findByUsername("testuser");
        verify(plannerRepository, times(1)).save(planner);
    }
}