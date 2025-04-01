package com.chilltime.planifyfront.utils;

/**
 * Clase Singleton para gestionar la sesión del usuario actual en la aplicación.
 * Almacena información del usuario logueado.
 */
public class SessionManager {

    // Instancia única del SessionManager (patrón Singleton)
    private static SessionManager instance;

    // Datos del usuario actual
    private Long currentUserId;
    private boolean authenticated = false;

    // Constructor privado para implementar Singleton
    private SessionManager() {}

    /**
     * Obtiene la instancia única del SessionManager
     * @return Instancia de SessionManager
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}