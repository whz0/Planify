package com.chilltime.planifyfront.model.service;

import com.chilltime.planifyfront.model.transfer.TEvento;
import javafx.concurrent.Task;

import java.io.IOException;

public class EventoSA {

    private final ApiClient apiClient;

    private final String BASE_URL = "/event";

    public EventoSA(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Crea un evento enviando una solicitud POST al endpoint "/create-event".
     *
     * @param evento El objeto TEvento a enviar.
     * @return Un Task que devuelve la respuesta del servidor.
     */
    public Task<String> createEvent(TEvento evento) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                try {
                    String requestBody = apiClient.getObjectMapper().writeValueAsString(evento);
                    return apiClient.post(BASE_URL + "/create-event", requestBody);
                } catch (IOException e) {
                    System.err.println("Error during API call: " + e.getMessage());
                    throw e;
                }
            }
        };
    }
}
