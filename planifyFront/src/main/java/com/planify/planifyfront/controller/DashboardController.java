package com.planify.planifyfront.controller;

import com.planify.planifyfront.view.FullCalendarView;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import java.time.YearMonth;

public class DashboardController {

    @FXML
    private AnchorPane calendarPane;

    @FXML
    public void initialize() {
        // Crear el calendario con el mes actual
        FullCalendarView fullCalendarView = new FullCalendarView(YearMonth.now());

        // Añadir la vista del calendario al Pane
        AnchorPane.setTopAnchor(fullCalendarView.getView(), 0.0);
        AnchorPane.setRightAnchor(fullCalendarView.getView(), 0.0);
        AnchorPane.setBottomAnchor(fullCalendarView.getView(), 0.0);
        AnchorPane.setLeftAnchor(fullCalendarView.getView(), 0.0);

        calendarPane.getChildren().add(fullCalendarView.getView());
    }

    // Método auxiliar para mostrar alertas de información
    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}