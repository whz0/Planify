package com.chilltime.planifyfront.controller;

import com.chilltime.planifyfront.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword; // Cambiado a PasswordField porque así está definido en el FXML

    @FXML
    private Button btnSignup;

    @FXML
    private Button btnSignin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Puedes inicializar algunos elementos aquí si es necesario
    }

    @FXML
    public void handleButtonAction(MouseEvent event) {
        if (event.getSource() == btnSignup) {
            App.changeView("register","Register");
        }
        if (event.getSource() == btnSignin) {
            App.changeView("dashboard","Dashboard");
        }
    }
}