package com.chilltime.planifyfront.controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.chilltime.planifyfront.model.transfer.TEvento;
import com.chilltime.planifyfront.test.BaseJavaFxTest;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class DashboardControllerTest extends BaseJavaFxTest {

    private DashboardController controller;
    private ListView<TEvento> listView;
    private Calendar calendar;

    @Override
    public void start(Stage stage) throws Exception {
        controller = new DashboardController();
        AnchorPane root = new AnchorPane();
        listView = new ListView<>();
        root.getChildren().add(listView);

        // Use reflection to set private fields
        setPrivateField("calendarPane", new AnchorPane());
        setPrivateField("eventsListView", listView);

        // Initialize calendar
        calendar = new Calendar("Test Calendar");
        setPrivateField("calendar", calendar);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Platform.runLater(() -> controller.initialize());
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = DashboardController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private Object getPrivateField(String fieldName) throws Exception {
        Field field = DashboardController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(controller);
    }

    @Test
    public void testAddEvent() throws Exception {
        // Arrange
        TEvento evento = new TEvento();
        evento.setNombre("Test Event");
        evento.setFecha(LocalDate.now());
        evento.setHora(LocalTime.now());
        evento.setUbicacion("Test Location");

        // Act
        Platform.runLater(() -> controller.addEvent(evento));

        // Wait for JavaFX thread
        sleep(100);

        // Assert
        ListView<TEvento> eventsListView = (ListView<TEvento>) getPrivateField("eventsListView");
        assertEquals(1, eventsListView.getItems().size());
        assertEquals("Test Event", eventsListView.getItems().get(0).getNombre());
    }

    @Test
    public void testRemoveEntry() throws Exception {
        // Arrange
        Entry<TEvento> entry = new Entry<>("Test Entry");

        // Act
        Platform.runLater(() -> {
            calendar.addEntry(entry);
            controller.removeEntry(entry);
        });

        // Wait for JavaFX thread
        sleep(100);

        // Assert
        assertTrue(calendar.findEntries("Test Entry").isEmpty());
    }

}