package com.chilltime.planifyfront.model.service;

public class ServiceFactoryImp extends ServiceFactory{

    private static ApiClient apiClient;

    public ServiceFactoryImp() {
        apiClient = new ApiClient();
    }

    @Override
    public EventSA crearEventoSA() {
        return new EventSA(apiClient);
    }

    @Override
    public CalendarSA crearCalendarioSA() {
        return new CalendarSA(apiClient);
    }
}
