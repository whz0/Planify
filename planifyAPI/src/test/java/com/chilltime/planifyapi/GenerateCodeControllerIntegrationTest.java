package com.chilltime.planifyapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@TestPropertySource(properties = {
        "logging.level.org.springframework=DEBUG",
        "logging.level.com.chilltime=DEBUG"
})
public class GenerateCodeControllerIntegrationTest {

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

    @Test
    @WithMockUser(username = "testuser", roles = {"PLANNER"})
    public void testCreateCode() throws Exception {
        //TODO hacer este test del diablo
        /*
        // 1. Register a planner
        MvcResult plannerResult = mockMvc.perform(post("/planner/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "testplanner",
                            "password": "password123",
                            "role": "ROLE_PLANNER"
                        }
                        """))
                .andExpect(status().isOk())
                .andReturn();

        // 2. Create a private calendar
        MvcResult calendarResult = mockMvc.perform(post("/calendar/create-private")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                        {
                            "name": "Test Calendar",
                            "description": "Test Description", 
                            "type": "private",
                            "active": true,
                            "id_planner": %d
                        }
                        """, 1)))
                .andExpect(status().isOk())
                .andReturn();

        // 3. Create a code for the calendar
        MvcResult codeResult = mockMvc.perform(post("/codigo/create-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                        {
                            "calendar": {
                                "id": %d,
                                "planner": {
                                    "id": %d
                                }
                            }
                        }
                        """, 1, 1)))
                .andExpect(status().isOk())
                .andReturn();

        // Verify the code was created and has correct format
        String codeResponseJson = codeResult.getResponse().getContentAsString();
        String code = extractCodeFromResponse(codeResponseJson);
        assertNotNull(code, "Code should not be null");
        assertEquals(6, code.length(), "Code should be 6 characters long");*/
    }

    /**
     * Extract ID from response JSON
     * @param jsonResponse The JSON response string
     * @return The extracted ID value
     */
    private Long extractIdFromResponse(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultNode = rootNode.path("result");
            if (!resultNode.isMissingNode() && resultNode.has("id")) {
                return resultNode.get("id").asLong();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Extract code from response JSON
     * @param jsonResponse The JSON response string
     * @return The extracted code value
     */
    private String extractCodeFromResponse(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultNode = rootNode.path("result");
            if (!resultNode.isMissingNode() && resultNode.has("code")) {
                return resultNode.get("code").asText();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}