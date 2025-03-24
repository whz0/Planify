package com.chilltime.planifyfront.model.service;

import com.calendarfx.model.Calendar;
import javafx.concurrent.Task;


public class CalendarioSA {

    private ApiClient apiClient;

    public CalendarioSA(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public Task<Calendar> leerCalendariosPorUsuario(String username) {
        return null;
    }

    public Task<Calendar> crearCalendario(String username, String nombre) {
        return null;
    }

}
