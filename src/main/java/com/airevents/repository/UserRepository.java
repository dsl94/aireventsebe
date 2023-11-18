package com.airevents.repository;

import com.airevents.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameIgnoreCase(String username);
    User findByStravaIdIgnoreCase(String stravaId);
    Optional<User> findByEmailIgnoreCase(String email);
}
