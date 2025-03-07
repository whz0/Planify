package com.planify.planifyfront.controller;

import com.planify.planifyfront.view.FullCalendarView;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.time.LocalDate;
import java.time.YearMonth;

public class CalendarController {

    @FXML
    private Pane calendarPane;

    @FXML
    public void initialize() {
        // Crear el calendario para el mes actual o el que desees mostrar
        FullCalendarView calendarView = new FullCalendarView(YearMonth.now());
        // Suponiendo que fullCalendarView es una instancia de FullCalendarView
        LocalDate eventDate = LocalDate.of(2025, 3, 15); // fecha del evento



        // Agregar la vista del calendario al Pane definido en el FXML
        calendarPane.getChildren().add(calendarView.getView());
    }
}
