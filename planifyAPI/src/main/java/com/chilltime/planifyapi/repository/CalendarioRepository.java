package com.chilltime.planifyapi.repository;

import com.chilltime.planifyapi.entity.Calendario;
import com.chilltime.planifyapi.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarioRepository extends JpaRepository<Calendario, Long> {


    boolean existsByNombreAndUsuario(String nombre, Usuario usuario);

}
