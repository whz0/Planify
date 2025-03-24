package com.chilltime.planifyfront.model.service;

import com.calendarfx.model.Calendar;
import com.chilltime.planifyfront.model.transfer.TCalendario;
import javafx.concurrent.Task;

import java.io.IOException;


public class CalendarioSA {

    private ApiClient apiClient;

    private final String BASE_URL = "/calendar";

    public CalendarioSA(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public Task<Calendar> leerCalendariosPorUsuario(String username) {
        return null;
    }

    public Task<String> crearCalendario(TCalendario calendario) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                try {
                    String requestBody = apiClient.getObjectMapper().writeValueAsString(calendario);
                    return apiClient.post(BASE_URL + "/create-private", requestBody);
                } catch (IOException e) {
                    System.err.println("Error during API call: " + e.getMessage());
                    throw e;
                }
            }
        };
    }

}
