package com.chilltime.planifyfront.controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.chilltime.planifyfront.model.service.ServiceFactory;
import com.chilltime.planifyfront.model.transfer.TCalendar;
import com.chilltime.planifyfront.model.transfer.TContext;
import com.chilltime.planifyfront.model.transfer.TEvent;
import com.chilltime.planifyfront.utils.CalendarUtils;
import com.chilltime.planifyfront.utils.LocalDateAdapter;
import com.chilltime.planifyfront.utils.LocalTimeAdapter;
import com.google.gson.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Consumer;

import static com.chilltime.planifyfront.utils.DialogWindows.showErrorDialog;
import static com.chilltime.planifyfront.utils.DialogWindows.showSuccessDialog;

public class CalendarFormController {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .create();

    @FXML
    private TextField nameField;

    @FXML
    private TextField descriptionField;


    private Consumer<Calendar<TEvent>> onAccept;
    private CalendarSourceHolder calendarSourceHolder;

    /**
     * Inicializa el ComboBox con los estilos disponibles y asigna una celda personalizada.
     */
    @FXML
    void initialize() {
    }

    /**
     * Se invoca al pulsar el botón "Aceptar". Valida los datos, crea el Calendar y ejecuta el callback.
     */
    @FXML
    void handleAccept() {
        String name = nameField.getText();
        String description = descriptionField.getText();

        if (name == null || name.trim().isEmpty()) {
            showError("El nombre no puede estar vacío");
            return;
        }

        // Crear el Calendar y asignar el estilo
        Calendar<TEvent> newCalendar = new Calendar<>(name);
        // Se añade el Calendar al CalendarSource proporcionado
        if (calendarSourceHolder != null) {
            calendarSourceHolder.getCalendarSource().getCalendars().add(newCalendar);
        }

        // Ejecuta el callback si se ha definido
        if (onAccept != null) {
            onAccept.accept(newCalendar);
        }
        // Llamar a la API para crear nuevo calendario privado
        TCalendar calendar = CalendarUtils.toTCalendar(newCalendar);
        calendar.setId_client(1L);
        calendar.setDescription(description);
        System.out.println(calendar);
        Task<String> apiTask = ServiceFactory.getInstance().createCalendarSA().crearCalendario(calendar);
        apiTask.setOnSucceeded(e->{
            try {
                TContext contexto = gson.fromJson(apiTask.getValue(), TContext.class);
                if (contexto.getData() == null) {
                    showErrorDialog("Error de la API", contexto.getMessage());
                }else{
                    System.out.println(contexto.getData());
                    TCalendar calendarioReturned = gson.fromJson(gson.toJson(contexto.getData()), TCalendar.class);
                    // Asumimos que la API devuelve el ID y lo asignamos
                    calendar.setId(calendarioReturned.getId());

                    DashboardController dashboardController = DashboardControllerSingleton.getInstance();
                    if (dashboardController != null) {
                        dashboardController.addCalendar(calendar);
                    }
                    showSuccessDialog("Calendario creado", "El calendario se ha creado correctamente.");
                }
            } catch (JsonSyntaxException | IOException ex) {
                showErrorDialog("Error", ex.getMessage()); //TODO hacerlo mas concreto
            }
        });
        apiTask.setOnFailed(e -> {
            System.out.println("Mensaje de error: " + apiTask.getValue());
            //showErrorDialog("Error con la API", apiTask);
        });
        new Thread(apiTask).start();

        closeWindow();
    }

    /**
     * Se invoca al pulsar el botón "Cancelar".
     */
    @FXML
    void handleCancel() {
        closeWindow();
    }

    /**
     * Muestra un diálogo de error con el mensaje indicado.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de validación");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Cierra la ventana actual.
     */
    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    /**
     * Celda personalizada para mostrar los colores asociados a cada Calendar.Style.
     */
    private static class StyleCell extends ListCell<Calendar.Style> {
        @Override
        protected void updateItem(Calendar.Style item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) {
                Rectangle icon = new Rectangle(12, 12);
                icon.getStyleClass().add(item.name().toLowerCase() + "-icon");
                setGraphic(icon);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setGraphic(null);
            }
        }
    }

    /**
     * Permite establecer un callback a ejecutar al crear el Calendar.
     *
     * @param onAccept callback que recibe el Calendar creado.
     */
    public void setOnAccept(Consumer<Calendar<TEvent>> onAccept) {
        this.onAccept = onAccept;
    }

    /**
     * Permite asignar el CalendarSourceHolder donde se añadirá el nuevo Calendar.
     *
     * @param calendarSourceHolder el contenedor del CalendarSource.
     */
    public void setCalendarSourceHolder(CalendarSourceHolder calendarSourceHolder) {
        this.calendarSourceHolder = calendarSourceHolder;
    }

    /**
     * Interfaz para encapsular el CalendarSource.
     */
    public interface CalendarSourceHolder {
        CalendarSource getCalendarSource();
    }
}
