package com.planify.planifyfront.view;

import com.planify.planifyfront.model.transfer.TEvento;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FullCalendarView {

    private ArrayList<AnchorPaneNode> allCalendarDays = new ArrayList<>(35);
    private VBox view;
    private Text calendarTitle;
    private YearMonth currentYearMonth;
    private HashMap<LocalDate, List<TEvento>> events;

    /**
     * Crea la vista del calendario para el mes indicado.
     * @param yearMonth año y mes a mostrar.
     */
    public FullCalendarView(YearMonth yearMonth) {
        events = new HashMap<>();
        currentYearMonth = yearMonth;
        // Crear el grid del calendario
        GridPane calendar = new GridPane();
        calendar.setPrefSize(600, 400);
        calendar.getStyleClass().add("calendar-grid"); // Uso de clase CSS
        calendar.setGridLinesVisible(true);
        // Se crean las celdas (AnchorPaneNode) para cada día
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                AnchorPaneNode ap = new AnchorPaneNode();
                ap.getStyleClass().add("cell");
                ap.setPrefSize(200,200);
                calendar.add(ap, j, i);
                allCalendarDays.add(ap);
            }
        }
        // Etiquetas con los nombres de los días de la semana
        Text[] dayNames = new Text[]{ new Text("    Sunday"), new Text("Monday"), new Text("Tuesday"),
                new Text("Wednesday"), new Text("Thursday"), new Text("Friday"),
                new Text("Saturday") };
        GridPane dayLabels = new GridPane();
        dayLabels.setPrefWidth(600);
        int col = 0;
        for (Text txt : dayNames) {
            txt.getStyleClass().add("day-name");
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(200, 10);
            AnchorPane.setBottomAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            dayLabels.add(ap, col++, 0);
        }
        // Botones para cambiar de mes y título del calendario
        calendarTitle = new Text();
        calendarTitle.getStyleClass().add("calendar-title");

        Button previousMonth = new Button("<<");
        previousMonth.getStyleClass().add("button-calendar");
        previousMonth.setOnAction(e -> previousMonth());

        Button nextMonth = new Button(">>");
        nextMonth.getStyleClass().add("button-calendar");
        nextMonth.setOnAction(e -> nextMonth());

        HBox titleBar = new HBox(10, previousMonth, calendarTitle, nextMonth);
        titleBar.getStyleClass().add("calendar-title-bar");
        titleBar.setAlignment(Pos.BASELINE_CENTER);

        // Inicializa el calendario con el mes actual
        populateCalendar(currentYearMonth);
        view = new VBox(titleBar, dayLabels, calendar);
    }

    public void addEvent(TEvento evento) {
        LocalDate eventDate = evento.getFecha();
        events.putIfAbsent(eventDate, new ArrayList<>());
        events.get(eventDate).add(evento);
        Platform.runLater(() -> {
            populateCalendar(currentYearMonth);
        });// Repinta el calendario después de agregar el evento
    }

    /**
     * Rellena el calendario para el mes indicado, creando para cada celda:
     * - Un Text con el número del día.
     * - Un VBox contenedor para los eventos.
     * @param yearMonth mes y año a renderizar.
     */
    public void populateCalendar(YearMonth yearMonth) {
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY")) {
            calendarDate = calendarDate.minusDays(1);
        }

        for (AnchorPaneNode ap : allCalendarDays) {
            // Clear existing events
            ap.getChildren().clear();
            ap.clearEvents();

            // Set date and day number
            Text dayNumber = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            dayNumber.getStyleClass().add("day-number");
            AnchorPane.setTopAnchor(dayNumber, 5.0);
            AnchorPane.setLeftAnchor(dayNumber, 5.0);
            ap.getChildren().add(dayNumber);

            // Add style for current month
            if (calendarDate.getMonth() == currentYearMonth.getMonth()) {
                ap.getStyleClass().remove("outside-month");
            } else {
                ap.getStyleClass().add("outside-month");
            }

            // Set date and add event markers
            ap.setDate(calendarDate);
            if (events.containsKey(calendarDate)) {
                List<TEvento> dayEvents = events.get(calendarDate);
                for (int i = 0; i < dayEvents.size(); i++) {
                    ap.addEventMarker();
                }
            }

            calendarDate = calendarDate.plusDays(1);
        }

        calendarTitle.setText(yearMonth.getMonth().toString() + " " + yearMonth.getYear());
    }


    /**
     * Recorre las celdas y para cada día agrega los eventos que correspondan.
     */
    private void updateEvents() {
        for (AnchorPaneNode cell : allCalendarDays) {
            LocalDate cellDate = cell.getDate();
            // Buscar el contenedor de eventos (suponemos que es el segundo hijo)
            VBox eventsContainer = null;
            for (Node node : cell.getChildren()) {
                if (node instanceof VBox) {
                    eventsContainer = (VBox) node;
                    break;
                }
            }
            if (eventsContainer != null) {
                // Limpiar eventos previos
                eventsContainer.getChildren().clear();
                // Recorrer la lista de eventos y agregarlos si la fecha coincide

            }
        }
    }

    /**
     * Retrocede un mes y actualiza el calendario.
     */
    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        populateCalendar(currentYearMonth);
    }

    /**
     * Avanza un mes y actualiza el calendario.
     */
    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        populateCalendar(currentYearMonth);
    }

    public List<TEvento> getEventsForDate(LocalDate date) {
        return events.getOrDefault(date, new ArrayList<>());
    }

    public VBox getView() {
        return view;
    }

    public ArrayList<AnchorPaneNode> getAllCalendarDays() {
        return allCalendarDays;
    }

    public void setAllCalendarDays(ArrayList<AnchorPaneNode> allCalendarDays) {
        this.allCalendarDays = allCalendarDays;
    }
}
