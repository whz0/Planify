package com.chilltime.planifyfront.model.service;

public class ServiceFactoryImp extends ServiceFactory{

    private static ApiClient apiClient;

    public ServiceFactoryImp() {
        apiClient = new ApiClient();
    }

    @Override
    public EventoSA crearEventoSA() {
        return new EventoSA(apiClient);
    }

    @Override
    public CalendarioSA crearCalendarioSA() {
        return new CalendarioSA(apiClient);
    }
}
