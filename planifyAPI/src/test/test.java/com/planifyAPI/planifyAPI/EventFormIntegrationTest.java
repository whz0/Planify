package com.planifyAPI.planifyAPI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.ComboBoxMatchers;
import org.testfx.matcher.control.LabeledMatchers;

import static org.testfx.api.FxAssert.verifyThat;



public class EventFormIntegrationTest extends ApplicationTest {

    private TextField nombreField;
    private DatePicker fechaPicker;
    private ComboBox<LocalTime> horaComboBox;
    private TextField ubicacionField;

    @Override
    public void start(Stage stage) throws Exception {
        // Cargar y mostrar la escena de tu aplicación
        // Por ejemplo:
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/eventForm.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        // Inicializar los campos de tu controlador
        nombreField = lookup("#nombreField").query();
        fechaPicker = lookup("#fechaPicker").query();
        horaComboBox = lookup("#horaComboBox").query();
        ubicacionField = lookup("#ubicacionField").query();
    }

    @Test
    public void testFullEventCreationFlow(FxRobot robot) {
        // Simular clic en el botón de crear evento
        robot.clickOn("#createEventButton");

        // Rellenar los campos del formulario
        robot.clickOn("#nombreField").write("Evento de prueba");
        robot.clickOn("#fechaPicker").write("2023-12-31");
        robot.clickOn("#horaComboBox").clickOn("10:00");
        robot.clickOn("#ubicacionField").write("Ubicación de prueba");

        // Simular clic en el botón de OK
        robot.clickOn("#okButton");


        // Verificar que se muestra un mensaje de éxito
        verifyThat(".alert", LabeledMatchers.hasText("El evento se ha creado correctamente."));
    }
}


