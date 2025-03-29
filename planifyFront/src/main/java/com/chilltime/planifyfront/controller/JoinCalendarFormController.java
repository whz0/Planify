package com.chilltime.planifyfront.controller;

import com.calendarfx.model.Calendar;
import com.chilltime.planifyfront.model.service.ServiceFactory;
import com.chilltime.planifyfront.model.transfer.TCalendar;
import com.chilltime.planifyfront.model.transfer.TContext;
import com.chilltime.planifyfront.model.transfer.TEvent;
import com.chilltime.planifyfront.utils.CalendarUtils;
import com.chilltime.planifyfront.utils.LocalDateAdapter;
import com.chilltime.planifyfront.utils.LocalTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

public class JoinCalendarFormController {
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .create();

    @FXML
    private TextField codeField;

    /**
     * Maneja la acción de unirse a un calendario mediante un código.
     */
    @FXML
    void handleAccept() {
        String code = codeField.getText().trim();

        if (code.isEmpty()) {
            showError("Por favor, ingrese un código de calendario");
            return;
        }

        // Crear una tarea para solicitar unirse al calendario por código
        /*Task<String> joinCalendarTask = ServiceFactory.getInstance()
                .createCalendarSA()
                .unirseACalendario(code);

        joinCalendarTask.setOnSucceeded(event -> {
            try {
                // Parsear la respuesta de la API
                TContext contexto = gson.fromJson(joinCalendarTask.getValue(), TContext.class);

                if (contexto.getData() == null) {
                    showError(contexto.getMessage());
                    return;
                }

                // Convertir los datos del calendario
                TCalendar calendarioCompartido = gson.fromJson(
                        gson.toJson(contexto.getData()),
                        TCalendar.class
                );

                // Actualizar el dashboard con el nuevo calendario
                DashboardController dashboardController = DashboardControllerSingleton.getInstance();
                if (dashboardController != null) {
                    dashboardController.addCalendar(calendarioCompartido);
                }

                // Mostrar mensaje de éxito
                showSuccess("Calendario unido", "Te has unido al calendario correctamente.");

                // Cerrar la ventana
                closeWindow();

            } catch (Exception ex) {
                showError("Error al procesar la respuesta: " + ex.getMessage());
            }
        });

        joinCalendarTask.setOnFailed(event -> {
            showError("No se pudo conectar con el servidor");
        });

        // Iniciar la tarea en un nuevo hilo
        new Thread(joinCalendarTask).start();*/
    }

    /**
     * Cancela la operación y cierra la ventana.
     */
    @FXML
    void handleCancel() {
        closeWindow();
    }

    /**
     * Muestra un diálogo de error.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Muestra un diálogo de éxito.
     */
    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Cierra la ventana actual.
     */
    private void closeWindow() {
        Stage stage = (Stage) codeField.getScene().getWindow();
        stage.close();
    }
}
