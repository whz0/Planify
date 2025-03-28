package com.chilltime.planifyapi.repository;

import com.chilltime.planifyapi.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {
}
