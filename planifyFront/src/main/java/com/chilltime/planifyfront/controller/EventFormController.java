package com.chilltime.planifyfront.controller;

import com.chilltime.planifyfront.model.service.ServiceFactory;
import com.chilltime.planifyfront.model.transfer.TEvent;
import com.chilltime.planifyfront.utils.LocalDateAdapter;
import com.chilltime.planifyfront.utils.LocalTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.calendarfx.model.Entry;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.chilltime.planifyfront.utils.DialogWindows.showErrorDialog;
import static com.chilltime.planifyfront.utils.DialogWindows.showSuccessDialog;

public class EventFormController {

    private Entry<TEvent> entry; // Variable para almacenar el Entry provisional

    @FXML
    private TextField nombreField;

    @FXML
    private DatePicker fechaPicker;

    @FXML
    private ComboBox<LocalTime> horaComboBox;

    @FXML
    private TextField ubicacionField;

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .create();

    @FXML
    private void initialize() {
        // Rellenar el ComboBox con horas
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                horaComboBox.getItems().add(LocalTime.of(hour, minute));
            }
        }

        // Add window close handler
        Platform.runLater(() -> {
            Stage stage = (Stage) nombreField.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                event.consume(); // Prevent default close
                handleCancel();
            });
        });
    }

    /**
     * Setter para la fecha predeterminada del DatePicker.
     */
    public void setDefaultDate(LocalDate date) {
        fechaPicker.setValue(date);
    }

    /**
     * Setter para recibir el Entry provisional.
     */
    public void setEntry(Entry<TEvent> entry) {
        this.entry = entry;
    }

    /**
     * Si se cancela la operación, se elimina el Entry provisional.
     */
    @FXML
    private void handleCancel() {
        // Eliminar el Entry provisional
        try {
            if(entry!= null){
            DashboardControllerSingleton.getInstance().removeEntry(entry);
            entry.setCalendar(null);
            }
            closeWindow();
        }catch (Exception e){
        }
    }



    /**
     * Si se crea el evento correctamente, se actualiza el Entry y se deja en el calendario.
     */
    @FXML
    private void handleCreate() {
        try {
            DashboardController dashboardCtrl = DashboardControllerSingleton.getInstance();
            // Realizar validaciones
            if (nombreField.getText().isEmpty()) {
                showErrorDialog("Error de validación", "El campo 'Nombre del Evento' no puede estar vacío.");
                return;
            }
            if (fechaPicker.getValue() == null) {
                showErrorDialog("Error de validación", "Debe seleccionar una fecha para el evento.");
                return;
            }
            if (horaComboBox.getValue() == null) {
                showErrorDialog("Error de validación", "Debe seleccionar una hora para el evento.");
                return;
            }
            if (ubicacionField.getText().isEmpty()) {
                showErrorDialog("Error de validación", "El campo 'Ubicación del Evento' no puede estar vacío.");
                return;
            }

            // Otras validaciones de formato y longitud...

            LocalDate fecha = fechaPicker.getValue();
            LocalTime hora = horaComboBox.getValue();
            String nombre = nombreField.getText();
            String ubicacion = ubicacionField.getText();
            TEvent event = new TEvent(null, nombre, fecha, hora, ubicacion, true);

            // Llamar a la API para crear el evento
            Task<String> apiTask = ServiceFactory.getInstance().createEventSA().createEvent(event);
            apiTask.setOnSucceeded(e -> {
                TEvent eventReturned = gson.fromJson(apiTask.getValue(), TEvent.class);
                // Asumir que la API devuelve el ID del evento
                event.setId(eventReturned.getId());
                // Actualizar el Entry provisional con los datos finales
                entry.setTitle(event.getName());
                entry.setUserObject(event);
                // Mostrar mensaje de éxito
                showSuccessDialog("Evento creado", "El evento se ha creado correctamente.");
                closeWindow();
            });

            apiTask.setOnFailed(e -> {
                showErrorDialog("Error de conexión", "No se pudo conectar a la API.");
            });

            new Thread(apiTask).start();
        } catch (Exception e) {
            showErrorDialog("Error inesperado", "Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}

