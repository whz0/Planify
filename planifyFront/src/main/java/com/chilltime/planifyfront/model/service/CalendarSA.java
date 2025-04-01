package com.chilltime.planifyfront.model.service;

import com.chilltime.planifyfront.model.transfer.TCalendar;
import javafx.concurrent.Task;

import java.io.IOException;

public class CalendarSA {

    private ApiClient apiClient;
    private final String BASE_URL = "/calendar";
    private final String CODE_URL = "/codigo";

    public CalendarSA(ApiClient apiClient) {
        this.apiClient = apiClient;
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

    public Task<String> getCalendarsWithEventsByUserId(Long userId) {
      return new Task<>() {
            @Override
            protected String call() throws Exception {
                try {
                    // Obtenemos primero los calendarios del usuario
                    String calendarsResponse = apiClient.get(BASE_URL + "/user/" + 1);

                    // Procesamos la respuesta para solicitar eventos de cada calendario
                    return calendarsResponse;
                  } catch (IOException e) {
                    System.err.println("Error during API call: " + e.getMessage());
                    throw e;
                }
            }
        };
  }

    /**
     * Tarea para unirse a un calendario mediante un código
     * @param userId ID del usuario
     * @param code Código del calendario
     * @return Respuesta de la API
     */
    public Task<String> unirseACalendario(Long userId, String code) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                try {
                   
                    // Construcción del URL con parámetros
                    String url = BASE_URL + "/join?userId=" + userId + "&code=" + code;
                    return apiClient.post(url, "");
                } catch (IOException e) {
                    System.err.println("Error during API call: " + e.getMessage());
                    throw e;
                }
            }
        };
    }

    /**
     * Tarea para generar un código de compartición para un calendario
     * @param calendarId ID del calendario
     * @return Respuesta de la API con el código generado
     */
    public Task<String> generarCodigoCompartir(Long calendarId) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                try {
                    return apiClient.post(CODE_URL + "/generate/" + calendarId, "");
                } catch (IOException e) {
                    System.err.println("Error during API call: " + e.getMessage());
                    throw e;
                }
            }
        };
    }


    /**
     * Tarea para validar un código
     * @param code Código a validar
     * @return Respuesta de la API
     */
    public Task<String> validarCodigo(String code) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                try {
                    return apiClient.get(CODE_URL + "/validate/" + code);
                } catch (IOException e) {
                    System.err.println("Error during API call: " + e.getMessage());
                    throw e;
                }
            }
        };
    }
}
