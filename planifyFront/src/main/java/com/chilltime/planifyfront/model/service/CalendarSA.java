package com.chilltime.planifyfront.model.service;

import com.calendarfx.model.Calendar;
import com.chilltime.planifyfront.model.transfer.TCalendar;
import javafx.concurrent.Task;

import java.io.IOException;


public class CalendarSA {

    private ApiClient apiClient;

    private final String BASE_URL = "/calendar";

    public CalendarSA(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public Task<Calendar> readCalendarByUser(String username) {
        return null;
    }

    public Task<String> crearCalendario(TCalendar calendar) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                try {
                    String requestBody = apiClient.getObjectMapper().writeValueAsString(calendar);
                    return apiClient.post(BASE_URL + "/create-private", requestBody);
                } catch (IOException e) {
                    System.err.println("Error during API call: " + e.getMessage());
                    throw e;
                }
            }
        };
    }

}
