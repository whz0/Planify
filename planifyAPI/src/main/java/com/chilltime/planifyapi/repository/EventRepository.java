package com.chilltime.planifyapi.repository;

import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCalendars(Calendar calendar);
}
