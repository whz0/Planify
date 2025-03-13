package com.planify.planifyfront;

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
import org.testfx.api.FxAssert;
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
    public void testFullEventCreationFlow() {
        // Simular clic en el botón de crear evento
        clickOn("#createEventButton");

        // Rellenar los campos del formulario
        clickOn("#nombreField").write("Evento de prueba");
        clickOn("#fechaPicker").write("2023-12-31");
        clickOn("#horaComboBox").clickOn("10:00");
        clickOn("#ubicacionField").write("Ubicación de prueba");

        // Simular clic en el botón de OK
        clickOn("#okButton");


        // Verificar que se muestra un mensaje de éxito
        FxAssert.verifyThat(".alert", LabeledMatchers.hasText("El evento se ha creado correctamente."));
    }
}


