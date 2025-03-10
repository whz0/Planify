package com.planify.planifyfront.controller;

import com.planify.planifyfront.model.service.ApiSA;
import com.planify.planifyfront.model.transfer.TEvento;
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
            showErrorDialog("Validation Error", "The event name field cannot be empty.");
            return;
        }
        if (fechaPicker.getValue() == null) {
            showErrorDialog("Validation Error", "You must select a date for the event.");
            return;
        }
        if (horaComboBox.getValue() == null) {
            showErrorDialog("Validation Error", "You must select a time for the event.");
            return;
        }
        if (ubicacionField.getText().isEmpty()) {
            showErrorDialog("Validation Error", "The event location field cannot be empty.");
            return;
        }

        String nombre = nombreField.getText();
        LocalDate fecha = fechaPicker.getValue();
        LocalTime hora = horaComboBox.getValue();
        String ubicacion = ubicacionField.getText();

        // Validate that the event date is in the future
        if (!fecha.isAfter(LocalDate.now())) {
            showErrorDialog("Validation Error", "The event date must be in the future.");
            return;
        }

        // Create TEvento with temporary id 0 (to be set upon successful API creation)
        TEvento evento = new TEvento(0, fecha, ubicacion, hora, nombre);

        // Call the Spring API using ApiSA (which points to http://localhost:1010/event/create-event)
        Task<String> apiTask = new ApiSA().createEvent(evento);
        apiTask.setOnSucceeded(e -> {
            try {
                int idResponse = Integer.parseInt(apiTask.getValue());
                if (idResponse > 0) {
                    evento.setId(idResponse);
                    // Update the calendar via DashboardController
                    DashboardController dashboardCtrl = DashboardControllerSingleton.getInstance();
                    if (dashboardCtrl != null) {
                        dashboardCtrl.addEvent(evento);
                    }
                    showSuccessDialog("Event created", "The event has been created successfully.");
                    closeWindow();
                } else {
                    showErrorDialog("API Error", "Error creating the event in the API.");
                }
            } catch (NumberFormatException | IOException ex) {
                showErrorDialog("API Response Error", "Invalid response from API: " + ex.getMessage());
            }
        });

        DashboardController dc =null;
        try {
            dc = DashboardControllerSingleton.getInstance();
        }catch (Exception e){
            showErrorDialog("No se pudo obtener controller", "Could not connect to the API.");
        }
        if (dc != null) {
            System.out.println("EFC");
            dc.addEvent(evento);
        }
        apiTask.setOnFailed(e -> showErrorDialog("Connection Error", "Could not connect to the API."));
        new Thread(apiTask).start();
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