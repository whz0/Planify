package com.chilltime.planifyfront.model.service;

public abstract class ServiceFactory {

    private static ServiceFactory instance;

    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactoryImp();
        }
        return instance;
    }

    // Method to set a custom instance (for testing purposes)
    public static void setInstance(ServiceFactory customInstance) {
        instance = customInstance;
    }

    public abstract EventSA createEventSA();

    public abstract CalendarSA createCalendarSA();

    public abstract PlannerSA createPlannerSA();

    public abstract CalendarCodeSA createCalendarCodeSA();

}
