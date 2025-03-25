package com.chilltime.planifyfront.controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.chilltime.planifyfront.model.transfer.TEvento;
import javafx.application.Platform;
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

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class DashboardController {

    @FXML
    private AnchorPane calendarPane;

    @FXML
    private ListView<TEvento> eventsListView;

    private CalendarView calendarView;
    private Calendar calendar;
    private CalendarSource userCalendarSource;

    @FXML
    public void initialize() {
        // Registrar instancia para usar en EventFormController
        DashboardControllerSingleton.setInstance(this);

        // Crear la vista principal del calendario
        calendarView = new CalendarView();

        // Crear un calendario de CalendarFX
        calendar = new Calendar("Eventos");

        // Agregar event handler para cambios en el Entry (ejemplo de persistencia)
        calendar.addEventHandler(event -> {
            if (CalendarEvent.ENTRY_USER_OBJECT_CHANGED.equals(event.getEventType())) {
                CalendarEvent calendarEvent = (CalendarEvent) event;
                Entry<?> entry = calendarEvent.getEntry();
            };
        });

        calendarView.setEntryFactory(param -> {
            Entry<TEvento> newEntry = new Entry<>("Nuevo Evento");
            LocalDate clickedDate = param.getDateControl().getDate();

            // Set a specific time for the entry instead of an all-day event
            LocalTime defaultTime = LocalTime.of(12, 0); // Default to noon
            newEntry.setInterval(clickedDate.atTime(defaultTime), clickedDate.atTime(defaultTime).plusHours(1));

            // Add the entry to the calendar
            calendar.addEntry(newEntry);

            // Open the event form
            crearEventoForm(clickedDate, newEntry);

            return newEntry;
        });


        calendarView.setEntryDetailsPopOverContentCallback(param -> null);

        updateTimeThread();

        // Crear CalendarSource y agregar el calendario
        userCalendarSource = new CalendarSource("Calendarios Privados");
        userCalendarSource.getCalendars().add(calendar);
        calendarView.getCalendarSources().add(userCalendarSource);

        // Configurar la vista del calendario en el AnchorPane
        VBox paddedCalendarContainer = new VBox(calendarView);
        AnchorPane.setTopAnchor(paddedCalendarContainer, 0.0);
        AnchorPane.setRightAnchor(paddedCalendarContainer, 0.0);
        AnchorPane.setBottomAnchor(paddedCalendarContainer, 0.0);
        AnchorPane.setLeftAnchor(paddedCalendarContainer, 0.0);
        calendarPane.getChildren().add(paddedCalendarContainer);

        // Configurar la ListView para mostrar los eventos
        eventsListView.setCellFactory(param -> new ListCell<TEvento>() {
            @Override
            protected void updateItem(TEvento evento, boolean empty) {
                super.updateItem(evento, empty);
                if (empty || evento == null) {
                    setText("Empty");
                } else {
                    setText(String.format("Nombre: %s - Día: %s - Hora: %s - Ubicación: %s",
                            evento.getNombre(),
                            evento.getFecha(),
                            evento.getHora().toString(),
                            evento.getUbicacion()));
                }
            }
        });

        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowPrintButton(false);
    }

    /**
     * Abre el formulario de creación de evento, pasando la fecha clicada y el Entry provisional.
     */
    public void crearFormulario(LocalDate date, Entry<TEvento> entry) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/eventForm.fxml"));
            Parent root = loader.load();
            EventFormController eventFormController = loader.getController();
            eventFormController.setDefaultDate(date);
            eventFormController.setEntry(entry);
            Stage stage = new Stage();
            stage.setTitle("Crear Evento");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para abrir el formulario desde el entry factory
    @FXML
    public void crearEventoForm(LocalDate localDate, Entry<TEvento> entry) {
        crearFormulario(localDate, entry);
    }

    @FXML
    public void crearEventoForm() {
        // Usar la fecha actual para crear el evento de forma manual
        LocalDate today = LocalDate.now();
        Entry<TEvento> newEntry = new Entry<>("Nuevo Evento");
        calendar.addEntry(newEntry);
        crearFormulario(today, newEntry);
    }


    /**
     * Elimina el Entry del calendario.
     */
    public void removeEntry(Entry<TEvento> entryToRemove) {
        if (entryToRemove != null) {
            Platform.runLater(() -> {
                // Ensure the removal happens on the JavaFX Application Thread
                calendar.removeEntry(entryToRemove);
            });
        }
    }

    /**
     * Agrega el evento creado al calendario (normalmente se invoca tras la creación exitosa).
     */
    public void addEvent(TEvento evento) {
        Entry<TEvento> entry = new Entry<>(evento.getNombre());
        entry.setUserObject(evento);
        entry.changeStartDate(evento.getFecha());
        entry.changeEndDate(evento.getFecha());
        calendar.addEntry(entry);
        eventsListView.getItems().add(evento);
    }


    @FXML
    public void crearCalendarioForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/calendarForm.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Crear Calendario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    private void updateTimeThread() {
        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
    }
}


