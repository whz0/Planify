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

    // Método para manejar el botón "Inicio"
    @FXML
    public void handleInicio() {
        mostrarAlerta("Inicio", "Navegando a la sección de Inicio.");
        // Aquí puedes añadir lógica para cargar contenido relacionado con "Inicio".
    }

    // Método para manejar el botón "Calendario"
    @FXML
    public void handleCalendario() {
        mostrarAlerta("Calendario", "Ya estás en el calendario.");
        // Aquí puedes añadir lógica para cargar contenido relacionado con "Calendario".
    }

    // Método para manejar el botón "Tareas"
    @FXML
    public void handleTareas() {
        mostrarAlerta("Tareas", "Navegando a la sección de Tareas.");
        // Aquí puedes añadir lógica para cargar contenido relacionado con "Tareas".
    }

    // Método para manejar el botón "Configuración"
    @FXML
    public void handleConfiguracion() {
        mostrarAlerta("Configuración", "Navegando a la Configuración.");
        // Aquí puedes añadir lógica para cargar contenido relacionado con "Configuración".
    }

    // Método para manejar el botón "Cerrar Sesión"
    @FXML
    public void handleCerrarSesion() {
        mostrarAlerta("Cerrar Sesión", "Has cerrado sesión.");
        // Aquí puedes agregar lógica para cerrar la sesión del usuario.
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