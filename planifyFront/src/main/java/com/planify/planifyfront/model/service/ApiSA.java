package com.planify.planifyfront.model.service;

import com.planify.planifyfront.model.transfer.TEvento;
import javafx.concurrent.Task;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiSA {

    private static final String BASE_URL = "http://localhost:1010/event";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiSA() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public Task<String> createEvent(TEvento evento) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                String requestBody = objectMapper.writeValueAsString(evento);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(BASE_URL + "/create-event"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                return response.body();
            }
        };
    }
}

