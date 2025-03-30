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
        // Create an event with ASCII characters
        ResultActions result = mockMvc.perform(post("/event/create-event")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content("{\"name\": \"Evento\", \"date\": \"2028-12-31\", \"time\": \"10:00\", \"location\": \"Ubicacion\", \"active\": \"true\"}"));

        // Check that the event was created successfully
        result.andExpect(status().is(200));
    }
    //Evento con date pasada
    @Test
    public void testCreateEventWithPastDate() throws Exception {
        // Create an event with ASCII characters
        ResultActions result = mockMvc.perform(post("/event/create-event")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content("{\"name\": \"Evento\", \"date\": \"2025-03-9\", \"time\": \"10:00\", \"location\": \"Ubicacion\"}"));

        // Check that the event was created successfully
        result.andExpect(status().is(400));
    }

    //Evento con datos vacios
    @Test
    public void testCreateEventWithEmptyFields() throws Exception {
        // Create an event with ASCII characters
        ResultActions result = mockMvc.perform(post("/event/create-event")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content("{\"name\": \"Evento\", \"date\": \"\", \"time\": \"10:00\", \"location\": \"Ubicacion\"}"));

        // Check that the event was created successfully
        result.andExpect(status().is(400));
    }

    //Evento con nombre no ASCII
    @Test
    public void testCreateEventWithNoASCIIName() throws Exception {
        // Create an event with ASCII characters
        ResultActions result = mockMvc.perform(post("/event/create-event")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content("{\"name\": \"España\", \"date\": \"2025-12-31\", \"time\": \"10:00\", \"location\": \"Ubicacion\"}"));

        // Check that the event was created successfully
        result.andExpect(status().is(400));
    }

    //Evento con ubicacion no ASCII
    @Test
    public void testCreateEventWithNoASCIILocation() throws Exception {
        // Create an event with ASCII characters
        ResultActions result = mockMvc.perform(post("/event/create-event")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content("{\"name\": \"Evento\", \"date\": \"2025-12-31\", \"time\": \"10:00\", \"location\": \"España\"}"));

        // Check that the event was created successfully
        result.andExpect(status().is(400));
    }

}