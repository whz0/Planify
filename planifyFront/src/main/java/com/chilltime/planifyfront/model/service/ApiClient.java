package com.chilltime.planifyfront.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {

    // URL base fija para el API
    private static final String BASE_URL = "http://34.175.115.84:1010";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    /**
     * Realiza una solicitud POST.
     *
     * @param endpoint La ruta relativa (por ejemplo, "/create-event")
     * @param requestBody El cuerpo de la solicitud en formato JSON.
     * @return La respuesta del servidor como String.
     * @throws IOException
     * @throws InterruptedException
     */
    public String post(String endpoint, String requestBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("HTTP error code: " + response.statusCode());
        }
        return response.body();
    }

    /**
     * Realiza una solicitud GET.
     *
     * @param endpoint La ruta relativa (por ejemplo, "/get-events")
     * @return La respuesta del servidor como String.
     * @throws IOException
     * @throws InterruptedException
     */
    public String get(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("HTTP error code: " + response.statusCode());
        }
        return response.body();
    }
}
