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
        // Registrar esta instancia para que EventFormController la pueda usar
        DashboardControllerSingleton.setInstance(this);

        // Crear la vista principal del calendario
        calendarView = new CalendarView();

        // Crear un calendario de CalendarFX (no se usa java.util.Calendar)
        calendar = new Calendar("Eventos");

        // Agregar un único event handler para detectar cambios en el Entry
        calendar.addEventHandler(event -> {
            if (CalendarEvent.ENTRY_USER_OBJECT_CHANGED.equals(event.getEventType())) {
                CalendarEvent calendarEvent = (CalendarEvent) event;
                Entry<?> entry = calendarEvent.getEntry();
                guardarEntryEnBackend(entry);
            }
        });

        // Configurar el callback para la creación de nuevas entradas.
        // Al hacer doble clic en una celda vacía, se captura la fecha clicada y se abre el formulario.
        calendarView.setEntryDetailsCallback(param -> {
            // Obtener la fecha donde se hizo doble clic
            LocalDate clickedDate = param.getDateControl().getDate();
            crearEventoForm();
            return null;
        });

        calendarView.setEntryDetailsPopOverContentCallback(param->{
            return null;
        });

        updateTimeThread();

        // Crear un CalendarSource y agregar el calendario
        userCalendarSource = new CalendarSource("Calendarios Privados");
        userCalendarSource.getCalendars().add(calendar);
        calendarView.getCalendarSources().add(userCalendarSource);

        // Configurar la vista del calendario para mostrar la página de mes
        VBox paddedCalendarContainer = new VBox(calendarView);
        AnchorPane.setTopAnchor(paddedCalendarContainer, 0.0);
        AnchorPane.setRightAnchor(paddedCalendarContainer, 0.0);
        AnchorPane.setBottomAnchor(paddedCalendarContainer, 0.0);
        AnchorPane.setLeftAnchor(paddedCalendarContainer, 0.0);
        calendarPane.getChildren().add(paddedCalendarContainer);

        // Configuración de la ListView para mostrar los eventos (puedes personalizarla según convenga)
        eventsListView.setCellFactory(param -> new ListCell<TEvento>() {
            @Override
            protected void updateItem(TEvento evento, boolean empty) {
                super.updateItem(evento, empty);
                if (empty || evento == null) {
                    setText("Empty");
                } else {
                    setText(String.format("Nombre :%s - Dia: %s - Hora %s - Ubicacion: %s",
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
     * Agrega el evento creado al calendario y lo persiste.
     * Este metodo es llamado desde el EventFormController tras la creación exitosa.
     */
    public void addEvent(TEvento evento) {
        // Crear un nuevo Entry a partir del objeto TEvento
        Entry<TEvento> entry = new Entry<>(evento.getNombre());
        entry.setUserObject(evento);
        entry.changeStartDate(evento.getFecha());
        entry.changeEndDate(evento.getFecha());
        // Aquí podrías agregar la hora si CalendarFX requiere una combinación de fecha/hora
        calendar.addEntry(entry);
        // Llamar a la persistencia (o, en este ejemplo, imprimir en consola)
        guardarEntryEnBackend(entry);
        // Opcional: Actualizar la ListView si lo deseas
        eventsListView.getItems().add(evento);
    }

    /**
     * Función que recibe un Entry y lo guarda en la base de datos.
     * Se transforma el Entry en un objeto TEvento para adaptarlo a la capa de persistencia.
     */
    private void guardarEntryEnBackend(Entry<?> entry) {
        // Extraer información del entry
        String title = entry.getTitle();
        LocalDate start = entry.getStartDate();
        LocalDate end = entry.getEndDate();

        // Aquí convertirías el entry a un TEvento y llamarías al servicio REST,
        // pero en este ejemplo se simula la operación con un log en consola:
        System.out.println("Guardando entry en la BD: " + title + " | Inicio: " + start + " | Fin: " + end);
    }

    /**
     * Abre el formulario de creación de evento, inicializando el DatePicker con la fecha indicada.
     *
     * @param date La fecha donde se hizo doble clic en el calendario.
     */
    public void crearFormulario(LocalDate date) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/eventForm.fxml"));
            Parent root = loader.load();
            EventFormController eventFormController = loader.getController();
            eventFormController.setDefaultDate(date);
            Stage stage = new Stage();
            stage.setTitle("CrearEvento");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Si se requiere mantener el metodo crearEvento() sin parámetro, se puede mantener para otras acciones.
    @FXML
    public void crearEventoForm() {
        crearFormulario(LocalDate.now());
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
                    Platform.runLater(()->{
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

