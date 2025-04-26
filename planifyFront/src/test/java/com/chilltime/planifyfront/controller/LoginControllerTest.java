package com.chilltime.planifyfront.controller;

import com.chilltime.planifyfront.test.BaseJavaFxTest;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginControllerTest extends BaseJavaFxTest {

    private LoginController controller;
    private TextField txtUsername;
    private PasswordField txtPassword;
    private Label lblErrors;
    private Button btnSignin;

    @Override
    public void start(Stage stage) throws Exception {
        controller = new LoginController();
        AnchorPane root = new AnchorPane();

        // Create and inject fields
        txtUsername = new TextField();
        txtPassword = new PasswordField();
        lblErrors = new Label();
        btnSignin = new Button("Sign In");

        setPrivateField("txtUsername", txtUsername);
        setPrivateField("txtPassword", txtPassword);
        setPrivateField("lblErrors", lblErrors);
        setPrivateField("btnSignin", btnSignin);

        root.getChildren().addAll(txtUsername, txtPassword, lblErrors, btnSignin);

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = LoginController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }



    @Test
    public void testSuccessfulLogin() throws Exception {
        // Simulate user input
        Platform.runLater(() -> {
            txtUsername.setText("validUser");
            txtPassword.setText("validPassword");
            btnSignin.fire(); // Simulate button click
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
            txtUsername.setText("");
            txtPassword.setText("");
            btnSignin.fire();;
        });

        // Wait for JavaFX thread
        sleep(500);

        // Verify error message
        assertEquals("El nombre de usuario y la contraseña no pueden estar vacíos.", lblErrors.getText());
    }

    @Test
    public void testInvalidCredentials() throws Exception {
        // Simulate user input
        Platform.runLater(() -> {
            txtUsername.setText("invalidUser");
            txtPassword.setText("invalidPassword");
            btnSignin.fire();
        });

        // Wait for JavaFX thread
        sleep(500);

        // Verify error message
        assertEquals("Credenciales inválidas.", lblErrors.getText());
    }

}
