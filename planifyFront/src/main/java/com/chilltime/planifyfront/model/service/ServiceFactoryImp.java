package com.chilltime.planifyfront.model.service;

public class ServiceFactoryImp extends ServiceFactory{

    private static ApiClient apiClient;

    public ServiceFactoryImp() {
        apiClient = new ApiClient();
    }

    @Override
    public EventSA createEventSA() {
        return new EventSA(apiClient);
    }

    @Override
    public CalendarSA createCalendarSA() {
        return new CalendarSA(apiClient);
    }
}
