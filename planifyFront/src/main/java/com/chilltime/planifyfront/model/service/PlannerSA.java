package com.chilltime.planifyfront.model.service;

import com.chilltime.planifyfront.model.transfer.TPlanner;
import com.chilltime.planifyfront.model.transfer.TPlanner;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

public class PlannerSA {

    private final ApiClient apiClient;

    private final String BASE_URL = "/planner";

    public PlannerSA(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Inicia sesión de un planner enviando una solicitud POST al endpoint
     * "/login-planner".
     *
     * @param username El nombre de usuario del planner.
     * @param password La contraseña del planner.
     * @return Un Task que devuelve la respuesta del servidor.
     */
    public Task<String> loginPlanner(String username, String password) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                try {
                    Map<String, String> credentials = new HashMap<>();
                    credentials.put("username", username);
                    credentials.put("password", password);
                    String requestBody = apiClient.getObjectMapper().writeValueAsString(credentials);

                    return apiClient.post(BASE_URL + "/login-planner", requestBody);
                } catch (Exception e) {
                    System.err.println("Error during login API call: " + e.getMessage());
                    throw e;
                }
            }
        };
    }

    public Task<String> registerPlanner(TPlanner planner) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {

                try {
                    String requestBody = apiClient.getObjectMapper().writeValueAsString(planner);
                    return apiClient.post(BASE_URL + "/register", requestBody);
                }

                catch (IOException e) {
                    System.err.println("Error during API call: " + e.getMessage());
                    throw e;
                }
            }
        };
    }


}