package com.chilltime.planifyapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@TestPropertySource(properties = {
        "logging.level.org.springframework=DEBUG",
        "logging.level.com.chilltime=DEBUG"
})
public class EventControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("planify")
            .withUsername("user")
            .withPassword("password")
            .withReuse(true)  // Reuse container between test runs
            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 2));

    @Autowired
    private MockMvc mockMvc;

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
        // Add any other required properties
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");

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

    //Evento con date pasada
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
