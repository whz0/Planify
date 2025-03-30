package com.chilltime.planifyapi;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(classes = PlanifyApiApplication.class)
@AutoConfigureMockMvc
@WithMockUser
public class EventControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("planifydb")
            .withUsername("user")
            .withPassword("password");

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void setUp() {
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
    }

    @Test
    public void testCreateEvent() throws Exception {
        // Crear un evento con caracteres ASCII
        ResultActions result = mockMvc.perform(post("/event/create-event")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content("{\"name\": \"Evento\", \"date\": \"2028-12-31\", \"time\": \"10:00\", \"location\": \"Ubicacion\", \"active\": true}"));

        // Verificar que el evento fue creado exitosamente
        result.andExpect(status().isOk());
    }

    @Test
    public void testCreateEventWithPastDate() throws Exception {
        // Crear un evento con caracteres ASCII
        ResultActions result = mockMvc.perform(post("/event/create-event")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content("{\"name\": \"Evento\", \"date\": \"2025-03-09\", \"time\": \"10:00\", \"location\": \"Ubicacion\", \"active\": true}"));

        // Verificar que el evento no fue creado debido a la fecha pasada
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateEventWithEmptyFields() throws Exception {
        // Crear un evento con campos vacíos
        ResultActions result = mockMvc.perform(post("/event/create-event")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content("{\"name\": \"Evento\", \"date\": null, \"time\": \"10:00\", \"location\": \"Ubicacion\", \"active\": true}"));

        // Verificar que el evento no fue creado debido a campos vacíos
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateEventWithNoASCIIName() throws Exception {
        // Crear un evento con nombre no ASCII
        ResultActions result = mockMvc.perform(post("/event/create-event")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content("{\"name\": \"España\", \"date\": \"2025-12-31\", \"time\": \"10:00\", \"location\": \"Ubicacion\", \"active\": true}"));

        // Verificar que el evento no fue creado debido a nombre no ASCII
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateEventWithNoASCIILocation() throws Exception {
        // Crear un evento con ubicación no ASCII
        ResultActions result = mockMvc.perform(post("/event/create-event")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content("{\"name\": \"Evento\", \"date\": \"2025-12-31\", \"time\": \"10:00\", \"location\": \"España\", \"active\": true}"));

        // Verificar que el evento no fue creado debido a ubicación no ASCII
        result.andExpect(status().isBadRequest());
    }
}
