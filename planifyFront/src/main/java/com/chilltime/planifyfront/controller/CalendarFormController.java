package com.chilltime.planifyfront.controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class CalendarFormController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField descriptionField;

    @FXML
    private ComboBox<Calendar.Style> styleComboBox;

    private Consumer<Calendar> onAccept;
    private CalendarSourceHolder calendarSourceHolder;

    /**
     * Inicializa el ComboBox con los estilos disponibles y asigna una celda personalizada.
     */
    @FXML
    private void initialize() {
        styleComboBox.getItems().setAll(Calendar.Style.values());
        styleComboBox.setButtonCell(new StyleCell());
        styleComboBox.setCellFactory(listView -> new StyleCell());
        styleComboBox.getStylesheets().clear();
    }

    /**
     * Se invoca al pulsar el botón "Aceptar". Valida los datos, crea el Calendar y ejecuta el callback.
     */
    @FXML
    private void handleAccept() {
        String name = nameField.getText();
        String description = descriptionField.getText();
        Calendar.Style style = styleComboBox.getValue();

        if (name == null || name.trim().isEmpty()) {
            showError("El nombre no puede estar vacío");
            return;
        }
        if (style == null) {
            showError("Debe seleccionar un color");
            return;
        }

        // Crear el Calendar y asignar el estilo
        Calendar newCalendar = new Calendar(name);
        newCalendar.setStyle(style);
        // Se almacena la descripción en el objeto de usuario
        newCalendar.setUserObject(description);

        // Se añade el Calendar al CalendarSource proporcionado
        if (calendarSourceHolder != null) {
            calendarSourceHolder.getCalendarSource().getCalendars().add(newCalendar);
        }

        // Ejecuta el callback si se ha definido
        if (onAccept != null) {
            onAccept.accept(newCalendar);
        }

        closeWindow();
    }

    /**
     * Se invoca al pulsar el botón "Cancelar".
     */
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    /**
     * Muestra un diálogo de error con el mensaje indicado.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de validación");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Cierra la ventana actual.
     */
    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    /**
     * Celda personalizada para mostrar los colores asociados a cada Calendar.Style.
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
     * Permite establecer un callback a ejecutar al crear el Calendar.
     *
     * @param onAccept callback que recibe el Calendar creado.
     */
    public void setOnAccept(Consumer<Calendar> onAccept) {
        this.onAccept = onAccept;
    }

    /**
     * Permite asignar el CalendarSourceHolder donde se añadirá el nuevo Calendar.
     *
     * @param calendarSourceHolder el contenedor del CalendarSource.
     */
    public void setCalendarSourceHolder(CalendarSourceHolder calendarSourceHolder) {
        this.calendarSourceHolder = calendarSourceHolder;
    }

    /**
     * Interfaz para encapsular el CalendarSource.
     */
    public interface CalendarSourceHolder {
        CalendarSource getCalendarSource();
    }
}
