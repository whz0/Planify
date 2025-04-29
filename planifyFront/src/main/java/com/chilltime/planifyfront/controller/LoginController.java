package com.chilltime.planifyfront.controller;

import com.chilltime.planifyfront.App;
import com.chilltime.planifyfront.model.service.ServiceFactory;
import com.chilltime.planifyfront.model.transfer.TContext;
import com.chilltime.planifyfront.model.transfer.TPlanner; // Asumiendo que el login devuelve TPlanner
import com.chilltime.planifyfront.utils.LocalDateAdapter;
import com.chilltime.planifyfront.utils.LocalTimeAdapter;
import com.chilltime.planifyfront.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color; // Para el color de error
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.chilltime.planifyfront.utils.DialogWindows.showErrorDialog;
// Podrías querer un showSuccessDialog si necesitas notificar algo al usuario, aunque navegar suele ser suficiente
// import static com.chilltime.planifyfront.utils.DialogWindows.showSuccessDialog;

public class LoginController {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .create();

    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnSignup;

    @FXML
    private Button btnSignin;

    /**
     * Maneja los clics en los botones Sign in y Sign up.
     * @param event El evento del ratón que disparó la acción.
     */
    @FXML
    public void handleButtonAction(MouseEvent event) {
        if (event.getSource() == btnSignup) {
            lblErrors.setText("");
            App.changeView("register", "Register");
            closeWindow();
        } else if (event.getSource() == btnSignin) {
            loginUser();
        }
    }

    /**
     * Lógica principal para el proceso de inicio de sesión.
     */
    private void loginUser() {
        String username = txtUsername.getText().trim(); // Usar trim para quitar espacios extra
        String password = txtPassword.getText();

        // 1. Validación básica de entrada
        if (username.isEmpty() || password.isEmpty()) {
            showError("El nombre de usuario y la contraseña no pueden estar vacíos.");
            return; // Detener el proceso si faltan datos
        }

        // Limpiar errores previos antes de la llamada
        showError("");

        // 2. Llamada Asíncrona al Servicio de Autenticación
        try {
            // Asume que tu PlannerSA tiene un método loginPlanner(username, password)
            // que devuelve Task<String> con la respuesta JSON (envuelta en TContext)
            Task<String> loginTask = ServiceFactory.getInstance().createPlannerSA().loginPlanner(username, password);

            loginTask.setOnSucceeded(e -> {
                try {
                    String jsonResponse = loginTask.getValue();
                    TContext context = gson.fromJson(jsonResponse, TContext.class);

                    if (context == null || context.getData() == null) {
                        // Error desde el backend (usuario/pass incorrecto, etc.)
                        String errorMessage = (context != null && context.getMessage() != null) ? context.getMessage() : "Credenciales inválidas.";
                        showError(errorMessage);
                        // Opcional: Mostrar también un diálogo
                        // showErrorDialog("Error de inicio de sesión", errorMessage);
                        txtPassword.clear(); // Limpiar contraseña en caso de error
                    } else {
                        // Éxito en la autenticación
                        // Asumiendo que context.getData() contiene el objeto TPlanner
                        TPlanner loggedInPlanner = gson.fromJson(gson.toJson(context.getData()), TPlanner.class);

                        if (loggedInPlanner == null || loggedInPlanner.getId() == null) {
                            showError("Error al procesar los datos del usuario.");
                            return;
                        }

                        // Guardar ID del usuario en la sesión
                        SessionManager.getInstance().setCurrentUserId(loggedInPlanner.getId());
                        SessionManager.getInstance().setAuthenticated(true);
                        System.out.println("[Login Controller] User logged in successfully. ID: " + loggedInPlanner.getId()); // Log

                        // Navegar al Dashboard
                        App.changeView("dashboard", "Planify");

                        // Cerrar la ventana de login
                        closeWindow();
                    }
                } catch (Exception parseException) {
                    // Error al procesar la respuesta del servidor
                    System.err.println("Error parsing login response: " + parseException.getMessage());
                    showError("Error procesando la respuesta del servidor.");
                    showErrorDialog("Error de Respuesta", "No se pudo procesar la respuesta del servidor: " + parseException.getMessage());
                    txtPassword.clear();
                }
            });

            loginTask.setOnFailed(e -> {
                // Error de conexión o en la tarea misma
                Throwable exception = loginTask.getException();
                System.err.println("Login task failed: " + exception.getMessage());
                showError("Error de conexión con el servidor.");
                showErrorDialog("Error de Conexión", "No se pudo conectar con el servidor: " + exception.getMessage());
                txtPassword.clear();
            });

            // Iniciar la tarea en un hilo separado
            new Thread(loginTask).start();

        } catch (Exception e) {
            // Error inesperado al iniciar la tarea o llamar al servicio
            System.err.println("Unexpected error during login attempt: " + e.getMessage());
            showError("Ocurrió un error inesperado.");
            showErrorDialog("Error Inesperado", "Ocurrió un error inesperado al intentar iniciar sesión: " + e.getMessage());
            txtPassword.clear();
        }
    }

    /**
     * Muestra un mensaje de error en la etiqueta lblErrors.
     * @param message El mensaje de error a mostrar. Si es null o vacío, limpia la etiqueta.
     */
    private void showError(String message) {
        Platform.runLater(() -> {
            if (message == null || message.isEmpty()) {
                lblErrors.setText("");
            } else {
                lblErrors.setText(message);
                lblErrors.setTextFill(Color.RED); // Asegura que el color sea rojo para errores
            }
        });
    }


    /**
     * Cierra la ventana actual (la ventana de login).
     */
    private void closeWindow() {
        // Obtiene el Stage (ventana) actual a través de uno de los nodos FXML
        Stage stage = (Stage) txtUsername.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    /**
     * Método de inicialización del controlador FXML.
     * Se llama automáticamente después de que se cargan los elementos FXML.
     */
    @FXML
    public void initialize() {
        lblErrors.setText(""); // Asegurar que no hay mensajes de error al inicio
        // Configurar acción al presionar Enter en el campo de contraseña
        txtPassword.setOnAction(event -> {
            if (!btnSignin.isDisabled()) {
                loginUser();
            }
        });

        // Opcional: Poner el foco inicial en el campo de usuario
        Platform.runLater(() -> txtUsername.requestFocus());
    }
}