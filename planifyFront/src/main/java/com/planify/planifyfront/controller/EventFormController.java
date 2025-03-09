package com.planify.planifyfront.controller;

import javafx.fxml.FXML;
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
        // Logic to create the event
        String nombre = nombreField.getText();
        LocalDate fecha = fechaPicker.getValue();
        LocalTime hora = horaComboBox.getValue();
        String ubicacion = ubicacionField.getText();

        // Add logic to save the event

        // Close the form window
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}