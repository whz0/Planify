package com.chilltime.planifyfront.model.service;

import com.chilltime.planifyfront.model.transfer.TEvent;
import javafx.concurrent.Task;

import java.io.IOException;

public class EventSA {

    private final ApiClient apiClient;

    private final String BASE_URL = "/event";

    public EventSA(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Crea un evento enviando una solicitud POST al endpoint "/create-event".
     *
     * @param event El objeto TEvento a enviar.
     * @return Un Task que devuelve la respuesta del servidor.
     */
    public Task<String> createEvent(TEvent event) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                try {
                    String requestBody = apiClient.getObjectMapper().writeValueAsString(event);
                    return apiClient.post(BASE_URL + "/create-event", requestBody);
                } catch (IOException e) {
                    System.err.println("Error during API call: " + e.getMessage());
                    throw e;
                }
            }
        };
    }
}
