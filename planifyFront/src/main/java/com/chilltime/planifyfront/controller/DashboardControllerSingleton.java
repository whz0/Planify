package com.chilltime.planifyfront.controller;

import com.chilltime.planifyfront.App;
import javafx.fxml.FXMLLoader;

import java.io.IOException;


public class DashboardControllerSingleton {

    private static DashboardController instance;

    public static DashboardController getInstance() throws IOException {
        return instance;
    }

    public static void setInstance(DashboardController instance) {
        DashboardControllerSingleton.instance = instance;
    }
}