package com.chilltime.planifyfront.model.service;

public abstract class ServiceFactory {

    private static ServiceFactory instance;

    public static synchronized ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactoryImp();
        }
        return instance;
    }

    public abstract EventSA createEventSA();

    public abstract CalendarSA createCalendarSA();

    public abstract CalendarCodeSA createCalendarCodeSA();
}
