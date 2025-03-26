package com.chilltime.planifyfront.controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.chilltime.planifyfront.test.BaseJavaFxTest;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class CalendarControllerTest extends BaseJavaFxTest {

    private CalendarFormController controller;
    private TextField nameField;
    private TextField descriptionField;
    private ComboBox<Calendar.Style> styleComboBox;

    @Override
    public void start(Stage stage) throws Exception {
        controller = new CalendarFormController();

        // Create fields
        nameField = new TextField();
        descriptionField = new TextField();
        styleComboBox = new ComboBox<>();

        // Set private fields using reflection
        setPrivateField("nameField", nameField);
        setPrivateField("descriptionField", descriptionField);
        setPrivateField("styleComboBox", styleComboBox);

        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
    public void testHandleAcceptWithValidData() throws Exception {
        // Arrange
        AtomicReference<Calendar> createdCalendar = new AtomicReference<>();
        controller.setOnAccept(calendar -> createdCalendar.set(calendar));

        CalendarSource source = new CalendarSource("Test Source");
        controller.setCalendarSourceHolder(() -> source);

        Platform.runLater(() -> {
            nameField.setText("Test Calendar");
            descriptionField.setText("Test Description");
            styleComboBox.setValue(Calendar.Style.STYLE1);
        });
        sleep(100);

        // Act
        Platform.runLater(() -> controller.handleAccept());
        sleep(100);

        // Assert
        Calendar calendar = createdCalendar.get();
        assertNotNull(calendar);
        assertEquals("Test Calendar", calendar.getName());
        assertEquals(Calendar.Style.STYLE1.toString().toLowerCase(), calendar.getStyle().toString().toLowerCase());
        assertTrue(source.getCalendars().contains(calendar));
    }

    @Test
    public void testHandleAcceptWithInvalidData() throws Exception {
        // Arrange
        AtomicReference<Calendar> createdCalendar = new AtomicReference<>();
        controller.setOnAccept(calendar -> createdCalendar.set(calendar));

        Platform.runLater(() -> {
            nameField.setText("");
            styleComboBox.setValue(null);
        });
        sleep(100);

        // Act
        Platform.runLater(() -> controller.handleAccept());
        sleep(100);

        // Assert
        assertNull(createdCalendar.get());
    }

    @Test
    public void testStyleComboBoxInitialization() throws Exception {
        // Act
        Platform.runLater(() -> controller.initialize());
        sleep(100);

        // Assert
        ComboBox<Calendar.Style> styleBox = (ComboBox<Calendar.Style>) getPrivateField("styleComboBox");
        assertNotNull(styleBox.getItems());
        assertEquals(Calendar.Style.values().length, styleBox.getItems().size());
    }

    @Test
    public void testHandleCancel() {
        // Arrange
        AtomicReference<Boolean> windowClosed = new AtomicReference<>(false);
        Platform.runLater(() -> {
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setOnHidden(event -> windowClosed.set(true));
            controller.handleCancel();
        });
        sleep(100);

        // Assert
        assertTrue(windowClosed.get());
    }
}