package com.planify.planifyfront.controller;

import com.planify.planifyfront.model.service.ApiSA;
import com.planify.planifyfront.model.transfer.TEvento;
import com.planify.planifyfront.utils.LocalDateAdapter;
import com.planify.planifyfront.utils.LocalTimeAdapter;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventFormController {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .create();

    @FXML
    private TextField nombreField;

    @FXML
    private DatePicker fechaPicker;

    @FXML
    private ComboBox<LocalTime> horaComboBox;

    @FXML
    private TextField ubicacionField;

    @FXML
    private void initialize() {
        // Populate the ComboBox with times
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                horaComboBox.getItems().add(LocalTime.of(hour, minute));
            }
        }
    }

    @FXML
    private void handleCancel() {
        // Close the form window
        closeWindow();
    }

    @FXML
    private void handleCreate() {
        // Validar campos obligatorios
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

        // Validar longitud del nombre
        String nombre = nombreField.getText();
        if (nombre.length() > 20) {
            showErrorDialog("Error de validación", "El campo 'Nombre del Evento' no puede tener más de 20 caracteres. ");
            return;
        }

        // Validar fecha futura
        LocalDate fecha = fechaPicker.getValue();
        if (!fecha.isAfter(LocalDate.now())) {
            showErrorDialog("Error de validación", "La fecha del evento debe ser futura.");
            return;
        }

        LocalTime hora = horaComboBox.getValue();
        String ubicacion = ubicacionField.getText();

        TEvento evento = new TEvento(0, nombre, fecha, hora, ubicacion);

        // Llamar a la API para crear el evento
        Task<String> apiTask = new ApiSA().createEvent(evento);
        apiTask.setOnSucceeded(e -> {
            try {
                TEvento eventoReturned = gson.fromJson(apiTask.getValue(), TEvento.class);

                evento.setId(eventoReturned.getId());
                // Actualizar el calendario a través de DashboardController
                DashboardController dashboardCtrl = DashboardControllerSingleton.getInstance();
                if (dashboardCtrl != null) {
                    dashboardCtrl.addEvent(evento);
                }
                showSuccessDialog("Evento creado", "El evento se ha creado correctamente.");
                closeWindow();
            } catch (JsonSyntaxException | IOException ex) {
                showErrorDialog("Error de respuesta de la API", "Respuesta inválida de la API: " + ex.getMessage());
            }
        });

        // Manejar errores de conexión con la API
        apiTask.setOnFailed(e -> showErrorDialog("Error de conexión", "No se pudo conectar a la API."));

        // Ejecutar la tarea en un hilo separado
        new Thread(apiTask).start();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null); // Sin texto de cabecera
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // Sin texto de cabecera
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        // Cerrar la ventana del formulario
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}