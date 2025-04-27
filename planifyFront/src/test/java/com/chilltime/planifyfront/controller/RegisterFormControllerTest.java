package com.chilltime.planifyfront.controller;


import com.chilltime.planifyfront.test.BaseJavaFxTest;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.chilltime.planifyfront.model.service.ServiceFactory;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterFormControllerTest extends BaseJavaFxTest {

    private RegisterFormController controller;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label lblErrors;


    @Override
    public void start(Stage stage) throws Exception {
        controller = new RegisterFormController();
        AnchorPane root = new AnchorPane();

        // Create and inject fields
        usernameField = new TextField();
        passwordField = new PasswordField();
        lblErrors = new Label();

        setPrivateField("username", usernameField);
        setPrivateField("password", passwordField);
        setPrivateField("lblErrors", lblErrors);

        root.getChildren().addAll(usernameField, passwordField, lblErrors);

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = RegisterFormController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    @Test
    public void testSuccessfulRegistration() throws Exception {
        // Simulate user input
        Platform.runLater(() -> {
            usernameField.setText("testuser32");
            passwordField.setText("password123");
            controller.handleRegister();
        });

        // Wait for JavaFX thread
        sleep(500);

        // Verify no error message is displayed
        assertEquals("", lblErrors.getText());
    }

    @Test
    public void testEmptyFields() throws Exception {
        // Simulate user input
        Platform.runLater(() -> {
            usernameField.setText("");
            passwordField.setText("");
            controller.handleRegister();
        });

        // Wait for JavaFX thread
        sleep(500);

        // Verify error message
        assertEquals("El usuario ha dejado alguno de los campos vacíos", lblErrors.getText());
    }

    @Test
    public void testInvalidUsername() throws Exception {
        // Simulate user input
        Platform.runLater(() -> {
            usernameField.setText("nombredeusuarioextremadamentelargo");
            passwordField.setText("password123");
            controller.handleRegister();
        });

        // Wait for JavaFX thread
        sleep(500);

        // Verify error message
        assertEquals("El nombre de usuario no es válido. Debe tener máximo 15 caracteres", lblErrors.getText());
    }

    @Test
    public void testInvalidPassword() throws Exception {
        // Simulate user input
        Platform.runLater(() -> {
            usernameField.setText("testuser432");
            passwordField.setText("short");
            controller.handleRegister();
        });

        // Wait for JavaFX thread
        sleep(500);

        // Verify error message
        assertEquals("La contraseña no es válida. Debe tener entre 8 y 15 caracteres", lblErrors.getText());
    }

    @Test
    public void testRegistrationWithExistingUsername() throws Exception {
        // Simulate user input
        Platform.runLater(() -> {
            usernameField.setText("existinguser");
            passwordField.setText("password123");
            controller.handleRegister();
        });

        // Wait for JavaFX thread
        sleep(500);

        // Verify error message
        assertEquals("El nombre de usuario ya existe", lblErrors.getText());
    }
}