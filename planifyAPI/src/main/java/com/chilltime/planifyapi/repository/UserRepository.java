package com.chilltime.planifyapi.repository;

import com.chilltime.planifyapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
