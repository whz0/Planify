package com.chilltime.planifyapi;

import com.chilltime.planifyapi.entity.Planner;
import com.chilltime.planifyapi.repository.PlannerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "logging.level.org.springframework=DEBUG",
        "logging.level.com.chilltime=DEBUG"
})
public class PlannerControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("planify")
            .withUsername("user")
            .withPassword("password")
            .withReuse(true)
            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 2));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlannerRepository plannerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public DataSource dataSource() {
            return DataSourceBuilder.create()
                    .url(postgreSQLContainer.getJdbcUrl())
                    .username(postgreSQLContainer.getUsername())
                    .password(postgreSQLContainer.getPassword())
                    .driverClassName("org.postgresql.Driver")
                    .build();
        }
    }

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    @BeforeEach
    void setUp() {
        // Create a test user for login tests
        Planner testUser = new Planner();
        testUser.setUsername("loginTestUser");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setRole("ROLE_USER");
        plannerRepository.save(testUser);
    }

    @AfterEach
    void cleanDatabase() {
        plannerRepository.deleteAll();
    }

    @Test
    public void testRegisterPlannerWithValidData() throws Exception {
        // Create a planner with valid data
        MvcResult result = mockMvc.perform(post("/planner/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "testUser",
                            "password": "password123",
                            "role": "ROLE_USER"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.message").value("Planner registrado correctamente"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("testUser"))
                .andReturn();

        // Verify that the planner was saved in the database
        Planner savedPlanner = plannerRepository.findByUsername("testUser");
        assertNotNull(savedPlanner, "The planner should be saved in the database");
        assertEquals("testUser", savedPlanner.getUsername());
        assertEquals("ROLE_USER", savedPlanner.getRole());
    }

    @Test
    public void testRegisterPlannerWithEmptyUsername() throws Exception {
        mockMvc.perform(post("/planner/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "",
                            "password": "password123",
                            "role": "ROLE_USER"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.message").value("El usuario ha dejado alguno de los campos vacíos"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void testRegisterPlannerWithEmptyPassword() throws Exception {
        mockMvc.perform(post("/planner/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "testUser",
                            "password": "",
                            "role": "ROLE_USER"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.message").value("El usuario ha dejado alguno de los campos vacíos"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void testRegisterPlannerWithUsernameTooLong() throws Exception {
        mockMvc.perform(post("/planner/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "usernameTooLongForTheSystem",
                            "password": "password123",
                            "role": "ROLE_USER"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status_code").value(400))
                .andExpect(jsonPath("$.message").value("El nombre de usuario no es válido. Debe tener máximo 15 caracteres"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void testRegisterPlannerWithDuplicateUsername() throws Exception {
        // First we register a user
        mockMvc.perform(post("/planner/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "duplicateUser",
                            "password": "password123",
                            "role": "ROLE_USER"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.message").value("Planner registrado correctamente"));

        // Try to register another user with the same name
        mockMvc.perform(post("/planner/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "duplicateUser",
                            "password": "anotherpassword",
                            "role": "ROLE_USER"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status_code").value(400))
                .andExpect(jsonPath("$.message").value("El nombre de usuario ya existe"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void testRegisterPlannerWithoutRoleAssignsDefaultRole() throws Exception {
        // Register a planner without an explicit role
        mockMvc.perform(post("/planner/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "userWithoutRole",
                            "password": "password123"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.message").value("Planner registrado correctamente"))
                .andExpect(jsonPath("$.data.role").value("ROLE_USER"));

        // Verify that the default role was assigned correctly
        Planner savedPlanner = plannerRepository.findByUsername("userWithoutRole");
        assertNotNull(savedPlanner);
        assertEquals("ROLE_USER", savedPlanner.getRole());
    }

    // Pruebas para la funcionalidad de inicio de sesión

    @Test
    public void testLoginPlannerWithValidCredentials() throws Exception {
        // Intento de inicio de sesión con credenciales válidas
        mockMvc.perform(post("/planner/login-planner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "loginTestUser",
                            "password": "password123"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.message").value("Login efectuado correctamente"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("loginTestUser"));
    }

    @Test
    public void testLoginPlannerWithInvalidPassword() throws Exception {
        // Intento de inicio de sesión con contraseña incorrecta
        mockMvc.perform(post("/planner/login-planner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "loginTestUser",
                            "password": "wrongPassword"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.message").value("El usuario o la contraseña no son correctos"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void testLoginPlannerWithNonexistentUsername() throws Exception {
        // Intento de inicio de sesión con nombre de usuario que no existe
        mockMvc.perform(post("/planner/login-planner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "nonExistentUser",
                            "password": "password123"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.message").value("El usuario o la contraseña no son correctos"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void testLoginPlannerWithEmptyUsername() throws Exception {
        // Intento de inicio de sesión con nombre de usuario vacío
        mockMvc.perform(post("/planner/login-planner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "",
                            "password": "password123"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.message").value("El usuario o la contraseña no son correctos"));
    }

    @Test
    public void testLoginPlannerWithEmptyPassword() throws Exception {
        // Intento de inicio de sesión con contraseña vacía
        mockMvc.perform(post("/planner/login-planner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "loginTestUser",
                            "password": ""
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.message").value("El usuario o la contraseña no son correctos"));
    }
}