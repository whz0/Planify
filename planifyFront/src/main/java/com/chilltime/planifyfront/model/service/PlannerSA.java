package com.chilltime.planifyfront.model.service;

import com.chilltime.planifyfront.model.transfer.TPlanner;
import javafx.concurrent.Task;

import java.io.IOException;

public class PlannerSA {
    private ApiClient apiClient;

    private final String BASE_URL = "/planner";

    public PlannerSA(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public Task<String> registerPlanner(TPlanner planner) {
      return new Task<>() {
          @Override
          protected String call() throws Exception {

              try {
                  String requestBody = apiClient.getObjectMapper().writeValueAsString(planner);
                  return apiClient.post(BASE_URL + "/register",requestBody);
              }

              catch (IOException e) {
                  System.err.println("Error during API call: " + e.getMessage());
                  throw e;
              }
          }
      };
    }
}
