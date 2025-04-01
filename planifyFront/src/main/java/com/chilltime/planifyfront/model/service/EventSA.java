package com.chilltime.planifyfront.model.service;

import com.chilltime.planifyfront.model.transfer.TEvent;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public Task<String> createEvent(TEvent event,Long calendarId) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                try {
                    Map<String, Object> requestMap = new HashMap<>();
                    requestMap.put("event", event);
                    requestMap.put("calendarId", calendarId);
                    String requestBody = apiClient.getObjectMapper().writeValueAsString(requestMap);
                    return apiClient.post(BASE_URL + "/create-event", requestBody);
                } catch (IOException e) {
                    System.err.println("Error during API call: " + e.getMessage() + e.getLocalizedMessage());
                    throw e;
                }
            }
        };
    }

    public Task<String> getEventsByCalendarId(Long calendarId) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                try {
                    return apiClient.get(BASE_URL + "/" + calendarId + "/events");
                } catch (IOException e) {
                    System.err.println("Error during API call: " + e.getMessage());
                    throw e;
                }
            }
        };
    }
}
