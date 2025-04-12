package com.chilltime.planifyfront.controller;

import com.chilltime.planifyfront.model.service.ServiceFactory;
import com.chilltime.planifyfront.model.transfer.TEvent;
import com.chilltime.planifyfront.model.transfer.TPlanner;
import com.chilltime.planifyfront.utils.LocalDateAdapter;
import com.chilltime.planifyfront.utils.LocalTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.chilltime.planifyfront.utils.DialogWindows.showErrorDialog;
import static com.chilltime.planifyfront.utils.DialogWindows.showSuccessDialog;

public class RegisterFormController {
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .create();

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private void handleRegister(){
        try{
            TPlanner planner = new TPlanner();
            planner.setUsername(username.getText());
            planner.setPassword(password.getText());
            // Llamar a la API para crear el evento
            Task<String> apiTask = ServiceFactory.getInstance().createPlannerSA().registerPlanner(planner);
            apiTask.setOnSucceeded(e -> {
                TPlanner plannerReturned = gson.fromJson(apiTask.getValue(), TPlanner.class);
                showSuccessDialog("Planner Registrado", "Te has registrado correctamente.");
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

    /**
     * Cierra la ventana actual.
     */
    private void closeWindow() {
        Stage stage = (Stage) username.getScene().getWindow();
        stage.close();
    }
}
