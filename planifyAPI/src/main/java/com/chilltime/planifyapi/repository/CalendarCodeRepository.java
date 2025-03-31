package com.chilltime.planifyapi.repository;

import com.chilltime.planifyapi.entity.CalendarCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalendarCodeRepository extends JpaRepository<CalendarCode, Integer> {
    Optional<CalendarCode> findByCode(String code);
    Optional<CalendarCode> findByCodeAndUsedFalse(String code);
}
