package com.chilltime.planifyapi.repository;

import com.chilltime.planifyapi.entity.CalendarCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodigoRepository extends JpaRepository<CalendarCode, Integer> {

}
