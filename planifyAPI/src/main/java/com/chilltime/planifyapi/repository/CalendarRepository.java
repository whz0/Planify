package com.chilltime.planifyapi.repository;

import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findByPlanner(Planner planner);
    boolean existsByNameAndPlanner(String name, Planner planner);
}
