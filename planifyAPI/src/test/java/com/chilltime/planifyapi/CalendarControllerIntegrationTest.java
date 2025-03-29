package com.chilltime.planifyapi;

import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.Client;
import com.chilltime.planifyapi.repository.CalendarRepository;
import com.chilltime.planifyapi.repository.ClientRepository;
import com.chilltime.planifyapi.service.SACalendar;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest(classes = PlanifyApiApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CalendarControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("planifydb")
            .withUsername("user")
            .withPassword("password")
            .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));;

    @Autowired
    private SACalendar calendarService;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeAll
    public static void setUp() {
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
        postgreSQLContainer.start();
    }

    @AfterAll
    public static void tearDown() {
        postgreSQLContainer.stop();
    }

    @BeforeEach
    void setUpDatas(){
        Client client = new Client();
        client.setRole("admin");
        client.setPassword("password");
        client.setUsername("Pepe");
        client.setCalendars(new ArrayList<Calendar>());
        clientRepository.save(client);
    }

    @AfterEach
    void tearDownDatas(){
        calendarRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    void connectionEstablished(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }


    @Test
    public void testCreateCalendar() throws Exception {

        Calendar calendar = new Calendar();
        calendar.setName("CalendarioValido");
        calendar.setDescription("Bueno");
        calendar.setActive(false);
        calendar.setType("privado");
        calendar.setId_client(1L);

        // Save the calendar using the service
        TContext context = calendarService.createPrivateCalendar(calendar);

        Assertions.assertThat(context.getStatus_code()).isEqualTo(200);
        Assertions.assertThat(context.getData()).isNotNull();
    }


    //Calendario con datos vacios
    @Test
    public void testCreatecalendarWithEmptyFields() throws Exception {
        // Create a calendar with ASCII characters
        Calendar calendar = new Calendar();
        calendar.setDescription("Bueno");
        calendar.setActive(false);
        calendar.setType("privado");
        calendar.setId_client(1L);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->{
            calendarService.createPrivateCalendar(calendar);
        }).withMessage("El nombre es obligatorio y no puede estar compuesto por espacios en blanco.");
    }


    //Calendario con nombre no ASCII
    @Test
    public void testCreatecalendarWithNoASCIIName() throws Exception {
        // Create a calendar with ASCII characters in name
        Calendar calendar = new Calendar();
        calendar.setName("Ñalendario");
        calendar.setDescription("Bueno");
        calendar.setActive(false);
        calendar.setType("privado");
        calendar.setId_client(1L);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->{
            calendarService.createPrivateCalendar(calendar);
        }).withMessage("El nombre debe contener solo caracteres alfanuméricos y espacios.");
    }


    //Calendario con nombre con solo " "
    @Test
    public void testCreatecalendarWithOnlyBlankField() throws Exception {
        // Create a calendar with " " as name
        Calendar calendar = new Calendar();
        calendar.setName(" ");
        calendar.setDescription("Bueno");
        calendar.setActive(false);
        calendar.setType("privado");
        calendar.setId_client(1L);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->{
            calendarService.createPrivateCalendar(calendar);
        }).withMessage("El nombre es obligatorio y no puede estar compuesto por espacios en blanco.");
    }


    //Calendario con descripcion con mas de 255 caracteres
    @Test
    public void testCreatecalendarWithTooLongDescription() throws Exception {
        // Create a calendar with a description that is longer than 255 characters
        Calendar calendar = new Calendar();
        calendar.setName("CalendarioValido");
        calendar.setDescription("1TG6HBbu0v9a6v29dz9FdC3xeb" +
                                "y454pC4B4GRquygdSvTATUzw2S" +
                                "hfhgZYg55PuZ9AwT95t4YFRZwS" +
                                "5gvh4qXqXMnuiJA05Uccv3QJnS" +
                                "pVcbZqqZhaDUv1Dqv0R91mvgYV" +
                                "4LBZ3FyW3q0NXLqQevJA7FRpnQ" +
                                "c02zh3eH5enk9bmZD8M0YmpGQX" +
                                "SP59JYgun7rC5LuycKDK9aJt8N" +
                                "a2jwWiAbuJb00ZPu0vRtLpj7Kn" +
                                "2KDGKcJMq2455Fp5HCYRkvLwkD");
        calendar.setActive(false);
        calendar.setType("privado");
        calendar.setId_client(1L);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->{
            calendarService.createPrivateCalendar(calendar);
        }).withMessage("La descripción debe tener un máximo de 255 caracteres.");
    }

    //Calendario con nombre con mas de 20 caracteres
    @Test
    public void testCreatecalendarWithTooLongName() throws Exception {
        // Create a calendar with a name that is longer than 20 characters
        Calendar calendar = new Calendar();
        calendar.setName("CalendarioQueNoEsValidoParaNadaPorSerDemasiadoLargo");
        calendar.setDescription("Bueno");
        calendar.setActive(false);
        calendar.setType("privado");
        calendar.setId_client(1L);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->{
            calendarService.createPrivateCalendar(calendar);
        }).withMessage("El campo nombre no puede tener más de 20 caracteres");
    }


    //Calendario con nombre ya existente
    @Test
    public void testCreatecalendarWithRepeatedName() throws Exception {
        // Create a calendar with a name repeated
        Calendar calendar = new Calendar();
        calendar.setName("CalendarioRepetido");
        calendar.setDescription("Bueno");
        calendar.setActive(false);
        calendar.setType("privado");
        calendar.setId_client(1L);

        calendarService.createPrivateCalendar(calendar);

        Calendar calendar2 = new Calendar();
        calendar2.setName("CalendarioRepetido");
        calendar2.setDescription("Mejor");
        calendar2.setActive(false);
        calendar2.setType("privado");
        calendar2.setId_client(1L);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->{
            calendarService.createPrivateCalendar(calendar2);
        }).withMessage("El nombre ya está en uso por otro calendario privado suyo. Por favor, elija otro nombre.");
    }
}

