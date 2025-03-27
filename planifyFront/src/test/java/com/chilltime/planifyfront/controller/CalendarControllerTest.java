package com.chilltime.planifyfront.controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.chilltime.planifyfront.model.transfer.TCalendario;
import com.chilltime.planifyfront.test.BaseJavaFxTest;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class CalendarControllerTest extends BaseJavaFxTest {

    private CalendarFormController controller;
    private CalendarSource calendarSource;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        controller = new CalendarFormController();
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();
        primaryStage = stage;

        // Crear e inyectar los TextField para name y description
        TextField nameField = new TextField();
        TextField descriptionField = new TextField();
        setPrivateField("nameField", nameField);
        setPrivateField("descriptionField", descriptionField);

        // Añadir los TextField al contenedor para asignarles escena
        root.getChildren().addAll(nameField, descriptionField);

        // Configurar el CalendarSourceHolder con un CalendarSource de prueba
        calendarSource = new CalendarSource("Test Source");
        controller.setCalendarSourceHolder(new CalendarFormController.CalendarSourceHolder() {
            @Override
            public CalendarSource getCalendarSource() {
                return calendarSource;
            }
        });
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = CalendarFormController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private Object getPrivateField(String fieldName) throws Exception {
        Field field = CalendarFormController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(controller);
    }

    @Test
    public void testCreateCalendar() throws Exception {
        // Asignar valores a los campos de texto
        TextField nameField = (TextField) getPrivateField("nameField");
        TextField descriptionField = (TextField) getPrivateField("descriptionField");
        Platform.runLater(() -> {
            nameField.setText("Test Calendar");
            descriptionField.setText("Test Description");
        });
        sleep(100);

        // Invocar handleAccept (se añade el Calendar al CalendarSource y se inicia el Task asíncrono)
        Platform.runLater(() -> controller.handleAccept());
        // Esperar un poco para que se ejecute el código asíncrono (en este caso, la adición es síncrona)
        sleep(500);

        // Verificar que el CalendarSource contiene un Calendar con el nombre esperado
        assertNotNull(calendarSource);
        assertTrue(calendarSource.getCalendars().stream()
                .anyMatch(cal -> cal.getName().equals("Test Calendar")));
    }

    @Test
    public void testCancelCalendarCreation() throws Exception {
        // Verificar que la ventana (Stage) está visible antes de cancelar
        assertTrue(primaryStage.isShowing());

        // Invocar handleCancel, que debe cerrar la ventana
        Platform.runLater(() -> controller.handleCancel());
        sleep(100);

        // Comprobar que el Stage se ha cerrado
        assertFalse(primaryStage.isShowing());
    }
}
