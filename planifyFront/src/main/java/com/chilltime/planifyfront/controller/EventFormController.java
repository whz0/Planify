package com.chilltime.planifyfront.controller;

import com.chilltime.planifyfront.model.service.ApiClient;
import com.chilltime.planifyfront.model.service.EventoSA;
import com.chilltime.planifyfront.model.service.ServiceFactory;
import com.chilltime.planifyfront.model.transfer.TEvento;
import com.chilltime.planifyfront.utils.LocalDateAdapter;
import com.chilltime.planifyfront.utils.LocalTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    /**
     * Metodo para establecer la fecha predeterminada del DatePicker.
     *
     * @param date La fecha que se establecerá en el DatePicker.
     */
    public void setDefaultDate(LocalDate date) {
        fechaPicker.setValue(date);
    }

    @FXML
    private void handleCancel() {
        // Cerrar la ventana del formulario
        closeWindow();
    }

    @FXML
    private void handleCreate() {
        // Validaciones de campos obligatorios y formato ASCII
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
        for (char c : ubicacionField.getText().toCharArray()) {
            if (c > 127) {
                showErrorDialog("Error de validación", "El campo 'Ubicación del Evento' tiene que ser ASCII.");
                return;
            }
        }
        String nombre = nombreField.getText();
        if (nombre.length() > 20) {
            showErrorDialog("Error de validación", "El campo 'Nombre del Evento' no puede tener más de 20 caracteres.");
            return;
        }
        for (char c : nombre.toCharArray()) {
            if (c > 127) {
                showErrorDialog("Error de validación", "El campo 'Nombre del Evento' tiene que ser ASCII.");
                return;
            }
        }

        // Validar que la fecha y hora sean futuras
        LocalDate fecha = fechaPicker.getValue();
        LocalTime hora = horaComboBox.getValue();
        if (fecha.isBefore(LocalDate.now()) || (fecha.isEqual(LocalDate.now()) && hora.isBefore(LocalTime.now()))) {
            showErrorDialog("Error de validación", "La fecha del evento debe ser futura.");
            return;
        }

        String ubicacion = ubicacionField.getText();
        TEvento evento = new TEvento(null, nombre, fecha, hora, ubicacion);

        // Llamar a la API para crear el evento
        Task<String> apiTask = ServiceFactory.getInstance().crearEventoSA().createEvent(evento);
        apiTask.setOnSucceeded(e -> {
            try {
                TEvento eventoReturned = gson.fromJson(apiTask.getValue(), TEvento.class);
                // Asumimos que la API devuelve el ID y lo asignamos
                evento.setId(eventoReturned.getId());
                // Agregar el evento al calendario a través del DashboardController
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
        apiTask.setOnFailed(e -> showErrorDialog("Error de conexión", "No se pudo conectar a la API."));
        new Thread(apiTask).start();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}
