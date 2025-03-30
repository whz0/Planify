package com.chilltime.planifyfront.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GenerateCodeFormController {

    @FXML
    private TextField codeTextField;

    @FXML
    private void initialize() {
        generateNewCode();
    }

    private void generateNewCode() {
        String code = generateRandomCode(8);
        codeTextField.setText(code);
    }

    private String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();

        for(int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }

    @FXML
    void handleGenerate() {
        generateNewCode();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) codeTextField.getScene().getWindow();
        stage.close();
    }
}