package com.planify.planifyfront.view;

import com.planify.planifyfront.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.YearMonth;
import java.util.HashMap;

public class ViewFactoryImp extends ViewFactory {

    private final HashMap<String, Scene> views;

    public ViewFactoryImp() {
        views = new HashMap<>();
        // Por ejemplo, precargamos la vista del calendario

        views.put("calendar", new Scene(new FullCalendarView(YearMonth.now()).getView()));
    }

    /**
     * Metodo que devuelve la escena decorada con marco personalizado.
     * Si fxmlFile no es nulo, se carga el FXML; de lo contrario, se usa una vista precargada.
     */
    @Override
    public Scene getView(String fxmlFile, String title) {
        Parent content = null;
        try {
            if (fxmlFile != null) {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxmlFile + ".fxml"));
                content = loader.load();
            } else {
                // Se usa la vista precargada según el título (por ejemplo, "calendar")
                Scene cachedScene = views.get(title);
                if (cachedScene != null) {
                    content = cachedScene.getRoot();
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
        }
        if(content == null) {
            // Si no se pudo cargar, se usa un contenedor vacío de respaldo
            content = new Pane();
        }
        Parent decoratedRoot = createCustomFrame(content, title);
        Scene scene = new Scene(decoratedRoot);
        scene.setFill(Color.web("#36393f"));// Fondo de la escena con color oscuro

        return scene;
    }

    /**
     * Envuelve el contenido en un contenedor que simula un marco oscuro con barra de título.
     */
    private Parent createCustomFrame(Parent content, String title) {
        BorderPane root = new BorderPane();
        // Establece el color oscuro para el "borde" de la ventana
        root.setStyle("-fx-background-color: #36393f;");

        // Crea la barra de título personalizada
        HBox titleBar = new HBox();
        titleBar.setStyle("-fx-background-color: #23272A; -fx-padding: 8; -fx-alignment: center-left;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Botón para minimizar
        Button minimizeButton = new Button("_");
        minimizeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px;");
        minimizeButton.setOnAction(e -> {
            Stage stage = (Stage) minimizeButton.getScene().getWindow();
            stage.setIconified(true);
        });

        // Botón para maximizar (alternar entre maximizado y normal)
        Button maximizeButton = new Button("[]");
        maximizeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px;");
        maximizeButton.setOnAction(e -> {
            Stage stage = (Stage) maximizeButton.getScene().getWindow();
            stage.setMaximized(!stage.isMaximized());
        });

        // Botón para cerrar, con fondo rojo
        Button closeButton = new Button("X");
        closeButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 14px;");
        closeButton.setOnAction(e -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });

        // Agrega los elementos a la barra de título: título, espacio, y botones de minimizar, maximizar y cerrar
        titleBar.getChildren().addAll(titleLabel, spacer, minimizeButton, maximizeButton, closeButton);

        // Habilitar el arrastre de la ventana a través de la barra de título
        final double[] xOffset = {0};
        final double[] yOffset = {0};
        titleBar.setOnMousePressed(e -> {
            xOffset[0] = e.getSceneX();
            yOffset[0] = e.getSceneY();
        });
        titleBar.setOnMouseDragged(e -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setX(e.getScreenX() - xOffset[0]);
            stage.setY(e.getScreenY() - yOffset[0]);
        });

        // Se asigna la barra de título a la parte superior y el contenido al centro
        root.setTop(titleBar);
        root.setCenter(content);
        return root;
    }

}
