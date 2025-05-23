package com.chilltime.planifyfront.model.service;

import javafx.concurrent.Task;
import com.chilltime.planifyfront.model.transfer.TCalendarCode;

import java.io.IOException;

public class CalendarCodeSA {

    private final ApiClient apiClient;

    private final String BASE_URL = "/calendar-code";

    public CalendarCodeSA(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public Task<String> generateCalendarCode(TCalendarCode calendarCode) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                try {
                    String requestBody = apiClient.getObjectMapper().writeValueAsString(calendarCode);
                    return apiClient.post(BASE_URL + "/generate", requestBody);
                } catch (IOException e) {
                    System.err.println("Error during API call: " + e.getMessage());
                    throw e;
                }
            }
        };
    }



}
