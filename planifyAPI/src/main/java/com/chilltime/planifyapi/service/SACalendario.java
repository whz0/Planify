package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.TContext;
import com.chilltime.planifyapi.entity.Calendario;
import com.chilltime.planifyapi.entity.Usuario;
import com.chilltime.planifyapi.repository.CalendarioRepository;
import com.chilltime.planifyapi.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;


@Service
public class SACalendario {

    @Autowired
    private CalendarioRepository calendarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public TContext crearCalendarioPrivado(Calendario calendario) {
        Usuario usuario = usuarioRepository.findById(calendario.getId_usuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        calendario.setUsuario(usuario);

        validarCalendario(calendario.getNombre(), calendario.getDescripcion(), calendario.getUsuario());

        return new TContext(200, "Creado correctamente", calendarioRepository.save(calendario));

    }

    private void validarCalendario(String nombre, String descripcion, Usuario usuario) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio y no puede estar compuesto por espacios en blanco.");
        }

        if (!nombre.matches("[a-zA-Z0-9 ]+")) {
            throw new IllegalArgumentException("El nombre debe contener solo caracteres alfanuméricos y espacios.");
        }

        if(nombre.length() > 20){
            throw new IllegalArgumentException("El campo nombre no puede tener más de 20 caracteres");
        }
        if (descripcion != null && descripcion.length() > 255) {
            throw new IllegalArgumentException("La descripción debe tener un máximo de 255 caracteres.");
        }

        if (calendarioRepository.existsByNombreAndUsuario(nombre, usuario)) {
            throw new IllegalArgumentException("El nombre ya está en uso por otro calendario privado suyo. Por favor, elija otro nombre.");
        }
    }


}
