package com.chilltime.planifyfront.controller;

import com.chilltime.planifyfront.model.service.ServiceFactory;
import com.chilltime.planifyfront.model.transfer.TCalendar;
import com.chilltime.planifyfront.model.transfer.TContext;
import com.chilltime.planifyfront.model.transfer.TEvent;
import com.chilltime.planifyfront.utils.LocalDateAdapter;
import com.chilltime.planifyfront.utils.LocalTimeAdapter;
import com.chilltime.planifyfront.utils.SessionManager;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.calendarfx.model.Entry;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.chilltime.planifyfront.utils.DialogWindows.showErrorDialog;
import static com.chilltime.planifyfront.utils.DialogWindows.showSuccessDialog;

public class EventFormController {

    private Entry<TEvent> entry; // Variable para almacenar el Entry provisional

    @FXML
    private TextField nombreField;

    @FXML
    private DatePicker fechaPicker;

    @FXML
    private ComboBox<LocalTime> horaComboBox;

    @FXML
    private TextField ubicacionField;

    @FXML
    private ComboBox<TCalendar> calendarComboBox;

    private ObservableList<TCalendar> calendars = FXCollections.observableArrayList();

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .create();

    @FXML
    private void initialize() {

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
        // Configurar ComboBox
        calendarComboBox.setItems(calendars);

        // Rellenar el ComboBox con horas
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                horaComboBox.getItems().add(LocalTime.of(hour, minute));
            }
        }

        // Add window close handler
        Platform.runLater(() -> {
            Stage stage = (Stage) nombreField.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                event.consume(); // Prevent default close
                handleCancel();
            });
        });

        loadUserCalendars();
    }

    /**
     * Setter para la fecha predeterminada del DatePicker.
     */
    public void setDefaultDate(LocalDate date) {
        fechaPicker.setValue(date);
    }

    /**
     * Setter para recibir el Entry provisional.
     */
    public void setEntry(Entry<TEvent> entry) {
        this.entry = entry;
    }

    /**
     * Si se cancela la operación, se elimina el Entry provisional.
     */
    @FXML
    private void handleCancel() {
        // Eliminar el Entry provisional
        try {
            if(entry!= null){
            DashboardControllerSingleton.getInstance().removeEntry(entry);
            entry.setCalendar(null);
            }
            closeWindow();
        }catch (Exception e){
        }
    }



    /**
     * Si se crea el evento correctamente, se actualiza el Entry y se deja en el calendario.
     */
    @FXML
    private void handleCreate() {
        try {
            DashboardController dashboardCtrl = DashboardControllerSingleton.getInstance();
            // Realizar validaciones
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

            TCalendar selectedCalendar = calendarComboBox.getValue();
            if (selectedCalendar == null) {
                showErrorDialog("Error de validación", "Debe seleccionar un calendario para el evento.");
                return;
            }

            LocalDate fecha = fechaPicker.getValue();
            LocalTime hora = horaComboBox.getValue();
            String nombre = nombreField.getText();
            String ubicacion = ubicacionField.getText();
            TEvent event = new TEvent(null, nombre, fecha, hora, ubicacion, true);

            Long calendarId = selectedCalendar.getId();

            // Llamar a la API para crear el evento
            Task<String> apiTask = ServiceFactory.getInstance().createEventSA().createEvent(event, calendarId);
            apiTask.setOnSucceeded(e -> {
                TEvent eventReturned = gson.fromJson(apiTask.getValue(), TEvent.class);
                // Asumir que la API devuelve el ID del evento
                event.setId(eventReturned.getId());
                // Actualizar el Entry provisional con los datos finales
                entry.setTitle(event.getName());
                entry.setUserObject(event);
                // Mostrar mensaje de éxito
                showSuccessDialog("Evento creado", "El evento se ha creado correctamente.");
                closeWindow();
            });

            apiTask.setOnFailed(e -> {
                showErrorDialog("Error de conexión", "No se pudo conectar a la API.");
            });

            new Thread(apiTask).start();
        } catch (Exception e) {
            showErrorDialog("Error inesperado", "Ocurrió un error inesperado: " + e.getMessage());
        }
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

    private void closeWindow() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}

