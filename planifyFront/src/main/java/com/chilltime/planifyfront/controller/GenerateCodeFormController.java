package com.chilltime.planifyfront.controller;

import com.chilltime.planifyfront.model.service.CalendarSA;
import com.chilltime.planifyfront.model.service.ServiceFactory;
import com.chilltime.planifyfront.model.transfer.TCalendar;
import com.chilltime.planifyfront.model.transfer.TContext;
import com.chilltime.planifyfront.utils.SessionManager;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static com.chilltime.planifyfront.utils.DialogWindows.showErrorDialog;
import static com.chilltime.planifyfront.utils.DialogWindows.showSuccessDialog;

public class GenerateCodeFormController {

    @FXML
    private TextField codeTextField;

    @FXML
    private ComboBox<TCalendar> calendarComboBox;

    private ObservableList<TCalendar> calendars = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        CalendarSA calendarSA = ServiceFactory.getInstance().createCalendarSA();

        // Configurar ComboBox
        calendarComboBox.setItems(calendars);

        // Configurar la visualización de los elementos del ComboBox
        calendarComboBox.setCellFactory(param -> new javafx.scene.control.ListCell<TCalendar>() {
            @Override
            protected void updateItem(TCalendar item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        // Configurar el texto mostrado en el ComboBox cuando se selecciona un elemento
        calendarComboBox.setButtonCell(new javafx.scene.control.ListCell<TCalendar>() {
            @Override
            protected void updateItem(TCalendar item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        // Cargar los calendarios del usuario
        loadUserCalendars();
    }

    private void loadUserCalendars() {
        Long userId = SessionManager.getInstance().getCurrentUserId();
        Task<String> task = ServiceFactory.getInstance().createCalendarSA().getCalendarsWithEventsByUserId(userId);

        task.setOnSucceeded(event -> {
            String response = task.getValue();
            try {
                // Parsear la respuesta JSON para obtener los calendarios
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
                TContext context = mapper.readValue(response, TContext.class);

                if (context.getStatus_code() == 200) {
                    List<TCalendar> calendarList = mapper.convertValue(context.getData(),
                            mapper.getTypeFactory().constructCollectionType(List.class, TCalendar.class));

                    Platform.runLater(() -> {
                        calendars.clear();
                        calendars.addAll(calendarList);
                        if (!calendars.isEmpty()) {
                            calendarComboBox.getSelectionModel().selectFirst();
                        }
                    });
                } else {
                    showErrorDialog("Error", context.getMessage());
                }
            } catch (IOException e) {
                showErrorDialog("Error", "No se pudieron cargar los calendarios: " + e.getMessage());
                e.printStackTrace();
            }
        });

        task.setOnFailed(event -> {
            showErrorDialog("Error", "Falló la carga de calendarios: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        // Ejecutar la tarea en un hilo separado
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void generateNewCode() {
        TCalendar selectedCalendar = calendarComboBox.getSelectionModel().getSelectedItem();

        if (selectedCalendar == null) {
            showErrorDialog("Error", "Por favor, seleccione un calendario.");
            return;
        }

        Task<String> task = ServiceFactory.getInstance().createCalendarCodeSA().generateCalendarCode(selectedCalendar.getId());

        task.setOnSucceeded(event -> {
            String response = task.getValue();
            try {
                // Parsear la respuesta JSON para obtener el código
                ObjectMapper mapper = new ObjectMapper();
                TContext context = mapper.readValue(response, TContext.class);

                if (context.getStatus_code() == 200) {
                    String code = context.getData().toString();

                    // Limpiar cualquier formato JSON innecesario (si existe)
                    code = code.replace("\"", "").trim();

                    String finalCode = code;
                    Platform.runLater(() -> {
                        codeTextField.setText(finalCode);
                        showSuccessDialog("Código Generado",
                                "Se ha generado un nuevo código para el calendario: " + selectedCalendar.getName());
                    });
                } else {
                    showErrorDialog("Error", context.getMessage());
                }
            } catch (IOException e) {
                showErrorDialog("Error", "No se pudo procesar la respuesta: " + e.getMessage());
                e.printStackTrace();
            }
        });

        task.setOnFailed(event -> {
            showErrorDialog("Error", "Falló la generación del código: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        // Ejecutar la tarea en un hilo separado
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    void handleGenerate() {
        generateNewCode();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) codeTextField.getScene().getWindow();
        stage.close();
    }
}