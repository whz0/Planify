package com.chilltime.planifyfront.controller;

import com.chilltime.planifyfront.App;
import com.chilltime.planifyfront.model.service.ServiceFactory;
import com.chilltime.planifyfront.model.transfer.TCalendar;
import com.chilltime.planifyfront.model.transfer.TContext;
import com.chilltime.planifyfront.model.transfer.TEvent;
import com.chilltime.planifyfront.model.transfer.TPlanner;
import com.chilltime.planifyfront.utils.LocalDateAdapter;
import com.chilltime.planifyfront.utils.LocalTimeAdapter;
import com.chilltime.planifyfront.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    private Label lblErrors;

    // TODO Actualizar lblErrors para q muestre error cuando funcione(usuario ya
    // registrado, etc)
    @FXML
    void handleRegister() {
        try {

            if(username.getText().isEmpty() || password.getText().isEmpty()) {
                lblErrors.setText("El usuario ha dejado alguno de los campos vacíos");
            }

            if(username.getText().length() > 15) {
                lblErrors.setText("El nombre de usuario no es válido. Debe tener máximo 15 caracteres");
            }

            if(password.getText().length() < 8 || password.getText().length() > 15) {
                lblErrors.setText("La contraseña no es válida. Debe tener entre 8 y 15 caracteres");
            }

            TPlanner planner = new TPlanner();
            planner.setUsername(username.getText());
            planner.setPassword(password.getText());
            planner.setRole("ROLE_PLANNER");
            planner.setActive(true);

            // Llamar a la API para crear el evento
            System.out.println("[Register form controller] Planner info: " + planner);

            Task<String> apiTask = ServiceFactory.getInstance().createPlannerSA().registerPlanner(planner);

            apiTask.setOnSucceeded(e -> {
                TContext context = gson.fromJson(apiTask.getValue(), TContext.class);

                if (context.getData() == null) {
                    showError(context.getMessage());
                    showErrorDialog("Error de registro", context.getMessage());
                } else {
                    TPlanner plannerReturned = gson.fromJson(gson.toJson(context.getData()), TPlanner.class);

                    showSuccessDialog("Planner Registrado", "Te has registrado correctamente.");
                    SessionManager.getInstance().setCurrentUserId(plannerReturned.getId());

                    App.changeView("dashboard", "Planify");
                    closeWindow();
                }
            });

            apiTask.setOnFailed(e -> {
                showErrorDialog("Error de conexión", "No se pudo conectar a la API");
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

    private void showError(String message) {
        lblErrors.setText(message);
    }

    public void handleButtonAction(MouseEvent mouseEvent) {
    }

    /**
     * TODO Logica para cambiar de vista de registro a login
     */
    public void changeView(MouseEvent mouseEvent) {
    }
}
