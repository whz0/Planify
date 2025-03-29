package com.chilltime.planifyapi.repository;

import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findByClient(Client client);
    boolean existsByNameAndClient(String name, Client client);
}
