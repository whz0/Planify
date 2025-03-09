package com.planify.planifyfront.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventFormController {

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
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCreate() {

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

        String nombre = nombreField.getText();
        LocalDate fecha = fechaPicker.getValue();
        LocalTime hora = horaComboBox.getValue();
        String ubicacion = ubicacionField.getText();

        try {

            saveEvent(nombre, fecha, hora, ubicacion);

            // Show success message (optional)
            showSuccessDialog("Evento creado", "El evento se ha creado correctamente.");

            // Close the form window
            closeWindow();
        } catch (Exception e) {
            // Handle any errors that occur during event creation
            showErrorDialog("Error al crear el evento", "Ocurrió un error al crear el evento: " + e.getMessage());
        }
    }

    private void saveEvent(String nombre, LocalDate fecha, LocalTime hora, String ubicacion) {

        System.out.println("Evento guardado: " + nombre + ", " + fecha + ", " + hora + ", " + ubicacion);
    }


    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        // Close the form window
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }

}