package com.planify.planifyfront.model.service;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.planify.planifyfront.model.transfer.TEvento;
import javafx.concurrent.Task;

import java.io.IOException;
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
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public Task<String> createEvent(TEvento evento) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                try {

                    String requestBody = objectMapper.writeValueAsString(evento);
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(new URI(BASE_URL + "/create-event"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                            .build();

                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() != 200) {
                        throw new IOException("HTTP error code: " + response.statusCode());
                    }

                    return response.body();
                } catch (Exception e) {
                    System.err.println("Error during API call: " + e.getMessage());
                    e.printStackTrace();
                    throw e;
                }
            }
        };
    }
}

