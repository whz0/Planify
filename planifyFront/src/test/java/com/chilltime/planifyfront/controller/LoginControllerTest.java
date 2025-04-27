package com.chilltime.planifyfront.controller;

import com.chilltime.planifyfront.model.service.PlannerSA;
import com.chilltime.planifyfront.model.service.ServiceFactory;
import com.chilltime.planifyfront.model.transfer.TContext;
import com.chilltime.planifyfront.test.BaseJavaFxTest;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginControllerTest extends BaseJavaFxTest {

    private LoginController controller;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label lblErrors;
    private Button btnSignin;

    @Override
    public void start(Stage stage) throws Exception {
        controller = new LoginController();
        AnchorPane root = new AnchorPane();

        // Create and inject fields
        usernameField = new TextField();
        passwordField = new PasswordField();
        lblErrors = new Label();
        btnSignin = new Button("Sign In");

        setPrivateField("txtUsername", usernameField);
        setPrivateField("txtPassword", passwordField);
        setPrivateField("lblErrors", lblErrors);
        setPrivateField("btnSignin", btnSignin);

        root.getChildren().addAll(usernameField, passwordField, lblErrors, btnSignin);

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = LoginController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    @BeforeEach
    public void setUp() {
        // Mock the PlannerSA service
        var mockPlannerSA = Mockito.mock(PlannerSA.class);

        // Mock login response for valid credentials
        Mockito.when(mockPlannerSA.loginPlanner("testuser", "password123"))
                .thenReturn(new Task<>() {
                    @Override
                    protected String call() {
                        return "{\"data\": {\"id\": 1}, \"message\": \"\"}";
                    }
                });

        // Mock login response for invalid credentials
        Mockito.when(mockPlannerSA.loginPlanner("wronguser", "wrongpassword"))
                .thenReturn(new Task<>() {
                    @Override
                    protected String call() {
                        return "{\"data\": null, \"message\": \"Credenciales inválidas.\"}";
                    }
                });

        // Mock the ServiceFactory to return the mocked PlannerSA
        var mockServiceFactory = Mockito.mock(ServiceFactory.class);
        Mockito.when(mockServiceFactory.createPlannerSA()).thenReturn(mockPlannerSA);
        ServiceFactory.setInstance(mockServiceFactory); // Ensure this method exists in your ServiceFactory
    }

    @Test
    public void testSuccessfulLogin() throws Exception {
        Platform.runLater(() -> {
            usernameField.setText("testuser");
            passwordField.setText("password123");
            btnSignin.fire(); // Simulate button click
        });

        sleep(500);

        // Verify no error message is displayed
        assertEquals("", lblErrors.getText());
    }

    @Test
    public void testEmptyFields() throws Exception {
        Platform.runLater(() -> {
            usernameField.setText("");
            passwordField.setText("");
            btnSignin.fire(); // Simulate button click
        });

        sleep(500);

        // Verify error message
        assertEquals("El usuario ha dejado alguno de los campos vacíos.", lblErrors.getText());
    }

    @Test
    public void testInvalidCredentials() throws Exception {
        Platform.runLater(() -> {
            usernameField.setText("wronguser");
            passwordField.setText("wrongpassword");
            btnSignin.fire(); // Simulate button click
        });

        sleep(500);

        // Verify error message
        assertEquals("Credenciales inválidas.", lblErrors.getText());
    }
}