package com.planify.planifyfront;

import com.planify.planifyfront.view.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {

    private static Stage primaryStage;

    public static void changeView(String fxmlFile, String title) {
        try {
            Scene scene = null;
            if(fxmlFile != null) {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxmlFile + ".fxml"));
                Parent root = loader.load();
                scene = new Scene(root);
            }
            else {
                scene = ViewFactory.getInstance().getView(title);
            }

            // Configuración común de la ventana (reutilizable)
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage; // Guarda la stage principal
        changeView("dashboard", "Planify"); // Carga la primera vista
    }

    public static void main(String[] args) {
        launch();
    }

}