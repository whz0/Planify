package com.planify.planifyfront.view;

import javafx.scene.Scene;

public abstract class ViewFactory {
    private static ViewFactory instance;
    public static ViewFactory getInstance() {
        if (instance == null) {
            instance = new ViewFactoryImp();
        }
        return instance;
    }
    public abstract Scene getView(String fxmlFile, String title);
}
