package com.chilltime.planifyapi.repository;

import com.chilltime.planifyapi.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
}
