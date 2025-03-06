package com.planifyAPI.planifyAPI.repository;

import com.planifyAPI.planifyAPI.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Integer> {

}
