package com.chilltime.planifyapi.repository;

import com.chilltime.planifyapi.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}
