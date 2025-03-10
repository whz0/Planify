package com.planify.planifyfront.view;

import com.planify.planifyfront.controller.DashboardControllerSingleton;
import com.planify.planifyfront.model.transfer.TEvento;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Create an anchor pane that can store additional data.
 */
public class AnchorPaneNode extends AnchorPane {
    private LocalDate date;

    private VBox eventsContainer;

    public AnchorPaneNode(Node... children) {
        super(children);
        // Create events container
        eventsContainer = new VBox(5);
        eventsContainer.setAlignment(Pos.CENTER);
        AnchorPane.setBottomAnchor(eventsContainer, 5.0);
        AnchorPane.setRightAnchor(eventsContainer, 5.0);
        getChildren().add(eventsContainer);

        this.setOnMouseClicked(e -> {
            try {
                DashboardControllerSingleton.getInstance().updateEventsPanel(date);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void clearEvents() {
        eventsContainer.getChildren().clear();
    }

    public void addEventMarker() {
        Text marker = new Text("•");
        marker.getStyleClass().add("event-marker");
        eventsContainer.getChildren().add(marker);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}