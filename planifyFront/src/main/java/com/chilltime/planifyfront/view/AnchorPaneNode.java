package com.chilltime.planifyfront.view;

import com.chilltime.planifyfront.controller.DashboardControllerSingleton;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDate;

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
                DashboardControllerSingleton.getInstance();
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