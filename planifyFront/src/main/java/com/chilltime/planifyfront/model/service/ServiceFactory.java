package com.chilltime.planifyfront.model.service;

public abstract class ServiceFactory {

    private static ServiceFactory instance;

    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactoryImp();
        }
        return instance;
    }

    public abstract EventoSA crearEventoSA();

    public abstract CalendarioSA crearCalendarioSA();
}
