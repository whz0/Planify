package com.chilltime.planifyapi.controller;

import com.chilltime.planifyapi.entity.Calendario;
import com.chilltime.planifyapi.entity.Usuario;
import com.chilltime.planifyapi.service.SACalendario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    private final SACalendario calendarioService;

    @Autowired
    public CalendarController(SACalendario calendarioService) {
        this.calendarioService = calendarioService;
    }

    @PostMapping("/create-private")
    public ResponseEntity<Calendario> crearCalendarioPrivado(
            @Valid @RequestBody CalendarioRequest request) {
        try {
            // Valida que el usuario no sea nulo (podrías agregar más validaciones aquí)
            if (request.getUsuario() == null) {
                throw new IllegalArgumentException("El usuario es obligatorio");
            }

            Calendario calendario = calendarioService.crearCalendarioPrivado(
                    request.getNombre(),
                    request.getDescripcion(),
                    request.getUsuario()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(calendario);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Clase DTO para encapsular la solicitud
    static class CalendarioRequest {
        @NotBlank(message = "El nombre es obligatorio")
        private String nombre;
        private String descripcion;
        @NotNull(message = "El usuario es obligatorio")
        private Usuario usuario;

        // Getters y Setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public Usuario getUsuario() { return usuario; }
        public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    }
}