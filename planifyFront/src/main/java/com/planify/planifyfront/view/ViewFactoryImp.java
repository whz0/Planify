package com.planify.planifyfront.view;

import javafx.scene.Scene;

import java.time.YearMonth;
import java.util.HashMap;

public class ViewFactoryImp extends ViewFactory{

    private final HashMap<String, Scene> views;

    public ViewFactoryImp() {
        views = new HashMap<>();
        views.put("calendar",new Scene(new FullCalendarView(YearMonth.now()).getView()));
    }


    @Override
    public Scene getView(String viewName) {
        return views.get(viewName);
    }
}
