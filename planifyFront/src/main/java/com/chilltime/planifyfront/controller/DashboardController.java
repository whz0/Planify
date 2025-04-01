package com.chilltime.planifyfront.controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.chilltime.planifyfront.model.service.CalendarSA;
import com.chilltime.planifyfront.model.service.EventSA;
import com.chilltime.planifyfront.model.service.ServiceFactory;
import com.chilltime.planifyfront.model.transfer.TCalendar;
import com.chilltime.planifyfront.model.transfer.TContext;
import com.chilltime.planifyfront.model.transfer.TEvent;
import com.chilltime.planifyfront.utils.CalendarUtils;
import com.chilltime.planifyfront.utils.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.chilltime.planifyfront.utils.DialogWindows.showErrorDialog;

public class DashboardController {

    @FXML
    private AnchorPane calendarPane;

    @FXML
    private ListView<TEvent> eventsListView;

    private CalendarView calendarView;
    private Calendar calendar;
    private CalendarSource userCalendarSource;

    @FXML
    public void initialize() {
        // Registrar instancia para usar en EventFormController
        DashboardControllerSingleton.setInstance(this);

        // Crear y configurar el calendario
        calendarView = new CalendarView();
        calendar = new Calendar("Eventos");

        // Event handler y entry factory setup
        configureCalendarHandlers();

        // Configurar el calendario
        userCalendarSource = new CalendarSource("Calendarios Privados");
        userCalendarSource.getCalendars().add(calendar);
        calendarView.getCalendarSources().add(userCalendarSource);
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowPrintButton(false);
        calendarView.setShowDeveloperConsole(true);
        calendarView.showMonthPage();

        // Configurar layout
        calendarPane.getChildren().clear();
        calendarPane.getChildren().add(calendarView);

        // Anclar el calendario
        calendarView.setPrefHeight(Double.MAX_VALUE);
        AnchorPane.setTopAnchor(calendarView, 0.0);
        AnchorPane.setRightAnchor(calendarView, 0.0);
        AnchorPane.setBottomAnchor(calendarView, 0.0);
        AnchorPane.setLeftAnchor(calendarView, 0.0);

        // Anclar el ListView
        AnchorPane.setBottomAnchor(eventsListView, 0.0);
        AnchorPane.setLeftAnchor(eventsListView, 0.0);
        AnchorPane.setRightAnchor(eventsListView, 0.0);
        eventsListView.setPrefHeight(180.0);

        // Configurar cell factory para la ListView
        configureCellFactory();

        // Iniciar thread de actualización de tiempo
        updateTimeThread();

        SessionManager.getInstance().setCurrentUserId(1L);
    }

    private void configureCalendarHandlers() {
        calendar.addEventHandler(event -> {
            if (CalendarEvent.ENTRY_USER_OBJECT_CHANGED.equals(event.getEventType())) {
                CalendarEvent calendarEvent = (CalendarEvent) event;
                Entry<?> entry = calendarEvent.getEntry();
            }
        });

        calendarView.setEntryFactory(param -> {
            Entry<TEvent> newEntry = new Entry<>("Nuevo Evento");
            LocalDate clickedDate = param.getDateControl().getDate();
            LocalTime defaultTime = LocalTime.of(12, 0);
            newEntry.setInterval(clickedDate.atTime(defaultTime), clickedDate.atTime(defaultTime).plusHours(1));
            calendar.addEntry(newEntry);
            crearEventoForm(clickedDate, newEntry);
            return newEntry;
        });

        calendarView.setEntryDetailsPopOverContentCallback(param -> null);
        calendarView.setEntryDetailsCallback(param -> null);
        calendarView.setShowSearchField(false);
        calendarView.setShowPageSwitcher(false);
    }

    private void configureCellFactory() {
        eventsListView.setCellFactory(param -> new ListCell<TEvent>() {
            @Override
            protected void updateItem(TEvent evento, boolean empty) {
                super.updateItem(evento, empty);
                if (empty || evento == null) {
                    setText(null);
                } else {
                    setText(String.format("Nombre: %s - Día: %s - Hora: %s - Ubicación: %s",
                            evento.getName(),
                            evento.getDate(),
                            evento.getTime().toString(),
                            evento.getLocation()));
                }
            }
        });
    }

    /**
     * Abre el formulario de creación de evento, pasando la fecha clicada y el Entry provisional.
     */
    public void crearFormulario(LocalDate date, Entry<TEvent> entry) {
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
    public void crearEventoForm(LocalDate localDate, Entry<TEvent> entry) {
        crearFormulario(localDate, entry);
    }

    @FXML
    public void crearEventoForm() {
        // Usar la fecha actual para crear el evento de forma manual
        LocalDate today = LocalDate.now();
        Entry<TEvent> newEntry = new Entry<>("Nuevo Evento");
        calendar.addEntry(newEntry);
        crearFormulario(today, newEntry);
    }


    /**
     * Elimina el Entry del calendario.
     */
    public void removeEntry(Entry<TEvent> entryToRemove) {
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
    public void addEvent(TEvent evento) {
        Entry<TEvent> entry = new Entry<>(evento.getName());
        entry.setUserObject(evento);
        entry.changeStartDate(evento.getDate());
        entry.changeEndDate(evento.getDate());
        calendar.addEntry(entry);
        eventsListView.getItems().add(evento);
    }

    /*
    *  Métodos para manejar la creación de formularios
    */

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

    @FXML
    public void unirseCalendario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/joinCalendarForm.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Unirse a Calendario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir el formulario para unirse a un calendario.");
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

    public void addCalendar(TCalendar calendari) {
        Calendar<TEvent> calendar = CalendarUtils.toCalendar(calendari);
        userCalendarSource.getCalendars().add(calendar);
    }

    @FXML
    public void generarCodigoForm(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/generateCodeForm.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Generar Código de Invitación");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace(); // Agrega esta línea para obtener más detalles del error
            showErrorDialog("Error Crítico",
                    "No se pudo inicializar el generador de códigos: " + e.getMessage());
        }
    }

    private void loadUserCalendars() {
        CalendarSA calendarSA = ServiceFactory.getInstance().createCalendarSA();
        Task<String> task = calendarSA.getCalendarsWithEventsByUserId(SessionManager.getInstance().getCurrentUserId());

        task.setOnSucceeded(event -> {
            String response = task.getValue();
            try {
                // Parsear la respuesta JSON para obtener los calendarios
                ObjectMapper mapper = new ObjectMapper();
                TContext context = mapper.readValue(response, TContext.class);

                if (context.getStatus_code() == 200) {
                    List<TCalendar> calendars = mapper.convertValue(context.getData(),
                            mapper.getTypeFactory().constructCollectionType(List.class, TCalendar.class));

                    // Limpiar calendarios existentes
                    userCalendarSource.getCalendars().clear();

                    // Añadir cada calendario y sus eventos
                    for (TCalendar tCalendar : calendars) {
                        Calendar<TEvent> calendar = CalendarUtils.toCalendar(tCalendar);
                        userCalendarSource.getCalendars().add(calendar);

                        // Cargar eventos para este calendario
                        loadCalendarEvents(tCalendar.getId());
                    }
                } else {
                    mostrarAlerta("Error", context.getMessage());
                }
            } catch (IOException e) {
                mostrarAlerta("Error", "No se pudieron cargar los calendarios: " + e.getMessage());
                e.printStackTrace();
            }
        });

        task.setOnFailed(event -> {
            mostrarAlerta("Error", "Falló la carga de calendarios: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        // Ejecutar la tarea en un hilo separado
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void loadCalendarEvents(Long calendarId) {
        EventSA eventSA = ServiceFactory.getInstance().createEventSA();
        Task<String> task = eventSA.getEventsByCalendarId(calendarId);

        task.setOnSucceeded(event -> {
            String response = task.getValue();
            try {
                // Parsear la respuesta JSON
                ObjectMapper mapper = new ObjectMapper();
                // Configurar para manejar LocalDate y LocalTime
                mapper.registerModule(new JavaTimeModule());
                TContext context = mapper.readValue(response, TContext.class);

                if (context.getStatus_code() == 200) {
                    List<TEvent> events = mapper.convertValue(context.getData(),
                            mapper.getTypeFactory().constructCollectionType(List.class, TEvent.class));

                    // Obtener el calendario correspondiente por ID
                    Calendar targetCalendar = null;
                    for (Calendar cal : userCalendarSource.getCalendars()) {
                        if (cal.getName().contains("ID:" + calendarId)) {
                            targetCalendar = cal;
                            break;
                        }
                    }

                    if (targetCalendar != null) {
                        // Añadir eventos al calendario
                        for (TEvent tEvent : events) {
                            Entry<TEvent> entry = new Entry<>(tEvent.getName());
                            entry.setUserObject(tEvent);
                            entry.changeStartDate(tEvent.getDate());
                            entry.changeStartTime(tEvent.getTime());
                            entry.changeEndDate(tEvent.getDate());
                            entry.changeEndTime(tEvent.getTime().plusHours(1)); // Asumiendo duración de 1 hora
                            entry.setLocation(tEvent.getLocation());

                            targetCalendar.addEntry(entry);

                            // Añadir a la lista de eventos
                            Platform.runLater(() -> {
                                eventsListView.getItems().add(tEvent);
                            });
                        }
                    }
                } else {
                    System.err.println("Error al cargar eventos: " + context.getMessage());
                }
            } catch (IOException e) {
                System.err.println("Error al procesar eventos: " + e.getMessage());
                e.printStackTrace();
            }
        });

        task.setOnFailed(event -> {
            System.err.println("Falló la carga de eventos: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        // Ejecutar la tarea
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

}


