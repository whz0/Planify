package com.chilltime.planifyapi.repository;

import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {


    boolean existsByNameAndClient(String name, Client client);

}
