package com.chilltime.planifyapi.repository;

import com.chilltime.planifyapi.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {

}
