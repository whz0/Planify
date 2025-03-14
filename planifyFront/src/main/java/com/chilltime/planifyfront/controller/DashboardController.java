// File: planifyFront/src/main/java/com/planify/planifyfront/controller/DashboardController.java
package com.chilltime.planifyfront.controller;

import com.chilltime.planifyfront.view.AnchorPaneNode;
import com.chilltime.planifyfront.view.FullCalendarView;
import com.chilltime.planifyfront.model.transfer.TEvento;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class DashboardController {
    @FXML
    private AnchorPane calendarPane;

    @FXML
    private ListView<TEvento> eventsListView;

    private FullCalendarView fullCalendarView;

    @FXML
    public void initialize() {
        fullCalendarView = new FullCalendarView(YearMonth.now());

        VBox paddedCalendarContainer = new VBox(fullCalendarView.getView());
        paddedCalendarContainer.setPadding(new Insets(20));
        AnchorPane.setTopAnchor(paddedCalendarContainer, 0.0);
        AnchorPane.setRightAnchor(paddedCalendarContainer, 0.0);
        AnchorPane.setBottomAnchor(paddedCalendarContainer, 0.0);
        AnchorPane.setLeftAnchor(paddedCalendarContainer, 0.0);
        calendarPane.getChildren().add(paddedCalendarContainer);

        eventsListView.setCellFactory(param -> new ListCell<TEvento>() {
            @Override
            protected void updateItem(TEvento evento, boolean empty) {
                super.updateItem(evento, empty);
                if (empty || evento == null) {
                    setText("Empty");
                } else {
                    setText(String.format("%s - %s - %s",
                            evento.getNombre(),
                            evento.getHora().toString(),
                            evento.getUbicacion()));
                }
            }
        });

        // Añadir manejador de eventos para las celdas del calendario
        for (AnchorPaneNode cell : fullCalendarView.getAllCalendarDays()) {
            cell.setOnMouseClicked(e -> {
                LocalDate date = cell.getDate();
                if (date != null) {
                    List<TEvento> eventos = fullCalendarView.getEventsForDate(date);
                    eventsListView.getItems().clear();
                    eventsListView.getItems().addAll(eventos);
                    updateEventsPanel(date);
                }
            });
        }
    }

    @FXML
    public void crearEvento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/eventForm.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Crear Evento");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateEventsPanel(LocalDate date) {
        eventsListView.getItems().clear();
        List<TEvento> eventos = fullCalendarView.getEventsForDate(date);
        if (eventos != null) {
            eventsListView.getItems().addAll(eventos);
        }
    }

    public void addEvent(TEvento event) {
        if (event != null) {
            fullCalendarView.addEvent(event);
            if (event.getFecha().equals(LocalDate.now())) {
                eventsListView.getItems().add(event);
            }
            eventsListView.refresh();
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}