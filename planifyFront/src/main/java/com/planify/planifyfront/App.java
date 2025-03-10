package com.planify.planifyfront;

import com.planify.planifyfront.view.ViewFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

    private static Stage primaryStage;

    public static void changeView(String fxmlFile, String title) {
        try {
            // Se delega en la fábrica para obtener la escena con marco personalizado
            Scene scene = ViewFactory.getInstance().getView(fxmlFile, title);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            System.err.println("Error loading view: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.initStyle(StageStyle.UNDECORATED);
        changeView("dashboard", "Planify"); // Carga la primera vista
    }

    public static void main(String[] args) {
        launch();
    }
}
