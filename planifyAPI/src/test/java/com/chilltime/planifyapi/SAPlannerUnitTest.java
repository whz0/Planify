package com.chilltime.planifyapi;

import com.chilltime.planifyapi.entity.Planner;
import com.chilltime.planifyapi.repository.PlannerRepository;
import com.chilltime.planifyapi.service.SAPlanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SAPlannerUnitTest {

    @Mock
    private PlannerRepository plannerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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

        // Act
        TContext result = saPlanner.register(planner);

        // Assert
        assertEquals(200, result.getStatus_code());
        assertEquals("El usuario ha dejado alguno de los campos vacíos", result.getMessage());
        assertNull(result.getData());

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

        // Act
        TContext result = saPlanner.register(planner);

        // Assert
        assertEquals(200, result.getStatus_code());
        assertEquals("El usuario ha dejado alguno de los campos vacíos", result.getMessage());
        assertNull(result.getData());

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

        // Act
        TContext result = saPlanner.register(planner);

        // Assert
        assertEquals(200, result.getStatus_code());
        assertEquals("El usuario ha dejado alguno de los campos vacíos", result.getMessage());
        assertNull(result.getData());

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

        // Act
        TContext result = saPlanner.register(planner);

        // Assert
        assertEquals(200, result.getStatus_code());
        assertEquals("El usuario ha dejado alguno de los campos vacíos", result.getMessage());
        assertNull(result.getData());

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

        // Act
        TContext result = saPlanner.register(planner);

        // Assert
        assertEquals(200, result.getStatus_code());
        assertEquals("El usuario ha dejado alguno de los campos vacíos", result.getMessage());
        assertNull(result.getData());

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

        // Act
        TContext result = saPlanner.register(planner);

        // Assert
        assertEquals(200, result.getStatus_code());
        assertEquals("El usuario ha dejado alguno de los campos vacíos", result.getMessage());
        assertNull(result.getData());

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

        // Act
        TContext result = saPlanner.register(planner);

        // Assert
        assertEquals(200, result.getStatus_code());
        assertEquals("El nombre de usuario no es válido. Debe tener máximo 15 caracteres", result.getMessage());
        assertNull(result.getData());

        // Verify
        // No verificamos la llamada a findByUsername porque sucede antes la validación de longitud
        verify(plannerRepository, never()).findByUsername(anyString());
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

        // Act
        TContext result = saPlanner.register(newPlanner);

        // Assert
        assertEquals(200, result.getStatus_code());
        assertEquals("El nombre de usuario ya existe", result.getMessage());
        assertNull(result.getData());

        // Verify
        verify(plannerRepository, times(1)).findByUsername("testuser");
        verify(plannerRepository, never()).save(any());
    }

    @Test
    public void testRegisterWithShortPassword() {
        // Arrange
        Planner planner = new Planner();
        planner.setUsername("testuser");
        planner.setPassword("short"); // Menos de 8 caracteres
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
    public void testRegisterWithLongPassword() {
        // Arrange
        Planner planner = new Planner();
        planner.setUsername("testuser");
        planner.setPassword("thispasswordistoolongforthevalidation"); // Más de 15 caracteres
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

    @Test
    public void testLoginWithCorrectCredentials() {
        // Arrange
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        Planner loginPlanner = new Planner();
        loginPlanner.setUsername("testuser");
        loginPlanner.setPassword(rawPassword);

        Planner savedPlanner = new Planner();
        savedPlanner.setId(1L);
        savedPlanner.setUsername("testuser");
        savedPlanner.setPassword(encodedPassword);
        savedPlanner.setRole("USER");

        when(plannerRepository.findByUsername("testuser")).thenReturn(savedPlanner);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        // Act
        TContext result = saPlanner.login(loginPlanner);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatus_code());
        assertEquals("Login efectuado correctamente", result.getMessage());
        assertEquals(savedPlanner, result.getData());

        // Verify
        verify(plannerRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }

    @Test
    public void testLoginWithIncorrectUsername() {
        // Arrange
        Planner loginPlanner = new Planner();
        loginPlanner.setUsername("nonexistentuser");
        loginPlanner.setPassword("password123");

        when(plannerRepository.findByUsername("nonexistentuser")).thenReturn(null);

        // Act
        TContext result = saPlanner.login(loginPlanner);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatus_code());
        assertEquals("El usuario o la contraseña no son correctos", result.getMessage());
        assertNull(result.getData());

        // Verify
        verify(plannerRepository, times(1)).findByUsername("nonexistentuser");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    public void testLoginWithIncorrectPassword() {
        // Arrange
        String rawPassword = "wrongpassword";
        String encodedPassword = "encodedPassword123";

        Planner loginPlanner = new Planner();
        loginPlanner.setUsername("testuser");
        loginPlanner.setPassword(rawPassword);

        Planner savedPlanner = new Planner();
        savedPlanner.setId(1L);
        savedPlanner.setUsername("testuser");
        savedPlanner.setPassword(encodedPassword);
        savedPlanner.setRole("USER");

        when(plannerRepository.findByUsername("testuser")).thenReturn(savedPlanner);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        // Act
        TContext result = saPlanner.login(loginPlanner);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatus_code());
        assertEquals("El usuario o la contraseña no son correctos", result.getMessage());
        assertNull(result.getData());

        // Verify
        verify(plannerRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }
}