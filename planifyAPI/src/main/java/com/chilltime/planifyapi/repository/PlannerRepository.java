package com.chilltime.planifyapi.repository;

import com.chilltime.planifyapi.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlannerRepository extends JpaRepository<Planner,Long> {
}
