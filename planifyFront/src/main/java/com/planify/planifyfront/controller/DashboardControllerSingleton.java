package com.planify.planifyfront.controller;

import com.planify.planifyfront.App;
import javafx.fxml.FXMLLoader;

import java.io.IOException;


public class DashboardControllerSingleton {

    private static DashboardController instance;

    public static DashboardController getInstance() throws IOException {
        if (instance == null) {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/dashboard.fxml"));
            loader.load();
            instance = loader.getController();
        }

        return instance;
    }
}