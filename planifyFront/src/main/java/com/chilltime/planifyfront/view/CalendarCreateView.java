package com.chilltime.planifyfront.view;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.function.Consumer;

/**
 * Panel que permite introducir el nombre y seleccionar un estilo para crear un Calendar
 * y añadirlo a un CalendarSource existente.
 */
public final class CalendarCreateView extends BorderPane {

    private final TextField nameField;
    private final ComboBox<Calendar.Style> styleComboBox;
    private Stage dialog;
    private final CalendarSourceHolder calendarSource;

    /**
     * Constructor.
     *
     * @param calendarSource el CalendarSource al que se añadirá el nuevo Calendar.
     * @param onAccept callback que recibe el Calendar creado.
     */
    public CalendarCreateView(CalendarSourceHolder calendarSource, Consumer<Calendar> onAccept) {
        this.calendarSource = calendarSource;

        // Campo para el nombre del Calendar
        nameField = new TextField();

        // ComboBox para seleccionar el estilo (color) del Calendar
        styleComboBox = new ComboBox<>();
        styleComboBox.getItems().setAll(Calendar.Style.values());
        styleComboBox.setButtonCell(new StyleCell());
        styleComboBox.setCellFactory(listView -> new StyleCell());

        // Botón Aceptar: solo habilitado cuando se han introducido datos válidos
        Button acceptButton = new Button("Aceptar");
        acceptButton.disableProperty().bind(Bindings.or(
                Bindings.isEmpty(nameField.textProperty()),
                Bindings.isNull(styleComboBox.valueProperty())
        ));
        acceptButton.setOnAction(evt -> {
            String calendarName = nameField.getText();
            Calendar.Style style = styleComboBox.getValue();
            // Creamos el Calendar con el nombre y estilo elegido
            Calendar newCalendar = new Calendar(calendarName);
            newCalendar.setStyle(style);
            // Se añade el Calendar al CalendarSource recibido
            calendarSource.getCalendarSource().getCalendars().add(newCalendar);
            // Ejecutamos el callback pasando el nuevo Calendar
            if (onAccept != null) {
                onAccept.accept(newCalendar);
            }
            close();
        });

        // Botón Cancelar: cierra el diálogo sin crear nada
        Button cancelButton = new Button("Cancelar");
        cancelButton.setOnAction(evt -> close());

        // Organiza los controles en un GridPane
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Nombre"), 0, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(new Label("Color"), 0, 1);
        gridPane.add(styleComboBox, 1, 1);
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(10));

        GridPane.setHgrow(nameField, Priority.ALWAYS);
        GridPane.setHgrow(styleComboBox, Priority.ALWAYS);

        // Barra de botones en la parte inferior
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(acceptButton, cancelButton);

        VBox bottomPane = new VBox();
        bottomPane.getChildren().addAll(new Separator(), buttonBar);
        bottomPane.setSpacing(10);
        bottomPane.setFillWidth(true);

        // Configura la distribución general del panel
        setCenter(gridPane);
        setBottom(bottomPane);
        setPadding(new Insets(15));
        setPrefWidth(300);
        // Se añade la hoja de estilos de CalendarFX para mantener la coherencia visual
        getStylesheets().add(CalendarView.class.getResource("calendar.css").toExternalForm());
    }

    /**
     * Muestra el diálogo modal para la creación del Calendar.
     *
     * @param owner La ventana propietaria.
     */
    public void show(Window owner) {
        if (dialog == null) {
            dialog = new Stage();
            dialog.initOwner(owner);
            dialog.setScene(new Scene(this));
            dialog.sizeToScene();
            dialog.centerOnScreen();
            dialog.setTitle("Añadir Calendario");
            dialog.initModality(Modality.APPLICATION_MODAL);
        }
        dialog.showAndWait();
    }

    /**
     * Cierra el diálogo y restablece los campos.
     */
    private void close() {
        nameField.clear();
        styleComboBox.setValue(null);
        if (dialog != null) {
            dialog.hide();
        }
    }

    /**
     * Clase interna para personalizar la visualización de los estilos en el ComboBox.
     */
    private static class StyleCell extends ListCell<Calendar.Style> {
        @Override
        protected void updateItem(Calendar.Style item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) {
                Rectangle icon = new Rectangle(12, 12);
                icon.getStyleClass().add(item.name().toLowerCase() + "-icon");
                setGraphic(icon);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setGraphic(null);
            }
        }
    }

    /**
     * Interfaz o clase wrapper para encapsular el CalendarSource.
     * Esto permite modificar o acceder al CalendarSource fácilmente.
     */
    public interface CalendarSourceHolder {
        CalendarSource getCalendarSource();
    }
}
