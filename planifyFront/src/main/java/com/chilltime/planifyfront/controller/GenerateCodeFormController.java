package com.chilltime.planifyfront.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GenerateCodeFormController {

    private static final int CODE_LENGTH = 8;
    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @FXML
    private TextField codeTextField;

    @FXML
    private void initialize() {
        generateNewCode();
    }

    private void generateNewCode() {
        String code = generateRandomCode(CODE_LENGTH);
        codeTextField.setText(code);
    }

    private String generateRandomCode(int length) {
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * CODE_CHARACTERS.length());
            code.append(CODE_CHARACTERS.charAt(index));
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