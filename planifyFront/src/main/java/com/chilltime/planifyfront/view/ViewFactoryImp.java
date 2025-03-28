package com.chilltime.planifyfront.view;

import com.chilltime.planifyfront.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.YearMonth;
import java.util.HashMap;

public class ViewFactoryImp extends ViewFactory {

    private final HashMap<String, Scene> views;

    public ViewFactoryImp() {
        views = new HashMap<>();
        views.put("calendar", new Scene(new FullCalendarView(YearMonth.now()).getView()));
    }

    /**
     * Metodo que devuelve la escena. Si fxmlFile no es nulo, se carga el FXML;
     * de lo contrario, se usa una vista precargada.
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
        if (content == null) {
            // Si no se pudo cargar, se usa un contenedor vacío de respaldo
            content = new Pane();
        }
        // Se crea la escena directamente con el contenido obtenido
        Scene scene = new Scene(content);
        scene.setFill(Color.web("#36393f")); // Fondo de la escena con color oscuro (opcional)
        return scene;
    }
}
